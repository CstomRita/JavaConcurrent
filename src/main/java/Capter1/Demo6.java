package Capter1;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 15:22 2019-04-22
 * @ Description：voliate
 * @ Modified By：
 * @Version: $
 */
public class Demo6 {

    /*volatile*/ boolean flag = true;
    void m() {
        System.out.println("m start");
        while (flag) {
            try {
                TimeUnit.SECONDS.sleep(1); //如果CPU有空闲，它是有可能从主存中刷新一下的(有可能 不可控)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        Demo6 demo6 = new Demo6();
        new Thread(() -> demo6.m()).start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        demo6.flag = false; // 修改flag，如果修改成功线程将停止，输出 m end
    }
}
