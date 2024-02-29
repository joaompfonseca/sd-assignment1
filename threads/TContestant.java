package threads;

import contestansbench.IContestansBench_Contestant;
import playground.IPlayground_Contestant;

public class TContestant implements Runnable {

    private final IContestansBench_Contestant contestansBench;
    private final IPlayground_Contestant playground;

    public TContestant(IContestansBench_Contestant contestansBench, IPlayground_Contestant playground) {
        this.contestansBench = contestansBench;
        this.playground = playground;
    }

    @Override
    public void run() {
        System.out.println("Contestant");
    }
}
