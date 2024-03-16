package threads;

import contestantsbench.IContestantsBench_Coach;
import playground.IPlayground_Coach;
import refereesite.IRefereeSite_Coach;

import java.util.Arrays;
import java.util.Comparator;

public class TCoach extends Thread {
    private final IContestantsBench_Coach contestantsBench;
    private final IPlayground_Coach playground;
    private final IRefereeSite_Coach refereeSite;
    private final int team;
    private final int contestantsPerTeam;
    private final int contestantsPerTrial;
    private final double mistakeProbability;

    public TCoach(IContestantsBench_Coach contestantsBench, IPlayground_Coach playground, IRefereeSite_Coach refereeSite, int team, int contestantsPerTeam, int contestantsPerTrial, double mistakeProbability) {
        this.contestantsBench = contestantsBench;
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.team = team;
        this.contestantsPerTeam = contestantsPerTeam;
        this.contestantsPerTrial = contestantsPerTrial;
        this.mistakeProbability = mistakeProbability;
    }

    private boolean[] selectContestants(int[] strengths) {
        // Tactic: choose contestants with the highest strength
        // TODO: maybe allow for different tactics
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

    private boolean[] selectAllContestants() {
        boolean[] selectedContestants = new boolean[contestantsPerTeam];
        Arrays.fill(selectedContestants, true);
        return selectedContestants;
    }

    @Override
    public void run() {
        log("thread started");
        while (true) {
            log("wait for referee command");
            boolean keepRunning = refereeSite.reviewNotes();
            if (!keepRunning) {
                // let contestants know the match is over
                contestantsBench.setTeamIsMatchEnd(team, true);
                contestantsBench.callContestants(team, selectAllContestants());
                break;
            }
            log("assemble team");
            int[] strengths = contestantsBench.getTeamStrengths(team);
            contestantsBench.callContestants(team, selectContestants(strengths, mistakeProbability));
            log("watch trial");
            playground.informReferee(team);
        }
        log("thread finished");
    }

    private void log(String msg) {
        System.out.printf("[Coach#%d]: %s\n", team, msg);
    }
}
