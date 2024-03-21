package generalrepository;

/**
 * Possible states of the referee.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public enum EGeneralRepository_Referee {
    START_OF_THE_MATCH("001"),
    START_OF_A_GAME("002"),
    TEAMS_READY("003"),
    WAIT_FOR_TRIAL_CONCLUSION("004"),
    END_OF_A_GAME("005"),
    END_OF_THE_MATCH("006"),
    ;

    public final String label;
    EGeneralRepository_Referee(String label) {
        this.label = label;
    }
}
