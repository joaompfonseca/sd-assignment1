package generalrepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import static generalrepository.EGeneralRepository.*;

public class MGeneralRepository implements IGeneralRepository {

    // Struct for contestants (Status and Strength)
    private static class Contestant {
        private String status;

        private int strength;

        public Contestant(String status, int strength) {
            this.status = status;
            this.strength = strength;
        }
    }

    // DataSets
    private String refereeStatus;
    private String coachesTeam1Status;
    private String coachesTeam2Status;
    private final HashMap<Integer, Contestant> contestantsTeam1;
    private final HashMap<Integer, Contestant> contestantsTeam2;
    private ArrayList<Integer> selectedContestantsTeam1;
    private ArrayList<Integer> selectedContestantsTeam2;
    private HashMap<Integer,Integer> wonGames;
    private int ropePosition;
    private int nextRopePosition;
    private int nTrials;
    private int nGames;
    private boolean matchEnd;

    // Reentrant Lock
    private final ReentrantLock lock = new ReentrantLock();

    // General Repository constructor
    public MGeneralRepository(int nContestants) {
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

        System.out.printf("                               Game of the Rope - Description of the internal state%n%n");
    }

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

    @Override
    public void pullTheRope(int team, int contestant) {
        lock.lock();

        if(team == 0) {
            contestantsTeam1.get(contestant).status = DO_YOUR_BEST.label;
            contestantsTeam1.get(contestant).strength--;
        } else {
            contestantsTeam2.get(contestant).status = DO_YOUR_BEST.label;
            contestantsTeam2.get(contestant).strength--;
        }

        print();

        lock.unlock();
    }

    @Override
    public void amDone() {
        lock.lock();

        // It's not necessary to do anything here because the contestant doesn't need to change its status or strength

        print();

        lock.unlock();
    }

    @Override
    public void assertTrialDecision(int p) {
        lock.lock();

        // Save rope position
        nextRopePosition = p;

        print();

        lock.unlock();
    }

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

    @Override
    public void announceNewGame() {
        lock.lock();

        refereeStatus = START_OF_A_GAME.label;
        ropePosition = 100;
        nTrials = 0;
        nGames++;

        System.out.println("Game " + nGames);

        print();

        lock.unlock();
    }

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
            output += " was won by team" + team + "by knockout out in " + nTrials + " trials.";
        } else {
            output += " ended by points.";
        }
        System.out.println(output);

        lock.unlock();
    }

    @Override
    public void declareMatchWinner(int team) {
        lock.lock();

        refereeStatus = END_OF_THE_MATCH.label;

        print();

        String output;
        if (team == -1) {
            output = "The match ended in a draw.";
        } else {
            output = "The match was won by team " + team + "(" + wonGames.get(0) + "-" + wonGames.get(1) + ") .";
        }
        System.out.println(output);

        matchEnd = true;

        lock.unlock();
    }

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

        System.out.printf("Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5       Trial        %n");
        System.out.printf("Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS%n");

        System.out.printf("%3s  %4s %3s %2s %3s %2s %3s %2s %3s %2s %3s %2s  %4s %3s %2s %3s %2s %3s %2s %3s %2s %3s %2s %1s %1s %1s . %1s %1s %1s %2s %2s%n",
                rStatus, cTeam1Status, c1Team1Status, c1Team1Strength, c2Team1Status, c2Team1Strength, c3Team1Status, c3Team1Strength, c4Team1Status, c4Team1Strength, c5Team1Status, c5Team1Strength,
                cTeam2Status, c1Team2Status, c1Team2Strength, c2Team2Status, c2Team2Strength, c3Team2Status, c3Team2Strength, c4Team2Status, c4Team2Strength, c5Team2Status, c5Team2Strength,
                s1Team1, s2Team1, s3Team1, s1Team2, s2Team2, s3Team2, nT, rP);

        //System.out.printf("%3s  %4s %3s %2s %3s %2s %3s %2s %3s %2s %3s %2s  %4s %3s %2s %3s %2s %3s %2s %3s %2s %3s %2s %1s %1s %1s . %1s %1s %1s %2s %2s%n",
        //        refereeStatus, coachesTeam1Status, contestantsTeam1.get(0).status, contestantsTeam1.get(0).strength, contestantsTeam1.get(1).status, contestantsTeam1.get(1).strength, contestantsTeam1.get(2).status, contestantsTeam1.get(2).strength, contestantsTeam1.get(3).status, contestantsTeam1.get(3).strength, contestantsTeam1.get(4).status, contestantsTeam1.get(4).strength,
        //        coachesTeam2Status, contestantsTeam2.get(0).status, contestantsTeam2.get(0).strength, contestantsTeam2.get(1).status, contestantsTeam2.get(1).strength, contestantsTeam2.get(2).status, contestantsTeam2.get(2).strength, contestantsTeam2.get(3).status, contestantsTeam2.get(3).strength, contestantsTeam2.get(4).status, contestantsTeam2.get(4).strength,
        //        3, 2, 1, 1, 2, 3, nTrials, ropePosition);

    }
}
