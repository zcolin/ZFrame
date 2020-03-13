## 2.1.0

1. 修复若干bug;
2. 移除GrennDao依赖，改为compileOnly,需要时自己在gradle中引入， implementation 'org.greenrobot:greendao:3.2.0';
3. ZResponse中移除.class参数，不需要再传入类的.class类型信息;
4. [HuTool](https://github.com/looly/hutool/)是java web一个非常强大的工具类库，
但是直接迁移到android却有些问题，比如java8支持、代码量较大、较多的类用不到等。从HuTool的库中搬运了如下工具类
（优化其内部耦合度较高，删减了一般分方法）：
   * ArrayUtil（完善，数组工具类）
   * Assert（新增，断言工具类）
   * CharUtil（新增，字符工具类）
   * CompareUtil（新增，对比工具类）
   * ExceptionUtil（新增，异常工具类）
   * MapUtil（新增，Map工具类）
   * NumberUtil（新增，数字工具类）
   * ObjectUtil（新增，对象工具类，包括判空、克隆、序列化等操作）
   * PageUtil（新增，分页工具类）
   * RegexUtil（完善，正则工具类）
   * StringBuilderUtil（新增，可复用的字符串生成器）
   * StringSpliter（新增，字符串切分器）
   * StringUtil（完善，字符串工具类）

