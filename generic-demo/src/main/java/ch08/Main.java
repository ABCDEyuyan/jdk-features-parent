package ch08;
import java.util.ArrayList;
import java.util.List;

/**
 * 这章讲的是PECS
 */
public class Main {

    public static void main(String[] args) {
        //wildcardProblem();
        //pe();
        //cs();
        //pecsDemo();

    }

    private static void wildcardProblem() {
        Box<?> box = new Box<Integer>(6);
        //Box<?>虽然可以解决问题，参考ch07中的print方法，带来了好处
        //但现在导致不能放任何数据
        //box.set(7);
        // box.set(new Object());

        //虽然可以取，但由于不知道是什么类型，只能用Object类型来接受返回的数据
        //这其实是违反了泛型的设计的初衷(不需要进行类型转换)，
        // 所以其实等价于取不了数据
        Integer result = (Integer) box.get();
        System.out.println(result);
    }

    private static void pe() {
        //上届是可以取（你提供（Provider）了数据），不能存（存起来用，consume），
        //PECS :Provider extends,Consumer super
        Box<? extends Number> box = null;
        //box.set(5);
        //Number result = box.get();
    }
    private static void cs() {
        Box<? super Integer> intSuperBox = new Box<Integer>();
        intSuperBox.set(5);//super--->consumer
        //Integer object = intSuperBox.get();
        // System.out.println(object);
    }

    private static void pecsDemo() {
        List<Integer> listSource = new ArrayList<>();
        listSource.add(1);
        listSource.add(2);
        listSource.add(3);

        List<Number> listTarget = new ArrayList<>();
        copy(listSource, listTarget);

        listTarget.forEach(System.out::println);
    }

    //src取出来-----》dest里面去
    private static void copy(List<? extends Number> src,
                             List<? super Number> dest) {
        for (Number number : src) {
            dest.add(number);
        }
    }
}


