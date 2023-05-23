package ch09;

import java.util.Set;

class SomeClass<T> {
    /**
     * 约束2：类型参数不能实例化
     */
    public void m() {
        // T t  = new T();
    }

  /*
  约束3：不能声明类型为类型参数的静态变量
  不能定义静态化的泛型类型变量，这与声明静态泛型方法不是一个意思,
  理由是T只是一个占位符，确定了真正的类型后才能正确使用，
  而确定真正类型的时机是在通常是在类实例化时，
  而这个静态的字段与方法在使用时，类的实例化可能压根就没有进行，
  那么此时是无法知道T到底是个什么类型的，所以不允许这样写
  *   */
    /*  static  T t; */

    /**
     * 约束3：静态方法不能使用类上声明的类型变量
     * <p>
     * 可以这样理解这个约束：因为泛型类型的使用分为2部曲，
     * 第一步：泛型类型调用或者叫参数化，
     * 目标是确定声明的类型变量的具体类型（实参，type argument）
     * 第二步：真正的使用，所有的Type Parameter就用类型实参代替。
     * 这样就可以保证存放与获取的是同样类型的数据。
     * <p>
     * SomeClass.sm1(传递数据）//少了第一步，T还没有确定是什么实参
     * 这就表示什么数据都可以传递，这就不符合泛型的初衷。
     *
     * @param t
     */
   /* public static  T m(){
        return t;
    }

  public static void m2(T t) {

  }
  */
    //下面是可以的
    public static <T> T m3(T t) {
        return null;
    }

    //约束7：方法参数类型不能是参数化的泛型类，这样不形成重载
    //擦除之后都是set类型，参数一样，不叫重载，所以编译出错
    //public void print(Set<String> strSet) { }
    public void print(Set<Integer> intSet) {
    }

}
