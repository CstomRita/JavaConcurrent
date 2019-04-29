package Capter2;

import java.util.concurrent.TimeUnit;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 09:36 2019-04-24
 * @ Description： ThreadLocal
 * @ Modified By：
 * @Version: $
 */
public class Demo3 {
    ThreadLocal<Integer> threadLocal = new ThreadLocal();

    public void m1() {
        threadLocal.set(3);
        System.out.println("m1 设置为 3");
    }

    public void m2() {
        Integer integer = threadLocal.get();
        System.out.println("m2 获取" + integer);

    }

    public static void main(String[] args) {
        Demo3 demo3 = new Demo3();
        new Thread(() -> demo3.m1()).start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> demo3.m2()).start();
    }
}
