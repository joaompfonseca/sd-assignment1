package contestansbench;

public interface IContestantsBench_Contestant {

    /**
     * The contestant seats down on the bench after the trial.
     */
    void seatDown(int team, int contestantNumber);
    /**
     * The contestant is selected by the coach to play the next trial.
     */
    void followCoachAdvice(int team);
}
