package introspectordemo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class IntrospectorDemo {
    public void inspector() throws Exception {

        MyButton myButton = new MyButton();
        BeanInfo beanInfo = Introspector.getBeanInfo(myButton.getClass());

        PropertyDescriptor[] descriptors =
                beanInfo.getPropertyDescriptors();

        //Property:属性的意思
        // Descriptor:描述的
        for (PropertyDescriptor descriptor : descriptors) {
            //相当于不看class相关的东西
            if(descriptor.getName().equals("class")){
                continue;
            }
            System.out.println("descriptor.getName() = " + descriptor.getName());
            System.out.println("descriptor.getReadMethod().getName() = " + descriptor.getReadMethod().getName());
            System.out.println("descriptor.getWriteMethod().getName() = " + descriptor.getWriteMethod().getName());

            System.out.println("---------------");
        }
    }


    public void inspector2() throws Exception {
        MyButton myButton = new MyButton();
        BeanInfo beanInfo = Introspector.getBeanInfo(myButton.getClass());

        String name = "id";
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getName().equals(name)) {
                Method writeMethod = descriptor.getWriteMethod();
                writeMethod.invoke(myButton, 15748);
                //读方法
                System.out.println(descriptor.getReadMethod().invoke(myButton));
            }
        }

        System.out.println(myButton.getId());


    }
}
