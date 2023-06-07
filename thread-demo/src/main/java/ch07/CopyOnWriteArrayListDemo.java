package ch07;

/**
 * CopyOnWriteArrayList是ArrayList的线程安全版本，内部也是通过数组实现，
 * 每次对数组的修改都完全拷贝一份新的数组来修改，修改完了再替换掉老数组，
 * 这样保证了只阻塞写操作，不阻塞读操作，实现读写分离。
 *
 * <h3>主要特性</h3>
 * <ul>
 *     <li>ArrayList的线程安全版本</li>
 *     <li>所有的可变操作，比如add，set都是通过对底层数组的一个全新拷贝实现的</li>
 *     <li>采用读写分离的思想，读操作不加锁，写操作加锁，且写操作占用较大内存空间，特别适合读多写少的场景，以空间换时间</li>
 *     <li>任何元素都可以存放，允许null值</li>
 *     <li>利用iterator进行remove,add,set操作是不允许，会抛异常</li>
 *     <li>只保证最终一致性，不保证实时一致性，一个正在读的线程是不能获取其它线程当前刚写入的数据的，下次读就可以获取到这个新数据</li>
 *     <li>CopyOnWriteArrayList的读操作支持随机访问，时间复杂度为O(1)</li>
 *     <li>使用ReentrantLock重入锁加锁，保证线程安全</li>
 * </ul>
 * <h3>用法</h3>
 * <p>把它当ArrayList使用即可，没有太特别的注意点，只不过是线程安全的</p>
 * <h3>参考资料</h3>
 * <ul>
 *     <li><a href="https://www.baeldung.com/java-copy-on-write-arraylist">粗略的全面介绍</a></li>
 *     <li><a href="https://juejin.cn/post/7063626688705331236">源码层面解释，比较清晰易懂</a></li>
 * </ul>
 * @see java.util.concurrent.CopyOnWriteArraySet
 */
public class CopyOnWriteArrayListDemo {
}
