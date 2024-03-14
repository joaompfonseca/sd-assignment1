package refereesite;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MRefereeSite implements IRefereeSite {

    private final ReentrantLock lock;
    private final Condition coachesWaited;
    private int waiting;
    private final Condition refereeCommand;
    private boolean isRefereeCommand;
    private boolean isMatchEnd;
    private int winTeamGame;
    private int winTeamMatch;

    public MRefereeSite() {
        lock = new ReentrantLock();
        coachesWaited = lock.newCondition();
        waiting = 0;
        refereeCommand = lock.newCondition();
        isRefereeCommand = false;
        isMatchEnd = false;
        winTeamGame = -1;
        winTeamMatch = -1;
    }

    @Override
    public boolean reviewNotes() {
        log("review notes");
        lock.lock();
        try {
            waiting++;
            if (waiting == 2) {
                coachesWaited.signal(); // alerts referee
            }
            while (!isRefereeCommand) {
                refereeCommand.await(); // releases lock and waits for referee command
            }
            waiting--;
            if (waiting == 0) {
                isRefereeCommand = false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return !isMatchEnd;
    }

    @Override
    public void announceNewGame() {
        log("announce new game");
    }

    @Override
    public void callTrial() {
        log("call trial");
        lock.lock();
        try {
            while (waiting < 2) {
                coachesWaited.await(); // releases lock and waits for coaches
            }
            isMatchEnd = false;
            isRefereeCommand = true;
            refereeCommand.signalAll(); // alerts coaches
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void declareGameWinner(int team) {
        log("declare game winner: team %d".formatted(team));
        lock.lock();
        try {
            winTeamGame = team;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void declareMatchWinner(int team) {
        log("declare match winner: team %d".formatted(team));
        lock.lock();
        try {
            winTeamMatch = team;
            while (waiting < 2) {
                coachesWaited.await(); // releases lock and waits for coaches
            }
            waiting = 0;
            isMatchEnd = true;
            isRefereeCommand = true;
            refereeCommand.signalAll(); // alerts coaches
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void log(String msg) {
        System.out.printf("[RefereeSite]: %s\n", msg);
    }
}
