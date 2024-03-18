package playground;

public interface IPlayground_Contestant {
    void getReady(int team, int contestant);

    int pullTheRope(int team, int strength, int contestant);

    void amDone();
}
