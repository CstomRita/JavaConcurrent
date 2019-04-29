package Capter4;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.*;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 19:04 2019-04-26
 * @ Description：6种线程池
 * @ Modified By：
 * @Version: $
 */
public class Demo4 {

    public static void main(String[] args) {

        /*************缓存线程池************/
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        System.out.println(executorService); //刚创建时没有线程
//        executorService.submit(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        System.out.println(executorService);
//        try {
//            TimeUnit.SECONDS.sleep(70);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // 60s后线程消亡
//        System.out.println(executorService);

        /***********单例线程池***********/
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        System.out.println(executorService);
//        for (int i = 0; i < 3; i++) {
//            executorService.submit(() -> {
//                try {
//                    System.out.println(Thread.currentThread().getName());
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        System.out.println(executorService);
//        executorService.shutdown();

        /************scheduleThreadPool**********/
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
//        scheduledExecutorService.scheduleWithFixedDelay(()->{
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                System.out.println(Thread.currentThread().getName());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        },2,1,TimeUnit.SECONDS);
//
//        //令4个任务占据线程池，发现定时任务不再执行
//        //说明定时任务到达时间需要执行的时候，
//        // 同样要去寻找线程池中是否有空闲的线程，
//        // 如果当前线程并没有空闲，也需要等待….(定时的那个时间也并不完全准确，也要看线程池的情况)
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < 4; i++) {
//            scheduledExecutorService.submit(()->{
//                while (true){}
//            });
//        }

        /****************WorkStealingPool***********/
        System.out.println(Runtime.getRuntime().availableProcessors());
        Executors.newWorkStealingPool();

    }
}
