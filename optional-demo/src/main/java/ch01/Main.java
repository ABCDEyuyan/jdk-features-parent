package ch01;


import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        //optionalBasic();
        //optionalCase();

        ClassInfo classInfo = new ClassInfo();
        Address address = new Address();
        address.setCity("zhuhai");
        address.setProvine("guangdong");

        BanZhang bz = new BanZhang("huangan");
        bz.setAddress(address);
        classInfo.setBanZhang(bz);
//        String result = getBanZhangProvince(classInfo);
//        System.out.println(result);

        Optional<ClassInfo> classInfoOptional = Optional.ofNullable(classInfo);

        //方法（指的getBanZhang和getBanZhang2）这样的方法，
        // 返回普通对象就用map，返回optional对象就用flatmap
        Optional<BanZhang> banZhang = classInfoOptional.map(c -> c.getBanZhang());
        Optional<Optional<BanZhang>> banZhang1 = classInfoOptional.map(c -> c.getBanZhang2());
        Optional<BanZhang> banZhang2 = classInfoOptional.flatMap(c -> c.getBanZhang2());


    }

    private static void optionalCase() {
        ClassInfo classInfo = new ClassInfo();
        Address address = new Address();
        address.setCity("zhuhai");
        address.setProvine("guangdong");

        BanZhang bz = new BanZhang("huangan");
        bz.setAddress(address);
        classInfo.setBanZhang(bz);
        String result = getBanZhangProvince2(classInfo);
        System.out.println(result);
    }

    private static void optionalBasic() {
        //什么是空引用异常

//        MySome mySome  = null;
//        mySome.doSth();
//
        //Optional是一个存放一个值的容器，这个值可能是null也可能是真的有值
        //创建Optional对象的方法一
        Optional<String> optional = Optional.of("ad");
        //of方法传null值得话直接运行时报错
        //Optional<String> optional2 = Optional.of(null);

        //ofNullable可以接受null值
        Optional<String> o = Optional.ofNullable(null);
        Optional<String> o2 = Optional.ofNullable("sadf");
//isPresent返回逻辑值，true表示Optional真的有存放值
        // System.out.println(o2.isPresent());

        if (o2.isPresent()) {
            // String s = o2.get();
            // System.out.println(s);
        }
        //如果没有值，调用get会抛异常
        // System.out.println(o.get());

        //ifPresent如果有值就调用参数回调
        // o2.ifPresent(s -> System.out.println(s));
        //没有值得话不会调用参数的回调函数
        // o.ifPresent(s -> System.out.println(s));

        Optional<Integer> optionalInteger = o2.map(s -> s.length());
        //把整数的值输出来
        //   optionalInteger.ifPresent(System.out::println);

        //orElse,Optional有值它返回值本身，没有就返回调用orElse时指定的数据
//        System.out.println(o2.orElse("aaaaa"));
//        System.out.println(o.orElse("aaaaa"));

        //orElseGet与orElse一样，只不过没有值得时候返回的supplier函数接口get方法的结果
        // System.out.println(o2.orElseGet(()->"aaaaa"));
        // System.out.println(o.orElseGet(()->"aaaaa"));


        // o2.filter(s->s.length()>5).ifPresent(System.out::println);


    }

    public static String getBanZhangProvince(ClassInfo classInfo) {
        // return classInfo.getBanZhang().getAddress().getProvine();
        if (classInfo != null) {
            BanZhang banZhang = classInfo.getBanZhang();
            if (banZhang != null) {
                Address address = banZhang.getAddress();
                if (address != null) {
                    return address.getProvine();
                }
            }
        }
        return "未知";
    }

    public static String getBanZhangProvince2(ClassInfo classInfo) {
        return Optional.ofNullable(classInfo)
                .map(c -> c.getBanZhang())
                .map(b -> b.getAddress())
                .map(addr -> addr.getProvine())
                .orElse("不知道哪个省");


    }
}
