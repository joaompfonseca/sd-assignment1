package playground;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class MPlayground implements IPlayground {

    private final int contestantsPerTrial;
    private final ReentrantLock lock;
    private final Condition trialStarted;
    private final Condition trialEnded;
    private final Condition trialDecided;
    private int contestantsDone;

    public MPlayground(int contestantsPerTrial) {
        this.contestantsPerTrial = contestantsPerTrial;
        lock = new ReentrantLock();
        trialStarted = lock.newCondition();
        trialEnded = lock.newCondition();
        trialDecided = lock.newCondition();
        contestantsDone = 0;
    }

    @Override
    public void getReady() {
        log("get ready");
        lock.lock();
        try {
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
            contestantsDone = 0;
        }
        lock.unlock();
    }

    @Override
    public void startTrial() {
        log("start trial");
        lock.lock();
        trialStarted.signalAll();
        lock.unlock();
    }

    @Override
    public int assertTrialDecision() {
        log("assert trial decision");
        lock.lock();
        try {
            trialEnded.await(); // releases lock and waits
            // TODO: set notes
            trialDecided.signalAll();
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
