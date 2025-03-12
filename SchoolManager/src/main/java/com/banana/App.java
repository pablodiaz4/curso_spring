package com.banana;

import com.banana.condig.SpringConfig;
import com.banana.models.Student;
import com.banana.persistence.StudentsRepositoryInf;
import com.banana.services.IStudentService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        System.out.println("School Manager.....");

//        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringConfig.class);
        ctx.refresh();

        StudentsRepositoryInf repo = ctx.getBean(StudentsRepositoryInf.class);
        System.out.println(repo);
        Student aStd = repo.getById(1L);
        System.out.println(aStd);

        IStudentService srv = ctx.getBean(IStudentService.class);
        System.out.println(srv);
        Student srvStd = srv.getStudentById(1L);
        System.out.println(srvStd);

    }
}
