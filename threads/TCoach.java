package threads;

import contestantsbench.IContestantsBench_Coach;
import playground.IPlayground_Coach;
import refereesite.IRefereeSite_Coach;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Coach thread.
 * <p>
 * The coach is the main entity responsible for the team's strategy. It selects the contestants to participate in each
 * trial and watches the trial to make sure everything goes according to plan.
 * <p>
 * The coach is also responsible for informing the contestants when the match is over.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class TCoach extends Thread {
    /**
     * Reference the contestants bench.
     */
    private final IContestantsBench_Coach contestantsBench;
    /**
     * Reference the playground.
     */
    private final IPlayground_Coach playground;
    /**
     * Reference the referee site.
     */
    private final IRefereeSite_Coach refereeSite;
    /**
     * The team number.
     */
    private final int team;
    /**
     * The number of contestants per team.
     */
    private final int contestantsPerTeam;
    /**
     * The number of contestants per trial.
     */
    private final int contestantsPerTrial;
    /**
     * The probability of making a mistake when selecting contestants.
     */
    private final double mistakeProbability;

    /**
     * Instantiation of a coach thread.
     *
     * @param contestantsBench    reference to the contestants bench
     * @param playground          reference to the playground
     * @param refereeSite         reference to the referee site
     * @param team                the team number
     * @param contestantsPerTeam  the number of contestants per team
     * @param contestantsPerTrial the number of contestants per trial
     * @param mistakeProbability  the probability of making a mistake when selecting contestants
     */
    public TCoach(IContestantsBench_Coach contestantsBench, IPlayground_Coach playground, IRefereeSite_Coach refereeSite, int team, int contestantsPerTeam, int contestantsPerTrial, double mistakeProbability) {
        this.contestantsBench = contestantsBench;
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.team = team;
        this.contestantsPerTeam = contestantsPerTeam;
        this.contestantsPerTrial = contestantsPerTrial;
        this.mistakeProbability = mistakeProbability;
    }

    /**
     * Selects the contestants to participate in the trial.
     *
     * @param strengths the strengths of the contestants
     * @return an array of booleans indicating which contestants were selected
     */
    private boolean[] selectContestants(int[] strengths) {
        // Tactic: choose contestants with the highest strength
        boolean[] selected = new boolean[contestantsPerTeam];
        int[][] strengthsIndexed = new int[contestantsPerTeam][2];
        for (int i = 0; i < contestantsPerTeam; i++) {
            strengthsIndexed[i][0] = strengths[i];
            strengthsIndexed[i][1] = i;
        }
        Arrays.sort(strengthsIndexed, Comparator.comparingInt(a -> -a[0]));
        for (int i = 0; i < contestantsPerTrial; i++) {
            selected[strengthsIndexed[i][1]] = true;
        }
        return selected;
    }

    /**
     * Selects the contestants to participate in the trial, with a probability of making a mistake.
     *
     * @param strengths          the strengths of the contestants
     * @param mistakeProbability the probability of making a mistake
     * @return an array of booleans indicating which contestants were selected
     */
    private boolean[] selectContestants(int[] strengths, double mistakeProbability) {
        // Tactic: after choosing the best contestants, randomly replace some to make mistakes
        boolean[] selected = selectContestants(strengths);
        for (int i = 0; i < contestantsPerTeam; i++) {
            if (selected[i] && Math.random() < mistakeProbability) {
                selected[i] = false;
                int j;
                do {
                    j = (int) (Math.random() * contestantsPerTeam);
                } while (selected[j]);
                selected[j] = true;
            }
        }
        return selected;
    }

    /**
     * Selects all contestants to participate in the trial.
     *
     * @return an array of booleans indicating which contestants were selected
     */
    private boolean[] selectAllContestants() {
        boolean[] selectedContestants = new boolean[contestantsPerTeam];
        Arrays.fill(selectedContestants, true);
        return selectedContestants;
    }

    /**
     * The coach's life cycle.
     */
    @Override
    public void run() {
        //log("thread started");
        while (true) {
            //log("wait for referee command");
            boolean keepRunning = refereeSite.reviewNotes(team);
            if (!keepRunning) {
                // let contestants know the match is over
                contestantsBench.setTeamIsMatchEnd(team, true);
                contestantsBench.callContestants(team, selectAllContestants());
                break;
            }
            //log("assemble team");
            int[] strengths = contestantsBench.getTeamStrengths(team);
            contestantsBench.callContestants(team, selectContestants(strengths, mistakeProbability));
            //log("watch trial");
            playground.informReferee(team);
        }
        //log("thread finished");
    }

    /**
     * Logs a message.
     *
     * @param msg the message to log
     */
    private void log(String msg) {
        System.out.printf("[Coach#%d]: %s\n", team, msg);
    }
}
