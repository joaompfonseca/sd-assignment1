package threads;

import contestansbench.IContestantsBench_Contestant;
import playground.IPlayground_Contestant;

public class TContestant extends Thread {
    private final IContestantsBench_Contestant contestantsBench;
    private final IPlayground_Contestant playground;
    private final int number;
    private final int team;

    public TContestant(IContestantsBench_Contestant contestantsBench, IPlayground_Contestant playground, int number, int team) {
        this.contestantsBench = contestantsBench;
        this.playground = playground;
        this.number = number;
        this.team = team;
    }

    @Override
    public void run() {
        this.log("thread started");
        while (true) { // TODO: implement stopping condition
            this.contestantsBench.seatDown();
            this.log("seat at the bench");
            this.contestantsBench.followCoachAdvice();
            this.log("stand in position");
            this.playground.getReady();
            this.log("do your best");
            this.playground.pullTheRope();
            this.playground.amDone();
        }
        // this.log("thread finished");
    }

    private void log(String msg) {
        System.out.printf("[Team#%d-Cont#%d]: %s\n", team, number, msg);
    }
}
