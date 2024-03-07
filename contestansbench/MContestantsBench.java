package contestansbench;

import threads.TContestant;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MContestantsBench implements IContestantsBench {

    private final int contestantsPerTeam;
    private final int contestantsPerTrial;
    private final ReentrantLock team0_lock;
    private final ReentrantLock team1_lock;
    private final Condition team0_seatedDown;
    private final Condition team1_seatedDown;
    private int team0_countSeatedDown;
    private int team1_countSeatedDown;
    private final Condition team0_teamAssembled;
    private final Condition team1_teamAssembled;
    private boolean[] team0_selectedContestants;
    private boolean[] team1_selectedContestants;
    private final Condition team0_teamInPosition;
    private final Condition team1_teamInPosition;
    private int team0_countInPosition;
    private int team1_countInPosition;
    private int[] team0_strengths;
    private int[] team1_strengths;

    public MContestantsBench(int contestantsPerTeam, int contestantsPerTrial) {
        this.contestantsPerTeam = contestantsPerTeam;
        this.contestantsPerTrial = contestantsPerTrial;
        team0_lock = new ReentrantLock();
        team1_lock = new ReentrantLock();
        team0_seatedDown = team0_lock.newCondition();
        team1_seatedDown = team1_lock.newCondition();
        team0_countSeatedDown = 0;
        team1_countSeatedDown = 0;
        team0_teamAssembled = team0_lock.newCondition();
        team1_teamAssembled = team1_lock.newCondition();
        team0_selectedContestants = new boolean[0];
        team1_selectedContestants = new boolean[0];
        team0_teamInPosition = team0_lock.newCondition();
        team1_teamInPosition = team1_lock.newCondition();
        team0_countInPosition = 0;
        team1_countInPosition = 0;

        // Initialize strengths
        team0_strengths = new int[contestantsPerTeam];
        team1_strengths = new int[contestantsPerTeam];
        for (int i = 0; i < contestantsPerTeam; i++) {
            team0_strengths[i] = 5;
            team1_strengths[i] = 5;
        }
    }

    @Override
    public void seatDown(int team, int contestantNumber) {
        log("seat down: team %d, contestantNumber %d".formatted(team, contestantNumber));
        if (team == 0) {
            team0_lock.lock();
            try {
                team0_countSeatedDown++;

                if (team0_countSeatedDown == contestantsPerTeam) {

                    // If contesntant in selectedContestans -1 in strength else +1
                    for (int i = 0; i < contestantsPerTeam; i++) {
                        System.out.println(team0_selectedContestants.length);
                        if (team0_selectedContestants[i]) {
                            team0_strengths[i]--;
                        } else {
                            team0_strengths[i]++;
                        }
                    }

                    team0_countInPosition = 0;
                    team0_seatedDown.signal();
                }
                do {
                    team0_teamAssembled.await(); // releases the lock and waits
                } while (!team0_selectedContestants[contestantNumber]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                team0_lock.unlock();
            }
        } else if (team == 1) {
            team1_lock.lock();
            try {
                team1_countSeatedDown++;
                if (team1_countSeatedDown == contestantsPerTeam) {

                    // If contesntant in selectedContestans -1 in strength else +1
                    for (int i = 0; i < contestantsPerTeam; i++) {
                        if (team0_selectedContestants[i]) {
                            team0_strengths[i]--;
                        } else {
                            team0_strengths[i]++;
                        }
                    }

                    team1_countInPosition = 0;
                    team1_seatedDown.signal();
                }
                do {
                    team1_teamAssembled.await(); // releases the lock and waits
                } while (!team1_selectedContestants[contestantNumber]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                team1_lock.unlock();
            }
        }
    }

    @Override
    public int[] getContestantsStrength(int team) {
        log("get contestants strength: team %d".formatted(team));

        if (team == 0) {
            return team0_strengths;
        } else {
            return team1_strengths;
        }
    }

    @Override
    public void callContestants(int team, boolean[] selectedContestants) {
        log("call contestants: team %d, selectedContestants %s".formatted(team, Arrays.toString(selectedContestants)));
        if (team == 0) {
            team0_lock.lock();
            try {
                if (team0_countSeatedDown < contestantsPerTeam)
                    team0_seatedDown.await(); // releases the lock and waits
                team0_selectedContestants = selectedContestants;
                team0_teamAssembled.signalAll();
                team0_teamInPosition.await(); // releases the lock and waits
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                team0_lock.unlock();
            }
        } else if (team == 1) {
            team1_lock.lock();
            try {
                if (team1_countSeatedDown < contestantsPerTeam)
                    team1_seatedDown.await(); // releases the lock and waits
                team1_selectedContestants = selectedContestants;
                team1_teamAssembled.signalAll();
                team1_teamInPosition.await(); // releases the lock and waits
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                team1_lock.unlock();
            }
        }
    }

    @Override
    public void followCoachAdvice(int team) {
        log("follow coach advice: team %d".formatted(team));
        if (team == 0) {
            team0_lock.lock();
            team0_countInPosition++;
            if (team0_countInPosition == contestantsPerTrial) {
                team0_countSeatedDown -= team0_countInPosition;
                team0_teamInPosition.signal();
            }
            team0_lock.unlock();
        } else if (team == 1) {
            team1_lock.lock();
            team1_countInPosition++;
            if (team1_countInPosition == contestantsPerTrial) {
                team1_countSeatedDown -= team1_countInPosition;
                team1_teamInPosition.signal();
            }
            team1_lock.unlock();
        }
    }

    private void log(String msg) {
        System.out.printf("[ContestantsBench]: %s\n", msg);
    }
}
