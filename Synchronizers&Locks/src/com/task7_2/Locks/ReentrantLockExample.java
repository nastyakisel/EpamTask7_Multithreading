package com.task7_2.Locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
	public static void main(String[] args) {
		
		CommonResource commonResource= new CommonResource();
		Lock locker = new ReentrantLock(); // создаем блокатор
        // и создаем и запускаем потоки
		
		for (int i = 1; i < 6; i++){  
            Thread t = new Thread(new CountThread(commonResource, locker));
            t.setName("Поток "+ i);
            t.start();
        }
	}
}

class CommonResource{
    
    int x=0;
}

class CountThread implements Runnable{
	  
    CommonResource res;
    Lock locker;
    CountThread(CommonResource res, Lock lock){
        this.res=res;
        locker = lock;
    }
    public void run(){
        try{
            /* устанавливаем блокировку -
             * поток ожидает получения блокировки на объекте
             */
        	locker.lock(); 
            res.x=1;
            for (int i = 1; i < 5; i++){
                System.out.printf("%s %d \n", Thread.currentThread().getName(), res.x);
                res.x++;
                Thread.sleep(100);
            }
        }
        catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
        finally{
            locker.unlock(); // снимаем блокировку
        }
    }
}