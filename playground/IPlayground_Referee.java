package playground;

/**
 * Interface of the referee in the playground.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IPlayground_Referee {
    /**
     * The referee waits for the coaches to be ready to announce the start of the trial.
     */
    void startTrial();

    /**
     * The referee waits for the contestants to be done to decide the result of the trial.
     *
     * @return the rope position
     */
    int assertTrialDecision();

    /**
     * The referee sets the rope position.
     *
     * @param ropePosition the rope position
     */
    void setRopePosition(int ropePosition);
}
