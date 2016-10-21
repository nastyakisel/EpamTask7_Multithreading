package com.philosophers.solution;


public class Chopstick {
	private boolean taken = false;

	public synchronized void take() throws InterruptedException {
		while (taken) // true пока палочка взята другим потоком - ждем
			wait(); // ждем, пока палочка не освободится другим потоком
		taken = true; // занимаем палочку
	}

	public synchronized void drop() {
		taken = false; // палочка освобождена
		notifyAll(); // "извещаем" об этом другие потоки
	}
}
