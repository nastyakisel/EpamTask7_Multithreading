package com.task7_2.Synchronizers;

import java.util.concurrent.Semaphore;

/*Имеется тележка (общий ресурс) и два работника 
 * (потоки java). Один работник, наполняет тележку песком. 
 * В это время второй работник, который перевозит груз и затем разгружает, 
 * не может взять тележку и отвезти ее.
 * http://movejava.blogspot.com.by/2013/06/javautilconcurrentsemaphore.html
 */


public class SemaphoreExample2 {
	public static void main(String[] args) {
        /* Создаем объект Семафора. Семафор инициализируется количеством
         * разрешенных потоков - permits. В данном случае разрешена работа только
         * одного потока.
         */
		Semaphore semaphore = new Semaphore(1); 
        new Worker(semaphore, "Adder", true).start(); // создаем потоки
        new Worker(semaphore, "Reducer", false).start();
    }
}
/* Класс тележки, который представляет собой общий ресурс. Одновременно
 * с ресурсом (тележкой) может работать только один поток. Ограничение
 * задано при создании объекта Семафора. 
 */
class Cart {
    private static int weight = 0;

    public static void addWeight(){ // наполняем тележку
        weight++;
    }

    public static void reduceWeight(){ // разгружаем тележку
        weight--;
    }

    public static int getWaight(){
        return weight;
    }
}


class Worker extends Thread {

    private Semaphore semaphore;
    private String workerName;
    private boolean isAdder;
    
    public Worker(Semaphore semaphore, String workerName, boolean isAdder) {
        this.semaphore = semaphore;
        this.workerName = workerName;
        this.isAdder = isAdder;
    }

    @Override
    public void run() {
        // Оба потока включаются в работу
    	System.out.println(workerName + " started working..."); 
        try {
            System.out.println(workerName + " waiting for cart...");
            /* Каждый вызов метода acquire() уменьшает значение счетчика (permits)
             * на единицу. У нас значение счетчика 1, значит, только один поток
             * может работать с данным ресурсом. Второк поток ожидает, пока 
             * первый поток не наполнит тележку (не завершит цикл).
             */
            semaphore.acquire();
            /* Значение счетчика равно нулю. Значит, второк поток блокируется, пока
             * первый не выйдет из блока.
             */
            System.out.println(workerName + " got access to cart...");
            for (int i = 0 ; i < 10 ; i++) {
                if (isAdder)
                    Cart.addWeight();
                else
                    Cart.reduceWeight();
                
                System.out.println(workerName + " changed weight to: " + Cart.getWaight());
                Thread.sleep(10L);
            }
            System.out.println(workerName + " finished working with cart...");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            /*Текущий поток выходит из блока, уступая место для работы другому
             * потоку. Каждый вызов метода release() увеличивает значение счетчика
             * на единицу.
             */
        	semaphore.release(); 
        }
    }
}
