/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-18 下午1:50
 **********************************************************/
package com.fosung.usedemo.demo.demo_mvp.presenter;

import com.fosung.frame.app.BaseFrameActivity;
import com.fosung.frame.http.response.StringResponse;
import com.fosung.frame.utils.CalendarUtil;
import com.fosung.frame.utils.LogUtil;
import com.fosung.frame.utils.ToastUtil;
import com.fosung.usedemo.demo.demo_mvp.contract.DemoMvpContract;
import com.fosung.usedemo.demo.demo_mvp.model.BaiduStringModel;
import com.fosung.usedemo.demo.demo_mvp.model.StudentModel;
import com.fosung.usedemo.demo.demo_mvp.model.UserModel;
import com.fosung.usedemo.demo.demo_mvp.model.entity.User;
import com.fosung.usedemo.greendao.entity.Student;
import com.fosung.usedemo.http.response.ZResponse;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Presenter层为桥梁，调用Model层{@link StudentModel}处理数据，处理完成后调用View层{@link DemoMvpPresenter}显示数据
 * 有一个View, 可以拥有多个Model，
 * <p/>
 * Presenter Demo
 */
public class DemoMvpPresenter implements DemoMvpContract.Presenter {

    private DemoMvpContract.View view;

    public DemoMvpPresenter(DemoMvpContract.View view) {
        this.view = view;
    }

    @Override
    public void getBaiduStringData(BaseFrameActivity context) {
        BaiduStringModel baiduModel = new BaiduStringModel();
        baiduModel.getBaiduString(new StringResponse(context) {
            @Override
            public void onError(int code, Call call, Exception e) {
                ToastUtil.toastShort(LogUtil.ExceptionToString(e));
            }

            @Override
            public void onSuccess(Response response, String resObj) {
                view.setTvText(resObj);
            }
        });
    }

    @Override
    public void saveStudent() {
        StudentModel studentModel = new StudentModel();
        Student student = studentModel.saveStudent();
        ToastUtil.toastShort("StudentId:" + student.getId());
    }

    @Override
    public void queryStudent() {
        StudentModel studentModel = new StudentModel();
        List<Student> students = studentModel.queryStudents("张苏纳");

        String str = "";
        if (students != null) {
            for (Student student : students) {
                str += "id:" + student.getId() + "  name:" + student.getName() + "  date:" + CalendarUtil.getDateTime(student.getDate()) + "\n";
            }
        }
        view.setTvText(str);
    }

    @Override
    public void getUser(BaseFrameActivity context) {
        UserModel userModel = new UserModel();
        userModel.getUser(new ZResponse<User>(User.class, context) {
            @Override
            public void onError(int code, String error) {
                ToastUtil.toastShort(error);
            }

            @Override
            public void onSuccess(Response response, User resObj) {
                view.setTvText(resObj.name);
            }
        });
    }
}
