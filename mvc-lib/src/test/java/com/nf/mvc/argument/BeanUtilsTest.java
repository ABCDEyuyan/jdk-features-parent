package com.nf.mvc.argument;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BeanUtilsTest {
    private Map<String,Object> requestMap ;

    @Before
    public void before(){
        requestMap = new HashMap<>();
        requestMap.put("id", "100");
        requestMap.put("name", "abc");
        requestMap.put("gender", "true");
        requestMap.put("salary", "5000.68");
        requestMap.put("deptId", "1");
        requestMap.put("deptName", "hr");
        requestMap.put("emp2.deptId", "2");
        requestMap.put("emp2.deptName", "rd");

        requestMap.put("dept.deptId", "3");
        requestMap.put("dept.deptName", "ms");
/*
        Map<String, String> nestedMap = new HashMap<>();
        nestedMap.put("deptId", "111");
        nestedMap.put("deptName", "nested");

        requestMap.put("dept", nestedMap);*/
    }

    @Test
    public  void testBeanUtilsGetNestedProperty() throws Exception{
        Emp2 emp2 = new Emp2();
        //Dept dept = new Dept(1, "asdf");
       // emp2.setDept(dept);
        System.out.println("BeanUtils.getNestedProperty(emp2,\"dept.deptName\") = " + BeanUtils.getNestedProperty(emp2, "dept.deptName"));
    }
    @Test
    public  void testBeanUtilsPopulate() throws Exception{
        Emp2 emp2 = new Emp2();
        BeanUtils.populate(emp2,requestMap);
        System.out.println("emp2 = " + emp2);
    }

    @Test
    public  void testHutoolBeanUtilsBasic() throws Exception{
      /*  Emp2 emp2 = new Emp2();
        BeanUtil.fillBeanWithMap(requestMap, emp2, true);
        System.out.println("emp2 = " + emp2);*/
    }
}
