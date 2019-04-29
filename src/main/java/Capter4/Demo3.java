package Capter4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 18:41 2019-04-26
 * @ Description：线程池并行计算
 * @ Modified By：
 * @Version: $
 */
public class Demo3 {

    static boolean isPrime(int num) {
        for (int i = 2; i < num/2; i++) {
            if (num % i == 0) return false;
        }
        return true;
    }

    static List<Integer> getPrime(int start,int end) {
        List<Integer> res = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) res.add(i);
        }
        return res;
    }

    public static void main(String[] args) {
        /****单线程********/
        long start = System.currentTimeMillis();
        getPrime(1,20000);
        System.out.println("单线程"+(System.currentTimeMillis() - start));


        /*********多线程*********/
        long start1 = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        Future<?> future1 = executorService.submit(() -> {
            getPrime(1, 5000);
        });
        Future<?> future2 = executorService.submit(() -> {
            getPrime(5001, 8000);
        });
        Future<?> future3 = executorService.submit(() -> {
            getPrime(8001, 13000);
        });
        Future<?> future4 = executorService.submit(() -> {
            getPrime(13001, 15000);
        });
        Future<?> future5 = executorService.submit(() -> {
            getPrime(15001, 16000);
        });
        Future<?> future6 = executorService.submit(() -> {
            getPrime(16001, 17000);
        });
        Future<?> future7 = executorService.submit(() -> {
            getPrime(17000, 18001);
        });
        Future<?> future8 = executorService.submit(() -> {
            getPrime(18001, 19000);
        });
        Future<?> future9 = executorService.submit(() -> {
            getPrime(19001, 20000);
        });
        try {
            System.out.println(executorService);
            future1.get();
            future2.get();
            future3.get();
            future4.get();
            future5.get();
            future6.get();
            future7.get();
            future8.get();
            future9.get();
            System.out.println(System.currentTimeMillis() - start1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
