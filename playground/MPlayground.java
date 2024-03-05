package playground;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class MPlayground implements IPlayground {

    private final int contestantsPerTrial;
    private final ReentrantLock lock;
    private final Condition trialReady;
    private int contestantsReady;
    private final Condition trialStarted;
    private final Condition trialEnded;
    private int contestantsDone;
    private final Condition trialDecided;

    public MPlayground(int contestantsPerTrial) {
        this.contestantsPerTrial = contestantsPerTrial;
        lock = new ReentrantLock();
        trialReady = lock.newCondition();
        contestantsReady = 0;
        trialStarted = lock.newCondition();
        trialEnded = lock.newCondition();
        contestantsDone = 0;
        trialDecided = lock.newCondition();
    }

    @Override
    public void getReady() {
        log("get ready");
        lock.lock();
        try {
            contestantsReady++;
            if (contestantsReady == contestantsPerTrial) {
                trialReady.signal();
            }
            trialStarted.await(); // releases lock and waits
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void pullTheRope() {

    }

    @Override
    public void amDone() {
        log("am done");
        lock.lock();
        contestantsDone++;
        if (contestantsDone == 2 * contestantsPerTrial) {
            trialEnded.signal();
        }
        lock.unlock();
    }

    @Override
    public void startTrial() {
        log("start trial");
        lock.lock();
        try {
            if (contestantsReady < contestantsPerTrial)
                trialReady.await(); // releases lock and waits
            contestantsDone = 0;
            trialStarted.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int assertTrialDecision() {
        log("assert trial decision");
        lock.lock();
        try {
            if (contestantsDone < 2 * contestantsPerTrial)
                trialEnded.await(); // releases lock and waits
            // TODO: set notes
            trialDecided.signalAll();
            contestantsReady = 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return 0;
    }

    @Override
    public int[] reviewNotes(int team) {
        log("review notes: team %d".formatted(team));
        lock.lock();
        try {
            if (contestantsDone < 2 * contestantsPerTrial)
                trialDecided.await(); // releases lock and waits
            // TODO: get notes
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return new int[]{5, 5, 5, 5, 5};
    }

    private void log(String msg) {
        System.out.printf("[Playground]: %s\n", msg);
    }
}
