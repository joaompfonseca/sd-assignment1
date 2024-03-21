package generalrepository;

/**
 * Interface for the playground in the general repository.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IGeneralRepository_Playground {
    /**
     * Set the new state of the contestant when he is getting ready.
     *
     * @param team      the team
     * @param contestant the contestant
     */
    void getReady(int team, int contestant);

    /**
     * Set the new state of the coach when he informs the referee.
     *
     * @param team the team
     */
    void informReferee(int team);

    /**
     * Set the new state of the referee when he starts the trial.
     */
    void startTrial();

    /**
     * Set the new state of the contestant when he pulls the rope.
     *
     * @param team     the team
     * @param contestant  the contestant
     * @param reduce if the strength of the contestant should be reduced
     */
    void pullTheRope(int team, int contestant, boolean reduce);

    /**
     * Set the new state of the contestant when he's done.
     */
    void amDone();

    /**
     * Set the new state of the referee when he asserts the trial decision.
     *
     * @param ropePosition the rope position
     */
    void assertTrialDecision(int ropePosition);
}
