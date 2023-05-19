package ch05.box2;

public class Main {
    public static void main(String[] args) {

        Box2<Fruit> box = new Box2(1);
        Boy boy = new Boy(box);
        Girl girl = new Girl(box);
        boy.setName("boy");
        girl.setName("girl");

        boy.start();
        girl.start();


    }
}
