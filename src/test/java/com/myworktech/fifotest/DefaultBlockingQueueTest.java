package com.myworktech.fifotest;

public class DefaultBlockingQueueTest extends AbstractBlockingQueueTest {
    @Override
    protected BlockingQueue getImplementation(int maxQueueSize) {
        return new DefaultBlockingQueue(maxQueueSize);
    }
}