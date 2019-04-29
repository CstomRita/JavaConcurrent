package Capter4;

import java.util.concurrent.*;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 16:56 2019-04-26
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class Demo2 {
    public static void main(String[] args) {
//        FutureTask futureTask = new FutureTask(() -> {
//            TimeUnit.SECONDS.sleep(5);
//            return "OK";
//        });
//        new Thread(futureTask).start();
//        try {
//            System.out.println(futureTask.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


        /*************future task在线程池中的使用*************/
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<Integer> future = executorService.submit(() -> {
            int i = 3;
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return i;
        });

        /** 1
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
         **/

        /****isDone()查看任务是否完成*******/
        while (!future.isDone()) {
            System.out.println("主线程等待");
        }
//        try {
//            System.out.println(future.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        future.cancel(true);
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());
//        任务完成前被取消返回true，注意是完成前，如果完成后调用isCancelled则返回false

    }



}
