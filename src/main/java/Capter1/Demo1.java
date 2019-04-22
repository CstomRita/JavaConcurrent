package Capter1;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 21:45 2019-04-21
 * @ Description：synchronized关键字
 * @ Modified By：
 * @Version: $
 */
public  class Demo1 {
    //1 锁定一个对象
    private Object o = new Object();
    public void test() {
        synchronized (o) {
            System.out.println();
        }
    }

    //2 锁定类对象等于修饰方法
    public synchronized void test1() {

    }
    public void test2() {
        synchronized (this) {

        }
    }

    // 3 锁定静态方法相等于锁定class对象
    public synchronized static void test3() {

    }
    public void test4() {
        synchronized (Demo1.class) {

        }
    }
}
