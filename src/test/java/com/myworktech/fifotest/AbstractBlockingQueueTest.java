package com.myworktech.fifotest;


import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public abstract class AbstractBlockingQueueTest {

    protected abstract BlockingQueue getImplementation(int maxQueueSize);

    @Test(expected = IllegalStateException.class)
    public void getFromEmptyQueue() {
        BlockingQueue queue = getImplementation(0);
        queue.get();
    }

    @Test(expected = IllegalStateException.class)
    public void putToFullQueue() {
        BlockingQueue queue = getImplementation(0);
        queue.put(new Object());
    }

    @Test
    public void putAndGet() {
        BlockingQueue queue = getImplementation(1);
        Object puttedObject = new Object();
        queue.put(puttedObject);
        Object gottenObject = queue.get();

        Assert.assertEquals(puttedObject, gottenObject);
    }

    @Test
    public void putTwoGetTwo() {
        BlockingQueue queue = getImplementation(2);
        Object puttedObject1 = new Object();
        Object puttedObject2 = new Object();

        queue.put(puttedObject1);
        queue.put(puttedObject2);

        Object gottenObject1 = queue.get();
        Object gottenObject2 = queue.get();

        Assert.assertEquals(puttedObject1, gottenObject1);
        Assert.assertEquals(puttedObject2, gottenObject2);
    }

    @Test
    public void putOneGetOnePutOneGetOne() {
        BlockingQueue queue = getImplementation(2);
        Object puttedObject1 = new Object();

        queue.put(puttedObject1);
        Object gottenObject1 = queue.get();
        Assert.assertEquals(puttedObject1, gottenObject1);


        Object puttedObject2 = new Object();
        queue.put(puttedObject2);
        Object gottenObject2 = queue.get();
        Assert.assertEquals(puttedObject2, gottenObject2);
    }

    @Test
    public void putTwoGetOnePutOneGetTwo() {
        BlockingQueue queue = getImplementation(2);
        Object puttedObject1 = new Object();
        Object puttedObject2 = new Object();

        queue.put(puttedObject1);
        queue.put(puttedObject2);

        Object gottenObject1 = queue.get();
        Assert.assertEquals(puttedObject1, gottenObject1);


        Object puttedObject3 = new Object();
        queue.put(puttedObject3);
        Object gottenObject2 = queue.get();
        Object gottenObject3 = queue.get();

        Assert.assertEquals(puttedObject2, gottenObject2);
        Assert.assertEquals(puttedObject3, gottenObject3);
    }

    /**
     * From your example
     * put 6 ->     6,_,_
     * put 7 ->     6,7,_
     * put 8 ->     6,7,8
     * put 9 -> buffer is full (block)
     * get -> 6     _,7,8
     * put 9 ->     9,7,8
     * get -> 7     9,_,8
     */
    @Test
    public void demo() throws Throwable {
        BlockingQueue queue = getImplementation(3);
        Object puttedObject6 = new Object();
        Object puttedObject7 = new Object();
        Object puttedObject8 = new Object();
        Object puttedObject9 = new Object();

        queue.put(puttedObject6);
        queue.put(puttedObject7);
        queue.put(puttedObject8);
        try {
            queue.put(puttedObject9);
            Assert.fail();
        } catch (IllegalStateException ignored) {
        }


        Object gottenObject6 = queue.get();
        Assert.assertEquals(puttedObject6, gottenObject6);
        queue.put(puttedObject9);
        Object gottenObject7 = queue.get();
        Assert.assertEquals(puttedObject7, gottenObject7);


        Field elementsCountField = queue.getClass().getDeclaredField("elementsCount");
        elementsCountField.setAccessible(true);
        int elementsCount = (int) elementsCountField.get(queue);

        Assert.assertEquals(2, elementsCount);
    }
}