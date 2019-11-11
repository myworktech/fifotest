package com.myworktech.fifotest.synchronizedQueue;

public class DefaultSynchronizedBlockingQueue implements SynchronizedBlockingQueue {

    private final int maxQueueSize;
    private final Object[] bufferOfObjects;

    private int readPointer = 0;
    private volatile int elementsCount = 0; // volatile is a must

    private final Object readLock = new Object();
    private final Object writeLock = new Object();


    public DefaultSynchronizedBlockingQueue(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        this.bufferOfObjects = new Object[maxQueueSize];
    }

    @Override
    public Object get() throws InterruptedException {
        if (elementsCount == 0)
            synchronized (readLock) {
                while (elementsCount == 0)
                    readLock.wait();
            }
        Object bufferOfObject;
        synchronized (this) {
            bufferOfObject = bufferOfObjects[readPointer];
            bufferOfObjects[readPointer] = null;
            elementsCount = elementsCount - 1;

            readPointer = readPointer + 1;
            if (readPointer == maxQueueSize)
                readPointer = 0;
        }

        synchronized (writeLock) {
            writeLock.notify();
        }
        return bufferOfObject;
    }

    @Override
    public void put(Object object) throws InterruptedException {
        if (elementsCount == maxQueueSize)
            synchronized (writeLock) {
                while (elementsCount == maxQueueSize)
                    writeLock.wait();
            }

        synchronized (this) {
            int writePointer = readPointer + elementsCount;
            if (writePointer >= maxQueueSize)
                writePointer = 0;
            bufferOfObjects[writePointer] = object;

            elementsCount = elementsCount + 1;
        }

        synchronized (readLock) {
            readLock.notify();
        }
    }
}