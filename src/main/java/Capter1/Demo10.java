package Capter1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 19:46 2019-04-22
 * @ Description：
 * 实现一个容器，容器中有两个方法，一个add添加元素，一个size显示元素个数
 * 要求：
 * 写两个线程，一个线程负责向容器中加元素，
 * 一个线程负责监视容器中元素个数，当元素个数为5时，线程2给出提示并退出。
 * @ Modified By：
 * @Version: $
 */
class Container{
    List list = new ArrayList();

    public void add(Object o) {
        list.add(o);
    }

    public int getSize() {
        return list.size();
    }
}

public class Demo10 {
    /*********************************Try 1 ***************************************/
   Container container = new Container();

    public void thread1_1() {
        int i = 0;
        while (true) {
            container.add(i++);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "add" + i);
        }
    }
    public void thread1_2() {
        while (true) {
            if (container.getSize() == 5) {
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "end");
    }

    /*********************************Try 2 ***************************************/
    volatile Container volatilecontainer = new Container();

    public void thread2_1() {
        int i = 0;
        while (true) {
            volatilecontainer.add(i++);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "add" + i);
        }
    }
    public void thread2_2() {
        while (true) {
            if (volatilecontainer.getSize() == 5) {
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + "end");
    }

    /***********************************************Try 3 *****************************************/
    final Object lock = new Object();

    public void thread3_1() {
        int i = 0;
        synchronized (lock) {
            while (true) {
                volatilecontainer.add(i++);
                try {
                    TimeUnit.SECONDS.sleep(1);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "add" + i);
                if (volatilecontainer.getSize() == 5) {
                    lock.notify(); //唤醒线程2
                    break; //notify要等待线程1执行完毕才能唤醒线程2，因此此处就需要跳出了
                    // 如果要wait获得锁，唤醒线程一定要执行完成退出才行。
                }
            }
        }
    }

    public void thread3_2() {
        synchronized (lock) {
            if (volatilecontainer.getSize() != 5) { //如果线程2获得了锁，首先做一个判断，如果是5直接输出，如果不是5等待wait
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "end");
        }
    }

    /*********************************************** Try 4 *****************************************/

    public void thread4_1() {
        int i = 0;
        synchronized (lock) {
            while (true) {
                volatilecontainer.add(i++);
                try {
                    TimeUnit.SECONDS.sleep(1);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "add" + i);
                if (volatilecontainer.getSize() == 5) {
                    lock.notify(); //唤醒线程2
                    //唤醒之后需要释放这个锁，可以再调用一个wait，立即释放
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void thread4_2() {
        synchronized (lock) {
            if (volatilecontainer.getSize() != 5) { //如果线程2获得了锁，首先做一个判断，如果是5直接输出，如果不是5等待wait
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "end");
            lock.notify();// 一定不要忘记这个notify，否则线程1会一直等待下去
        }
    }

    /*********************************************** Try 5 *****************************************/
    CountDownLatch latch = new CountDownLatch(1);

    public void thread5_1() {
        int i = 0;
        while (true) {
            volatilecontainer.add(i++);
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "add" + i);
            if (volatilecontainer.getSize() == 5) {
                latch.countDown();
            }
        }
    }
    public void thread5_2() {
        if (volatilecontainer.getSize() != 5) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "end");
        }
    }

    /*********************************************** run *****************************************/

    public static void main(String[] args) {
        Demo10 demo10 = new Demo10();
        /**
         * Try1：并不能解决问题
         * 线程2获取的container是在缓存中的，线程1的修改并不可见
         * 如果要可见，需要设置为volatile
         */
        new Thread(() -> demo10.thread1_1()).start();
        new Thread(() -> demo10.thread1_2()).start();

        /**
         * 可以基本满足条件，但是还存在很多问题
         * 1. 线程2一直在循环中等待，造成了CPU的浪费
         * 2. 虽然使用了volatile关键字保证可见，但是线程2在满足条件通知的时候线程1可能又加了元素，导致通知完成后条件可能已经不满足了，通知有一定不准确
         */
        new Thread(() -> demo10.thread2_1()).start();
        new Thread(() -> demo10.thread2_2()).start();

        /**
         * 为了防止线程2一直没有等到CPU执行
         * 哪怕满足条件，线程2一直没有等待CPU，更没有去争夺那个锁
         * 线程1即便notify了，在队列中也没有找到线程2
         * 因此需要线程2首先启动，保证线程2在锁对象的队列中。
         */
        /**
         * 这里导致的问题是线程2满足条件后，线程1没有办法继续执行了
         * 如何解决这个问题，即如何令唤醒线程在notify中不用等待执行完成之后就可以释放锁
         */
        new Thread(() -> demo10.thread3_2()).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> demo10.thread3_1()).start();

        /**
         * 再优化
         */
        new Thread(() -> demo10.thread4_2()).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> demo10.thread4_1()).start();

         /**
         * 使用latch实现
         */
        new Thread(() -> demo10.thread5_2()).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> demo10.thread5_1()).start();

    }
}
