package com.myworktech.fifotest.synchronizedQueue;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class SynchronizedQueueTest {

    private SynchronizedBlockingQueue getImplementation() {
        return new DummySyncDefaultBlockingQueue(1);
    }

    @Test
    public void checkBlockOnGetFromEmpty() throws InterruptedException {
        SynchronizedBlockingQueue queue = getImplementation();

        Thread readThread = new Thread(() -> {
            try {
                queue.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        readThread.start();

        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        final AtomicLong counter = new AtomicLong(0);

        while (!atomicBoolean.get() && counter.get() < 10) {
            if (readThread.getState().equals(Thread.State.WAITING)) {
                atomicBoolean.set(true);
            } else {
                counter.incrementAndGet();
                Thread.sleep(1);
            }
        }

        Assert.assertTrue(atomicBoolean.get());
        System.out.println(counter.get());
    }


    @Test
    public void checkBlockOnPutToFull() throws InterruptedException {
        SynchronizedBlockingQueue queue = getImplementation();

        Thread writeThread = new Thread(() -> {
            try {
                queue.put(new Object());
                queue.put(new Object());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        writeThread.start();

        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        final AtomicLong counter = new AtomicLong(0);

        while (!atomicBoolean.get() && counter.get() < 10) {
            if (writeThread.getState().equals(Thread.State.WAITING)) {
                atomicBoolean.set(true);
            } else {
                counter.incrementAndGet();
                Thread.sleep(1);
            }
        }

        Assert.assertTrue(atomicBoolean.get());
        System.out.println(counter.get());
    }

    @Test
    public void testBlockOnReadFromEmptyAndThenUnblockOnPut() throws InterruptedException {
        SynchronizedBlockingQueue queue = getImplementation();

        final Object testObject = new Object();

        final AtomicReference<Object> readObject = new AtomicReference<>();

        final AtomicBoolean blockedStatus = new AtomicBoolean(false);
        final AtomicLong waitingForBlockCounter = new AtomicLong(0);

        Thread runnerThread = new Thread(() -> {

            Thread writeThread = new Thread(() -> {
                try {
                    queue.put(testObject);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            writeThread.setName("write thread");


            Thread readThread = new Thread(() -> {
                try {
                    Object newValue = queue.get();
                    Thread.sleep(1000);
                    readObject.set(newValue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            readThread.setName("read thread");
            readThread.start();


            while (!blockedStatus.get() && waitingForBlockCounter.get() < 10) {
                if (readThread.getState().equals(Thread.State.WAITING)) {
                    blockedStatus.set(true);
                    writeThread.start();

                } else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                waitingForBlockCounter.incrementAndGet();

            }

            try {
                writeThread.join();
                readThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });
        runnerThread.setName("runner thread");
        runnerThread.start();
        runnerThread.join();

        Assert.assertTrue(blockedStatus.get());

        Assert.assertEquals(testObject, readObject.get());
        System.out.println(waitingForBlockCounter.get());
    }
}