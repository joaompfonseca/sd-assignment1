package generalrepository;

/**
 * Possible states of the contestant.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public enum EGeneralRepository_Contestant {
    SEAT_AT_THE_BENCH("001"),
    STAND_IN_POSITION("002"),
    DO_YOUR_BEST("003"),
    ;
    public final String label;
    EGeneralRepository_Contestant(String label) {
        this.label = label;
    }
}
