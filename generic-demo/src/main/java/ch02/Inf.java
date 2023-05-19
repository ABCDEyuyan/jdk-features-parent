package ch02;

/**
 * T叫做Type Variable（类型变量）
 * 是标准的java命名规则
 * @param <T>
 */
public interface Inf<T>{
    void m1(T t);
    T m2();
    T m3(T t);
}
