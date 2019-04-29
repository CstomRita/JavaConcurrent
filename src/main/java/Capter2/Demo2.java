package Capter2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 19:57 2019-04-23
 * @ Description：实现一个容器，具有 get put getCount方法
 * 能够支持两个生成者线程和10个消费者线程调用
 * @ Modified By：
 * @Version: $
 */
public class Demo2 {

    /**
     * wait notify实现
     */
    static class Container1<T extends Object> {
        private List<T> list = new ArrayList<T>();
        private int MAX_SIZE = 10;
        private int size = 0;

        public synchronized void put(T t) { //锁定的是this对象，container实例
            // 1 用while
            while (size == 10) {
                try {
                    // 2 容器满了 需要wait
                    System.out.println(Thread.currentThread().getName() + "容器已满");
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(t);
            System.out.println(Thread.currentThread().getName() + "添加" + t);
            size++;
            // 3 唤醒全部线程
            this.notifyAll();
        }

        public synchronized T get() {
            while (size == 0) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = list.get(size-1);
            list.remove(size-1);
            size--;
            System.out.println(Thread.currentThread().getName() + "获取" + t);
            this.notifyAll();
            return t;
        }
    }

    /**
     * lock condition
     */
    static class Container2<T extends Object>{
        private List<T> list = new ArrayList<>();
        private int MAX_SIZE = 10;
        private int size = 0;

        private ReentrantLock lock = new ReentrantLock();
        private Condition produer = lock.newCondition();//生产者队列
        private Condition consumer = lock.newCondition();//消费者等待队列

        public void put(T t) {
            lock.lock();
            try {
                while (size == MAX_SIZE) {
                    System.out.println(Thread.currentThread().getName() + "容器已满");
                    produer.await();//加入生产者等待队列
                }
                list.add(t);
                System.out.println(Thread.currentThread().getName() + "添加" + t);
                size++;
                consumer.signalAll();//通知消费者等待队列醒来
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }

        public T get() {
            lock.lock();
            T t = null;
            try {
                while (size == 0) {
                 //   System.out.println(Thread.currentThread().getName() + "容器已空");
                    consumer.await();
                }
                t = list.get(size-1);
                list.remove(size-1);
                size--;
                System.out.println(Thread.currentThread().getName() + "获取" + t);
                produer.signalAll();
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            return t;
        }

    }

    public static void main(String[] args) {
        Container1<Integer> container1 = new Container1<Integer>();
        Container2<Integer> container2 = new Container2<Integer>();
        //10个消费者，取10次
        for (int i = 0 ; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    container2.get();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"消费者"+ i ).start();
        }

        //2个生产者，放10次
        for (int i = 0 ; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 50; j++) { //消费者想获取多少数，生产者就要给出多少，否则生成者线程死亡，消费者还在等
                    container2.put(new Random().nextInt(10));
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },"生成者"+ i ).start();
        }
    }

}
