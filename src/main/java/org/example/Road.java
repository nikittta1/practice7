package org.example;
public class Road extends Stage
{
    public Road(int length)
    {
        super(length, "Дорога " + length + " метров");
    }

    @Override
    public void go(Car car)
    {
        synchronized (Main.outLock) {
            System.out.println(car.getName() + " начал этап: " + description);
        }
        try
        {
            Thread.sleep(length / car.getSpeed() * 1000L);

            synchronized (Main.outLock) {
                System.out.println(car.getName() + " закончил этап: " + description);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
