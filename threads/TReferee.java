package threads;

import playground.IPlayground_Referee;
import refereesite.IRefereeSite_Referee;

public class TReferee extends Thread{

    private final IPlayground_Referee playground;
    private final IRefereeSite_Referee refereeSite;

    public TReferee(IPlayground_Referee playground, IRefereeSite_Referee refereeSite) {
        this.playground = playground;
        this.refereeSite = refereeSite;
    }

    @Override
    public void run() {
        this.log("started");
    }

    private void log(String msg) {
        System.out.printf("[Referee]: %s\n", msg);
    }
}
