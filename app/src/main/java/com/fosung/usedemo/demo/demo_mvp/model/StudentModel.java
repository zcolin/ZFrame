/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午1:51
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.model;

import com.fosung.usedemo.db.daobiz.StudentDaoBiz;
import com.fosung.usedemo.greendao.dao.StudentDao;
import com.fosung.usedemo.greendao.entity.Student;

import java.util.Date;
import java.util.List;

/**
 * Model层为数据处理层， 处理网络操作和数据库操作的信息获取和提交
 * <p/>
 * 处理数据，此处demo为Student的数据库操作
 */
public class StudentModel {

    public Student saveStudent() {
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

    public List<Student> queryStudents(String name) {
        StudentDaoBiz biz = new StudentDaoBiz();
        List<Student> listStudent = biz.queryObjects(Student.class, StudentDao.Properties.Name.eq("张苏纳"));
        return listStudent;
    }

    //where的第二种写法
/*    public List<Student> queryStudents(String name) {
        StudentDaoBiz biz = new StudentDaoBiz();
        List<Student> listStudent = biz.queryObjects(Student.class, "where NAME=?", "张苏纳");
        return listStudent;
    }*/
}
