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
        log("thread started");
        while (true) {
            log("seat at the bench");
            contestantsBench.seatDown(team, number);
            boolean keepRunning = contestantsBench.followCoachAdvice(team);
            if (!keepRunning) {
                break;
            }
            log("stand in position");
            playground.getReady(team);
            log("do your best");
            playground.pullTheRope();
            playground.amDone();
        }
        log("thread finished");
    }

    private void log(String msg) {
        System.out.printf("[Team#%d-Cont#%d]: %s\n", team, number, msg);
    }
}
