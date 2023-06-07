package ch07;

/**
 * <h3>基本使用</h3>
 * <p>把{@link TestHashMap}案例中的Map改成{@link  java.util.concurrent.ConcurrentHashMap}就可以有准确的结果</p>
 *
 * <h3>基本特性</h3>
 * <ul>
 *     <li>与Hashtable一样，读写操作都是线程安全的</li>
 *     <li>读操作通常不需要锁</li>
 *     <li>读操作通常不会阻塞，但只反映最近的更新情况，不是绝对的能读取到其它线程新修改的数据</li>
 *     <li>对于聚合操作，比如putAll,clear，并发检索数据的线程可能只能获取到插入或删除的一部分条目</li>
 *     <li>iterator，splitter这样的操作，返回的是当时iterator被创建时的数据情况，不会抛出并发修改异常</li>
 *     <li>iterator被设计为一次只能被一个线程使用</li>
 *     <li>聚合状态方法，比如size,isEmpty,containsValue通常在map不被其它线程更新时才有用，
 *     否则，这些方法返回的只是瞬时结果是不准确的，对于监视与评估是有效的，但对于程序控制来说不够的</li>
 *     <li>与Hashtable一样（HashMap不是），大多数API不允许键以及值存放null值，但put与compute这样的方法的null表示是否要删除这个条目的意思，
 *     比如concurrentMap.compute("test", (s, o) -> null);</li>
 * </ul>
 *<h3>参考资料</h3>
 *<ul>
 *    <li><a href="https://www.baeldung.com/java-concurrent-map">粗略的全面介绍</a></li>
 *    <li><a href="https://juejin.cn/post/6896387191828643847">源码层面解析各Map的问题与实现</a></li>
 *    <li><a href="https://zhuanlan.zhihu.com/p/346803874">ConcurrentHashMap的面试题分析</a></li>
 *</ul>
 */
public class ConcurrentHashMapDemo {
}
