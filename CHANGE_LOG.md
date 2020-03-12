## 2.1.0
1. 移除GrennDao依赖，改为compileOnly,需要时自己在gradle中引入， implementation 'org.greenrobot:greendao:3.2.0';
2. ZResponse中移除.class参数，不需要再传入类的.class类型信息;
3. 修复若干bug。
