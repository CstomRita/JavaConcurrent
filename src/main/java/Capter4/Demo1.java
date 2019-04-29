package Capter4;

import sun.nio.ch.ThreadPool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 15:10 2019-04-26
 * @ Description：ThreadPool
 * @ Modified By：
 * @Version: $
 */
public class Demo1 {
    public static void main(String[] args) {

        ExecutorService service = Executors.newFixedThreadPool(5); //创建了一个线程池。里面有5个线程

        //提交7个任务
        for (int i = 0; i < 7; i++) {
            service.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        System.out.println(service);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(service);
    }
}
