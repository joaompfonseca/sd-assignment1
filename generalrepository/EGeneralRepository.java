package generalrepository;

public enum EGeneralRepository {
    START_OF_THE_MATCH("001"),
    START_OF_A_GAME("002"),
    TEAMS_READY("003"),
    WAIT_FOR_TRIAL_CONCLUSION("004"),
    END_OF_A_GAME("005"),
    END_OF_THE_MATCH("006"),
    WAIT_FOR_REFEREE_COMMAND("001"),
    ASSEMBLE_TEAM("002"),
    WATCH_TRIAL("003"),
    SEAT_AT_THE_BENCH("001"),
    STAND_IN_POSITION("002"),
    DO_YOUR_BEST("003"),
    ;

    public final String label;
    EGeneralRepository(String label) {
        this.label = label;
    }
}
