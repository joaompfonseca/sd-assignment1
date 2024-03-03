package playground;

public interface IPlayground_Referee {

    /**
     * The referee starts a trial.
     */
    void startTrial();


    /**
     * The referee determines the result of a trial.
     */
    int assertTrialDecision();

}
