package Capter1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 16:18 2019-04-22
 * @ Description：volatile的非原子性
 * @ Modified By：
 * @Version: $
 */
public class Demo8 {

    AtomicInteger count = new AtomicInteger(0);
    void m() {
        for (int i = 0; i < 10000; i++)
            count.incrementAndGet(); //替代count++，是原子操作
    }


    public static void main(String[] args) {
        Demo8 demo8 = new Demo8();

        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            threads.add(new Thread(() ->demo8.m()));
        }
        threads.forEach((o) -> o.start());

        // 等待所有线程执行完毕
        threads.forEach((o) -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(demo8.count);//如果没有问题的话，输出结果应该是50000，5个线程分别进行10000++的操作
    }
}
