package Capter1;

import java.util.concurrent.TimeUnit;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 17:02 2019-04-22
 * @ Description：synchronized的锁对象
 * @ Modified By：
 * @Version: $
 */
public class Demo9 {

    Object o = new Object();
    public void m1() {
        synchronized (o) {
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + o.hashCode());
            }
            }
    }

    /**
     * Q：如果更改一下题目，线程A运行时占有锁O，更改O对象，
     * 线程B启动后，线程A释放原有的锁，再去获取锁O时，获取的是哪一个对象的锁？？
     * A：获取的是新对象o的锁
     */
    public void m2() {
        synchronized (o) {
            int i = 0;
            while (i < 10) {
                i++;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + o.hashCode());
            }
            if (Thread.currentThread().getName().equals("Thread-1")) {
                // 令线程1 占有新锁
                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + ":" + o.hashCode());
                }
            }
        }

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "重新获得锁");
        synchronized (o) { //令旧线程重新获取的锁，此时旧线程获取的就是当前o指向的对象锁了，一直blocked
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + o.hashCode());
            }
        }
    }


    public static void main(String[] args) {
        Demo9 demo9 = new Demo9();
        new Thread(() -> demo9.m2()).start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        demo9.o = new Object();  //将o指向了一个新的对象,如果没有这一句，下面的线程永远不会执行
        /*
         * 如果锁指向的实际对象发生了改变，新的线程将以这个新的对象作为上锁的基准，
         * 旧线程已经获取了原先旧对象的锁，依然可以运行。
         * 此时新旧线程获取的锁不同，将失去同步效果。
         */
        new Thread(() -> demo9.m2()).start();

    }
}
