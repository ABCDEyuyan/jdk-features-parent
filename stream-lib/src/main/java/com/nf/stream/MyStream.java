package com.nf.stream;


import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 精简化一个简单流的实现，比较完整的见stream-demo模块的impl包下的代码
 * @param <T>
 */
public interface MyStream<T> {

    MyStreamImpl<T> filter(Predicate<T> predicate);

    void forEach(Consumer<T> consumer);

    MyStreamImpl<T> limit(int n);

    static <T> MyStreamImpl<T> makeEmptyStream(){
        return new MyStreamImpl.Builder<T>().isEnd(true).build();
    }
}
