package ch08.active;

/**
 * 线程活跃性：DeadLock(死锁),LiveLock(活锁),Starvation(饿死)
 * <h3>快速理解</h3>
 * <ol>
 *     <li>死锁：2个线程都阻塞不运行了,都等着对方释放锁</li>
 *     <li>活锁：2个线程都是在运行中的，但条件总是不满足，比如两个人互相让路，总让到同一边导致两人都不能前进</li>
 *     <li>饿死：没有机会得到执行，比如老实排队的人总被人插队</li>
 * </ol>
 * <h3>参考资料</h3>
 * <ul>
 *     <li><a href="https://www.baeldung.com/cs/deadlock-livelock-starvation#:~:text=Livelock%20is%20a%20deadlock-like%20situation%20in%20which%20processes,result%20of%20continuous%20resource%20denial%20to%20a%20process.">死锁、活锁、饿死的理论介绍</a></li>
 *     <li><a href="https://www.geeksforgeeks.org/deadlock-starvation-and-livelock/">Deadlock, Starvation, and Livelock</a></li>
 *     <li><a href="https://www.baeldung.com/java-deadlock-livelock">死锁与活锁的demo</a></li>
 *     <li><a href="https://www.geeksforgeeks.org/deadlock-starvation-java/">死锁与饿死的demo(饿死的例子不太好)</a></li>
 *     <li><a href="https://www.w3schools.blog/starvation-in-java">饿死的demo</a></li>
 * </ul>
 */
public class Main {

}
