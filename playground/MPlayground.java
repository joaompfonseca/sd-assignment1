package playground;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MPlayground implements IPlayground {
    private static class TeamData {
        private final Condition trialReady;
        private int contestantsReady;

        public TeamData(ReentrantLock lock) {
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
    private boolean isTrialStarted;
    private int ropePosition;
    private final Condition trialEnded;
    private int contestantsDone;
    private final Condition trialDecided;
    private boolean isTrialDecided;

    public MPlayground(int contestantsPerTrial) {
        this.contestantsPerTrial = contestantsPerTrial;
        lock = new ReentrantLock();
        teamData[0] = new TeamData(lock);
        teamData[1] = new TeamData(lock);
        refereeInformed = lock.newCondition();
        countInformed = 0;
        trialStarted = lock.newCondition();
        isTrialStarted = false;
        ropePosition = 0;
        trialEnded = lock.newCondition();
        contestantsDone = 0;
        trialDecided = lock.newCondition();
        isTrialDecided = false;
    }

    @Override
    public void setRopePosition(int ropePosition) {
        log("set rope position: %d".formatted(ropePosition));
        this.ropePosition = ropePosition;
    }

    @Override
    public void getReady(int team) {
        log("get ready: team %d".formatted(team));
        TeamData teamData = this.teamData[team];
        lock.lock();
        try {
            teamData.contestantsReady++;
            if (teamData.contestantsReady == contestantsPerTrial) {
                teamData.trialReady.signal(); // alerts coach
            }
            while (!isTrialStarted) {
                trialStarted.await(); // releases lock and waits for referee
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void informReferee(int team) {
        log("inform referee: team %d".formatted(team));
        TeamData teamData = this.teamData[team];
        lock.lock();
        try {
            while (teamData.contestantsReady < contestantsPerTrial) {
                teamData.trialReady.await(); // releases lock and waits
            }
            countInformed++;
            if (countInformed == 2) {
                refereeInformed.signal(); // alerts referee
            }
            while (!isTrialDecided) {
                trialDecided.await(); // releases lock and waits for referee decision
            }
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
            isTrialDecided = false;
            isTrialStarted = true;
            trialStarted.signalAll(); // alerts contestants
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int pullTheRope(int team, int strength) {
        log("pull the rope: team %d, strength %d".formatted(team, strength));
        lock.lock();
        ropePosition += (team == 0) ? -strength : strength;
        lock.unlock();
        return strength - 1;
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
            while (!isTrialDecided) {
                trialDecided.await(); // releases lock and waits for referee decision
            }
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
            isTrialStarted = false;
            isTrialDecided = true;
            trialDecided.signalAll(); // alerts contestants and coaches
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return ropePosition;
    }

    private void log(String msg) {
        System.out.printf("[Playground]: %s\n", msg);
    }
}
