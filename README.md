# ZFrame --Android快速开发集成框架

## Summary
ZFrame是Android端的快速开发框架，此项目主要对常用的数据库、图片加载、权限处理、Http请求、常用工具类进行
统一封装，基本涵盖了除了UI以外的所有功能，易于上手，上手之后能够快速的完成项目框架搭建。
此项目主要包含五大模块:Orm数据库、OkHttp封装、Glide图片加载、PermissionHelper权限处理、工具类集合.
Orm数据库采用GreenDao, Http采用OkHttp, 图片加载采用Glide, 这三个库是经过从性能和易用度综合考虑确定的.封装的基本能满足平时的使用了，遇到特殊问题，就google去吧。


## Feature
1. Orm数据库：对GreenDao进行了封装，对常用的增删改查直接继承即可不用再写函数。
1. Http请求：OkHttp进行了封装，主要目的就是可以异步回调时主线程中执行任务,其实这套封装是参考鸿洋大神的，进行了多处更改。感谢!
1. 图片缓存：图片加载基本没做什么工作，但是考虑可能会换库，所以封装一层.
1. Activity回调：封装ResultActivityHelper使用回调，免除startActivityForResult时需要在onActivityResult中写判断。
1. 权限处理：封装6.0的权限管理控制，基于回调模式。
1. 工具类集合：工具类是经过多个项目积累整理的，基本都会用到，涉及面比较广。

## reference library version
1. GeeenDao ：3.2.0
1. OkHtpp : 3.5.0
1. Glide : 4.0.0
1. Gson : 2.7

## Gradle
app的build.gradle中添加
```
dependencies {
    compile 'com.github.zcolin:ZFrame:latest.release'
}
```
工程的build.gradle中添加
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

## Usage
#### HttpDemo
Http的主要操作类为com.zcolin.frame.http.ZHttp, 内置了get post upload download 等多参数函数，其中使用最多的为通过报文
协议直接将服务器返回的json报文解析成对象，进行回调，以下为此功能说明
```
//首先建一个返回对象的基类，此类实现ZReply接口，实现接口的三个函数，如下
public class HttpBaseReplyBean implements ZReply {
    public int    error; //协议中的字段，根据协议自定
    public String status;//协议中的字段，根据协议自定

    /**
     * 判断返回的数据是否成功，具体规则由接口协议制定
     */
    @Override
    public boolean isSuccess() {
        return "success".equals(status);
    }

    /**
     * 服务端返回的状态码
     */
    @Override
    public int getReplyCode() {
        return error;
    }

    /**
     * 服务端返回的错误信息
     */
    @Override
    public String getErrorMessage() {
        return "";
    }
}

//具体调用，ZResponse的构造第二和第三个参数分别为进度条的context和进度条信息，如果只填第一个参数，则不显示进度条
ZHttp.get(HttpUrl.URL_BAIDU_TEST, new ZResponse<BaiduWeatherReply>(BaiduWeatherReply.class, mActivity, "正在获取数据……") {
    @Override
    public void onError(int code, String error) {
        super.onError(code, error);
        //TODO 错误处理，父类已默认调用了ToastUtil.toastShort(error);
    }

    @Override
    public void onSuccess(Response response, BaiduWeatherReply resObj) {
        if (resObj.results.size() > 0) {
            BaiduWeatherReply.ResultsBean bean = resObj.results.get(0);
            textView.setText("city:" + bean.currentCity + " pm25:" + bean.pm25);
        }
    }
});
```
#### DB 使用
以下实体类的规则为GreenDao的官方规则，具体可以google
```
// 首先定义实体
@Entity(nameInDb = "NOTE", generateConstructors = false)
public class Employee {
    @Id
    public long id;

    @NotNull
    public String name;

    public String group;

    public String company;

    public Date date;
}

//daomaster为build代码时自动生成
Employee employee = new Employee();
`
`
`
DaoMaster daomaster = new DaoMaster(getDaoOpenHelper(BaseApp.APP_CONTEXT, "default").getWritableDatabase());
DaoHelper<DaoSession> daoHelper = new DaoHelper(daomaster.newSession());   
boolean b = daoHelper.insertOrReplaceObject(employee);

