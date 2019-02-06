package com.vsk.courtrf.test.service;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class testsrv implements itfAsync {

    private  Queue<String> queue1 = new ConcurrentLinkedQueue<String>();
    private  boolean running1 = false;

    private  Queue<String> queue2 = new ConcurrentLinkedQueue<String>();
    private  boolean running2 = false;

    @Async("processExecutor")
    public  void dummyRun1() {
        if ( running1 ) {
            System.out.println("#1 NOT STARTED");
            return; }
        System.out.println("#1 STARTED");
        running1 = true;
        while ( queue1.size() > 0 ) {
            String st1 = queue1.poll();
            System.out.println("#1 RUN with status = " + queue1.size() + "::" + st1);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        running1 = false;

    }

    @Async("processExecutor")
    public  void addString1(String st) {
        //stList.add(st);
        queue1.add("#1 new line");
        dummyRun1();

    }

    //@Async("processExecutor")
    public  void dummyRun2() {

        if ( running2 ) {
            System.out.println("#2 NOT STARTED");
            return; }
        System.out.println("#2 STARTED");
        running2 = true;
        while ( queue2.size() > 0 ) {
            String st1 = queue2.poll();
            System.out.println("#2 RUN with status = " + queue2.size() + "::" + st1);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        running2 = false;
    }

    @Async("processExecutor")
    public  void addString2(String st) {
        //stList.add(st);
        queue2.add("#1 new line");
        dummyRun2();
    }

}
