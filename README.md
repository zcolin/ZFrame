# FrameTemplate
##程序初始框架，mvp架构.

####各种工具类封装，Http使用okHttp封装，DB使用GreenDao封装，图片使用Glide

1 封装ResultActivityHelper使用回调，免除startActivityForResult时需要在onActivityResult中写判断。

2 封装常用Dialog，progressDialog，EditTextWithClear,下拉刷新，BaseAdapter，BaseRecyclerAdapter，SpannableStringUtil等。

3 封装6.0的权限管理控制，基于回调模式。

4 封装沉浸式状态栏管理。

5 视频录制。

6 二维码扫描、多图片选择库集成

具体使用Demo参见com.fosung.userdemo.demo;