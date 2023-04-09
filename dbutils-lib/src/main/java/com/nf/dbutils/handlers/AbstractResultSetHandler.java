package com.nf.dbutils.handlers;

import com.nf.dbutils.DefaultRowProcessor;
import com.nf.dbutils.ResultSetHandler;
import com.nf.dbutils.RowProcessor;

public abstract class AbstractResultSetHandler<T>
        implements ResultSetHandler<T> {
    // 这是默认的行处理器，只是一个匿名内部类，
    //因为接口通过默认方法的形式，把功能已经实现了，不需要额外单独创建一个类
    private static final RowProcessor
            DEFAULT_ROW_PROCESSOR = new DefaultRowProcessor();
    //protected子类就可以直接使用，
    // 也可以搞一个public的get方法让子类访问
    protected RowProcessor rowProcessor;

    public AbstractResultSetHandler() {
        this(DEFAULT_ROW_PROCESSOR);
    }

    public AbstractResultSetHandler(RowProcessor rowProcessor) {
        this.rowProcessor = rowProcessor;
    }


}
