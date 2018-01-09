/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */

package com.zcolin.frame.demo;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcolin.frame.demo.db.entity.Employee;
import com.zcolin.frame.demo.db.entity.EmployeeDao;
import com.zcolin.frame.util.ToastUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zcolin.frame.demo.db.DaoManager.getDaoHelper;


/**
 * DBDemo
 */
public class DbDemoActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llContent;
    private TextView     textView;
    private ArrayList<Button> listButton      = new ArrayList<>();
    private int               currentSortType = 0;//当前排序方式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_db);

        init();
        queryObject();
    }

    private void init() {
        llContent = getView(R.id.ll_content);
        textView = getView(R.id.textview);
        textView.setMovementMethod(new ScrollingMovementMethod());
        listButton.add(addButton("插入数据"));
        listButton.add(addButton("替换或插入数据"));
        listButton.add(addButton("获取数据列表"));
        listButton.add(addButton("条件查询降序"));
        listButton.add(addButton("清空数据库"));

        for (Button btn : listButton) {
            btn.setOnClickListener(this);
        }
    }

    private Button addButton(String text) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(mActivity);
        button.setText(text);
        button.setGravity(Gravity.CENTER);
        button.setAllCaps(false);
        llContent.addView(button, params);
        return button;
    }

    /**
     * 插入对象
     */
    public Employee insertObject() {
        Employee employee = getEmployee();
        boolean b = getDaoHelper().insertObject(employee);
        ToastUtil.toastShort(b ? "插入成功" : "插入失败-主键重复");
        return employee;
    }

    /**
     * 有则替换，无则插入
     */
    public Employee insertOrReplaceObject() {
        Employee employee = getEmployee();
        boolean b = getDaoHelper().insertOrReplaceObject(employee);
        ToastUtil.toastShort(b ? "插入成功" : "插入失败-主键重复");
        return employee;
    }

    /**
     * 查询所有数据的数据列表
     */
    public void queryObject() {
        List<Employee> list = getDaoHelper().queryAll(Employee.class);
        setText(list);
    }

    /**
     * 查询group为“部门一”的人员,从第二条开始限制为3个,按照日期降序
     * <p>
     */
    public void queryObjectWithCondition() {
        QueryBuilder<Employee> queryBuilder = getDaoHelper().getQueryBuilder(Employee.class);
        queryBuilder.where(EmployeeDao.Properties.Group.eq("部门二"));
        queryBuilder.offset(1);
        queryBuilder.limit(3);
        if (currentSortType == 0) {
            currentSortType = 1;
            queryBuilder.orderDesc(EmployeeDao.Properties.Date);
        } else {
            currentSortType = 0;
            queryBuilder.orderAsc(EmployeeDao.Properties.Date);
        }
        List<Employee> list = getDaoHelper().queryObjects(queryBuilder);
        setText(list);
    }

    /**
     * 删除所有数据
     */
    public void deleteAllObject() {
        boolean b = getDaoHelper().deleteAll(Employee.class);
    }

    private Employee getEmployee() {
        Employee employee = new Employee();
        Date date = new Date();
        long t = date.getTime();

        employee.setId(t % 20);
        employee.setCompany("fosung");
        employee.setName("张" + t % 15);
        employee.setGroup(t % 2 == 0 ? "部门一" : "部门二");
        employee.setDate(date);
        return employee;
    }

    private void setText(List<Employee> list) {
        if (list != null && list.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Employee o : list) {
                builder.append("id:")
                       .append(o.getId())
                       .append("  name:")
                       .append(o.getName())
                       .append("  group:")
                       .append(o.getGroup())
                       .append("  time")
                       .append(o.getDate())
                       .append("\n");
            }
            textView.setText(builder);
        } else {
            textView.setText(null);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == listButton.get(0)) {
            insertObject();
            queryObject();
        } else if (v == listButton.get(1)) {
            insertOrReplaceObject();
            queryObject();
        } else if (v == listButton.get(2)) {
            queryObject();
        } else if (v == listButton.get(3)) {
            queryObjectWithCondition();
        } else if (v == listButton.get(4)) {
            deleteAllObject();
            queryObject();
        }
    }
}
