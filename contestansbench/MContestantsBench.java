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
        private int[] strengths;
        private boolean isMatchEnd;

        public TeamData() {
            lock = new ReentrantLock();
            seatedDown = lock.newCondition();
            teamAssembled = lock.newCondition();
            countSeatedDown = 0;
            selected = new boolean[contestantsPerTeam];
            strengths = new int[contestantsPerTeam];
            isMatchEnd = false;
        }
    }

    private final int contestantsPerTeam;
    private final int maxStrength;
    private final TeamData[] teamData = new TeamData[2];


    public MContestantsBench(int contestantsPerTeam, int maxStrength) {
        this.contestantsPerTeam = contestantsPerTeam;
        this.maxStrength = maxStrength;
        teamData[0] = new TeamData();
        teamData[1] = new TeamData();
    }

    @Override
    public int[] getTeamStrengths(int team) {
        log("get strengths: team %d".formatted(team));
        TeamData teamData = this.teamData[team];
        return teamData.strengths;
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
    public int seatDown(int team, int contestant, int strength) {
        log("seat down: team %d, contestant %d, strength %d".formatted(team, contestant, strength));
        TeamData teamData = this.teamData[team];
        teamData.lock.lock();
        try {
            teamData.countSeatedDown++;
            teamData.selected[contestant] = false;
            teamData.strengths[contestant] = strength;
            if (teamData.countSeatedDown == contestantsPerTeam) {
                teamData.seatedDown.signal();
            }
            while (!teamData.selected[contestant]) {
                teamData.teamAssembled.await(); // releases the lock and waits
                if (!teamData.selected[contestant] && teamData.strengths[contestant] < maxStrength) {
                   teamData.strengths[contestant]++; // stays seated, so strength increases
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            teamData.lock.unlock();
        }
        return teamData.strengths[contestant];
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
