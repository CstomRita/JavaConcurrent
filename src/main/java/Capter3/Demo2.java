package Capter3;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 17:26 2019-04-24
 * @ Description：抢票小程序
 * 主要看各个容器是否同步
 * @ Modified By：
 * @Version: $
 */
public class Demo2 {

    /*********************** ArrayList 通过加锁实现*******************/
    List<Integer> tickets1 = new ArrayList<>();
    public void init1() { //添加票
        for(int i = 0; i < 1000; i++) {
            this.tickets1.add(i);
        }
    }

    public void getTicket1() {
        synchronized (tickets1) {
            if (tickets1.size() == 0) {
                System.out.println(Thread.currentThread().getName() + "票已经空");
            }else {
                Integer i = tickets1.remove(0);
                System.out.println(Thread.currentThread().getName() + "获取:" + i);
            }
        }
    }
    /*********************** 同步容器不加锁 调用原子操作 会有问题*******************/
    Vector<Integer> tickets2 = new Vector<>();
    public void init2() {
        for(int i = 0; i < 1000; i++) {
            this.tickets2.add(i);
        }
    }
    public void getTicket2() { //虽然size是原子操作，remove是原子操作，但是这两个操作之间可能有空闲，可能会造成其他线程修改条件，如果想没有问题还是要加锁
        if (tickets2.size() == 0) {
            System.out.println(Thread.currentThread().getName() + "票已经空");
        }else {
            try { //此处是为了模拟操作间的空闲
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "获取:" + tickets2.remove(0));
        }
    }

    /****************************** 并发容器不加锁 *********************/
    Queue<Integer> tickets3 = new ConcurrentLinkedQueue<>();
    public void init3() {
        for (int i = 0; i < 1000;i++) {
            tickets3.add(i);
        }
    }
    public void getTicket3() {
        if (tickets3.size() == 0) {
            System.out.println(Thread.currentThread().getName() + "票已经空");
        }else {
            try { //此处是为了模拟操作间的空闲
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "获取:" + tickets3.poll());
        }
    }


    public static void main(String[] args) {
        Demo2 demo2 = new Demo2();

        demo2.init3();
        System.out.println("票余额" + demo2.tickets3.size());

        List<Thread> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new Thread(() -> {
                for (int j = 0; j < 200; j++) {
                    demo2.getTicket3();
                }
            },"抢票者"+i));
        }

        list.forEach(thread -> {
            thread.start();
        });

        //等待其他线程执行完毕
        list.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("票余额" + demo2.tickets3.size());
    }

}
