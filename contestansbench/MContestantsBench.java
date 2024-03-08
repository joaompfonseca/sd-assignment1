package contestansbench;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class MContestantsBench implements IContestantsBench {
    private class TeamData {
        private final ReentrantLock lock;
        private final Condition seatedDown;
        private final Condition teamAssembled;
        private int countSeatedDown;
        private boolean[] selected;
        private boolean isMatchEnd;

        public TeamData() {
            lock = new ReentrantLock();
            seatedDown = lock.newCondition();
            teamAssembled = lock.newCondition();
            countSeatedDown = 0;
            selected = new boolean[contestantsPerTeam];
            isMatchEnd = false;
        }
    }

    private final int contestantsPerTeam;
    private final TeamData[] teamData = new TeamData[2];


    public MContestantsBench(int contestantsPerTeam) {
        this.contestantsPerTeam = contestantsPerTeam;
        teamData[0] = new TeamData();
        teamData[1] = new TeamData();
    }

    @Override
    public int[] getTeamStrengths(int team) {
        log("get strengths: team %d".formatted(team));
        return new int[]{5, 5, 5, 5, 5}; // TODO: implement
    }

    @Override
    public void setTeamIsMatchEnd(int team, boolean isMatchEnd) {
        log("set is match end: team %d, is match end %b".formatted(team, isMatchEnd));
        TeamData teamData = this.teamData[team];
        teamData.lock.lock();
        teamData.isMatchEnd = isMatchEnd;
        teamData.lock.unlock();
    }

    @Override
    public void seatDown(int team, int contestant) {
        log("seat down: team %d, contestant %d".formatted(team, contestant));
        TeamData teamData = this.teamData[team];
        teamData.lock.lock();
        try {
            teamData.selected[contestant] = false;
            teamData.countSeatedDown++;
            if (teamData.countSeatedDown == contestantsPerTeam) {
                teamData.seatedDown.signal();
            }
            while (!teamData.selected[contestant]) {
                teamData.teamAssembled.await(); // releases the lock and waits
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            teamData.lock.unlock();
        }
    }

    @Override
    public void callContestants(int team, boolean[] selected) {
        log("call contestants: team %d, selected %s".formatted(team, Arrays.toString(selected)));
        TeamData teamData = this.teamData[team];
        teamData.lock.lock();
        try {
            while (teamData.countSeatedDown < contestantsPerTeam) {
                teamData.seatedDown.await(); // releases the lock and waits
            }
            teamData.selected = selected;
            teamData.teamAssembled.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            teamData.lock.unlock();
        }
    }

    @Override
    public boolean followCoachAdvice(int team) {
        log("follow coach advice: team %d".formatted(team));
        TeamData teamData = this.teamData[team];
        teamData.lock.lock();
        teamData.countSeatedDown--;
        teamData.lock.unlock();
        return !teamData.isMatchEnd;
    }

    private void log(String msg) {
        System.out.printf("[ContestantsBench]: %s\n", msg);
    }
}
