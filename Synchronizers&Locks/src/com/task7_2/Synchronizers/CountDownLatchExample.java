package com.task7_2.Synchronizers;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
	
	/* Создаем объект CountDownLatch и передаем ему количество операций,
	 * которые должны быть выполнены, пока потоки ждут окончания этих
	 * операций, чтобы продоложить выполнение кода.
	 * Условий 8 - 5 автомобилей подъезжают к стартовой прямой, ждут три 
	 * команды (На старт, внимание, марш).
	 */
	private static final CountDownLatch START = new CountDownLatch(8);
    //Условная длина гоночной трассы
    private static final int trackLength = 500000;

    public static void main(String[] args) throws InterruptedException {
        /*Запускаем пять потоков (машин). Каждый поток выполняет метод run()
         * (подъезжает к трассе),
         * каждый поток уменьшает значение счетчика на единицу. 
         */
    	for (int i = 1; i <= 5; i++) {
            new Thread(new Car(i, (int) (Math.random() * 100 + 50))).start();
            Thread.sleep(1000);
        }

        while (START.getCount() > 3) //Проверяем, собрались ли все автомобили
            Thread.sleep(100);              //у стартовой прямой. Если нет, ждем 100ms

        Thread.sleep(1000);
        System.out.println("На старт!");
        START.countDown();// После команды, снова уменьшаем счетчик на 1
        Thread.sleep(1000);
        System.out.println("Внимание!");
        START.countDown();//После команды, снова уменьшаем счетчик на 1
        Thread.sleep(1000);
        System.out.println("Марш!");
        START.countDown();//После команды, снова уменьшаем счетчик на 1
        /*После последней команды, значение счетчика равно нулю, и все
         * заблокированные потоки одновременно разблокируются.
         */
    }

    
    public static class Car implements Runnable {
        private int carNumber;
        private int carSpeed;

        public Car(int carNumber, int carSpeed) {
            this.carNumber = carNumber;
            this.carSpeed = carSpeed;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Автомобиль №%d подъехал к стартовой прямой.\n", carNumber);
                /*Автомобиль подъехал к старту, операция выполнена,
                 * значение счетчика уменьшилось на единицу.
                 */
                START.countDown();
                /*метод await() блокирует поток, вызвайший этот метод,пока
                 * счетчик CountDownLatch не примет значение 0
                 */
                START.await();
                Thread.sleep(trackLength / carSpeed);
                System.out.printf("Автомобиль №%d финишировал!\n", carNumber);
            } catch (InterruptedException e) {
            }
        }
    }
}
