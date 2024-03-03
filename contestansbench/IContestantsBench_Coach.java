package contestansbench;

public interface IContestantsBench_Coach {

    /**
     * The coach gets information about contestants' strengths.
     */
    int[] reviewNotes();

    /**
     * The coach selects which contestants will play the next trial.
     */
    void callContestants(int[] contestants);
}
