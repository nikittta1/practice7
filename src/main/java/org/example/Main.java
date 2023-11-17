package org.example;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Main
{
    public static final Object outLock = new Object();
    public static final int CARS_COUNT = 6;

    public static void main(String[] args)
    {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        Race race = new Race(new Road(60), new Tunnel(1), new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        CyclicBarrier startBarrier = new CyclicBarrier(CARS_COUNT, () -> System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!"));
        CyclicBarrier endBarrier = new CyclicBarrier(CARS_COUNT+1, () -> System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!"));

        Car.setStartBarrier(startBarrier);
        Car.setEndBarrier(endBarrier);

        for (int i = 0; i < cars.length; i++)
        {
            cars[i] = new Car(race, 200 + (int) (Math.random() * 1));
        }

        for (Car car : cars)
        {
            new Thread(car).start();
        }
        try {
            endBarrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Car.showWinners();
}
}