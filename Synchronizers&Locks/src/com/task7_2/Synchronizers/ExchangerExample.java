package com.task7_2.Synchronizers;

import java.util.concurrent.Exchanger;


//http://tutorials.jenkov.com/java-util-concurrent/exchanger.html
	
public class ExchangerExample {
	public static void main(String[] args) {
		// Создаем Exchanger, точка синхронизации двух потоков
		Exchanger exchanger = new Exchanger();

		/*Отправляем объекту Exchanger и объекты, которыми потоки будут
		 *обмениваться = "A" и "B"
		 */
		ExchangerRunnable exchangerRunnable1 =
		        new ExchangerRunnable(exchanger, "A");

		ExchangerRunnable exchangerRunnable2 =
		        new ExchangerRunnable(exchanger, "B");

		new Thread(exchangerRunnable1).start();
		new Thread(exchangerRunnable2).start();
	}
}

class ExchangerRunnable implements Runnable{

    Exchanger exchanger;
    Object object;

    public ExchangerRunnable(Exchanger exchanger, Object object) {
        this.exchanger = exchanger;
        this.object = object;
    }

    public void run() {
        try {
            Object previous = this.object;

            /*Поток вызывает метод exchange, блокируется и ждет другой поток
             * Происходит обмен объектами (Object)
             */
            
            this.object = this.exchanger.exchange(this.object);

            System.out.println(
                    Thread.currentThread().getName() +
                    " exchanged " + previous + " for " + this.object
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
