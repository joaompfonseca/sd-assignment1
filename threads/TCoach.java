package threads;

import contestansbench.IContestantsBench_Coach;
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

    public TCoach(IContestantsBench_Coach contestantsBench, IPlayground_Coach playground, IRefereeSite_Coach refereeSite, int team, int contestantsPerTeam, int contestantsPerTrial) {
        this.contestantsBench = contestantsBench;
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.team = team;
        this.contestantsPerTeam = contestantsPerTeam;
        this.contestantsPerTrial = contestantsPerTrial;
    }

    private boolean[] selectContestants(int[] strengths) {
        // Tactic: choose contestants with the highest strength
        // TODO: maybe allow for different tactics
        Integer[] indexes = new Integer[strengths.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, Comparator.comparingInt(i -> strengths[i]));
        boolean[] selectedContestants = new boolean[contestantsPerTeam];
        for (int i = 0; i < contestantsPerTrial; i++) {
            selectedContestants[indexes[i]] = true;
        }
        for (int i = contestantsPerTrial; i < contestantsPerTeam; i++) {
            selectedContestants[indexes[i]] = false;
        }
        return selectedContestants;
    }

    @Override
    public void run() {
        log("thread started");
        while (true) { // TODO: implement stopping condition
            log("wait for referee command");
            int[] strengths = contestantsBench.getContestantsStrength(team);
            boolean[] selectedContestants = selectContestants(strengths);
            contestantsBench.callContestants(team, selectedContestants);
            log("assemble team");
            refereeSite.informReferee();
            log("watch trial");
            int information = playground.reviewNotes(team); // TODO: what information should be returned?
        }
        // log("thread finished");
    }

    private void log(String msg) {
        System.out.printf("[Coach#%d]: %s\n", team, msg);
    }
}
