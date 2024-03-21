package generalrepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static generalrepository.EGeneralRepository.*;

public class MGeneralRepository implements IGeneralRepository {
    /**
     * Representation of a contestant.
     */
    private static class Contestant {
        /**
         * The status of the contestant.
         */
        private String status;
        /**
         * The strength of the contestant.
         */
        private int strength;

        /**
         * Instantiation of a contestant.
         *
         * @param status the status of the contestant
         * @param strength the strength of the contestant
         */
        public Contestant(String status, int strength) {
            this.status = status;
            this.strength = strength;
        }
    }

    /**
     * The referee status.
     */
    private String refereeStatus;
    /**
     * The coaches team 1 status.
     */
    private String coachesTeam1Status;
    /**
     * The coaches team 2 status.
     */
    private String coachesTeam2Status;
    /**
     * The contestants team 1.
     */
    private final HashMap<Integer, Contestant> contestantsTeam1;
    /**
     * The contestants team 2.
     */
    private final HashMap<Integer, Contestant> contestantsTeam2;
    /**
     * The selected contestants team 1.
     */
    private final ArrayList<Integer> selectedContestantsTeam1;
    /**
     * The selected contestants team 2.
     */
    private final ArrayList<Integer> selectedContestantsTeam2;
    /**
     * The won games.
     */
    private final HashMap<Integer,Integer> wonGames;
    /**
     * The rope position.
     */
    private int ropePosition;
    /**
     * The next rope position.
     */
    private int nextRopePosition;
    /**
     * The number of trials.
     */
    private int nTrials;
    /**
     * The number of games.
     */
    private int nGames;
    /**
     * The match end flag.
     */
    private boolean matchEnd;
    /**
     * The lock.
     */
    private final ReentrantLock lock;
    /**
     * The file writer.
     */
    private final PrintWriter fileWriter;

    /**
     * Instantiation of the general repository.
     *
     * @param nContestants the number of contestants
     */
    public MGeneralRepository(int nContestants){
        this.refereeStatus = START_OF_THE_MATCH.label;
        this.coachesTeam1Status = null;
        this.coachesTeam2Status = null;
        this.contestantsTeam1 = new HashMap<>();
        for (int i = 0; i < nContestants; i++) {
            this.contestantsTeam1.put(i, new Contestant(null, 5));
        }
        this.contestantsTeam2 = new HashMap<>();
        for (int i = 0; i < nContestants; i++) {
            this.contestantsTeam2.put(i, new Contestant(null, 5));
        }
        this.selectedContestantsTeam1 = new ArrayList<>();
        this.selectedContestantsTeam2 = new ArrayList<>();
        this.wonGames = new HashMap<>();
        for (int i = 0; i <= 1; i++) {
            this.wonGames.put(i, 0);
        }
        this.ropePosition = 100;
        this.nTrials = 0;
        this.nGames = 0;

        this.lock = new ReentrantLock();

        // Timestamp
        Instant now = Instant.now();
        String timestamp = now.toString();
        timestamp = timestamp.split("\\.")[0];
        String filename = "logs/log_" + timestamp + ".log";

        // Check if folder logs exists if not create it
        File file = new File("logs");
        if (!file.exists()) {
            boolean created = file.mkdir();
            if (!created) {
                throw new RuntimeException("Error creating logs folder");
            }
        }

        try {
            this.fileWriter = new PrintWriter(filename);
        } catch (Exception e) {
            throw new RuntimeException("Error creating file writer", e);
        }

        fileWriter.printf("                               Game of the Rope - Description of the internal state%n%n");
    }

