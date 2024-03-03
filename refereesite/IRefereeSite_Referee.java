package refereesite;

public interface IRefereeSite_Referee {

    /**
     * The referee announces a new game.
     */
    void announceNewGame();

    /**
     * The referee announces a new trial. The coaches need to select the contestants.
     */
    void callTrial();

    /**
     * The referee declares the winner of a game. A game has many trials.
     */
    void declareGameWinner(int winTeam);

    /**
     * The referee declares the winner of a match. A match has many games.
     */
    void declareMatchWinner(int winTeam);
}
