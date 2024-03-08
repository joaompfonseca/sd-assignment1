package playground;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MPlayground implements IPlayground {
    private static class TeamData {
        private final ReentrantLock lock;
        private final Condition trialReady;
        private int contestantsReady;

        public TeamData() {
            lock = new ReentrantLock();
            trialReady = lock.newCondition();
            contestantsReady = 0;
        }
    }

    private final int contestantsPerTrial;
    private final TeamData[] teamData = new TeamData[2];
    private final ReentrantLock lock;
    private final Condition refereeInformed;
    private int countInformed;
    private final Condition trialStarted;
    private final Condition trialEnded;
    private int contestantsDone;
    private final Condition trialDecided;

    public MPlayground(int contestantsPerTrial) {
        this.contestantsPerTrial = contestantsPerTrial;
        lock = new ReentrantLock();
        teamData[0] = new TeamData();
        teamData[1] = new TeamData();
        refereeInformed = lock.newCondition();
        countInformed = 0;
        trialStarted = lock.newCondition();
        trialEnded = lock.newCondition();
        contestantsDone = 0;
        trialDecided = lock.newCondition();
    }

    @Override
    public void getReady(int team) {
        log("get ready");
        TeamData teamData = this.teamData[team];
        teamData.lock.lock();
        teamData.contestantsReady++;
        if (teamData.contestantsReady == contestantsPerTrial) {
            teamData.trialReady.signal(); // alerts coach
        }
        teamData.lock.unlock();
        lock.lock();
        try {
            trialStarted.await(); // releases lock and waits for referee
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void informReferee(int team) {
        log("inform referee");
        TeamData teamData = this.teamData[team];
        teamData.lock.lock();
        try {
            while (teamData.contestantsReady < contestantsPerTrial) {
                teamData.trialReady.await(); // releases lock and waits
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            teamData.lock.unlock();
        }
        lock.lock();
        try {
            countInformed++;
            if (countInformed == 2) {
                refereeInformed.signal(); // alerts referee
            }
            trialDecided.await(); // releases lock and waits for referee decision
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void startTrial() {
        log("start trial");
        lock.lock();
        try {
            while (countInformed < 2) {
                refereeInformed.await(); // releases lock and waits for the last coach
            }
            trialStarted.signalAll(); // alerts contestants
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void pullTheRope() {
        log("pull the rope");
    }

    @Override
    public void amDone() {
        log("am done");
        lock.lock();
        try {
            contestantsDone++;
            if (contestantsDone == 2 * contestantsPerTrial) {
                trialEnded.signal(); // alerts referee
            }
            trialDecided.await(); // releases lock and waits for referee decision
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
            while (contestantsDone < 2 * contestantsPerTrial) {
                trialEnded.await(); // releases lock and waits for the last contestant
            }
            teamData[0].contestantsReady = 0;
            teamData[1].contestantsReady = 0;
            countInformed = 0;
            contestantsDone = 0;
            trialDecided.signalAll(); // alerts contestants and coaches
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return 0;
    }

    private void log(String msg) {
        System.out.printf("[Playground]: %s\n", msg);
    }
}
