package ch08;


/**
 * 1.泛型接口,此接口的实现参考jdk中的Function，主要是用来演示super与extend的实际应用
 * T，R是两个类型级别声明的类型参数（Type Parameter，Type Variable）
 * @param <T>
 * @param <R>
 * @see java.util.function.Function
 */
interface MyFunction<T, R> {
    /**
     * 参数类型是T，返回类型是R类型
     * @param t
     * @return
     */
    R apply(T t);

    /**
     * 1.它是一个接口默认方法
     * 2.它是一个泛型方法，也声明了一个泛型参数叫V
     * 3.它返回的类型还是MyFunction本身，
     *      3.1 apply的参数是T
     *      3.2 apply的返回类型是V
     * 4.andThen方法的参数也是一个MyFunction类型
     *      4.1 apply的入参是R类型
     *      4.2 apply的返回类型是V类型
     *  5.先执行自己的apply 接着就执行andThen传递过来的apply
     *  f1 f2  f(f1)
     *  f= 2x+1
     *  g = x*2
     *  x=5  --->  10+1  -->11*2 =22
     *
     *  6. T---->R---->V  ---->andThen方法的返回类型
     * @param after
     * @param <V>
     * @return
     */
    default <V> MyFunction<T,V>andThen(MyFunction<R,V> after) {
        return (T t) -> after.apply(apply(t));
    }

    default <V> MyFunction<T,V>andThen2(MyFunction<? super R,V> after) {
        return (T t) -> after.apply(apply(t));
    }

    default <V> MyFunction<T,V>andThen3(MyFunction<? super R,? extends V> after) {
        return (T t) -> after.apply(apply(t));
    }
}