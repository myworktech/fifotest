package com.myworktech.fifotest;

public interface BlockingQueue {
    Object get();
    void put(Object object);
}
