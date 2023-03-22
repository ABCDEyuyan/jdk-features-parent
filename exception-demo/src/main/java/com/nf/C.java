package com.nf;

import javax.imageio.IIOException;
import java.io.FileNotFoundException;

public class C {

    public void m1() {
      throw new ArithmeticException("算术运算出错");
    }

    public void m2() throws FileNotFoundException{
        //throw new FileNotFoundException("文件找不到");
        throw new RuntimeException();


    }


    public void doSth() throws FileNotFoundException{
        throw new FileNotFoundException("文件找不到");
      /*  try {
            throw new FileNotFoundException("文件找不到");
        } catch (FileNotFoundException e) {

        }*/
    }
}
