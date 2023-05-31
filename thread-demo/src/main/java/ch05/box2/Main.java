package ch05.box2;

public class Main {
    public static void main(String[] args) {
        Box2<Fruit> box = new Box2(3);

        Boy boy = new Boy(box);
        boy.setName("boy");

        Boy boy2 = new Boy(box);
        boy2.setName("boy2");


        Girl girl = new Girl(box);
        girl.setName("girl");

        boy.start();
        boy2.start();
        girl.start();
    }
}
