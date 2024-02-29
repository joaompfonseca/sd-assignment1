package playground;

public interface IPlayground_Contestant {

    /**
     * The contestant joins the playground to play a trial.
     */
    void followCoachAdvice();

    /**
     * The contestant is ready to play the trial.
     */
    void getReady();

    /**
     * The contestant pulls the rope once.
     */
    void pullTheRope();

    /**
     * The contestant announces that he has finished pulling the rope.
     */
    void amDone();
}
