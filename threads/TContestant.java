package threads;

import contestantsbench.IContestantsBench_Contestant;
import playground.IPlayground_Contestant;

public class TContestant extends Thread {
    private final IContestantsBench_Contestant contestantsBench;
    private final IPlayground_Contestant playground;
    private final int team;
    private final int contestant;
    private int strength;
    private final int maxSleepMs;

    public TContestant(IContestantsBench_Contestant contestantsBench, IPlayground_Contestant playground, int team, int contestant, int strength, int maxSleepMs) {
        this.contestantsBench = contestantsBench;
        this.playground = playground;
        this.team = team;
        this.contestant = contestant;
        this.strength = strength;
        this.maxSleepMs = maxSleepMs;
    }

    @Override
    public void run() {
        log("thread started");
        while (true) {
            log("seat at the bench");
            strength = contestantsBench.seatDown(team, contestant, strength);
            boolean keepRunning = contestantsBench.followCoachAdvice(team);
            if (!keepRunning) {
                break;
            }
            log("stand in position");
            playground.getReady(team);
            log("do your best");
            try {
                Thread.sleep((long) (Math.random() * maxSleepMs));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            strength = playground.pullTheRope(team, strength);
            playground.amDone();
        }
        log("thread finished");
    }

    private void log(String msg) {
        System.out.printf("[Team#%d-Cont#%d]: %s\n", team, contestant, msg);
    }
}
