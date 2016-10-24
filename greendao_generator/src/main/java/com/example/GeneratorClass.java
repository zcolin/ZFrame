/*
 *  author   colin
 *  company  fosung
 *  email    wanglin2046@126.com
 *  date     16-7-13 下午4:55
 */

package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class GeneratorClass {
    public static void main(String[] args) throws Exception {
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(1, "com.fosung.smartliving.greendao.entity");//Bean所在目录
        schema.setDefaultJavaPackageDao("com.fosung.smartliving.greendao.dao");//dao所在目录

        // 有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        schema.enableActiveEntitiesByDefault();
        schema.enableKeepSectionsByDefault();
        
        /*开始添加实体*/
        addNote(schema);

        new DaoGenerator().generateAll(schema, "app/src/main/java-gen");
    }

    private static void addNote(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Student」（既类名）
        Entity student = schema.addEntity("Student");
        //重新给表命名
        // note.setTableName("NOTE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        /*设置表中的字段*/
        student.addIdProperty().autoincrement();
        student.addStringProperty("name")
               .notNull();
        student.addStringProperty("text")
               .notNull();
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        student.addStringProperty("comment");
        student.addDateProperty("date");
    }
}
