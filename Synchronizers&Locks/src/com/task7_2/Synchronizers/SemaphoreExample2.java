package com.task7_2.Synchronizers;

import java.util.concurrent.Semaphore;

/*������� ������� (����� ������) � ��� ��������� 
 * (������ java). ���� ��������, ��������� ������� ������. 
 * � ��� ����� ������ ��������, ������� ��������� ���� � ����� ����������, 
 * �� ����� ����� ������� � ������� ��.
 * http://movejava.blogspot.com.by/2013/06/javautilconcurrentsemaphore.html
 */


public class SemaphoreExample2 {
	public static void main(String[] args) {
        /* ������� ������ ��������. ������� ���������������� �����������
         * ����������� ������� - permits. � ������ ������ ��������� ������ ������
         * ������ ������.
         */
		Semaphore semaphore = new Semaphore(1); 
        new Worker(semaphore, "Adder", true).start(); // ������� ������
        new Worker(semaphore, "Reducer", false).start();
    }
}
/* ����� �������, ������� ������������ ����� ����� ������. ������������
 * � �������� (��������) ����� �������� ������ ���� �����. �����������
 * ������ ��� �������� ������� ��������. 
 */
class Cart {
    private static int weight = 0;

    public static void addWeight(){ // ��������� �������
        weight++;
    }

    public static void reduceWeight(){ // ���������� �������
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
        // ��� ������ ���������� � ������
    	System.out.println(workerName + " started working..."); 
        try {
            System.out.println(workerName + " waiting for cart...");
            /* ������ ����� ������ acquire() ��������� �������� �������� (permits)
             * �� �������. � ��� �������� �������� 1, ������, ������ ���� �����
             * ����� �������� � ������ ��������. ������ ����� �������, ���� 
             * ������ ����� �� �������� ������� (�� �������� ����).
             */
            semaphore.acquire();
            /* �������� �������� ����� ����. ������, ������ ����� �����������, ����
             * ������ �� ������ �� �����.
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
            /*������� ����� ������� �� �����, ������� ����� ��� ������ �������
             * ������. ������ ����� ������ release() ����������� �������� ��������
             * �� �������.
             */
        	semaphore.release(); 
        }
    }
}
