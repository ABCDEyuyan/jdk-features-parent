package com.nf.mvc.sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Comparable<Student> {
    private int id;
    private String name;
    private int height;

    @Override
    public int compareTo(Student o) {
        System.out.println(this.getId() + "o:" + o.getId());
        return o.getId() - this.id ;
    }
}
