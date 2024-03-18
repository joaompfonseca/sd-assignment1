package generalrepository;

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
     */
    void pullTheRope(int team, int contestant);

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
