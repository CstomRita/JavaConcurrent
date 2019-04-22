package Capter1;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 22:05 2019-04-21
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
public class Demo2 {

    public synchronized void m1() {
        System.out.println(Thread.currentThread() + "m1 start");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread() + "m1 end");
    }

    public void m2() {
        try {
            Thread.sleep(5000);
            System.out.println(Thread.currentThread() + "m2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        final Demo2 demo2 = new Demo2();
        /**
         * Lamada表达式
         */
        new Thread(()->demo2.m1()).start();
        new Thread(()->demo2.m2()).start();
    }
}