    /**
     * Set the new state of the contestant when he seats down.
     *
     * @param team the team
     * @param id the contestant id
     * @param increaseStrength if the strength of the contestant should be increased
     */
    @Override
    public void seatDown(int team, int id, boolean increaseStrength) {
        lock.lock();

        if(team == 0) {
            contestantsTeam1.get(id).status = SEAT_AT_THE_BENCH.label;
            if (increaseStrength) {
                contestantsTeam1.get(id).strength++;
            }
        } else {
            contestantsTeam2.get(id).status = SEAT_AT_THE_BENCH.label;
            if (increaseStrength) {
                contestantsTeam2.get(id).strength++;
            }
        }

        if(!matchEnd) {
            print();
        }

        lock.unlock();
    }

    /**
     * Set the new state of the coach when he calls the contestants.
     *
     * @param team     the team
     */
    @Override
    public void callContestants(int team) {
        lock.lock();

        if(team == 0) {
            coachesTeam1Status = ASSEMBLE_TEAM.label;
        } else {
            coachesTeam2Status = ASSEMBLE_TEAM.label;
        }

        if(!matchEnd) {
            print();
        }

        lock.unlock();
    }

    /**
     * Set the new state of the contestant when he is called by the coach.
     *
     * @param team     the team
     * @param id       the contestant id
     */
    @Override
    public void followCoachAdvice(int team, int id) {
        lock.lock();

        if(team == 0) {
            contestantsTeam1.get(id).status = STAND_IN_POSITION.label;
        } else {
            contestantsTeam2.get(id).status = STAND_IN_POSITION.label;
        }

        if(!matchEnd) {
            print();
        }

        lock.unlock();
    }

    /**
     * Set the new state of the contestant when he is getting ready.
     *
     * @param team      the team
     * @param id        the id contestant
     */
    @Override
    public void getReady(int team, int id) {
        lock.lock();

        if(team == 0) {
            contestantsTeam1.get(id).status = DO_YOUR_BEST.label;
            selectedContestantsTeam1.add(id);
        } else {
            contestantsTeam2.get(id).status = DO_YOUR_BEST.label;
            selectedContestantsTeam2.add(id);
        }

        print();

        lock.unlock();
    }

    /**
     * Set the new state of the coach when he informs the referee.
     *
     * @param team the team
     */
    @Override
    public void informReferee(int team) {
        lock.lock();

        if(team == 0) {
            coachesTeam1Status = WATCH_TRIAL.label;
        } else {
            coachesTeam2Status = WATCH_TRIAL.label;
        }

        print();

        lock.unlock();
    }

    /**
     * Set the new state of the referee when he starts the trial.
     */
    @Override
    public void startTrial() {
        lock.lock();

        refereeStatus = WAIT_FOR_TRIAL_CONCLUSION.label;

        if (ropePosition == 100) {
            ropePosition = 0;
        } else {
            ropePosition = nextRopePosition;
        }

        print();

        lock.unlock();
    }

    /**
     * Set the new state of the contestant when he pulls the rope.
     *
     * @param team     the team
     * @param contestant  the contestant
     * @param reduce if the strength of the contestant should be reduced
     */
    @Override
    public void pullTheRope(int team, int contestant, boolean reduce) {
        lock.lock();

        if(team == 0) {
            contestantsTeam1.get(contestant).status = DO_YOUR_BEST.label;
            if (reduce) {
                contestantsTeam1.get(contestant).strength--;
            }
        } else {
            contestantsTeam2.get(contestant).status = DO_YOUR_BEST.label;
            if (reduce) {
                contestantsTeam2.get(contestant).strength--;
            }
        }

        print();

        lock.unlock();
    }

    /**
     * Set the new state of the contestant when he's done.
     */
    @Override
    public void amDone() {
        lock.lock();

        // It's not necessary to do anything here because the contestant doesn't need to change its status or strength

        print();

        lock.unlock();
    }

    /**
     * Set the new state of the referee when he asserts the trial decision.
     *
     * @param p the rope position
     */
    @Override
    public void assertTrialDecision(int p) {
        lock.lock();

        // Save rope position
        nextRopePosition = p;

        print();

        lock.unlock();
    }

