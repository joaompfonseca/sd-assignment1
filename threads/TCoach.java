package threads;

import contestansbench.IContestantsBench_Coach;
import playground.IPlayground_Coach;
import refereesite.IRefereeSite_Coach;

public class TCoach extends Thread {
    private final IContestantsBench_Coach contestantsBench;

    private final IPlayground_Coach playground;
    private final IRefereeSite_Coach refereeSite;
    private final int team;

    public TCoach(IContestantsBench_Coach contestantsBench, IPlayground_Coach playground, IRefereeSite_Coach refereeSite, int team) {
        this.contestantsBench = contestantsBench;
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.team = team;
    }

    @Override
    public void run() {
        playground.reviewNotes();
        System.out.println("Coach");
    }
}
