package com.myworktech.fifotest;

public class TwoPointersDefaultBlockingQueue implements BlockingQueue {

    private final int maxQueueSize;
    private final Object[] bufferOfObjects;

    private int readPointer = 0;
    private int writePointer = 0;
    private int elementsCount = 0;


    public TwoPointersDefaultBlockingQueue(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        this.bufferOfObjects = new Object[maxQueueSize];
    }

    @Override
    public Object get() {
        if (elementsCount == 0)
            throw new IllegalStateException();

        Object object = bufferOfObjects[readPointer];
        bufferOfObjects[readPointer] = null;
        elementsCount = elementsCount - 1;
        readPointer = shiftPointer(readPointer);

        return object;
    }

    @Override
    public void put(Object object) {
        if (elementsCount == maxQueueSize)
            throw new IllegalStateException();

        bufferOfObjects[writePointer] = object;
        writePointer = shiftPointer(writePointer);
        elementsCount = elementsCount + 1;
    }

    private int shiftPointer(int pointer) {
        pointer = pointer + 1;
        return pointer == maxQueueSize ? 0 : pointer;
    }
}