    /**
     * Set the new state of the coach when he waits for the referee.
     *
     * @param team      the team
     */
    @Override
    public void reviewNotes(int team) {
        lock.lock();

        if (team == 0) {
            coachesTeam1Status = WAIT_FOR_REFEREE_COMMAND.label;
        } else {
            coachesTeam2Status = WAIT_FOR_REFEREE_COMMAND.label;
        }

        if(!matchEnd){
            print();
        }

        lock.unlock();
    }

    /**
     * Set the new state of the referee when he announces a new game.
     */
    @Override
    public void announceNewGame() {
        lock.lock();

        refereeStatus = START_OF_A_GAME.label;
        ropePosition = 100;
        nTrials = 0;
        nGames++;

        fileWriter.println("Game " + nGames);
        fileWriter.printf("Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5       Trial        %n");
        fileWriter.printf("Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS%n");

        print();

        lock.unlock();
    }

    /**
     * Set the new state of the referee when he calls a trial.
     */
    @Override
    public void callTrial() {
        lock.lock();

        selectedContestantsTeam1.clear();
        selectedContestantsTeam2.clear();

        refereeStatus = TEAMS_READY.label;

        nTrials++;

        print();

        lock.unlock();
    }

    /**
     * Set the new state of the referee when he declares the game winner.
     *
     * @param team the team
     * @param knockout the knockout flag
     */
    @Override
    public void declareGameWinner(int team, boolean knockout) {
        lock.lock();

        refereeStatus = END_OF_A_GAME.label;

        print();

        if(team == 0){
            wonGames.put(0, wonGames.get(0) + 1);
        } else if (team == 1) {
            wonGames.put(1, wonGames.get(1) + 1);
        }

        String output = "Game " + nGames;
        if (team == -1) {
            output += " was a draw.";
        } else if (knockout) {
            output += " was won by team " + team + " by knockout out in " + nTrials + " trials.";
        } else {
            output += " ended by points.";
        }
        fileWriter.println(output);

        lock.unlock();
    }

    /**
     * Set the new state of the referee when he declares the match winner.
     *
     * @param team the team
     */
    @Override
    public void declareMatchWinner(int team) {
        lock.lock();

        refereeStatus = END_OF_THE_MATCH.label;

        print();

        String output;
        if (team == -1) {
            output = "The match ended in a draw.";
        } else {
            output = "The match was won by team " + team + " (" + wonGames.get(0) + "-" + wonGames.get(1) + ").";
        }
        fileWriter.println(output);

        // Close file writer
        fileWriter.close();

        matchEnd = true;

        lock.unlock();
    }

