import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class Main {
    final static int maxPhilosopherCount = 5;
    final static boolean[] forks = new boolean[maxPhilosopherCount];

    public static void main(String[] args) throws InterruptedException {
        Semaphore sem = new Semaphore(2);

        for (int i = 1; i <= Main.maxPhilosopherCount; i++) {
            new Philosopher(sem, i).start();
            sleep(500);
        }
    }
}

class Philosopher extends Thread {
    // Семафор, ограничивающий число философов
    Semaphore sem;

    // кол-во приемов пищи
    Integer num = 0;
    // условный номер философа
    Integer id;

    // в качестве параметров конструктора передаем идентификатор философа, семафор, набор вилок
    Philosopher(Semaphore sem, Integer id) {
        this.sem = sem;
        this.id = id;
    }

    @Override
    public void run() {
        boolean forkLeft;
        boolean forkRight;
        try {
            sem.acquire();
            while (num < 3) {
                synchronized (Main.forks) {
                    if (id == 1) {
                        forkLeft = Main.forks[Main.maxPhilosopherCount - 1];
                    } else {
                        forkLeft = Main.forks[id - 2];
                    }
                    forkRight = Main.forks[id - 1];

                    if (!(forkLeft && forkRight)) {

                        forkLeft = true;
                        forkRight = true;

                        if (id == 1) {
                            Main.forks[Main.maxPhilosopherCount - 1] = forkLeft;
                        } else {
                            Main.forks[id - 2] = forkLeft;
                        }
                        Main.forks[id - 1] = forkRight;
                        System.out.println("Philosopher " + id + " is eating");
                        num++;
                    }
                }
                sleep(500);

                synchronized (Main.forks) {
                    forkLeft = false;
                    forkRight = false;
                    if (id == 1) {
                        Main.forks[Main.maxPhilosopherCount - 1] = forkLeft;
                    } else {
                        Main.forks[id - 2] = forkLeft;
                    }
                    Main.forks[id - 1] = forkRight;

                }
            }

            sem.release();
            System.out.println("Philosopher " + id + " is thinking");
            sleep(500);

        } catch (InterruptedException e) {
        }
    }
}

