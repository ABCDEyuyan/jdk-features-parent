# 大纲
- ch01:线程的创建
- ch02:线程的常用属性与方法
    - sleep
    - join
    - interrupt
    - yield
    - setDaemon
- ch03:数据传递与接收
- ch04: 线程的安全性
    - 线程干扰：synchronize,AtomInteger
    - 内存一致性错误:valotile

- ch05:线程间的通信
  - 线程状态
  
- ch06:线程池
  - 池的创建
  - 提交任务与获取结果
  - 任务调度

- ch07:并发集合
  - HashMap的问题
  - ConcurrentHashMap

hashMap并发异常问题

```java
public class TestHashMap {
    public static void main(String[] args) {
        Map map = new HashMap<String, Integer>(1000 * 10);

        for (int i = 0; i < 10; i++) {
            // new Runner(map).start();
            //这个方法可以测试,线程安全时,
            //Hashmap的大小最后能够达到多少 
            new Runner(map).run();
        }
    }

}

class Runner extends Thread {
Map map;

    Runner(Map map) {
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            map.put(this.getName() + i, i);
        }
        //如果是线程安全,那么HashMap的大小,最后能够达到1W.           
        System.out.println(this.getName() + ": " + map.size());

    }

}
```


- ch08:锁
    - ReentrantLock
      - Condition（替代wait,notify)
    - ReadWriteLock(ReentrantReadWriteLock)
    - Semaphore

# 概念

多CPU指的下面几种情况

- 单个物理cpu，单有多个核心
- 多个物理CPU，单个cpu有一个核心
- 多个物理CPU，每个CPU也有多个核心

如果是单CPU是不能同时执行多个任务的，我们看到可以做多个任务是个假象，因为它是靠时间片来实现的。



- 并行与并发：

并行：真正同时在做事，比如9:03:00秒这一个时刻，有多个任务在执行。

并发：在一个时间段内有多个任务在交替执行，在9:03:00-9:05:00有2个任务在执行，一会执行A任务一会执行B任务

串行：任务顺次执行，形成一个串，单线程就是典型的串行

- 多线程执行的特点
  - 交替运行，无法预测
  - cpu有缓存，提高效率（减少内存访问次数），但可能产生可见性的问题
  - 多线程情况下，好多操作并不具备原子性
  - 为了效率，可能会调整指令执行的顺序
  - 多线程程序通常会操作共同的东西
  - 通常对共享的有读写操作，而不仅仅是读取
- 线程安全的问题：当多个线程访问某个类时，不管运行时环境采用何种调度方式或者这些线程如何交互执行，并且在主调用代码中不需要任何额外的同步或者协同操作，这个类都能表现出正确的行为，那么这就称这个类是线程安全的

线程不安全的本质原因：线程执行的特点+ 操作共享的东西

java解决线程不安全有多少种方法：

- synchronized：每次只有一个线程可以获取锁，所以导致对可能不安全的代码是串行执行
    它解决了原子性，有序性，可见性
- Lock接口的各种实现类的使用：类似于synchronize的机制，但采用的乐观锁。
- 把类设计为不可变：对象是只读的
- 把共享的东西设计为线程专有的，比如Servlet对象的HttpServletRequest与HttpServletResponse就是每个线程独有的一个对象，通常会使用ThreadLocal来解决
- 把操作原子化,比如AtomicInteger等等。一行java代码不等于是一个原子操作
  因为一行java代码可以编译为多行字节码，而且还可能一行字节码指令对应多个cpu汇编指令
  一个汇编指令可能对应多个执行单元。java的原子操作基本就是：简单类型的赋值与读取值
  int a = 5; int b = a;,但是不能想当然的认为double与long的赋值与取值是原子操作
  虽然有些编译器的实现，这2个类型的确是原子操作的。
- volatile:只解决可见性和重排序（happens-before)


- 线程安全类的说明
 Hashtable类种很多的方法是线程安全的，因为它们都用了synchronized方法修饰，
  但并不表示多个线程安全的方法一起使用也是线程安全的，比如线程A使用了安全的方法m1,线程B使用了安全的方法m2
  这2个安全的方法同时被2个线程使用，可能也会导致问题，
  这些类的安全的方法指的是多个线程同时使用一个安全的方法时是安全的。


# 阅读材料

https://zhuanlan.zhihu.com/p/85403728（可见性、有序性、原子性问题）

https://blog.csdn.net/qq_22771739/article/details/82529874(线程状态)

https://zhuanlan.zhihu.com/p/29881777(内存模型）

--------------------------------------------------------
ch04主要讲了java中解决线程安全的以下几个方法 

- synchronized,分为3种写法
  - 静态方法，锁对象是类的class
  - 实例方法，锁对象是this
  - 同步代码块，锁对象可以自己指定，一般就是这样写``private Object lock = new Object`

一定要确保锁对象是同一个，才能生效。

synchronized 不能修饰在接口里，也不会被继承，也就是说子类重写了父类的synchronized方法，
需要额外再添加synchronized以表明它是一个同步方法（详细的情况见书20,21页）

synchronized:是一个悲观锁，而且是一个可重入的锁
public synchronized void m(){
m()
}
如果像上面这样递归的，第一次加锁成功，第二次调用，因为当前的线程是
获取到锁的线程，所以加锁仍然成功，数量+1，相当于我进入到锁几次
当每个方法执行完毕，锁数量-1 ，直到0，然后再解锁

下面的这种情况也可以进入到n，这就叫可重入
public synchronized void m(){
n();
}

public synchronized void n(){
}

- 原子类
- ThreadLocal，这里只是演示了这个类型的作用，用在案例中不是特别合适
  因为我们的案例是本来就期望多个线程操作同样的东西，但是我们的ThreadLocal没有操作
  同样的数据，只是每个线程都操作一个ThreadLocal而已


- 可见性的问题。
  案例中的代码运行时可能导致程序不能退出，记住：只是可能，不是一定是这样

# 阅读材料
https://blog.csdn.net/xmtblog/article/details/111306127（synchronized 8种使用场景）
---------------------------------------------------------------------------------