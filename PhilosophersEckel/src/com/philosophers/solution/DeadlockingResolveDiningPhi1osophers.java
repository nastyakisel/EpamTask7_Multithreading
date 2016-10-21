package com.philosophers.solution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// ���������� ������� � ��������� ���������� - �������, ���������, ������������
//�������� (������ �� �������).

public class DeadlockingResolveDiningPhi1osophers {
	public static void main(String[] args) throws Exception {
		int ponder = 5; 
		/*if(args.length > 0)
			ponder = Integer.parseInt(args[0]); */
		int size = 5; 
		
		/*if(args.length > 1)
			size = Integer.parseInt(args[1]); */
		
		ExecutorService exec = Executors.newCachedThreadPool(); // ��� �������
		Chopstick[] sticks = new Chopstick[size]; // ������ �������
		for(int i = 0; i < size; i++)
			sticks[i] = new Chopstick(); // ��������� ������ ���������
		
		/* ������������ �������
		for(int i=0; i < size; i++) // ������� � ��������� 5 �������(���������)
			exec.execute(new Philosopher(sticks[i], sticks[(i+1) % size], i, ponder)); 
		
		������� �������� ��������� ������� - ������� - ������� 0,1,
		������� - 1,2, �������� - 2,3 ���������� - 3,4, ������ - 4,0.
		��� ���� ����� ��������� �������� ����������, ��-�� ����. �������:
		�������� ��������� ��������.
		������ ������� ���� ������ �������, � ��������� � ���������
		�������� ����� ������� - �������� ����������. �� ������� ���� ����
		������� ������ ���������� ������ � ����� ������������ �������, ������-
		������� ������ ���������.
		�������� ��������� ���������� - ���������� ������� ��������� ������
		� ��������� ������������ ��������.
		������ ����������� ������� � ������������ �������, �� �������������.
		
		*/
		
		/* ������� ����� ���� ���������.
		 * ���� �� ������� �������� �������� ����������  - ��������� ����������
		 * �������� ������� � �������� ������� - ����� �� ������� ���� �����, 
		 * ����� - ������.
		 * ��� "��������" ����, ����� ��� �������� ����� ������� � ���� � ���-
		 * ������� ������������������.
		 * 
		 */
		for(int i=0; i < size; i++) // ������� � ��������� 5 �������(���������)
			if (i < (size-1))  // ��� ������ ������� ��������� 
				exec.execute(new Philosopher(sticks[i], sticks[i+1], i, ponder)); 
			else
				// ��� ���������� ��������
				exec.execute(new Philosopher(sticks[0], sticks[i], i, ponder));
			
		
		
		if(args.length == 3 && args[2].equals("timeout")) 
			TimeUnit.SECONDS.sleep(5);
		else {
			System.out.println("������� 'Enter', ����� ��������� ������"); 
			System.in.read();
			}
			exec.shutdownNow();
			}
	}

