package playground;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class MPlayground implements IPlayground{

    private final ReentrantLock lock;
    private final Condition condition;

    public MPlayground() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void reviewNotes() {
        lock.lock();
        try {
            System.out.println("Inside reviewNotes() method.");
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Coach is reviewing the notes.");
            lock.unlock();
        }
    }

    public void followCoachAdvice() {

    }

    public void getReady() {

    }

    public void pullTheRope() {

    }

    public void amDone() {

    }

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

    public void assertTrialDecision() {

    }
}
