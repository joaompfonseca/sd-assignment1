package playground;

/**
 * Interface for the coach in the playground.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IPlayground_Coach {
    /**
     * The coach waits for the contestants from his team to be ready. The last coach informs the referee that the team is
     * ready. The coach waits for the trial to be decided by the referee.
     *
     * @param team the team
     */
    void informReferee(int team);
}
