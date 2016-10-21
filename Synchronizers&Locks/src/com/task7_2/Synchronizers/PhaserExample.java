package com.task7_2.Synchronizers;

import java.util.ArrayList;
import java.util.concurrent.Phaser;

/*Есть пять остановок. На первых четырех из них могут стоять пассажиры и ждать 
 * автобуса. Автобус выезжает из парка и останавливается на каждой остановке 
 * на некоторое время. После конечной остановки автобус едет в парк. Нам нужно 
 * забрать пассажиров и высадить их на нужных остановках.
 */
public class PhaserExample {

	private static final Phaser PHASER = new Phaser(1);// Сразу регистрируем
														// главный поток

	public static void main(String[] args) {

		// Аррайлист потоков
		ArrayList<Passenger> passengers = new ArrayList<>();

		for (int i = 1; i < 5; i++) { // Сгенерируем пассажиров на остановках
			if ((int) (Math.random() * 2) > 0)
				passengers.add(new Passenger(i, i + 1));// Этот пассажир выходит
														// на следующей

			if ((int) (Math.random() * 2) > 0)
				passengers.add(new Passenger(i, 5)); // Этот пассажир выходит на
														// конечной
		}
		for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    // выводим сообщение
                	System.out.println("Автобус выехал из парка.");
                    /* и завершаем фазу, метод возвращает номер 
                     * фазы. Поток не приостанавливается,
                     *  а продолжает выполнятся;
                     */
                    int y = PHASER.arrive();//В фазе 0 всего 1 участник - автобус
                                    //(главный поток)
                    System.out.println(y);
                    break;
                case 6:
                    System.out.println("Автобус уехал в парк.");
                    /* сообщает о завершении всех фаз главным потоком
                     * и снимает его с регистрации. 
                     */
                    PHASER.arriveAndDeregister();
                    break;
                default:
                    // получаем номер текущей фазы
                	int currentBusStop = PHASER.getPhase();
                    System.out.println("Остановка № " + currentBusStop);
                    // для каждого потока
                    for (Passenger p : passengers)          
                        // если номер "пассажира" совпадает с номером "остановки"
                    	if (p.departure == currentBusStop) {
                    		//Регистрируем поток, который будет выполнять фазы
                    		PHASER.register();
                            p.start();        
                        }
                    /* поток завершил выполнение фазы,
                     * gоток приостанавливается до момента, пока все остальные 
                     * стороны не закончат выполнять данную фазу.
                     */
                    PHASER.arriveAndAwaitAdvance();
            }

	}
}
	public static class Passenger extends Thread {
		private int departure;
		private int destination;

		public Passenger(int departure, int destination) {
			this.departure = departure;
			this.destination = destination;
			System.out.println(this + " ждёт на остановке № " + this.departure);
		}
		
		@Override
        public void run() {
            try {
                System.out.println(this + " сел в автобус.");

                while (PHASER.getPhase() < destination) //Пока автобус не приедет на нужную остановку(фазу)
                    PHASER.arriveAndAwaitAdvance(); //завершаем выполнение фазы и ждем, пока 
                                       //другие потоки не закончат выполнять данную фазу

                Thread.sleep(1);
                System.out.println(this + " покинул автобус.");
                /* как только фаза == destination - завершение фазы, снимаем поток
                 * с регистрации
                 */
                PHASER.arriveAndDeregister();   
            } catch (InterruptedException e) {
            }
        }
		
		@Override
        public String toString() {
            return "Пассажир{" + departure + " -> " + destination + '}';
        }
	}

}
