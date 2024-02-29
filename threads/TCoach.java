package threads;

import contestansbench.IContestansBench_Coach;
import playground.IPlayground_Coach;
import refereesite.IRefereeSite_Coach;

public class TCoach implements Runnable {

    private final IPlayground_Coach playground;
    private final IContestansBench_Coach contestansBench;
    private final IRefereeSite_Coach refereeSite;

    public TCoach(IPlayground_Coach playground, IContestansBench_Coach contestansBench, IRefereeSite_Coach refereeSite) {
        this.playground = playground;
        this.contestansBench = contestansBench;
        this.refereeSite = refereeSite;
    }

    @Override
    public void run() {
        playground.reviewNotes();
        System.out.println("Coach");
    }
}
