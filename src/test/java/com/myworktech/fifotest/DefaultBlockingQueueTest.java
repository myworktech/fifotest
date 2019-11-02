package com.myworktech.fifotest;


import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

public class DefaultBlockingQueueTest {

    @Test(expected = IllegalStateException.class)
    public void getFromEmptyQueue() {
        DefaultBlockingQueue queue = new DefaultBlockingQueue(0);
        queue.get();
    }

    @Test(expected = IllegalStateException.class)
    public void putToFullQueue() {
        DefaultBlockingQueue queue = new DefaultBlockingQueue(0);
        queue.put(new Object());
    }

    @Test
    public void putAndGet() {
        DefaultBlockingQueue queue = new DefaultBlockingQueue(1);
        Object puttedObject = new Object();
        queue.put(puttedObject);
        Object gottenObject = queue.get();

        Assert.assertEquals(puttedObject, gottenObject);
    }

    @Test
    public void putTwoGetTwo() {
        DefaultBlockingQueue queue = new DefaultBlockingQueue(2);
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
        DefaultBlockingQueue queue = new DefaultBlockingQueue(2);
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
        DefaultBlockingQueue queue = new DefaultBlockingQueue(2);
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

    /** From your example
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
        DefaultBlockingQueue queue = new DefaultBlockingQueue(3);
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

        Assert.assertEquals(2, queue.getElementsCount());


        Field bufferOfObjectsField = queue.getClass().getDeclaredField("bufferOfObjects");
        bufferOfObjectsField.setAccessible(true);
        Object[] v = (Object[]) bufferOfObjectsField.get(queue);
        System.out.println(Arrays.toString(v));
    }

}