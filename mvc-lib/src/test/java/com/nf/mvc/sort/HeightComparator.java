package com.nf.mvc.sort;

import java.util.Comparator;

public class HeightComparator implements Comparator<Student> {
    @Override
    public int compare(Student o1, Student o2) {
        return o1.getHeight()-o2.getHeight();
    }
}
