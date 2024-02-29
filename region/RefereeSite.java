package region;

interface RefereeSite {

    /**
     * The referee announces a new game.
     * <br>
     * Called by REFEREE.
     */
    void announceNewGame();

    /**
     * The referee announces a new trial. The coaches need to select the contestants.
     * <br>
     * Called by REFEREE.
     */
    void callTrial();

    /**
     * The coach informs the referee that the selected contestants are ready to play the trial.
     * <br>
     * Called by COACH.
     */
    void informReferee();

    /**
     * The referee declares the winner of a game. A game has many trials.
     * <br>
     * Called by REFEREE.
     */
    void declareGameWinner();

    /**
     * The referee declares the winner of a match. A match has many games.
     * <br>
     * Called by REFEREE.
     */
    void declareMatchWinner();
}
