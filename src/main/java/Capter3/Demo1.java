package Capter3;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ Author     ：ChangSiteng
 * @ Date       ：Created in 16:15 2019-04-24
 * @ Description：单例模式
 * @ Modified By：
 * @Version: $
 */
public class Demo1 {

    /******************************饿汉模式******************************/

    private static Object object1 = new Object();

    public static Object getInstace1() {
        return object1;
    }

    /****************************synchronized方法*********************/
    private static Object object2;
    public static synchronized Object getInstance2() {
        //锁住Demo1.class
        if (object2 == null) {
            object2 = new Object();
        }
        return object2;
    }
    /****************************双重检测*********************/
    private static Object lock = new Object();
    private static Object object3;
    public static Object getInstance3() {
        if (object3 == null) {
            synchronized (lock) { //不能用object3作为锁，因为object3是null，没有办法锁定一个null对象
                if (object3 == null) {
                    object3 = new Object();
                }
            }
        }
        return object3;
    }
    /****************************内部静态类*********************/
    private static class Object4{
        private static Object object4 = new Object();
    }
    public static Object getInstance4() {
        return Object4.object4;
    }
}
