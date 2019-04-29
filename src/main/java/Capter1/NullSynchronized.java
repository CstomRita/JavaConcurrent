package Capter1;

import java.util.concurrent.TimeUnit;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 17:20 2019-04-24
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class NullSynchronized {

    private static Object object;


    public void m1() {
        synchronized (object) {
            while (true) {}
        }
    }

    public void m2() {
        synchronized (object) {
            System.out.println("m2");
        }
    }

    public static void main(String[] args) {
        NullSynchronized nullSynchronized = new NullSynchronized();
        new Thread(() -> nullSynchronized.m1()).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> nullSynchronized.m2()).start();
    }
}
