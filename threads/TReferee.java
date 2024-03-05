package threads;

import playground.IPlayground_Referee;
import refereesite.IRefereeSite_Referee;

public class TReferee extends Thread {
    private final int N_MATCHES = 1;
    private final int N_GAMES_PER_MATCH = 3;
    private final int N_TRIALS_PER_GAME = 6;
    private final IPlayground_Referee playground;
    private final IRefereeSite_Referee refereeSite;

    public TReferee(IPlayground_Referee playground, IRefereeSite_Referee refereeSite) {
        this.playground = playground;
        this.refereeSite = refereeSite;
    }

    @Override
    public void run() {
        log("thread started");
        for (int match = 0; match < N_MATCHES; match++) {
            log("start of the match");
            int[] gameWins = new int[2];
            for (int game = 0; game < N_GAMES_PER_MATCH; game++) {
                refereeSite.announceNewGame();
                log("start of a game");
                int ropePosition = 0;
                for (int trial = 0; trial < N_TRIALS_PER_GAME; trial++) {
                    refereeSite.callTrial();
                    log("teams ready");
                    playground.startTrial();
                    log("wait for trial conclusion");
                    int shift = playground.assertTrialDecision();
                    ropePosition =+ shift;
                    if (Math.abs(ropePosition) >= 4) {
                        break; // Team won by knockout
                    }
                }
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
                log("end of a game");
            }
            int winTeamMatch;
            if (gameWins[0] > gameWins[1]) {
                winTeamMatch = 0; // Team 0 won the match
            }
            else if (gameWins[0] < gameWins[1]) {
                winTeamMatch = 1; // Team 1 won the match
            }
            else {
                winTeamMatch = -1; // Draw
            }
            refereeSite.declareMatchWinner(winTeamMatch);
            log("end of the match");
        }
        log("thread finished");
    }

    private void log(String msg) {
        System.out.printf("[Referee]: %s\n", msg);
    }
}
