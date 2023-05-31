package ch05.box;

import java.util.LinkedList;

/**
 * 这是学校教材上的写法，有bug，没有考虑虚假唤醒的问题<br/>
 *
 * <h3>虚假唤醒（spurious wakeiups)</h3>
 * <p>定义：在线程等待唤醒的过程中，等待的线程被唤醒后，
 * 在条件不满足的情况下依然继续向下执行代码.<br/>
 * 虚假唤醒这种情况不仅仅是java语音独有的问题，其它编程语言也都有这种情况。
 * 是os底层的问题，在java中，即使没有调用过notify或notifyAll，都有可能偶尔被唤醒
 * </p>
 * <h3>虚假唤醒出现的情况</h3>
 * 下面是一个便于理解的说法，并不正确。
 * <ol>
 *     <li>假定A线程因为某个条件不满足进入到了wait状态</li>
 *     <li>其它线程调用了notify或者notifyAll</li>
 *     <li>第一步处于阻塞状态的线程被唤醒，接着wait方法之后的代码运行，
 *     但此时条件还是处于不满足的情况，这种情况就不应该被唤醒，只有条件满足后才应该被唤醒</li>
 * </ol>
 * <h3>解决办法</h3>
 * 通常是用while循环而不是用if来对条件进行判断，<b>见wait方法的源码注释</b>
 * <pre class="code">
 *  synchronized(obj){
 *      while(condition does not hold(条件不满足)){
 *          obj.wait();
 *      }
 *  }
 * </pre>
 *<h3>虚假唤醒参考资料</h3>
 * https://blog.csdn.net/hgmolk/article/details/109855802
 * https://www.zhihu.com/question/271521213
 * https://en.m.wikipedia.org/wiki/Spurious_wakeup
 * https://jenkov.com/tutorials/java-concurrency/thread-signaling.html
 * @param <T>
 */
public class Box<T> {
    private LinkedList<T> box = new LinkedList<>();
    private int size;

    public Box(int size) {
        this.size = size;
    }

    public synchronized void put(T fruit) {
        if (box.size() < size) {
            System.out.println("放入：" + fruit);
            box.add(fruit);
        } else {

            try {
                System.out.println("满了---");
                this.notifyAll();
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized T take() {
        T fruit = null;
        if (box.size() > 0) {
            fruit = box.removeFirst();
            System.out.println("取出:" + fruit);
        } else {
            try {
                System.out.println("空的---");
                this.notifyAll();
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return fruit;
    }
}
