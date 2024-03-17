package playground;

/**
 * Interface for the contestant in the playground.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IPlayground_Contestant {
    /**
     * The last contestant from each team informs the coach that they are ready. The contestant waits for the trial to
     * start by the referee.
     *
     * @param team the team
     */
    void getReady(int team);

    /**
     * The contestant pulls the rope with a similar distance as his current strength. The contestant loses 1 unit of
     * strength after pulling the rope.
     *
     * @param team     the team
     * @param strength the current strength
     * @return the updated strength
     */
    int pullTheRope(int team, int strength);

    /**
     * The last contestant informs the referee that the trial is over. The contestant waits for the referee to decide
     * the result of the trial.
     */
    void amDone();
}
