package refereesite;

/**
 * Interface for the coach in the referee site.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IRefereeSite_Coach {
    /**
     * The coach waits for the referee command.
     *
     * @return true if the match has not ended, false otherwise
     */
    boolean reviewNotes();
}
