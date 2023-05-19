package ch02;

/**
 * 1. 类型形参（Type Parameter）
 * 2. 形参的名字：只要是合法的java名字即可，不一定只能是T或者一个字母
 * 3. 使用Box的时候，T不能是基本类型
 * 常见的类型参数的名字
 * E - 元素 (常用在java集合的框架里)
 * K - 键
 * N - 数字
 * T - 类型
 * V - 值
 * S,U,V etc. - 2nd, 3rd, 4th types
 */
public class Box<T> {
    private T data;  ///


    public void set(T d) {
        this.data = d;
    }

    public  T get(){
        return this.data;
    }
}
