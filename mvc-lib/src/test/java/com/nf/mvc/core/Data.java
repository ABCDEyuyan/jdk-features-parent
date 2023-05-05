package com.nf.mvc.core;

import java.util.Objects;

public class Data {
    private int val1;
    private int val2;

    public Data(int val1, int val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("---equals --- val1: " + this.val1 + " val2:" + this.val2);
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return val1 == data.val1 && val2 == data.val2;
    }


    @Override
    public int hashCode() {
        System.out.println("---hashcode --- val1: " + this.val1 + " val2:" + this.val2);
        return Objects.hash(val1, val2);
    }

    @Override
    public String toString() {
        return "Data{" +
                "val1=" + val1 +
                ", val2=" + val2 +
                '}';
    }
}