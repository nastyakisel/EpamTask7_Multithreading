package com.philosophers.solution;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {
	private Chopstick left; 
	private Chopstick right; 
	private final int id;
	private final int ponderFactor; 
	private Random rand = new Random(47);
	
	private void pause() throws InterruptedException { 
		if(ponderFactor == 0) 
			return;
			TimeUnit.MILLISECONDS.sleep(
				rand.nextInt(ponderFactor * 250));  // Философ думает
			}
	
	public Philosopher(Chopstick left, Chopstick right, 
			int ident, int ponder) { 
		this.left = left; 
		this.right = right; 
		id = ident;
		ponderFactor = ponder;
			}
	
	public void run() { 
		try {
	
			while(!Thread.interrupted()) {
				System.out.println(this + " " + "думает"); 
				pause(); // Философ думает
				// Философ проголодался и берет по очереди сначала правую палочку,
				// затем левую
				System.out.println(this + " " + "берет правую");
				right.take();
				System.out.println(this + " " + "берет левую"); 
				left. take();
				System.out.println(this + " " + "ест"); 
				pause(); // Ест
				right.drop(); // кладет палочки назад
				left.drop();
			}
			} catch(InterruptedException e) {
				System.out.println(this + " " + "выход через прерывание");
			}
	}
				public String toString() { 
					return "Философ " + id; } 
	
}
