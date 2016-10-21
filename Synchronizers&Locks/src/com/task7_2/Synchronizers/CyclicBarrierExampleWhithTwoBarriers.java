package com.task7_2.Synchronizers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExampleWhithTwoBarriers {

	public static void main(String[] args) {
	
	Runnable barrier1Action = new Runnable() { // задача, которая будет выпол-
		                   // няться, когда потоки "встретятся"
	    public void run() {
	        System.out.println("BarrierAction 1 executed ");
	    }
	};
	Runnable barrier2Action = new Runnable() {
	    public void run() {
	        System.out.println("BarrierAction 2 executed ");
	    }
	};

	/*ограничиваем количество потоков, которые должны встретиться, двумя.
	 * Также передаем объекту CyclicBarrier действие - barrier1Action, которое 
	 * должно произойти, когда потоки встретятся
	 */
	CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
	CyclicBarrier barrier2 = new CyclicBarrier(2, barrier2Action);

	CyclicBarrierRunnable barrierRunnable1 =
	        new CyclicBarrierRunnable(barrier1, barrier2);
	
	CyclicBarrierRunnable barrierRunnable2 =
	        new CyclicBarrierRunnable(barrier1, barrier2);


	new Thread(barrierRunnable1).start();
	new Thread(barrierRunnable2).start();
}
}


class CyclicBarrierRunnable implements Runnable{

    CyclicBarrier barrier1 = null;
    CyclicBarrier barrier2 = null;

    public CyclicBarrierRunnable(CyclicBarrier barrier1, CyclicBarrier barrier2) {
        this.barrier1 = barrier1;
        this.barrier2 = barrier2;
    }

    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() +
                                " waiting at barrier 1");
            this.barrier1.await(); // поток достиг барьера № 1. Поток блокируется
                             // и ждет, пока другой поток достигнет барьера

            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() +
                                " waiting at barrier 2");
            this.barrier2.await(); // поток достиг барьера № 2. Поток блокируется
                             // и ждет, пока другой поток достигнет барьера

            System.out.println(Thread.currentThread().getName() +
                                " done!"); // действие, после того, как потоки 
                                    // прошли барьер

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
