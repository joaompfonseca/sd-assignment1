import contestantsbench.IContestantsBench;
import playground.IPlayground;
import playground.MPlayground;
import refereesite.IRefereeSite;
import refereesite.MRefereeSite;
import threads.TCoach;
import threads.TContestant;
import threads.TReferee;
import contestantsbench.MContestantsBench;

// Game of the Rope using ReentrantLocks
public class GameOfTheRope {
    private final static int N_GAMES_PER_MATCH = 3;
    private final static int N_TRIALS_PER_GAME = 6;
    private final static int N_CONTESTANTS_PER_TEAM = 5;
    private final static int N_CONTESTANTS_PER_TRIAL = 3;
    private final static int MAX_STRENGTH = 5;
    private final static int MAX_SLEEP_MS = 250;


    public static void main(String[] args) {

        // Information sharing regions
        IPlayground playground = new MPlayground(N_CONTESTANTS_PER_TRIAL);
        IContestantsBench contestantsBench = new MContestantsBench(N_CONTESTANTS_PER_TEAM, MAX_STRENGTH);
        IRefereeSite refereeSite = new MRefereeSite();
        // TODO: Initialize the general information repository

        // Referee
        Thread tReferee = new TReferee(playground, refereeSite, N_GAMES_PER_MATCH, N_TRIALS_PER_GAME);

        // Coaches
        Thread[] tCoaches = new Thread[2];
        for (int team = 0; team < 2; team++) {
            tCoaches[team] = new TCoach(contestantsBench, playground, refereeSite, team, N_CONTESTANTS_PER_TEAM, N_CONTESTANTS_PER_TRIAL);
        }

        // Contestants
        Thread[] tContestants = new Thread[2 * N_CONTESTANTS_PER_TEAM];
        for (int team = 0; team < 2; team++) {
            for (int number = 0; number < N_CONTESTANTS_PER_TEAM; number++) {
                tContestants[team * N_CONTESTANTS_PER_TEAM + number] = new TContestant(contestantsBench, playground, team, number, MAX_STRENGTH, MAX_SLEEP_MS);
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
