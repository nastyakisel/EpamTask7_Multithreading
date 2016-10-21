package com.task7_2.Synchronizers;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
	
	/* ������� ������ CountDownLatch � �������� ��� ���������� ��������,
	 * ������� ������ ���� ���������, ���� ������ ���� ��������� ����
	 * ��������, ����� ����������� ���������� ����.
	 * ������� 8 - 5 ����������� ���������� � ��������� ������, ���� ��� 
	 * ������� (�� �����, ��������, ����).
	 */
	private static final CountDownLatch START = new CountDownLatch(8);
    //�������� ����� �������� ������
    private static final int trackLength = 500000;

    public static void main(String[] args) throws InterruptedException {
        /*��������� ���� ������� (�����). ������ ����� ��������� ����� run()
         * (���������� � ������),
         * ������ ����� ��������� �������� �������� �� �������. 
         */
    	for (int i = 1; i <= 5; i++) {
            new Thread(new Car(i, (int) (Math.random() * 100 + 50))).start();
            Thread.sleep(1000);
        }

        while (START.getCount() > 3) //���������, ��������� �� ��� ����������
            Thread.sleep(100);              //� ��������� ������. ���� ���, ���� 100ms

        Thread.sleep(1000);
        System.out.println("�� �����!");
        START.countDown();// ����� �������, ����� ��������� ������� �� 1
        Thread.sleep(1000);
        System.out.println("��������!");
        START.countDown();//����� �������, ����� ��������� ������� �� 1
        Thread.sleep(1000);
        System.out.println("����!");
        START.countDown();//����� �������, ����� ��������� ������� �� 1
        /*����� ��������� �������, �������� �������� ����� ����, � ���
         * ��������������� ������ ������������ ��������������.
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
                System.out.printf("���������� �%d �������� � ��������� ������.\n", carNumber);
                /*���������� �������� � ������, �������� ���������,
                 * �������� �������� ����������� �� �������.
                 */
                START.countDown();
                /*����� await() ��������� �����, ��������� ���� �����,����
                 * ������� CountDownLatch �� ������ �������� 0
                 */
                START.await();
                Thread.sleep(trackLength / carSpeed);
                System.out.printf("���������� �%d �����������!\n", carNumber);
            } catch (InterruptedException e) {
            }
        }
    }
}
