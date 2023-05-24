package impl.function;

@FunctionalInterface
public interface ForEach <T>{

    /**
     * 迭代器
     * @param item 被迭代的每一项
     * */
    void apply(T item);
}
