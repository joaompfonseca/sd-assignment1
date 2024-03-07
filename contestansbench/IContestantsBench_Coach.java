package contestansbench;

public interface IContestantsBench_Coach {

    /**
     * The coach selects which contestants will play the next trial.
     */
    void callContestants(int team, boolean[] selectedContestants);

    /**
     * The coach asks for the contestants energy.
     */
    int[] getContestantsStrength(int team);
}
