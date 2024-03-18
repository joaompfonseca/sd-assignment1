package generalrepository;

public enum EGeneralRepository {
    START_OF_THE_MATCH("STM"),
    START_OF_A_GAME("STG"),
    TEAMS_READY("TR"),
    WAIT_FOR_TRIAL_CONCLUSION("WTC"),
    END_OF_A_GAME("ETG"),
    END_OF_THE_MATCH("ETM"),
    WAIT_FOR_REFEREE_COMMAND("WRC"),
    ASSEMBLE_TEAM("AT"),
    WATCH_TRIAL("WT"),
    SEAT_AT_THE_BENCH("SAB"),
    STAND_IN_POSITION("SIP"),
    DO_YOUR_BEST("DYB"),
    ;

    public final String label;
    EGeneralRepository(String label) {
        this.label = label;
    }
}
