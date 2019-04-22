package Capter1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 16:18 2019-04-22
 * @ Description：volatile的非原子性
 * @ Modified By：
 * @Version: $
 */
public class Demo7 {
    volatile int count = 0;
    void m() {
        for (int i = 0; i < 10000; i++) count++;
    }

    public static void main(String[] args) {
        Demo7 demo7 = new Demo7();

        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
           threads.add(new Thread(() ->demo7.m()));
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

        System.out.println(demo7.count);//如果没有问题的话，输出结果应该是50000，5个线程分别进行10000++的操作
    }
}
