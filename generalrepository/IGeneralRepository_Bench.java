package generalrepository;

/**
 * Interface for the bench in the general repository.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public interface IGeneralRepository_Bench {
    /**
     * Set the new state of the contestant when he seats down.
     *
     * @param team the team
     * @param id the contestant id
     * @param increaseStrength if the strength of the contestant should be increased
     */
    void seatDown(int team, int id, boolean increaseStrength);

    /**
     * Set the new state of the coach when he calls the contestants.
     *
     * @param team     the team
     */
    void callContestants(int team);

    /**
     * Set the new state of the contestant when he is called by the coach.
     *
     * @param team     the team
     * @param id       the contestant id
     */
    void followCoachAdvice(int team, int id);
}
