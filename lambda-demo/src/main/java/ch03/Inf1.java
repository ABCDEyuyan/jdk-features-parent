package ch03;

/**
 * 1.这个注解表明接口是一个函数接口
 * 2.函数接口：就是只有一个抽象方法的接口
 * 3.当给一个接口声明了这个注解之后，超过一个抽象方法会直接编译出错
 * 4.这个注解不是必须要加的，只是建议加
 * 5.加不加与使用lambda表达式“没有”关系
 */
@FunctionalInterface
public interface Inf1 {
   int add(int x,int y);


    default  void m(){
    System.out.println(".....");
}
}
