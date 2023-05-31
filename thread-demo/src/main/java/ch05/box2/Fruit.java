package ch05.box2;

import java.util.Random;

public class Fruit {
    private String[] names= {"苹果","香蕉","小番茄","榴莲","青瓜"};
    private int index =0;
    private Random random = new Random();
    public Fruit(){
        index = random.nextInt(names.length);
    }

    @Override
    public String toString() {
        return names[index];
    }

 /*   public static void main(String[] args) {
        System.out.println(new Fruit());
        System.out.println(new Fruit());
        System.out.println(new Fruit());
        System.out.println(new Fruit());
    }*/
}
