package com.example.javapractice;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SynchronousTest {

    ExecutorService executorService;
    RaceConditionSum raceConditionSum;

    @Before
    public void init() {
        executorService = Executors.newFixedThreadPool(3);
        raceConditionSum = new RaceConditionSum();
    }

    @Test
    public void testSynchronous() throws InterruptedException{

        // expect to fail. Since the three thread would have race condition at
        // setting the sum value
        IntStream.range(0, 1000).forEach(count -> executorService.submit(raceConditionSum::count));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertNotEquals(1000, raceConditionSum.getCount());

    }

    @Test
    public void synchronousMethod() throws InterruptedException{

        // expect to success. Set the sum method as synchronous to be sure
        // only one thread access this method in one time
        IntStream.range(0, 1000).forEach(count -> executorService.submit(raceConditionSum::synchronousSum));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, raceConditionSum.getCount());

    }

    @Test
    public void synchronousStaticMethod() throws InterruptedException{

        // expect to success. Set the sum as a static method of accessing a static attribute
        // only one thread access this method in one time
        IntStream.range(0, 1000).forEach(count -> executorService.submit(RaceConditionSum::staticSum));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, RaceConditionSum.staticCount);

    }

    @Test
    public void synchronousBlock() throws InterruptedException{

        // expect to success. Set the sum in a synchronous block.
        // only one thread access this block in one time
        IntStream.range(0, 1000).forEach(count -> executorService.submit(raceConditionSum::synchronousSumBlock));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertEquals(1000, raceConditionSum.getCount());

    }
}

class RaceConditionSum {

    public static int staticCount = 0;
    private int count = 0;

    public void count(){
        this.setCount(this.getCount() + 1);
    }

    public static synchronized void staticSum() {
        staticCount = staticCount + 1;
    }

    public synchronized void synchronousSum() {
        this.setCount(this.getCount() + 1);
    }

    public void synchronousSumBlock() {
        synchronized (this) {
            this.setCount(this.getCount() + 1);
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
