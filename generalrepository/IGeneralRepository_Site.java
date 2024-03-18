package generalrepository;

public interface IGeneralRepository_Site {
    /**
     * Set the new state of the coach when he waits for the referee.
     *
     * @param team      the team
     */
    void reviewNotes(int team);

    /**
     * Set the new state of the referee when he announces a new game.
     */
    void announceNewGame();

    /**
     * Set the new state of the referee when he calls a trial.
     */
    void callTrial();

    /**
     * Set the new state of the referee when he declares the game winner.
     *
     * @param team the team
     * @param knockout the knockout flag
     */
    void declareGameWinner(int team, boolean knockout);

    /**
     * Set the new state of the referee when he declares the match winner.
     *
     * @param team the team
     */
    void declareMatchWinner(int team);
}
