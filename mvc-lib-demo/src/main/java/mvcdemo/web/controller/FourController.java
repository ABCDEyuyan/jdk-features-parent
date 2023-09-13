package mvcdemo.web.controller;


import com.nf.mvc.ViewResult;
import mvcdemo.Student;

import javax.servlet.ServletException;
import java.io.IOException;

import static com.nf.mvc.handler.HandlerHelper.json;


public class FourController {

    public ViewResult process() throws ServletException, IOException {
        System.out.println("4444-----------");
        //return new ForwardViewResult("/index.jsp");



        Student student = new Student(100, "abc");
       // return new JsonViewResult(student);
      //  return json(student);

        return json(student);
    }
}