    /**
     * Print the current state of the game.
     */
    public void print() {

        String rStatus = refereeStatus == null ? "###" : refereeStatus;

        String cTeam1Status = coachesTeam1Status == null ? "####" : coachesTeam1Status;
        String cTeam2Status = coachesTeam2Status == null ? "####" : coachesTeam2Status;

        String c1Team1Status = contestantsTeam1.get(0).status == null ? "###" : contestantsTeam1.get(0).status;
        String c2Team1Status = contestantsTeam1.get(1).status == null ? "###" : contestantsTeam1.get(1).status;
        String c3Team1Status = contestantsTeam1.get(2).status == null ? "###" : contestantsTeam1.get(2).status;
        String c4Team1Status = contestantsTeam1.get(3).status == null ? "###" : contestantsTeam1.get(3).status;
        String c5Team1Status = contestantsTeam1.get(4).status == null ? "###" : contestantsTeam1.get(4).status;
        String c1Team2Status = contestantsTeam2.get(0).status == null ? "###" : contestantsTeam2.get(0).status;
        String c2Team2Status = contestantsTeam2.get(1).status == null ? "###" : contestantsTeam2.get(1).status;
        String c3Team2Status = contestantsTeam2.get(2).status == null ? "###" : contestantsTeam2.get(2).status;
        String c4Team2Status = contestantsTeam2.get(3).status == null ? "###" : contestantsTeam2.get(3).status;
        String c5Team2Status = contestantsTeam2.get(4).status == null ? "###" : contestantsTeam2.get(4).status;

        String c1Team1Strength = contestantsTeam1.get(0).status == null ? "##" : Integer.toString(contestantsTeam1.get(0).strength);
        String c2Team1Strength = contestantsTeam1.get(1).status == null ? "##" : Integer.toString(contestantsTeam1.get(1).strength);
        String c3Team1Strength = contestantsTeam1.get(2).status == null ? "##" : Integer.toString(contestantsTeam1.get(2).strength);
        String c4Team1Strength = contestantsTeam1.get(3).status == null ? "##" : Integer.toString(contestantsTeam1.get(3).strength);
        String c5Team1Strength = contestantsTeam1.get(4).status == null ? "##" : Integer.toString(contestantsTeam1.get(4).strength);
        String c1Team2Strength = contestantsTeam2.get(0).status == null ? "##" : Integer.toString(contestantsTeam2.get(0).strength);
        String c2Team2Strength = contestantsTeam2.get(1).status == null ? "##" : Integer.toString(contestantsTeam2.get(1).strength);
        String c3Team2Strength = contestantsTeam2.get(2).status == null ? "##" : Integer.toString(contestantsTeam2.get(2).strength);
        String c4Team2Strength = contestantsTeam2.get(3).status == null ? "##" : Integer.toString(contestantsTeam2.get(3).strength);
        String c5Team2Strength = contestantsTeam2.get(4).status == null ? "##" : Integer.toString(contestantsTeam2.get(4).strength);

        String nT = nTrials == 0 ? "##" : Integer.toString(this.nTrials);
        String rP = ropePosition == 100 ? "##" : Integer.toString(this.ropePosition);

        String s1Team1, s2Team1, s3Team1, s1Team2, s2Team2, s3Team2;

        if (!selectedContestantsTeam1.isEmpty()) {
            s1Team1 = Integer.toString(selectedContestantsTeam1.getFirst());
        } else {
            s1Team1 = "#";
        }

        if (1 < selectedContestantsTeam1.size()) {
            s2Team1 = Integer.toString(selectedContestantsTeam1.get(1));
        } else {
            s2Team1 = "#";
        }

        if (2 < selectedContestantsTeam1.size()) {
            s3Team1 = Integer.toString(selectedContestantsTeam1.get(2));
        } else {
            s3Team1 = "#";
        }

        if (!selectedContestantsTeam2.isEmpty()) {
            s1Team2 = Integer.toString(selectedContestantsTeam2.getFirst());
        } else {
            s1Team2 = "#";
        }

        if (1 < selectedContestantsTeam2.size()) {
            s2Team2 = Integer.toString(selectedContestantsTeam2.get(1));
        } else {
            s2Team2 = "#";
        }

        if (2 < selectedContestantsTeam2.size()) {
            s3Team2 = Integer.toString(selectedContestantsTeam2.get(2));
        } else {
            s3Team2 = "#";
        }

        fileWriter.printf("%3s  %4s %3s %2s %3s %2s %3s %2s %3s %2s %3s %2s  %4s %3s %2s %3s %2s %3s %2s %3s %2s %3s %2s %1s %1s %1s . %1s %1s %1s %2s %2s%n",
                rStatus, cTeam1Status, c1Team1Status, c1Team1Strength, c2Team1Status, c2Team1Strength, c3Team1Status, c3Team1Strength, c4Team1Status, c4Team1Strength, c5Team1Status, c5Team1Strength,
                cTeam2Status, c1Team2Status, c1Team2Strength, c2Team2Status, c2Team2Strength, c3Team2Status, c3Team2Strength, c4Team2Status, c4Team2Strength, c5Team2Status, c5Team2Strength,
                s1Team1, s2Team1, s3Team1, s1Team2, s2Team2, s3Team2, nT, rP);
    }
}
