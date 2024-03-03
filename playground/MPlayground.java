package playground;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class MPlayground implements IPlayground {

    private final ReentrantLock lock;
    private final Condition condition;

    public MPlayground() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    @Override
    public void getReady() {

    }

    @Override
    public void pullTheRope() {

    }

    @Override
    public void amDone() {

    }

    @Override
    public void startTrial() {
        lock.lock();
        try {
            System.out.println("Inside startTrial() method.");
            sleep(10000);
            condition.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Referee is starting the trial.");
            lock.unlock();
        }
    }

    @Override
    public int assertTrialDecision() {
        return 0;
    }
}
