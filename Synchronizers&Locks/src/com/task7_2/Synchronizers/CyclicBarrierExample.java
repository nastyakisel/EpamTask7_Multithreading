package com.task7_2.Synchronizers;

import java.util.concurrent.CyclicBarrier;

/*http://tutorials.jenkov.com/java-util-concurrent/cyclicbarrier.html
 * 
 */
public class CyclicBarrierExample {
	private static final CyclicBarrier BARRIER = new CyclicBarrier(3, new FerryBoat());
    //»нициализируем барьер на три потока и таском, который будет выполн€тьс€, когда
    //у барьера соберетс€ три потока. ѕосле этого, они будут освобождены.

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 9; i++) {
            new Thread(new Car(i)).start();
            Thread.sleep(400);
        }
    }

    //“аск, который будет выполн€тьс€ при достижении сторонами барьера
    public static class FerryBoat implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                System.out.println("ѕаром переправил автомобили!");
            } catch (InterruptedException e) {
            }
        }
    }

    //—тороны, которые будут достигать барьера
    public static class Car implements Runnable {
        private int carNumber;

        public Car(int carNumber) {
            this.carNumber = carNumber;
        }

        @Override
        public void run() {
            try {
                System.out.printf("јвтомобиль є%d подъехал к паромной переправе.\n", carNumber);
                //ƒл€ указани€ потоку о том что он достиг барьера, нужно вызвать метод await()
                //ѕосле этого данный поток блокируетс€, и ждет пока остальные стороны достигнут барьера
                BARRIER.await();
                System.out.printf("јвтомобиль є%d продолжил движение.\n", carNumber);
            } catch (Exception e) {
            }
        }
    }
}
