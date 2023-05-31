package ch05.box;

public class Main {
    public static void main(String[] args) {

        Box<Fruit> box = new Box(1);
        Boy boy = new Boy(box);
        Girl girl = new Girl(box);
        boy.setName("boy");
        girl.setName("girl");

        boy.start();
        girl.start();


    }
}
