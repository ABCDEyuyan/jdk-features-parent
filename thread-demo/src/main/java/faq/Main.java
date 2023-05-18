package faq;

public class Main {
    public static void main(String[] args) {
        //因为Idea提供的输出窗口是有行数限制的，
        // 所以下面的输出看不到比较小的数的输出结果
        for (int i = 0; i < 10000000; i++) {
            System.out.println(i);
        }
    }
}
