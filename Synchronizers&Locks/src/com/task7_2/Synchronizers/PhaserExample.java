package com.task7_2.Synchronizers;

import java.util.ArrayList;
import java.util.concurrent.Phaser;

/*���� ���� ���������. �� ������ ������� �� ��� ����� ������ ��������� � ����� 
 * ��������. ������� �������� �� ����� � ��������������� �� ������ ��������� 
 * �� ��������� �����. ����� �������� ��������� ������� ���� � ����. ��� ����� 
 * ������� ���������� � �������� �� �� ������ ����������.
 */
public class PhaserExample {

	private static final Phaser PHASER = new Phaser(1);// ����� ������������
														// ������� �����

	public static void main(String[] args) {

		// ��������� �������
		ArrayList<Passenger> passengers = new ArrayList<>();

		for (int i = 1; i < 5; i++) { // ����������� ���������� �� ����������
			if ((int) (Math.random() * 2) > 0)
				passengers.add(new Passenger(i, i + 1));// ���� �������� �������
														// �� ���������

			if ((int) (Math.random() * 2) > 0)
				passengers.add(new Passenger(i, 5)); // ���� �������� ������� ��
														// ��������
		}
		for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    // ������� ���������
                	System.out.println("������� ������ �� �����.");
                    /* � ��������� ����, ����� ���������� ����� 
                     * ����. ����� �� ������������������,
                     *  � ���������� ����������;
                     */
                    int y = PHASER.arrive();//� ���� 0 ����� 1 �������� - �������
                                    //(������� �����)
                    System.out.println(y);
                    break;
                case 6:
                    System.out.println("������� ����� � ����.");
                    /* �������� � ���������� ���� ��� ������� �������
                     * � ������� ��� � �����������. 
                     */
                    PHASER.arriveAndDeregister();
                    break;
                default:
                    // �������� ����� ������� ����
                	int currentBusStop = PHASER.getPhase();
                    System.out.println("��������� � " + currentBusStop);
                    // ��� ������� ������
                    for (Passenger p : passengers)          
                        // ���� ����� "���������" ��������� � ������� "���������"
                    	if (p.departure == currentBusStop) {
                    		//������������ �����, ������� ����� ��������� ����
                    		PHASER.register();
                            p.start();        
                        }
                    /* ����� �������� ���������� ����,
                     * g���� ������������������ �� �������, ���� ��� ��������� 
                     * ������� �� �������� ��������� ������ ����.
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
			System.out.println(this + " ��� �� ��������� � " + this.departure);
		}
		
		@Override
        public void run() {
            try {
                System.out.println(this + " ��� � �������.");

                while (PHASER.getPhase() < destination) //���� ������� �� ������� �� ������ ���������(����)
                    PHASER.arriveAndAwaitAdvance(); //��������� ���������� ���� � ����, ���� 
                                       //������ ������ �� �������� ��������� ������ ����

                Thread.sleep(1);
                System.out.println(this + " ������� �������.");
                /* ��� ������ ���� == destination - ���������� ����, ������� �����
                 * � �����������
                 */
                PHASER.arriveAndDeregister();   
            } catch (InterruptedException e) {
            }
        }
		
		@Override
        public String toString() {
            return "��������{" + departure + " -> " + destination + '}';
        }
	}

}
