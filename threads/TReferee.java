package threads;

import playground.IPlayground_Referee;
import refereesite.IRefereeSite_Referee;

public class TReferee implements Runnable{

    private final IPlayground_Referee playground;
    private final IRefereeSite_Referee refereeSite;

    public TReferee(IPlayground_Referee playground, IRefereeSite_Referee refereeSite) {
        this.playground = playground;
        this.refereeSite = refereeSite;
    }

    @Override
    public void run() {
        playground.startTrial();
        System.out.println("Referee");
    }
}
