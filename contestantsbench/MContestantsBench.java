package contestantsbench;

import generalrepository.IGeneralRepository_Bench;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of the contestants bench monitor.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class MContestantsBench implements IContestantsBench {
    /**
     * Representation of team specific data.
     */
    private class TeamData {
        /**
         * The condition variable for seated down.
         */
        private final Condition seatedDown;
        /**
         * The condition variable for team assembled.
         */
        private final Condition teamAssembled;
        /**
         * The number of contestants seated down.
         */
        private int countSeatedDown;
        /**
         * The selected contestants.
         */
        private boolean[] selected;
        /**
         * The strengths of the contestants.
         */
        private final int[] strengths;
        /**
         * Flag to indicate if the match has ended.
         */
        private boolean isMatchEnd;

        /**
         * Instantiation of team specific data.
         *
         * @param lock the lock from the contestants bench
         */
        public TeamData(ReentrantLock lock) {
            seatedDown = lock.newCondition();
            teamAssembled = lock.newCondition();
            countSeatedDown = 0;
            selected = new boolean[contestantsPerTeam];
            strengths = new int[contestantsPerTeam];
            isMatchEnd = false;
        }
    }

    /**
     * The lock.
     */
    private final ReentrantLock lock;
    /**
     * The number of contestants per team.
     */
    private final int contestantsPerTeam;
    /**
     * The maximum strength of a contestant.
     */
    private final int maxStrength;
    /**
     * The team specific data.
     */
    private final TeamData[] teamData = new TeamData[2];
    /**
     * The general repository.
     */
    private final IGeneralRepository_Bench generalRepository;

    /**
     * Instantiation of the contestants bench.
     *
     * @param contestantsPerTeam the number of contestants per team
     * @param maxStrength        the maximum strength of a contestant
     */
    public MContestantsBench(int contestantsPerTeam, int maxStrength, IGeneralRepository_Bench generalRepository) {
        this.contestantsPerTeam = contestantsPerTeam;
        this.maxStrength = maxStrength;
        lock = new ReentrantLock();
        teamData[0] = new TeamData(lock);
        teamData[1] = new TeamData(lock);
        this.generalRepository = generalRepository;
    }

    /**
     * The coach gets the strengths of the contestants of his team. The coach waits until all his contestants are seated
     * down.
     *
     * @param team the team
     * @return the strengths of the team
     */
    @Override
    public int[] getTeamStrengths(int team) {
        TeamData teamData = this.teamData[team];
        lock.lock();
        try {
            while (teamData.countSeatedDown < contestantsPerTeam) {
                teamData.seatedDown.await(); // releases the lock and waits
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return teamData.strengths;
    }

    /**
     * The coach sets the match end flag to alert the contestants form his team that the match has ended.
     *
     * @param team       the team
     * @param isMatchEnd the match end flag
     */
    @Override
    public void setTeamIsMatchEnd(int team, boolean isMatchEnd) {
        TeamData teamData = this.teamData[team];
        lock.lock();
        teamData.isMatchEnd = isMatchEnd;
        lock.unlock();
    }

    /**
     * The contestant seats down and waits for the coach to call him. The last contestant to seat down alerts the coach.
     * The contestant waits for the coach to select him. If the coach does not select him, the contestant increases his
     * strength and waits again.
     *
     * @param team       the team
     * @param contestant the contestant
     * @param strength   the strength of the contestant
     * @return the strength of the contestant
     */
    @Override
    public int seatDown(int team, int contestant, int strength) {
        TeamData teamData = this.teamData[team];
        lock.lock();
        try {
            teamData.countSeatedDown++;
            teamData.selected[contestant] = false;
            teamData.strengths[contestant] = strength;
            generalRepository.seatDown(team, contestant, false);
            if (teamData.countSeatedDown == contestantsPerTeam) {
                teamData.seatedDown.signal();
            }
            while (!teamData.selected[contestant]) {
                teamData.teamAssembled.await(); // releases the lock and waits
                if (!teamData.selected[contestant] && teamData.strengths[contestant] < maxStrength) {
                   teamData.strengths[contestant]++; // stays seated, so strength increases
                   generalRepository.seatDown(team, contestant, true);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return teamData.strengths[contestant];
    }

    /**
     * The coach calls the contestants selected for the trial. The coach waits for all his contestants to seat down.
     * The coach alerts the contestants that the team has been assembled.
     *
     * @param team     the team
     * @param selected the selected contestants
     */
    @Override
    public void callContestants(int team, boolean[] selected) {
        TeamData teamData = this.teamData[team];
        lock.lock();
        try {
            while (teamData.countSeatedDown < contestantsPerTeam) {
                teamData.seatedDown.await(); // releases the lock and waits
            }
            teamData.selected = selected;
            generalRepository.callContestants(team);
            teamData.teamAssembled.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The contestant follows the coach advice.
     *
     * @param team the team
     * @param contestant the contestant
     * @return true if the match has not ended, false otherwise
     */
    @Override
    public boolean followCoachAdvice(int team, int contestant) {
        TeamData teamData = this.teamData[team];
        lock.lock();
        teamData.countSeatedDown--;
        generalRepository.followCoachAdvice(team, contestant);
        lock.unlock();
        return !teamData.isMatchEnd;
    }
}
