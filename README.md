# Java多线程并发学习

参考 马士兵Java多线程高并发编程

## Capter1 Java多线程基础

#### Demo1 synchronized关键字

<font color=red>**synchronized锁定的是一个对象。可以修饰方法和代码块**</font>

1. 可以创建一个对象，锁定这个对象
2. 对于非静态类，可以锁定这个类实例对象 = 修饰<font color=red>**非静态方法**</font>
3. 修饰的静态方法 = 锁定这个类的class对象
4. 如果在静态类(只有内部静态类)，可以选择锁定外部类的对象或者内部类的class对象，静态内部类的非静态方法锁定的是外部类的对象，静态内部类的静态方法锁定的内部类的class对象。

> 静态类
>
> 1. 只能在内部类中定义静态类。
> 2. 静态类的方法可以是静态的方法也可以是非静态的方法，静态的方法可以在外层通过静态类调用，而非静态的方法必须要创建类的对象之后才能调用。
>    静态类只能引用外部类的static成员变量（变量或者方法）（也就是类变量）
> 3. 非静态类中可以包括静态成员。
>    

#### Demo2 同步方法执行过程中，非同步方法能否运行

当然可以运行，非同步方法的运行没有什么限制。

> 补充 Java8中Lambda表达式
>
> <font color=red>**使用Lambda表达式则只需要使用一句话就可代替使用匿名类的方式，特别的能用Lambda代替的接口只有一个方法，也称为函数接口**</font>
>
> 【匿名内部类格式】 
>
> ```
> new 父类构造器（参数列表）|实现接口（）   
>    {   
>      //匿名内部类的类体部分   
>   } 
> ```
>
> ![image-20190422214017810](src/main/resources/image/image-20190422214017810.png)
>
> ```java
> new Thread(() -> System.out.println("Hello World!"));
> 等同于
> new Thread(new Runnable(){
>             @Override
>             public void run() {
>                 System.out.println("Hello World!"); 
>             }
>         })
> ```

#### Demo3 业务中只对写加锁，对读不加锁是否可行

主要看业务处理的要求

对用户余额进行操作，写线程对余额加锁但是并不能阻挡不加锁的读方法，在写线程还未真正写入之前，读出的余额还是之前的没有变化，第二次读时线程写入导致两次读取不一致。

类似于转账业务，在转账方显示转账成功但是实际上还没有到账，对方第一次查询是之前的数据，第二次查询就显示数据变化了。

解决方法：这里对读加锁也并不足够，这里和数据库事务还不一样，线程在两次读取中是获取锁之后是有一个释放和再获取的过程的，数据库事务是没有释放的。

#### Demo4 拥有一把锁A的线程能否调用还需要锁A的另外一个方法

如果是不同线程必然要等待锁A的释放才能调用需要锁A的另外一个方法

但是由于处在同一个线程中，synchronized支持<font color=red>**可重入锁**</font>，是可以调用不需要等待的。

##### 重入锁的的另外一种情形：子类同步方法调用父类同步方法

```java
class Demo extends Demo4 {
    public synchronized void m() {
        System.out.println("子类"+"m");
        super.m1();
    }
}

Demo demo = new Demo();
new Thread(()->demo.m()).start();
```

m方法中锁定的是子类实例对象demo

父类中<font color=red>**同步方法锁定的是this**</font>，是父类类型的子类实例对象

不管是父类还是子类锁定的都是子类实例的Demo这一个对象。

#### Demo5 同步方法中运行时异常，锁都会被释放，一定要处理异常

此时需要特别注意处理数据时出现异常的情况

如果处理数据时改了一半一旦出现异常，释放了锁，其他线程就会获取这个锁在一半处理一半未处理的脏数据上进行处理，将会引起数据问题。

如果线程中出现异常，一定要进行特殊处理，恢复数据保证数据安全。

<font color=red>**解决方法利用try/catch，在catch中不释放锁回滚数据。**</font>

#### Demo6 voliate和synchronized

> 补充：concurrent包下的TimeUnit类
>
> 可以根据秒、分..不同单位进行睡眠
>
> ```java
> TimeUnit.SECONDS.sleep(1);
> ```

<font color = red>**volatile关键字令一个变量在多个线程间是可见的。**</font>

volatile到底做了什么，涉及到Java对线程处理时的内存模型JMM。

> JMM模型的补充 在SummarizeGuide中

<font color=red>**volatile并不是每次读取这个值的时候都从内存读取**</font>

<font color=red>**而是在有线程修改刷回主内存时会通知其他线程缓冲区中的值已经过期需要重新读取。**</font>

Q ：如果没有volatile，线程使用的变量值一直是缓冲的固定值吗，它本身有可能从主存中刷新么？

A：线程并不是一直使用缓存中的变量，这取决于CPU的忙碌程度；如果CPU特别忙，他会一直使用缓存中的变量值；如果CPU有空闲，它是有可能从主存中刷新一下的(有可能 不可控)

Q：如何保证两个线程间的可见性

A：保证线程间的可见性，要么是volatile要么是synchronized，但是synchronized并发性比volatile差很多，能用volatile还是volatile。

#### Demo7 volatile不能代替synchronized

synchronized保证了可见性和原子性

volatile仅仅保证了可见性，他不能改变多个线程同时修改一个变量时产生的冲突问题

