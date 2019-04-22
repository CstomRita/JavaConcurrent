package Capter1;

import java.util.concurrent.TimeUnit;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 23:05 2019-04-21
 * @ Description：运行时异常 数据回滚
 * @ Modified By：
 * @Version: $
 */
public class Demo5 {

    private int i = 0;
    public synchronized void m1() {
        while (true) {
            i++;
            System.out.println(Thread.currentThread().getName() + ":" + i);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //此处会抛出异常，如果不处理，i就是5
           /*
           if (i == 5) {
                i = i / 0;
            }
            */
           // 处理运行时异常
            if ( i == 5) {
                try {
                    i = i/0;
                }catch (Exception e) {
                    // 此时还未释放锁
                    i = 0;
                }
            }
        }
    }

    public static void main(String[] args) {
        Demo5 d1 = new Demo5();
        new Thread(()->d1.m1()).start();

        //如果不释放锁，m2不会执行
        new Thread(()->d1.m1()).start();
    }
}
