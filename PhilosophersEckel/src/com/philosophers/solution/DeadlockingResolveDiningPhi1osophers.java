package com.philosophers.solution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// ѕредметные области с подобными проблемами - вокзалы, аэропорты, транспортное
//движение (пробки на дорогах).

public class DeadlockingResolveDiningPhi1osophers {
	public static void main(String[] args) throws Exception {
		int ponder = 5; 
		/*if(args.length > 0)
			ponder = Integer.parseInt(args[0]); */
		int size = 5; 
		
		/*if(args.length > 1)
			size = Integer.parseInt(args[1]); */
		
		ExecutorService exec = Executors.newCachedThreadPool(); // пул потоков
		Chopstick[] sticks = new Chopstick[size]; // массив палочек
		for(int i = 0; i < size; i++)
			sticks[i] = new Chopstick(); // заполн€ем массив палочками
		
		/* Ќеправильное решение
		for(int i=0; i < size; i++) // создаем и запускаем 5 потоков(философов)
			exec.execute(new Philosopher(sticks[i], sticks[(i+1) % size], i, ponder)); 
		
		 аждому философу назначаем палочки - первому - палочки 0,1,
		второму - 1,2, третьему - 2,3 четвертому - 3,4, п€тому - 4,0.
		ѕри этом может произойти взаимна€ блокировка, из-за след. проблем:
		ѕроблема кругового ожидани€.
		 аждый философ вз€л правую палочку, и находитс€ в состо€нии
		ожидани€ левой палочки - взаимна€ блокировка. ѕо крайней мере один
		философ должен удерживать ресурс и ждать освобождени€ ресурса, удержи-
		ваемого другим философом.
		ѕроблема взаимного исключени€ - нескольким потокам требуетс€ доступ
		к совместно используемым ресурсам.
		ѕотоки освобождают ресурсы в естественном пор€дке, не принудительно.
		
		*/
		
		/* –ешений может быть несколько.
		 * ќдно из решений проблемы взаимной блокировки  - назначить последнему
		 * философу палочки в обратном пор€дке - чтобы он сначала брал левую, 
		 * потом - правую.
		 * Ёто "разорвет" круг, когда все философы берут палочки и ждут в оди-
		 * наковой последовательности.
		 * 
		 */
		for(int i=0; i < size; i++) // создаем и запускаем 5 потоков(философов)
			if (i < (size-1))  // дл€ первых четырех философов 
				exec.execute(new Philosopher(sticks[i], sticks[i+1], i, ponder)); 
			else
				// дл€ последнего философа
				exec.execute(new Philosopher(sticks[0], sticks[i], i, ponder));
			
		
		
		if(args.length == 3 && args[2].equals("timeout")) 
			TimeUnit.SECONDS.sleep(5);
		else {
			System.out.println("Ќажмите 'Enter', чтобы завершить работу"); 
			System.in.read();
			}
			exec.shutdownNow();
			}
	}

