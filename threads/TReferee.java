package threads;

import playground.IPlayground_Referee;
import refereesite.IRefereeSite_Referee;

public class TReferee extends Thread {
    private final int N_TEAMS = 2; // Makes code more readable
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
        this.log("thread started");
        for (int match = 0; match < N_MATCHES; match++) {
            this.log("start of the match");
            int[] gameWins = new int[N_TEAMS];
            for (int game = 0; game < N_GAMES_PER_MATCH; game++) {
                this.refereeSite.announceNewGame();
                this.log("start of a game");
                int ropePosition = 0;
                for (int trial = 0; trial < N_TRIALS_PER_GAME; trial++) {
                    this.refereeSite.callTrial();
                    this.log("teams ready");
                    this.playground.startTrial();
                    this.log("wait for trial conclusion");
                    ropePosition = this.playground.assertTrialDecision();
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
                this.refereeSite.declareGameWinner(winTeamGame);
                this.log("end of a game");
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
            this.refereeSite.declareMatchWinner(winTeamMatch);
            this.log("end of the match");
        }
    }

    private void log(String msg) {
        System.out.printf("[Referee]: %s\n", msg);
    }
}
