package threads;

import contestansbench.IContestantsBench_Contestant;
import playground.IPlayground_Contestant;

public class TContestant extends Thread {
    private final int MIN_STRENGTH = 1;
    private final int MAX_STRENGTH = 5;

    private final IContestantsBench_Contestant contestantsBench;
    private final IPlayground_Contestant playground;
    private final int team;
    private final int strength;

    public TContestant(IContestantsBench_Contestant contestantsBench, IPlayground_Contestant playground, int team) {
        this.contestantsBench = contestantsBench;
        this.playground = playground;
        this.team = team;
        this.strength = MAX_STRENGTH;
    }

    @Override
    public void run() {
        System.out.println("Contestant");
    }
}
