package generalrepository;

/**
 * Possible states of the coach.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public enum EGeneralRepository_Coach {
    WAIT_FOR_REFEREE_COMMAND("001"),
    ASSEMBLE_TEAM("002"),
    WATCH_TRIAL("003"),
    ;
    public final String label;
    EGeneralRepository_Coach(String label) {
        this.label = label;
    }
}
