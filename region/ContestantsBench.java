package region;

interface ContestantsBench {

    /**
     * The coach selects which contestants will play the next trial.
     * <br>
     * Called by COACH.
     */
    void callContestants();

    /**
     * The contestant sits down and waits for the next trial.
     * <br>
     * Called by CONTESTANT.
     */
    void seatDown();
}
