package Capter1;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 22:32 2019-04-21
 * @ Description：脏读的例子
 * @ Modified By：
 * @Version: $
 */
public class Demo3 {
    private int balance = 0;

    public synchronized void setBalance(int balance) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.balance = balance;
    }

    public  int getBalance() {
        return this.balance;
    }

    public static void main(String[] args) {
        Demo3 demo3 = new Demo3();
        new Thread(() -> demo3.setBalance(100)).start();
        System.out.println(demo3.getBalance());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(demo3.getBalance());
    }
}
