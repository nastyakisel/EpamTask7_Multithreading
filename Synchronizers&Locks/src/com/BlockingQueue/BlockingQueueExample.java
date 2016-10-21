package com.BlockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueExample {
	 public static void main(String[] args) throws Exception {

	        BlockingQueue queue = new ArrayBlockingQueue(1024);

	        Producer producer = new Producer(queue);
	        Consumer consumer = new Consumer(queue);
	        
	        Thread thread1 = new Thread(producer);
	        thread1.setName("Producer");
	        Thread thread2 = new Thread(consumer);
	        thread2.setName("Consumer");

	        thread1.start();
	        thread2.start();
	        
	        Thread.sleep(4000);
	    }
}

class Producer implements Runnable{

    protected BlockingQueue queue = null;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            queue.put("1");
            System.out.println(Thread.currentThread().getName() + " положил 1");
            Thread.sleep(1000);
            queue.put("2");
            System.out.println(Thread.currentThread().getName() + " положил 2");
            Thread.sleep(1000);
            queue.put("3");
            System.out.println(Thread.currentThread().getName() + " положил 3");
            queue.put("4");
            System.out.println(Thread.currentThread().getName() + " положил 4");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class Consumer implements Runnable{

    protected BlockingQueue queue = null;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            System.out.println(queue.take());
            System.out.println(Thread.currentThread().getName() + " взял 1");
            System.out.println(queue.take());
            System.out.println(Thread.currentThread().getName() + " взял 2");
            System.out.println(queue.take());
            System.out.println(Thread.currentThread().getName() + " взял 3");
            System.out.println(queue.take());
            System.out.println(Thread.currentThread().getName() + " взял еще");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}