package refereesite;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MRefereeSite implements IRefereeSite {

    private final ReentrantLock lock;
    private final Condition trialCalled;
    private final Condition refereeInformed;
    private int countInformReferee;
    private int winTeamGame;
    private int winTeamMatch;

    public MRefereeSite() {
        lock = new ReentrantLock();
        trialCalled = lock.newCondition();
        refereeInformed = lock.newCondition();
        countInformReferee = 0;
        winTeamGame = -1;
        winTeamMatch = -1;
    }

    @Override
    public void informReferee() {
        log("inform referee");
        lock.lock();
        try {
            countInformReferee++;
            if (countInformReferee == 2) {
                refereeInformed.signal();
            }
        } finally {
            lock.unlock();
        }
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
            countInformReferee = 0;
            trialCalled.signalAll();
            if (countInformReferee < 2)
                refereeInformed.await(); // releases lock and waits
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
        } finally {
            lock.unlock();
        }
    }

    private void log(String msg) {
        System.out.printf("[RefereeSite]: %s\n", msg);
    }
}
