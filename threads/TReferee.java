package threads;

import playground.IPlayground_Referee;
import refereesite.IRefereeSite_Referee;

/**
 * Referee thread.
 * <p>
 * The referee is the main entity responsible for the match. It starts the match, the games and the trials, and
 * declares the winner of the match.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public class TReferee extends Thread {
    /**
     * Reference the playground.
     */
    private final IPlayground_Referee playground;
    /**
     * Reference the referee site.
     */
    private final IRefereeSite_Referee refereeSite;
    /**
     * The number of games per match.
     */
    private final int gamesPerMatch;
    /**
     * The number of trials per game.
     */
    private final int trialsPerGame;

    /**
     * Instantiation of a referee thread.
     *
     * @param playground    reference to the playground
     * @param refereeSite   reference to the referee site
     * @param gamesPerMatch the number of games per match
     * @param trialsPerGame the number of trials per game
     */
    public TReferee(IPlayground_Referee playground, IRefereeSite_Referee refereeSite, int gamesPerMatch, int trialsPerGame) {
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.gamesPerMatch = gamesPerMatch;
        this.trialsPerGame = trialsPerGame;
    }

    /**
     * The referee thread life cycle.
     */
    @Override
    public void run() {
        log("thread started");
        log("start of the match");
        int[] gameWins = new int[2];
        for (int game = 0; game < gamesPerMatch; game++) {
            log("start of a game");
            refereeSite.announceNewGame();
            int ropePosition = 0;
            playground.setRopePosition(ropePosition);
            for (int trial = 0; trial < trialsPerGame; trial++) {
                log("teams ready");
                refereeSite.callTrial();
                log("wait for trial conclusion");
                playground.startTrial();
                ropePosition = playground.assertTrialDecision();
                if (Math.abs(ropePosition) >= 4) {
                    break; // Team won by knockout
                }
            }
            log("end of a game");
            int winTeamGame;
            if (ropePosition < 0) {
                winTeamGame = 0; // Team 0 won the game
                gameWins[0]++;
            } else if (ropePosition > 0) {
                winTeamGame = 1; // Team 1 won the game
                gameWins[1]++;
            } else {
                winTeamGame = -1; // Draw
            }
            refereeSite.declareGameWinner(winTeamGame);
        }
        int winTeamMatch;
        if (gameWins[0] > gameWins[1]) {
            winTeamMatch = 0; // Team 0 won the match
        } else if (gameWins[0] < gameWins[1]) {
            winTeamMatch = 1; // Team 1 won the match
        } else {
            winTeamMatch = -1; // Draw
        }
        log("end of the match");
        refereeSite.declareMatchWinner(winTeamMatch);
        log("thread finished");
    }

    /**
     * Logs a message.
     *
     * @param msg the message to log
     */
    private void log(String msg) {
        System.out.printf("[Referee]: %s\n", msg);
    }
}
