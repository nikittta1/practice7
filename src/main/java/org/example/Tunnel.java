package org.example;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tunnel extends Stage
{
    private static final int length = 80;
    private final Semaphore semaphore;
    private final Object lock;

    public Tunnel(int limit)
    {
        super(length, "Тоннель " + length + " метров");

        semaphore = new Semaphore(limit);
        lock = new Object();
    }

    @Override
    public void go(Car car)
    {
        synchronized (Main.outLock) {
            System.out.println(car.getName() + " готовится к этапу(ждет): " + description);
        }
        try
        {
            synchronized (lock) {
                semaphore.acquire();
                synchronized (Main.outLock) {
                    System.out.println(car.getName() + " начал этап: " + description);
                }
            }
            Thread.sleep(length / car.getSpeed() * 1000L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            synchronized (Main.outLock) {
                System.out.println(car.getName() + " закончил этап: " + description);
            }
            semaphore.release();
        }
    }
}
