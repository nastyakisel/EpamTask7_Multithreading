package com.task7_2.Locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/*≈сть склад, где могут одновременно быть размещено не более 3 товаров. 
 * ѕроизводитель должен произвести 5 товаров, а покупатель должен эти 
 * товары купить. ¬ то же врем€ покупатель не может купить товар, если на 
 * складе нет никаких товаров.
 */

public class ConditionExample {
	public static void main(String[] args) {
		
		Store store = new Store();
		Producer producer = new Producer(store);
		Consumer consumer = new Consumer(store);

		new Thread(producer).start(); // поток -производитель
		new Thread(consumer).start(); // поток -покупатель
	}
}

	//  ласс ћагазин, хран€щий произведенные товары
class Store {
		private int product = 0;
		ReentrantLock locker;
		Condition condition;

		Store() {
			// создает объект ReentrantLock
			locker = new ReentrantLock();
			// используем его дл€ создани€ услови€, св€занного с блокировкой
			condition = locker.newCondition();
		}

		public void get() {

			try {
				// устанавливаем блокировку
				locker.lock();
				// пока нет доступных товаров на складе, ожидаем
				while (product < 1)
					/*
					 * поток ждет, пока е будет выполнено условие и пока другой
					 * поток не вызовет методы signal/signalAll
					 */
					condition.await();

				product--;
				System.out.println("ѕокупатель купил 1 товар");
				System.out.println("“оваров на складе: " + product);

				/*
				 * возобновл€етс€ работа всех потоков, у которых ранее был
				 * вызван метод await()
				 */
				condition.signalAll();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			} finally {
				locker.unlock(); // снимаем блокировку
			}
		}

		public void put() {

			try {
				locker.lock(); // блокируем
				// пока на складе 3 товара, ждем освобождени€ места
				while (product >= 3)
					condition.await(); // ждем

				product++;
				System.out.println("ѕроизводитель добавил 1 товар");
				System.out.println("“оваров на складе: " + product);
				// "пробуждаем" все потоки на объекте
				condition.signalAll();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			} finally {
				locker.unlock();
			}
		}
	}


// класс ѕроизводитель
class Producer implements Runnable {

	Store store;

	Producer(Store store) {
		this.store = store;
	}

	public void run() {
		for (int i = 1; i < 6; i++) {
			store.put();
		}
	}
}

//  ласс ѕотребитель
class Consumer implements Runnable {

	Store store;

	Consumer(Store store) {
		this.store = store;
	}

	public void run() {
		for (int i = 1; i < 6; i++) {
			store.get();
		}
	}
}

