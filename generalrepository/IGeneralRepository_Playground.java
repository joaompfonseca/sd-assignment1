package generalrepository;

public interface IGeneralRepository_Playground {

    void getReady(int team, int contestant);

    void informReferee(int team);

    void startTrial();

    void pullTheRope(int team, int contestant);

    void amDone();

    void assertTrialDecision(int ropePosition);
}
