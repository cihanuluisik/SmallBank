package com.sbank.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Component
public class LockAndRunManager {

    private static final int RETRY_SLEEP_TIME = 50;

    public void lockAndRun(Lock lock, Runnable runnable) throws InterruptedException {
        while (true) {
            if (lock.tryLock()) {
                try {
                    runnable.run();
                    break;
                } finally {
                    lock.unlock();
                }
            }
            Thread.sleep(RETRY_SLEEP_TIME);
        }
    }

    public void lockBothAndRun(Lock lock, Lock lock2, Runnable runnable) throws InterruptedException {
        while (true) {
            if (lock.tryLock()) {
                try {
                    if (lock2.tryLock()){
                        try {
                            runnable.run();
                        } finally {
                            lock2.unlock();
                        }
                    }
                    break;
                } finally {
                    lock.unlock();
                }
            }
            Thread.sleep(RETRY_SLEEP_TIME);
        }
    }

}
