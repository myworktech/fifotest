package com.myworktech.fifotest.synchronizedQueue;

public interface SynchronizedBlockingQueue {
    Object get() throws InterruptedException;

    void put(Object object) throws InterruptedException;
}
