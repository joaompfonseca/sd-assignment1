import contestansbench.IContestantsBench;
import playground.IPlayground;
import playground.MPlayground;
import refereesite.IRefereeSite;
import refereesite.MRefereeSite;
import threads.TCoach;
import threads.TContestant;
import threads.TReferee;
import contestansbench.MContestansBench;

// Game of the Rope using ReentrantLocks
public class GameOfTheRope {
    public static void main(String[] args) {

        //Create the playground
        IPlayground playground = new MPlayground();

        //Create the Contestants Bench
        IContestantsBench contestantsBench = new MContestansBench();

        //Create the Referee Site
        IRefereeSite refereeSite = new MRefereeSite();

        //Create the Coach Thread
        TCoach coach = new TCoach(playground,contestantsBench,refereeSite);
        Thread tCoach = new Thread(coach);

        //Create the Referee Thread
        TReferee referee = new TReferee(playground,refereeSite);
        Thread tReferee = new Thread(referee);

        //Create 10 Contestants Threads
        TContestant[] contestants = new TContestant[10];
        for (int i = 0; i < 10; i++) {
            contestants[i] = new TContestant(contestantsBench,playground);
        }

        //Start the threads
        tCoach.start();
        tReferee.start();
        for (int i = 0; i < 10; i++) {
            Thread tContestant = new Thread(contestants[i]);
            tContestant.start();
        }

        try {
            tCoach.join();
            tReferee.join();
            for (int i = 0; i < 10; i++) {
                Thread tContestant = new Thread(contestants[i]);
                tContestant.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
