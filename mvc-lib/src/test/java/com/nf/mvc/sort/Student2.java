package com.nf.mvc.sort;

import com.nf.mvc.support.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Order(101)
public class Student2 implements StudentInf {
    private int id;
    private String name;
    private int height;


}
