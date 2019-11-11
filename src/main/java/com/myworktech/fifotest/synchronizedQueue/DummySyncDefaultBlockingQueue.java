package com.myworktech.fifotest.synchronizedQueue;

public class DummySyncDefaultBlockingQueue implements SynchronizedBlockingQueue {

    private final int maxQueueSize;
    private final Object[] bufferOfObjects;

    private int readPointer = 0;
    private int elementsCount = 0;

    public DummySyncDefaultBlockingQueue(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        this.bufferOfObjects = new Object[maxQueueSize];
    }

    public Object get() throws InterruptedException {
        if (elementsCount == 0)
            synchronized (this) {
                while (elementsCount == 0) {
                    this.wait();
                }
            }

        synchronized (this) {
            Object object = bufferOfObjects[readPointer];
            bufferOfObjects[readPointer] = null;
            elementsCount = elementsCount - 1;

            readPointer = readPointer + 1;
            if (readPointer == maxQueueSize)
                readPointer = 0;

            this.notify();
            return object;
        }
    }

    public void put(Object object) throws InterruptedException {
        if (elementsCount == maxQueueSize)
            synchronized (this) {
                while (elementsCount == maxQueueSize) {
                    this.wait();
                }
            }

        synchronized (this) {
            if (elementsCount == maxQueueSize)
                throw new IllegalStateException();

            int writePointer = readPointer + elementsCount;

            if (writePointer >= maxQueueSize)
                writePointer = 0;
            bufferOfObjects[writePointer] = object;

            elementsCount = elementsCount + 1;

            this.notify();
        }


    }
}