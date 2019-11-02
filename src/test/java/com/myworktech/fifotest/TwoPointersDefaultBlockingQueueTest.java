package com.myworktech.fifotest;

public class TwoPointersDefaultBlockingQueueTest extends AbstractBlockingQueueTest {
    @Override
    protected BlockingQueue getImplementation(int maxQueueSize) {
        return new TwoPointersDefaultBlockingQueue(maxQueueSize);
    }
}