package com.nf.stream;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class MyStreamImpl<T> implements MyStream<T>{
    /**
     * 当前元素
     */
    private T head;
    /**
     * 下一个元素的处理方法，也就是说通过这个函数接口来创建出下一个元素出来
     */
    private Supplier<MyStreamImpl<T>> nextItemEvalProcess;
    /**
     * 流时候已经结束，但这个示例项目对它没处理，看看就好
     */
    private boolean isEnd;

    /**
     * 对当前流强制求值:就是对下一项进行处理
     * @return: 返回一个新的流
     */
    private MyStreamImpl<T> eval(){
        return this.nextItemEvalProcess.get();
    }

    private  boolean isEmpty(){
        return this.isEnd;
    }

    @Override
    public String toString() {
        return "MyStreamImpl{" +
                "head=" + head +
                ", isEnd=" + isEnd +
                '}';
    }
    //==================Builder类=====================
    public static  class  Builder<T>{
        private MyStreamImpl<T> stream;

        public Builder() {
            this.stream =  new MyStreamImpl<>();
        }

        public Builder<T> head(T head) {
            stream.head = head;
            return this;
        }

        Builder<T> isEnd(boolean isEnd) {
            stream.isEnd = isEnd;
            return this;
        }

        public Builder<T> nextItemEvalProcess(Supplier<MyStreamImpl<T>> nextItemEvalProcess) {
            stream.nextItemEvalProcess = nextItemEvalProcess;
            return this;
        }

        public MyStreamImpl<T> build(){return  stream;}
    }

    //===================Api实现=======================


    @Override
    public MyStreamImpl<T> filter(Predicate<T> predicate) {
        Supplier<MyStreamImpl<T>> lastNextItemEvalProcess = this.nextItemEvalProcess;
        this.nextItemEvalProcess = ()-> {
                    MyStreamImpl<T> myStream = lastNextItemEvalProcess.get();
                    return filter(predicate, myStream);
                };


        // 求值链条 加入一个新的process filter
        return this;
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        forEach(consumer,this.eval());
    }

    @Override
    public MyStreamImpl<T> limit(int n) {
        Supplier<MyStreamImpl<T>> lastNextItemEvalProcess = this.nextItemEvalProcess;
        this.nextItemEvalProcess = ()-> {
                    MyStreamImpl<T> myStream = lastNextItemEvalProcess.get();
                    return limit(n, myStream);
                };

        // 求值链条 加入一个新的process limit
        return this;
    }


    //===================私有方法，与API实现方法配套使用======================
    private static <T> MyStreamImpl<T> filter(Predicate<T> predicate, MyStreamImpl<T> myStream){
        if(myStream.isEmpty()){
            return MyStream.makeEmptyStream();
        }

        if(predicate.test(myStream.head)){
            return new Builder<T>()
                    .head(myStream.head)
                    .nextItemEvalProcess(()->filter(predicate, myStream.eval()))
                    .build();
        }else{
            return filter(predicate, myStream.eval());
        }
    }

    private static <T> void forEach(Consumer<T> consumer, MyStreamImpl<T> myStream){
        if(myStream.isEmpty()){
            return;
        }
        // 先消费当前元素
        consumer.accept(myStream.head);
        //接着调用myStream.eval获取下一个元素（在新的流里），然后再递归消费
        forEach(consumer, myStream.eval());
    }

    private static <T> MyStreamImpl<T> limit(int num, MyStreamImpl<T> myStream){
        if(num == 0 || myStream.isEmpty()){
            return MyStream.makeEmptyStream();
        }

        return new Builder<T>()
                .head(myStream.head)
                .nextItemEvalProcess(()->limit(num-1, myStream.eval()))
                .build();
    }
}
