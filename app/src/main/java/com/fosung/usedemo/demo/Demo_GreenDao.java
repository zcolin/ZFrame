/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/

package com.fosung.usedemo.demo;

import com.fosung.usedemo.db.daobiz.StudentDaoBiz;
import com.fosung.usedemo.greendao.entity.Student;

import java.util.Date;
import java.util.List;

/**
 * 此类在Mvp中使用为Model层，{@link com.fosung.usedemo.demo.demo_mvp.model.StudentModel}
 */
public class Demo_GreenDao {
    public static Student insertObject() {
        Student student = new Student();
        student.setComment("comment");
        student.setName("张苏纳");
        student.setText("zhhangsan");
        student.setDate(new Date());
        StudentDaoBiz studentBiz = new StudentDaoBiz();
        studentBiz.insertObject(student);
        System.out.println(student.getId());
        return student;
    }

    public static void queryObject() {
        StudentDaoBiz biz = new StudentDaoBiz();
        List<Student> students = biz.queryAll(Student.class);
        for (Student student : students)
            System.out.println(student.getName());
    }
}
