package Capter1;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 22:45 2019-04-21
 * @ Description：synchronized的可重复锁
 * @ Modified By：
 * @Version: $
 */
public class Demo4 {

    public synchronized void m1() {
        System.out.println("m1");
        m2();
    }

    public synchronized void m2() {
        System.out.println("m2");
    }

    public static void main(String[] args) {
        Demo4 demo4 = new Demo4();
        new Thread(() -> demo4.m1()).start();

        //子类同步方法调用父类同步方法
        Demo demo = new Demo();
        new Thread(()->demo.m()).start();
    }
}

class Demo extends Demo4 {
    public synchronized void m() {
        System.out.println("子类"+"m");
        super.m1();
    }
}
