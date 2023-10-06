package com.nf.stream;

public class IntegerStreamGenerator {

    public static MyStreamImpl<Integer> getIntegerStream(int low, int high) {
        return getIntegerStreamInner(low,high,true);
    }

    private static MyStreamImpl<Integer> getIntegerStreamInner(int low, int high, boolean isStart){
        if(low > high){
            // 到达边界条件，返回空的流
            return MyStream.makeEmptyStream();
        }

        if(isStart){
            return new MyStreamImpl.Builder<Integer>()
                    .nextItemEvalProcess(()->getIntegerStreamInner(low,high,false))
                    .build();
        }else{
            return new MyStreamImpl.Builder<Integer>()
                    // 当前元素 low
                    .head(low)
                    // 下一个元素 low+1
                    .nextItemEvalProcess(()->getIntegerStreamInner(low+1,high,false))
                    .build();
        }
    }
}
