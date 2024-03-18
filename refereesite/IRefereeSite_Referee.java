package refereesite;

/**
 * Interface for the referee in the referee site.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IRefereeSite_Referee {
    /**
     * The referee announces a new game.
     */
    void announceNewGame();

    /**
     * The referee calls the trial. The referee waits for the coaches to be ready to receive the command to call the
     * trial. The coaches will know the match has not ended.
     */
    void callTrial();

    /**
     * The referee declares the team that won the game.
     *
     * @param team the team that won the game
     * @param knockout true if the game was a knockout, false otherwise
     */
    void declareGameWinner(int team, boolean knockout);

    /**
     * The referee declares the team that won the match. The referee waits for the coaches to be ready to receive the
     * command to declare the match winner. The coaches will know the match has ended.
     *
     * @param team the team that won the match
     */
    void declareMatchWinner(int team);
}