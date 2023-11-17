package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Car implements Runnable {
    private static final List<String> winnersLists;
    private static final Object winnersLock = new Object();
    private static CyclicBarrier startBarrier;
    private static CyclicBarrier endBarrier;
    private static int carsCount;

    private static int winnersCount;

    static {
        carsCount = 0;
        winnersLists = new CopyOnWriteArrayList<>();
    }

    private final String name;
    private final Race race;
    private final int speed;
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        this.name = "Участник #" + ++carsCount;
    }
    public static void setStartBarrier(CyclicBarrier startBarrier) {
        Car.startBarrier = startBarrier;
    }
    public static void setEndBarrier(CyclicBarrier endBarrier) {
        Car.endBarrier = endBarrier;
    }

    public static void showWinners() {
        for (int i = 0; i < winnersLists.size() && i < 3; i++) {
            System.out.println(winnersLists.get(i) + ". Место - " + (i + 1));
        }
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            startBarrier.await();

            for (int i = 0; i < race.getStages().size(); i++) {
                Stage currentStage = race.getStages().get(i);
                currentStage.go(this);
            }

            int winnerNumber;
            synchronized (winnersLock) {
                winnerNumber = ++winnersCount;
                if (winnerNumber <= 3) {
                    winnersLists.add(this.name);
                }
            }
            endBarrier.await();

            synchronized (Main.outLock) {
                Main.outLock.notifyAll(); // Уведомление других потоков
            }
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}