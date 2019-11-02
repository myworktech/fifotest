package com.myworktech.fifotest;

public class DefaultBlockingQueue implements BlockingQueue {

    private final int maxQueueSize;
    private Object[] bufferOfObjects;

    private int readPointer = 0;
    private int elementsCount = 0;


    public DefaultBlockingQueue(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        this.bufferOfObjects = new Object[maxQueueSize];
    }

    public int getElementsCount() {
        return elementsCount;
    }

    @Override
    public Object get() {
        if (elementsCount == 0)
            throw new IllegalStateException();

        Object bufferOfObject = bufferOfObjects[readPointer];
        bufferOfObjects[readPointer] = null;
        elementsCount = elementsCount - 1;

        readPointer = readPointer + 1;
        if (readPointer == maxQueueSize)
            readPointer = 0;
        return bufferOfObject;
    }

    @Override
    public void put(Object object) {
        if (elementsCount == maxQueueSize)
            throw new IllegalStateException();


        int writePointer = readPointer + elementsCount;

        if (writePointer >= maxQueueSize)
            writePointer = 0;
        this.bufferOfObjects[writePointer] = object;

        elementsCount = elementsCount + 1;

    }
}