> volatile仅保证缓冲区的数据和主存中的数据是一致的
>
> 读取时读取的是主存中数据，但是读取之后在操作的过程中是否有其他线程对这个值进行了修改，这是volatile无法保证的。
>
> 例如：对于Num++;操作，线程1和线程2都执行一次，最后输出Num的值可能是：1或者2
> 输出结果1的解释：当线程1执行Num++;语句时，先是读入Num的值为0，倘若此时让出CPU执行权，线程2获得执行，线程2会重新从主内存中，读入Num的值还是0，然后线程2执行+1操作，最后把Num=1刷新到主内存中； 线程2执行完后，线程1由开始执行，但之前已经读取的Num的值0，所以它还是在0的基础上执行+1操作，也就是还是等于1，并刷新到主内存中。所以最终的结果是1



> 补充：Java8中的list.foreach方法
>
> ```java
> threads.forEach((o) -> o.start());
> ```

[synchronized和volatile的区别]

1. 可见性和原子性
2. synchronized的效率更低

####Demo8 Java中的原子类

 如果仅仅是处理++、—这些较为简单的操作，在Java中提供了一些原子类

AtomicXX类[AtomicInteger等等]，使用时不需要加volatile。

Q：连续调用AtomicXX类的原子方法是否有可能出现线程安全。

A：有可能会出现线程安全问题，虽然每一个原子方法都可以保证原子性，但是在每个原子方法执行的<font color=red>**间隔之间**</font>是有可能被其他线程影响的，一样会有原子性的问题。

Q：AtomicXX类的原理，是如何保证原子性和可见性的？

#### Demo9 synchronized锁如果更换了对象

synchronized锁定的是堆内存中实际实例对象上，并不是栈中引用变量上

如果锁指向的实际对象发生了改变，新的线程将以这个新的对象作为上锁的基准，旧线程已经获取了原先旧对象的锁一直未释放，依然可以运行。此时新旧线程获取的锁不同，<font color=red>**将失去同步效果**</font>。

> ![image-20190422214036818](/Users/changsiteng/IdeaProjects/JavaConcurrent/src/main/resources/image/image-20190422214036818.png)
>
> 此时两个线程各自获取的已经是各自不同的锁了。

Q：如果更改一下题目，线程A运行时占有锁O，更改O对象，线程B启动后，线程A释放原有的锁，再去获取锁O时，获取的是哪一个对象的锁？？

A：只要旧线程将之前的锁释放掉了，再去获取时就需要获取当前o指向的新对象的锁，此时和新线程的锁对象是一致的。

![image-20190422214056310](/Users/changsiteng/IdeaProjects/JavaConcurrent/src/main/resources/image/image-20190422214056310.png)

<font color=red>**对于一个加锁对象，不要修改它的引用，声明为final**</font>

```java
final Object lock = new Object();
```

因此，<font color=red>**不要用字符串常量作为锁定的对象**</font>，因为：

1. 字符串的值一旦修改，指向的将是一个新的对象
2. 两个不同的字符串引用s1\s2指向同一个字符串常量，锁定的将是同一个对象，同一把锁；如果没有注意到这是同一把锁，很有可能引起死锁。

#### Demo10 一个面试题，实现一个容器

Q：实现一个容器，容器中有两个方法，一个add添加元素，一个size显示元素个数

要求：写两个线程，一个线程负责向容器中加元素，一个线程负责监视容器中元素个数，当元素个数为5时，线程2给出提示并退出。

Step1：为了保证容量变量在线程间是可见的，需要volatile关键字。

> 导致的问题：
>
> ```java
> /**
>  * 可以基本满足条件，但是还存在很多问题
>  * 1. 线程2一直在循环中等待，造成了CPU的浪费
>  * 2. 虽然使用了volatile关键字保证可见，但是线程2在满足条件通知的时候线程1可能又加了元素，导致通知完成后条件可能已经不满足了，通知有一定不准确
>  */
> ```

Step2：为了避免线程2在等待满足条件时一直死循环浪费资源，考虑wait/notify机制。

> 1. wait/notify机制必须通过加锁对象来实现，通过调用锁定对象的wait方法和notify对象，因此必须加锁。
> 2. wait会立刻释放锁，但是notify要等待线程执行完毕之后才会释放锁
> 3. wait/notify中为了防止wait线程没有在条件满足之前进入wait队列，<font color=red>**要求wait线程先启动。**</font>

Step3：为了解决不精确的问题，只能通过加锁来解决。

Step4：如何解决notify时想立刻释放锁，而不是等待线程完成再释放。

notify的时候再进行一次wait。

![image-20190422214109819](/Users/changsiteng/IdeaProjects/JavaConcurrent/src/main/resources/image/image-20190422214109819.png)

> 当然还有其他可以立即释放锁的方式，后面在看

```java
线程1：
								lock.notify(); //唤醒线程2
                    //唤醒之后需要释放这个锁，可以再调用一个wait，立即释放
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
线程2：
 				 try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notify();// 一定不要忘记这个notify，否则线程1会一直等待下去
```

Step5：最简单的方式是利用latch门闩的await/countDown机制代替wait/notify机制

`CountDownLatch`门闩，当值为0时表示门闩已开，另外一个线程可以运行

latch.await()用于等待，但是不需要锁定任何对象，等待latch的值为0时运行。

##### CountDownLatch



#### Demo 线程间的通信方式(两种方式)

1. 维护一个共享内存，做线程同步
2. 线程间发消息：wait/notify机制



