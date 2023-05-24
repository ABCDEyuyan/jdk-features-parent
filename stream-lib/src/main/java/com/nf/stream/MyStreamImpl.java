package com.nf.stream;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class MyStreamImpl<T> implements MyStream<T>{
    private T head;
    private Supplier<MyStreamImpl<T>> nextItemEvalProcess;
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


    //=========================================
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

        consumer.accept(myStream.head);
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
