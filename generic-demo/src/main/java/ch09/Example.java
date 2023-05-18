package ch09;

import java.util.Set;

//约束7：方法参数类型不能是参数化的泛型类，这样不形成重载
class Example {
    //擦除之后都是set类型，参数一样，不叫重载，所以编译出错
    //public void print(Set<String> strSet) { }
    public void print(Set<Integer> intSet) {
    }
}
