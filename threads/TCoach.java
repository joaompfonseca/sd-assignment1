package threads;

import contestansbench.IContestantsBench_Coach;
import refereesite.IRefereeSite_Coach;

import java.util.Arrays;
import java.util.Comparator;

public class TCoach extends Thread {
    private final int MIN_STRENGTH = 1;
    private final int MAX_STRENGTH = 5;
    private final IContestantsBench_Coach contestantsBench;
    private final IRefereeSite_Coach refereeSite;
    private final int team;
    private final int contestantsPerTrial;

    public TCoach(IContestantsBench_Coach contestantsBench, IRefereeSite_Coach refereeSite, int team, int contestantsPerTrial) {
        this.contestantsBench = contestantsBench;
        this.refereeSite = refereeSite;
        this.team = team;
        this.contestantsPerTrial = contestantsPerTrial;
    }

    @Override
    public void run() {
        this.log("thread started");
        int[] strengths;
        int[] selectedContestants = new int[contestantsPerTrial];
        for (int i = 0; i < contestantsPerTrial; i++) {
            selectedContestants[i] = i;
        }
        while (true) { // TODO: implement stopping condition
            this.log("wait for referee command");
            this.contestantsBench.callContestants(selectedContestants);
            this.log("assemble team");
            this.refereeSite.informReferee();
            this.log("watch trial");
            strengths = this.contestantsBench.reviewNotes();
            // Tactic: choose contestants with the highest strength
            // TODO: maybe allow for different tactics
            selectedContestants = Arrays.stream(strengths)
                    .boxed()
                    .sorted(Comparator.reverseOrder())
                    .limit(contestantsPerTrial)
                    .mapToInt(Integer::intValue)
                    .toArray();
        }
        // this.log("thread finished");
    }

    private void log(String msg) {
        System.out.printf("[Coach#%d]: %s\n", team, msg);
    }
}
