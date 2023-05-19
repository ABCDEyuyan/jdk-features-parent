package ch05.box3;

import java.util.Random;

public class Fruit {
    private static String[] names = {"苹果","香蕉","小番茄","榴莲","青瓜"};
    private  static Random random = new Random();
    private int index =0;
    public Fruit(){
        index = random.nextInt(names.length);
    }

    @Override
    public String toString() {
        return names[index];
    }
}
