package ch03.methodref;

/**
 * // 关联到特定类的任意对象的实例方法
 * InfMr5 infMr51 = SomeClassMR::doSth2; //doSth2是一个实例方法
 * 方法关联生成的类代码类似下面：
 * class 动态生成的类 implements InfMr5 {
 *     public void doSth(SomeClassMR classMR, String a){
 *        classMR.doSth2(a)
 *     }
 * }
 * infMr51.doSth()
 */
public interface InfMr5 {
    void doSth(SomeClassMR classMR, String a);
}
