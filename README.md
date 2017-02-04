# FrameTemplate
##程序初始框架,主要包含五部分:Orm数据库、OkHttp封装、Glide图片加载、PermissionHelper权限处理、工具类集合.
####Orm数据库采用GreenDao，Http采用OkHttp，图片加载采用Glide, 这三个库是经过从性能和易用度综合考虑确定的.封装的基本能满足平时的使用了，遇到特殊问题，就google去吧。


Feature
=
1. 对GreenDao进行了封装，对常用的增删改查直接继承即可不用再写函数。
1. OkHttp进行了封装，主要目的就是可以异步回调时主线程中执行任务,其实这套封装是参考鸿洋大神的，进行了多处更改。感谢!
2. 图片加载基本没做什么工作，但是考虑可能会换库，所以封装一层.
1. 封装ResultActivityHelper使用回调，免除startActivityForResult时需要在onActivityResult中写判断。
1. 封装6.0的权限管理控制，基于回调模式。
1. 工具类是经过多个项目积累整理的，基本都会用到，涉及面比较广。


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

Usage
=
```
//HttpDemo,当然还可以进一步封装，但这不是框架层要考虑的了，具体示例在我的AllLibDemo中用
OkHttpUtils.get()
   .url(url)
   .tag(cancelTag)
   .build()
   .execute(response);
//继承BaseFrameActivity的可以直接调用    
startActivityWithCallback(intent, new ResultActivityHelper.ResultActivityListener() {
    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == RESULT_OK) {
	    userDetail.nick_name = data.getStringExtra("data");
	    initData();
	}
    }
});   
//权限使用
PermissionHelper.requestPermission(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionsResultAction() {
    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied(String permission) {

    }
});

```
