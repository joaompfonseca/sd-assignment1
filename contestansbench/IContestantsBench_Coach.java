package contestansbench;

public interface IContestantsBench_Coach {
    void callContestants(int team, boolean[] selectedContestants);

    // Extra
    int[] getTeamStrengths(int team);

    void setTeamIsMatchEnd(int team, boolean isMatchEnd);
}
