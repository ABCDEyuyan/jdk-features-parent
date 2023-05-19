package exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //concatAndConstructorMethodReference();
        Trader raoul = new Trader("Raoul", "剑桥");
        Trader mario = new Trader("Mario", "米兰");
        Trader alan = new Trader("Alan", "剑桥");
        Trader brian = new Trader("Brian", "剑桥");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(raoul, 2011, 400),
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
/*
        transactions
                .stream()
                .filter(t->t.getYear() == 2011)
                .sorted((a,b)->
                        Integer.valueOf(a.getValue()).compareTo(b.getValue()))
                .forEach(System.out::println);*/

        Stream<Trader> traderStream = Stream.of(raoul, mario, alan, brian);

      /*  traderStream.map(Trader::getCity)
                .distinct()
                .forEach(System.out::println);*/

       /* traderStream.filter(t->t.getCity().equals("剑桥"))
                .sorted((s1,s2)->s1.getName().compareTo(s2.getName()))
                .forEach(System.out::println);*/

       // traderStream.map(Trader::getName).forEach(System.out::println);

       /* System.out.println(traderStream.anyMatch(trader -> trader.getCity()
                .equals("米兰")));*/


     /*   Double sum = transactions.stream()
                .filter(t -> t.getTrader()
                        .getCity()
                        .equals("剑桥"))
                .collect(Collectors
                            .summingDouble(Transaction::getValue));
        System.out.println(sum); */


    /*    Optional<Transaction> max = transactions.stream()
                .max((t1, t2) -> Integer.valueOf(t1.getValue())
                        .compareTo(t2.getValue()));
        System.out.println(max.get());*/


    /*    Optional<Transaction> min = transactions.stream()
                .min((t1, t2) -> Integer.valueOf(t1.getValue())
                        .compareTo(t2.getValue()));
        System.out.println(min.get());*/


    }

    private static void concatAndConstructorMethodReference() {
        ArrayList<String> manArray = new ArrayList<>();
        manArray.add("刘德华");
        manArray.add("成龙");
        manArray.add("吴彦祖");
        manArray.add("周润发");
        manArray.add("周星驰");
        manArray.add("吴京");

        ArrayList<String> womanList = new ArrayList<>();
        womanList.add("林心如");
        womanList.add("孙俪");
        womanList.add("柳岩");
        womanList.add("林青霞");
        womanList.add("王祖贤");
        womanList.add("张曼玉");


        Stream<String> manStream = manArray.stream()
                .filter(s -> s.length() == 3)
                .limit(3);
        Stream<String> womenStream = womanList.stream()
                .filter(s -> s.substring(0, 1)
                        .equals("林"))
                .skip(1);
        List<Actor> collect = Stream.concat(manStream, womenStream)
                //.map(s -> new Actor(s))
                .map(Actor::new)
                .collect(Collectors.toList());

        collect.forEach(System.out::println);
    }
}
