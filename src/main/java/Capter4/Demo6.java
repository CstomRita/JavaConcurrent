package Capter4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 20:38 2019-04-26
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class Demo6 {
    static boolean isPrime(int target) {
        for (int i = 2; i <= target / 2; i++) {
            if (target % i == 0) return false;
        }
        return true;
    }
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            list.add(1000000 + r.nextInt(10000000));
        }

        long start = System.currentTimeMillis();
        list.forEach(o -> isPrime(o));
        System.out.println("普通方式:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        list.parallelStream().forEach(o -> isPrime(o)); //使用ForkJoin框架的多线程流遍历
        System.out.println("parallelStream:" + (System.currentTimeMillis() - start));
    }
}
