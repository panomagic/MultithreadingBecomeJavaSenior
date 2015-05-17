import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Есть счетчик, подсчитывающий количество вызовов.
 *
 * Почему счетчик показывает разные значения и не считает до конца?
 * Как это можно исправить не используя synchronized?
 * О: используя в цикле запуска нитей один из методов:
 * - join() - чтобы каждый раз перед запуском новой нити предыдущая выполняла цикл увеличения счетчика полностью
 * - sleep(ms) - чтобы нити стартовали с задержкой в ms миллисекунд, в течение которых каждая из них успеет выполнить цикл увеличения счетчика полностью
 *
 * Попробуйте закомментировать обращение к yield().
 * Измениться ли значение?
 * О: да, в среднем значения стали больше за счет уменьшения количества прерываний каждой нити
 */
public class AtomicCounterTutor {

    int counter=0;

    class TestThread implements Runnable {
        String threadName;

        public TestThread(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            for (int i=0;i<10000;i++) { //каждая нить считает от 0 до 9999, увеличивая counter
                counter++;
                //Thread.yield();   //"досрочное" завершение тек. нити и переключение процессора на следующую
            }
        }
    }

    @Test
    public void testThread() throws InterruptedException {
        List<Thread> threads = new ArrayList<Thread>();
        for (int i=0;i<100;i++) {
            threads.add(new Thread(new TestThread("t"+i))); //создаем массив из 100 нитей, готовых к работе
            System.out.println("Нить t"+i);
        }
        System.out.println("Starting threads");
        for (int i = 0; i < 100; i++) {
            threads.get(i).start();
            System.out.println("Нить " + i + " досчитала до " + counter);
            //threads.get(i).join();        //добавляем данный метод, чтобы каждый раз перед запуском новой нити предыдущая выполняла цикл увеличения счетчика полностью
            //threads.get(i).sleep(200);  //альтернативно, можно использовать sleep(ms), чтобы нити стартовали с небольшой задержкой, в течение которой каждая из них успеет выполнить цикл увеличения счетчика полностью
        }
        /*
        try {
            for (int i=0;i<100;i++) {
                threads.get(i).join();  //дожидаемся завершения работы нити i
                System.out.println("Нить " + i + " прервалась на " + counter);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        System.out.println("Counter="+counter);

    }

}