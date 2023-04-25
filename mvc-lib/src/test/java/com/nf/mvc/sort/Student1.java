package com.nf.mvc.sort;

import com.nf.mvc.support.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Order(10)
public class Student1 implements StudentInf  {
    private int id;
    private String name;
    private int height;


}