针对DaoMaster已经封装了一个辅助类，具体见app中的com.zcolin.frame.demo.db.DaoManager
```
#### Activity的回调处理
```
//继承BaseFrameActivity和 BaseFrameFrag的可以直接调用   
startActivityWithCallback(intent, new ResultActivityHelper.ResultActivityListener() {
    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == RESULT_OK) {
	    userDetail.nick_name = data.getStringExtra("data");
	    initData();
	}
    }
});
```

#### 权限回调
```   
//默认常用的权限(包括摄像头、设备信息、SD卡、联系人、拨打电话权限)
PermissionHelper.requestCameraPermission(mActivity, new PermissionsResultAction() {
    @Override
    public void onGranted() {
        
    }

    @Override
    public void onDenied(String permission) {

    }
});

//默认权限中没有的，根据需要自己直接申请
PermissionHelper.requestPermission(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionsResultAction() {
    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(String permission) {

    }
});

```
#### 常用工具类介绍与使用 在Frame框架Utils包中，可以查看源码或者查看生成的JavaDoc。 常用包括：
* ActivityUtil ：Activity 相关操作 工具类。
* AppUtil：APP管理工具类，如获取应用相关信息，应用退出 重启 安装 卸载.判断程序运行状况等函数的定义。
* ArrayUtils: 数组操作工具类。
* BitmapUtil: Bitmap工具类，如图片缩放，保存图片，复制图片，圆角处理等。
* CalendarUtil：日期操作工具类，主要格式化时间。
* DeviceUtil：设备操作工具类，如获取设备识别码，获取设置屏保时间，获取设置字体缩放大小等。
* DisplayUtil：像素转换工具类，如px互转dp，获取设备density等。
* FastClickUtils：防止控件被重复点击的辅助类。
* FileOpenUtil：使用第三方程序打开文件操作工具类。
* FileUtil：文件操作工具类,如文件拷贝，文件读写操作。
* GsonUtil：使用gson对Json数据和对象互转的工具类。
* KeyBoardUtil：软键盘操作如弹出、关闭工具类。
* LogUtil：日志操作工具类。
* MD5Util&RSAUtils:加密解密工具类。
* RegexUtil：正则工具类，匹配常用的如邮箱，手机号等。
* ScreenUtil：屏幕工具类，如获取屏幕宽高，获取截图等。
* SDCardUtil:SD卡工具类，如获取sd卡是否可用，获取sd可用容量等。
* SpannableStringBuilderUtil：SpannableStringBuilder的工具类。
* SpannableStringUtil：SpannableString的工具类。
* SPUtil：SharedPreferences配置文件读写封装。
* StateListUtil：StateList的构建类。
* StringFormatUtil：字符串格式化工具类。
* StringUtil：String相关操作工具类。
* SystemDownloadApk：托管系统下载工具类。
* SystemIntentUtil：调用系统Intent工具类。
* ToastUtil：Toast工具类，可以在子线程调用。

## copyright
本软件使用 Apache License 2.0 协议，请严格遵照协议内容：
1. 需要给代码的用户一份Apache Licence。
1. 如果你修改了代码，需要在被修改的文件中说明。
1. 在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议，商标，专利声明和其他原来作者规定需要包含的说明。
1. 如果再发布的产品中包含一个Notice文件，则在Notice文件中需要带有Apache Licence。你可以在Notice中增加自己的许可，但不可以表现为对Apache Licence构成更改。
1. Apache Licence也是对商业应用友好的许可。使用者也可以在需要的时候修改代码来满足需要并作为开源或商业产品发布/销售
1. 你可以二次包装出售，但还请保留文件中的版权和作者信息，并在你的产品说明中注明ZFrame。
1. 你可以以任何方式获得，你可以修改包名或类名，但还请保留文件中的版权和作者信息。
