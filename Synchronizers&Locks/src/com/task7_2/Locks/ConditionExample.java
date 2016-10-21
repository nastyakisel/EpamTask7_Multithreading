package com.task7_2.Locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/*���� �����, ��� ����� ������������ ���� ��������� �� ����� 3 �������. 
 * ������������� ������ ���������� 5 �������, � ���������� ������ ��� 
 * ������ ������. � �� �� ����� ���������� �� ����� ������ �����, ���� �� 
 * ������ ��� ������� �������.
 */

public class ConditionExample {
	public static void main(String[] args) {
		
		Store store = new Store();
		Producer producer = new Producer(store);
		Consumer consumer = new Consumer(store);

		new Thread(producer).start(); // ����� -�������������
		new Thread(consumer).start(); // ����� -����������
	}
}

	// ����� �������, �������� ������������� ������
class Store {
		private int product = 0;
		ReentrantLock locker;
		Condition condition;

		Store() {
			// ������� ������ ReentrantLock
			locker = new ReentrantLock();
			// ���������� ��� ��� �������� �������, ���������� � �����������
			condition = locker.newCondition();
		}

		public void get() {

			try {
				// ������������� ����������
				locker.lock();
				// ���� ��� ��������� ������� �� ������, �������
				while (product < 1)
					/*
					 * ����� ����, ���� � ����� ��������� ������� � ���� ������
					 * ����� �� ������� ������ signal/signalAll
					 */
					condition.await();

				product--;
				System.out.println("���������� ����� 1 �����");
				System.out.println("������� �� ������: " + product);

				/*
				 * �������������� ������ ���� �������, � ������� ����� ���
				 * ������ ����� await()
				 */
				condition.signalAll();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			} finally {
				locker.unlock(); // ������� ����������
			}
		}

		public void put() {

			try {
				locker.lock(); // ���������
				// ���� �� ������ 3 ������, ���� ������������ �����
				while (product >= 3)
					condition.await(); // ����

				product++;
				System.out.println("������������� ������� 1 �����");
				System.out.println("������� �� ������: " + product);
				// "����������" ��� ������ �� �������
				condition.signalAll();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			} finally {
				locker.unlock();
			}
		}
	}


// ����� �������������
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

// ����� �����������
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

