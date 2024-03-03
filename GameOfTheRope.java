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

    final static int N_MATCHES = 1;
    final static int N_GAMES_PER_MATCH = 3;
    final static int N_TRIALS_PER_GAME = 6;
    final static int N_TEAMS = 2;
    final static int N_CONTESTANTS_PER_TEAM = 5;
    final static int N_CONTESTANTS_PER_TRIAL = 3;


    public static void main(String[] args) {

        // Information sharing regions
        IPlayground playground = new MPlayground();
        IContestantsBench contestantsBench = new MContestantsBench();
        IRefereeSite refereeSite = new MRefereeSite();
        // TODO: Initialize the general information repository

        // Referee
        Thread tReferee = new TReferee(playground, refereeSite);

        // Coaches
        Thread[] tCoaches = new Thread[N_TEAMS];
        for (int team = 0; team < N_TEAMS; team++) {
            tCoaches[team] = new TCoach(contestantsBench, playground, refereeSite, team);
        }

        // Contestants
        Thread[] tContestants = new Thread[N_TEAMS * N_CONTESTANTS_PER_TEAM];
        for (int team = 0; team < N_TEAMS; team++) {
            for (int i = 0; i < N_CONTESTANTS_PER_TEAM; i++) {
                tContestants[team * N_CONTESTANTS_PER_TEAM + i] = new TContestant(contestantsBench, playground, team);
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
