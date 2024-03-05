import contestansbench.IContestantsBench;
import playground.IPlayground;
import playground.MPlayground;
import refereesite.IRefereeSite;
import refereesite.MRefereeSite;
import threads.TCoach;
import threads.TContestant;
import threads.TReferee;
import contestansbench.MContestantsBench;

// Game of the Rope using ReentrantLocks
public class GameOfTheRope {
    final static int N_CONTESTANTS_PER_TEAM = 5;
    final static int N_CONTESTANTS_PER_TRIAL = 3;


    public static void main(String[] args) {

        // Information sharing regions
        IPlayground playground = new MPlayground(N_CONTESTANTS_PER_TRIAL);
        IContestantsBench contestantsBench = new MContestantsBench(N_CONTESTANTS_PER_TEAM, N_CONTESTANTS_PER_TRIAL);
        IRefereeSite refereeSite = new MRefereeSite();
        // TODO: Initialize the general information repository

        // Referee
        Thread tReferee = new TReferee(playground, refereeSite);

        // Coaches
        Thread[] tCoaches = new Thread[2];
        for (int team = 0; team < 2; team++) {
            tCoaches[team] = new TCoach(contestantsBench, playground, refereeSite, team, N_CONTESTANTS_PER_TEAM, N_CONTESTANTS_PER_TRIAL);
        }

        // Contestants
        Thread[] tContestants = new Thread[2 * N_CONTESTANTS_PER_TEAM];
        for (int team = 0; team < 2; team++) {
            for (int number = 0; number < N_CONTESTANTS_PER_TEAM; number++) {
                tContestants[team * N_CONTESTANTS_PER_TEAM + number] = new TContestant(contestantsBench, playground, number, team);
            }
        }

        // Start the threads
        tReferee.start();
        for (Thread tCoach : tCoaches) {
            tCoach.start();
        }
        for (Thread contestant : tContestants) {
            contestant.start();
        }

        // Wait for the threads to finish
        try {
            tReferee.join();
            for (Thread tCoach : tCoaches) {
                tCoach.join();
            }
            for (Thread contestant : tContestants) {
                contestant.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
