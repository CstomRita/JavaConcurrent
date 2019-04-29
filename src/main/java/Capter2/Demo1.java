package Capter2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 19:48 2019-04-23
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class Demo1 {

    Lock lock = new ReentrantLock();

    /**************************** lock unlock **********************/
    public void m1() {
        lock.lock();
        try {
            while (true) {  //为了防止同步代码中有什么异常，导致锁一直占有，把同步代码写到try中
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m2() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**************************** trylock **********************/
    public void m3() {
        boolean locked = lock.tryLock();
        if (locked) { // 获得锁
            try {
                System.out.println("m3" + "get lock");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (locked) lock.unlock(); //不要忘记释放锁啊啊啊啊啊啊啊啊
            }
        } else {
            System.out.println("m3 donnot get lock");
        }
    }

    public void m4() {
        boolean locked = false;
        try {
            locked = lock.tryLock(4, TimeUnit.SECONDS);//等待五秒是否获取到锁
            if (locked) {//获得到锁
                System.out.println("m4 get lock");
            } else {
                System.out.println("m4 donnot get lock");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (locked) lock.unlock();
        }

    }

    /**************************** lockInterruptibly **********************/
    public void m5() { // m5一直占据lock
        lock.lock();
        try {
            while (true) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void m6() {
        boolean locked = false;//因为不一定能获得锁，如果获取到了锁就要释放锁，需要设置标志位
        try {
            lock.lockInterruptibly();
            locked = lock.tryLock();//是否获得了锁，如果线程本身获得了锁，tryLock也会返回true
            while (true) {
                System.out.println("m6 get lock");
            }
        } catch (InterruptedException e) {
            System.out.println("m6 intterupted");
        } finally {
            if (locked) lock.unlock();//别忘了释放锁
        }
    }

    /**************************** runner **********************/

    public static void main(String[] args) {
        Demo1 demo1 = new Demo1();
        /* lock unlock */
         new Thread(() -> demo1.m1()).start();
         try {
         TimeUnit.SECONDS.sleep(2);
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         new Thread(() -> demo1.m2()).start();


        /*try lock */

        new Thread(() -> demo1.m3()).start();
        new Thread(() -> demo1.m4()).start();

        /* interrupt  */
        new Thread(() -> demo1.m5()).start();
        Thread thread = new Thread(() -> demo1.m6());
        thread.start();
        for(int i = 0; i < 10000; i++) {
            // do smoething
        }
        thread.interrupt(); //计算完成后，主线程打断m2

    }
}
