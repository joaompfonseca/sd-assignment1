package threads;

import contestantsbench.IContestantsBench_Contestant;
import playground.IPlayground_Contestant;

/**
 * Contestant thread.
 * <p>
 * The contestant is the main entity responsible for the team's performance. It waits for the coach's instructions and
 * then performs the trial.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class TContestant extends Thread {
    /**
     * Reference the contestants bench.
     */
    private final IContestantsBench_Contestant contestantsBench;
    /**
     * Reference the playground.
     */
    private final IPlayground_Contestant playground;
    /**
     * The team number.
     */
    private final int team;
    /**
     * The contestant number.
     */
    private final int contestant;
    /**
     * The contestant's strength.
     */
    private int strength;
    /**
     * The maximum sleep time in milliseconds.
     */
    private final int maxSleepMs;

    /**
     * Instantiation of a contestant thread.
     *
     * @param contestantsBench reference to the contestants bench
     * @param playground       reference to the playground
     * @param team             the team number
     * @param contestant       the contestant number
     * @param strength         the contestant's strength
     * @param maxSleepMs       the maximum sleep time in milliseconds
     */
    public TContestant(IContestantsBench_Contestant contestantsBench, IPlayground_Contestant playground, int team, int contestant, int strength, int maxSleepMs) {
        this.contestantsBench = contestantsBench;
        this.playground = playground;
        this.team = team;
        this.contestant = contestant;
        this.strength = strength;
        this.maxSleepMs = maxSleepMs;
    }

    /**
     * The contestant's life cycle.
     */
    @Override
    public void run() {
        while (true) {
            strength = contestantsBench.seatDown(team, contestant, strength);
            boolean keepRunning = contestantsBench.followCoachAdvice(team, contestant);
            if (!keepRunning) {
                break;
            }
            playground.getReady(team, contestant);
            try {
                Thread.sleep((long) (Math.random() * maxSleepMs));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            strength = playground.pullTheRope(team, strength, contestant);
            playground.amDone();
        }
    }
}
