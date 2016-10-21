package com.task7_2.Locks;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* Создадим ScoreBoard class, который имеет два метода - один для обновления 
 * данных, второй - для получения оценки состояния (может быть 
 * хорошее и плохое). ReentrantReadWriteLock будет следить за паралллельностью.
 * 
 */

public class ReentrantReadWriteLockExample {
	public static void main(String[] args) {
		final int threadCount = 2;
		final ExecutorService exService = Executors
				.newFixedThreadPool(threadCount);
		final ScoreBoard scoreBoard = new ScoreBoard();
		// один поток для обновления данных
		exService.execute(new ScoreUpdateThread(scoreBoard));
		// один поток для чтения данных
		exService.execute(new ScoreHealthThread(scoreBoard));
		exService.shutdown();
	}
}

class ScoreBoard {
	private boolean scoreUpdated = false;
	private int score = 0;
	String health = "Not Available";
	
	// создаем экземпляр ReentrantReadWriteLock
	final ReentrantReadWriteLock rrwl = new ReentrantReadWriteLock();

	// метод для чтения состояния здоровья
	public String getMatchHealth() {
		// получаем блокировку чтения
		rrwl.readLock().lock();
		
		if (scoreUpdated) { // если данные обновлены
			rrwl.readLock().unlock(); // отдаем блокировку чтения
			
			rrwl.writeLock().lock(); // получаем блокировку записи
			try {
				if (scoreUpdated) { // если данные обновлены
					score = fetchScore(); // записываем значение score для чтения 
					                   // переменной health
					scoreUpdated = false; // меняем флаг
				}
				rrwl.readLock().lock(); // блокируем чтение и
			} finally {
				rrwl.writeLock().unlock();
			}
		}
		
		try {
			if (score % 2 == 0) {
				health = "Bad Score"; // читаем переменные 
			} else {
				health = "Good Score";
			}
		} finally {
			rrwl.readLock().unlock(); // отдаем блокиовку
		}
		return health;
	}

	// метод для обновления данных
	public void updateScore() {
		try {
			rrwl.writeLock().lock(); // получаем блокировку записи - 
			                // для изменения записи
			scoreUpdated = true;  // меняем флаг
		} finally {
			rrwl.writeLock().unlock(); // отдаем бловировку записи
		}
	}

	private int fetchScore() {
		Calendar calender = Calendar.getInstance();
		return calender.get(Calendar.MILLISECOND);
	}
}

class ScoreHealthThread implements Runnable {
	private ScoreBoard scoreBoard;
	public ScoreHealthThread(ScoreBoard scoreTable) {
		this.scoreBoard = scoreTable;
	}
	@Override
	public void run() {
		for(int i= 0; i< 5; i++) {
			System.out.println("Match Health: "+ scoreBoard.getMatchHealth());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
} 

class ScoreUpdateThread implements Runnable {
	private ScoreBoard scoreBoard;
	public ScoreUpdateThread(ScoreBoard scoreTable) {
		this.scoreBoard = scoreTable;
	}
	@Override
	public void run() {
		for(int i= 0; i < 5; i++) {
			System.out.println("Score Updated.");		
			scoreBoard.updateScore();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
} 
