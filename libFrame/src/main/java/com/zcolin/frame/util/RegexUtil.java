package com.zcolin.frame.util;


import com.zcolin.frame.exception.ZFrameException;
import com.zcolin.frame.util.func.Func1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 * 提供验证邮箱、手机号、电话号码、身份证号码、数字等方法
 */
@SuppressWarnings("unused")
public final class RegexUtil {
    /**
     * 常用正则表达式代码提供 验证数字：^[0-9]*$ <br>
     * 验证n位的数字：^\d{n}$ <br>
     * 验证至少n位数字：^\d{n,}$ <br>
     * 验证m-n位的数字：^\d{m,n}$ <br>
     * 验证零和非零开头的数字：^(0|[1-9][0-9]*)$ <br>
     * 验证有两位小数的正实数：^[0-9]+(.[0-9]{2})?$ <br>
     * 验证有1-3位小数的正实数：^[0-9]+(.[0-9]{1,3})?$ <br>
     * 验证非零的正整数：^\+?[1-9][0-9]*$ <br>
     * 验证非零的负整数：^\-[1-9][0-9]*$ <br>
     * 验证非负整数（正整数 + 0） ^\d+$ <br>
     * 验证非正整数（负整数 + 0） ^((-\d+)|(0+))$ <br>
     * 验证长度为3的字符：^.{3}$ <br>
     * 验证由26个英文字母组成的字符串：^[A-Za-z]+$ <br>
     * 验证由26个大写英文字母组成的字符串：^[A-Z]+$ <br>
     * 验证由26个小写英文字母组成的字符串：^[a-z]+$ <br>
     * 验证由数字和26个英文字母组成的字符串：^[A-Za-z0-9]+$ <br>
     * 验证由数字、26个英文字母或者下划线组成的字符串：^\w+$ <br>
     * 验证用户密码:^[a-zA-Z]\w{5,17}$ 正确格式为：以字母开头，长度在6-18之间，只能包含字符、数字和下划线。 <br>
     * 验证是否含有 ^%&',;=?$\" 等字符：[^%&',;=?$\x22]+ <br>
     * 验证汉字：^[\u4E00-\u9FA5\uF900-\uFA2D]+$ <br>
     * 验证Email地址：^\w+[-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$ <br>
     * 验证InternetURL：^http://([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$
     * ；^[a-zA-z]+://(w+(-w+)*)(.(w+(-w+)*))*(?S*)?$ <br>
     * 验证手机号码：^(\(\d{3,4}\)|\d{3,4}-)?\d{7,8}$：--正确格式为：XXXX-XXXXXXX，XXXX-
     * XXXXXXXX，XXX-XXXXXXX，XXX-XXXXXXXX，XXXXXXX，XXXXXXXX。 <br>
     * 验证身份证号（15位或18位数字）：^\d{15}|\d{}18$ <br>
     * 验证一年的12个月：^(0?[1-9]|1[0-2])$ 正确格式为：“01”-“09”和“1”“12” <br>
     * 验证一个月的31天：^((0?[1-9])|((1|2)[0-9])|30|31)$ 正确格式为：01、09和1、31。 <br>
     * 整数：^-?\d+$ <br>
     * 非负浮点数（正浮点数 + 0）：^\d+(\.\d+)?$ <br>
     * 正浮点数
     * ^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9
     * ][0-9]*))$ <br>
     * 非正浮点数（负浮点数 + 0） ^((-\d+(\.\d+)?)|(0+(\.0+)?))$ <br>
     * 负浮点数
     * ^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1
     * -9][0-9]*)))$ <br>
     * 浮点数 ^(-?\d+)(\.\d+)?$ <br>
     */

    /**
     * 分组
     */
    public final static Pattern        GROUP_VAR = Pattern.compile("\\$(\\d+)");
    /**
     * 正则中需要被转义的关键字
     */
    public final static Set<Character> RE_KEYS   = new HashSet<>(Arrays.asList('$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|'));

    /**
     * Pattern池
     */
    private static final HashMap<RegexWithFlag, Pattern> POOL = new HashMap<>();

    /**
     * 手机号码，中间4位星号替换
     *
     * @param phone 手机号
     * @return 星号替换的手机号
     */
    public static String phoneNoHide(String phone) {
        // 括号表示组，被替换的部分$n表示第n组的内容
        // 正则表达式中，替换字符串，括号的意思是分组，在replace()方法中，
        // 参数二中可以使用$n(n为数字)来依次引用模式串中用括号定义的字串。
        // "(\d{3})\d{4}(\d{4})", "$1****$2"的这个意思就是用括号，
        // 分为(前3个数字)中间4个数字(最后4个数字)替换为(第一组数值，保持不变$1)(中间为*)(第二组数值，保持不变$2)
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 银行卡号，保留最后4位，其他星号替换
     *
     * @param cardId 卡号
     * @return 星号替换的银行卡号
     */
    public static String cardIdHide(String cardId) {
        return cardId.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1");
    }

    /**
     * 身份证号，中间10位星号替换
     *
     * @param id 身份证号
     * @return 星号替换的身份证号
     */
    public static String idHide(String id) {
        return id.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1** **** ****$2");
    }

    /**
     * 是否为车牌号（沪A88888）
     *
     * @param vehicleNo 车牌号
     * @return 是否为车牌号
     */

    public static boolean isVehicleNo(String vehicleNo) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}$");
        return pattern.matcher(vehicleNo).find();

    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }


    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     *               <p>虚拟运营商好短：17</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isMobile(String mobile) {
        String regex = "(\\+\\d+)?1[345789]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isDigit(String digit) {
        String regex = "-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isDecimals(String decimals) {
        String regex = "-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isURL(String url) {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isIpAddress(String ipAddress) {
        String regex = "\\d+\\.\\d+\\.\\d+\\.\\d+";
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * 匹配是否为APK包
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isApk(String str) {
        String regex = "^(.*)\\.(apk)$";
        return Pattern.matches(regex, str);
    }


    /**
     * 匹配是否为视频文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isVideo(String str) {
        String regex = "^(.*)\\.(mpeg-4|h.264|h.265|rmvb|xvid|vp6|h.263|mpeg-1|mpeg-2|avi|" + "mov|mkv|flv|3gp|3g2|asf|wmv|mp4|m4v|tp|ts|mtp|m2t)$";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为音频文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isAudio(String str) {
        String regex = "^(.*)\\.(aac|vorbis|flac|mp3|mp2|wma)$";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为文本文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isTxt(String str) {
        String regex = "^(.*)\\.(txt|xml|html)$";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为压缩文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isZip(String str) {
        String regex = "^(.*)\\.(zip|rar|7z)$";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为Word文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isWord(String str) {
        String regex = "^(.*)\\.(doc|docx)";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为PPT文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPPT(String str) {
        String regex = "^(.*)\\.(ppt|pptx)";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为excel文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isExcel(String str) {
        String regex = "^(.*)\\.(xls|xlsx)";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为vcf文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isVcf(String str) {
        String regex = "^(.*)\\.(vcf)";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为PDF文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPdf(String str) {
        String regex = "^(.*)\\.(pdf)";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为Sql文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isSql(String str) {
        String regex = "^(.*)\\.(sql|db)";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为Img文件
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isImg(String str) {
        String regex = "^(.*)\\.(jpg|bmp|png|gif|jpeg|psd)";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否为QQ
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isQQ(String str) {
        String regex = "[1-9][0-9]{4,}";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否首尾含有空白字符
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isStartOrEndContainNone(String str) {
        String regex = "^\\s*|\\s*";
        return Pattern.matches(regex, str);
    }

    /**
     * 匹配是否空白行
     *
     * @param str 匹配字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isBlankLine(String str) {
        String regex = "\\n\\s*\\r";
        return Pattern.matches(regex, str);
    }

    /**
     * 正则替换指定值<br>
     * 通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
     *
     * <p>
     * 例如：原字符串是：中文1234，我想把1234换成(1234)，则可以：
     *
     * <pre>
     * ReUtil.replaceAll("中文1234", "(\\d+)", "($1)"))
     *
     * 结果：中文(1234)
     * </pre>
     *
     * @param content             文本
     * @param regex               正则
     * @param replacementTemplate 替换的文本模板，可以使用$1类似的变量提取正则匹配出的内容
     * @return 处理后的文本
     */
    public static String replaceAll(CharSequence content, String regex, String replacementTemplate) {
        final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return replaceAll(content, pattern, replacementTemplate);
    }

    /**
     * 正则替换指定值<br>
     * 通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
     *
     * @param content             文本
     * @param pattern             {@link Pattern}
     * @param replacementTemplate 替换的文本模板，可以使用$1类似的变量提取正则匹配出的内容
     * @return 处理后的文本
     *
     * @since 3.0.4
     */
    public static String replaceAll(CharSequence content, Pattern pattern, String replacementTemplate) {
        if (StringUtil.isEmpty(content)) {
            return StringUtil.str(content);
        }

        final Matcher matcher = pattern.matcher(content);
        boolean result = matcher.find();
        if (result) {
            final Set<String> varNums = findAll(GROUP_VAR, replacementTemplate, 1, new HashSet<String>());
            final StringBuffer sb = new StringBuffer();
            do {
                String replacement = replacementTemplate;
                for (String var : varNums) {
                    int group = Integer.parseInt(var);
                    replacement = replacement.replace("$" + var, matcher.group(group));
                }
                matcher.appendReplacement(sb, escape(replacement));
                result = matcher.find();
            } while (result);
            matcher.appendTail(sb);
            return sb.toString();
        }
        return StringUtil.str(content);
    }

    /**
     * 替换所有正则匹配的文本，并使用自定义函数决定如何替换
     *
     * @param str        要替换的字符串
     * @param regex      用于匹配的正则式
     * @param replaceFun 决定如何替换的函数
     * @return 替换后的文本
     *
     * @since 4.2.2
     */
    public static String replaceAll(CharSequence str, String regex, Func1<Matcher, String> replaceFun) {
        return replaceAll(str, Pattern.compile(regex), replaceFun);
    }

    /**
     * 替换所有正则匹配的文本，并使用自定义函数决定如何替换
     *
     * @param str        要替换的字符串
     * @param pattern    用于匹配的正则式
     * @param replaceFun 决定如何替换的函数,可能被多次调用（当有多个匹配时）
     * @return 替换后的字符串
     *
     * @since 4.2.2
     */
    public static String replaceAll(CharSequence str, Pattern pattern, Func1<Matcher, String> replaceFun) {
        if (StringUtil.isEmpty(str)) {
            return StringUtil.str(str);
        }

        final Matcher matcher = pattern.matcher(str);
        final StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            try {
                matcher.appendReplacement(buffer, replaceFun.call(matcher));
            } catch (Exception e) {
                throw new ZFrameException(e);
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组0的内容
     *
     * @param regex   正则
     * @param content 被查找的内容
     * @return 结果列表
     *
     * @since 3.1.2
     */
    public static List<String> findAllGroup0(String regex, CharSequence content) {
        return findAll(regex, content, 0);
    }

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容
     *
     * @param regex   正则
     * @param content 被查找的内容
     * @return 结果列表
     *
     * @since 3.1.2
     */
    public static List<String> findAllGroup1(String regex, CharSequence content) {
        return findAll(regex, content, 1);
    }

    /**
     * 取得内容中匹配的所有结果
     *
     * @param regex   正则
     * @param content 被查找的内容
     * @param group   正则的分组
     * @return 结果列表
     *
     * @since 3.0.6
     */
    public static List<String> findAll(String regex, CharSequence content, int group) {
        return findAll(regex, content, group, new ArrayList<String>());
    }

    /**
     * 取得内容中匹配的所有结果
     *
     * @param <T>        集合类型
     * @param regex      正则
     * @param content    被查找的内容
     * @param group      正则的分组
     * @param collection 返回的集合类型
     * @return 结果集
     */
    public static <T extends Collection<String>> T findAll(String regex, CharSequence content, int group, T collection) {
        if (null == regex) {
            return collection;
        }

        return findAll(Pattern.compile(regex, Pattern.DOTALL), content, group, collection);
    }

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组0的内容
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @return 结果列表
     *
     * @since 3.1.2
     */
    public static List<String> findAllGroup0(Pattern pattern, CharSequence content) {
        return findAll(pattern, content, 0);
    }

    /**
     * 取得内容中匹配的所有结果，获得匹配的所有结果中正则对应分组1的内容
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @return 结果列表
     *
     * @since 3.1.2
     */
    public static List<String> findAllGroup1(Pattern pattern, CharSequence content) {
        return findAll(pattern, content, 1);
    }

    /**
     * 取得内容中匹配的所有结果
     *
     * @param pattern 编译后的正则模式
     * @param content 被查找的内容
     * @param group   正则的分组
     * @return 结果列表
     *
     * @since 3.0.6
     */
    public static List<String> findAll(Pattern pattern, CharSequence content, int group) {
        return findAll(pattern, content, group, new ArrayList<String>());
    }

    /**
     * 取得内容中匹配的所有结果
     *
     * @param <T>        集合类型
     * @param pattern    编译后的正则模式
     * @param content    被查找的内容
     * @param group      正则的分组
     * @param collection 返回的集合类型
     * @return 结果集
     */
    public static <T extends Collection<String>> T findAll(Pattern pattern, CharSequence content, int group, T collection) {
        if (null == pattern || null == content) {
            return null;
        }

        if (null == collection) {
            throw new NullPointerException("Null collection param provided!");
        }

        final Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            collection.add(matcher.group(group));
        }
        return collection;
    }

    /**
     * 转义字符，将正则的关键字转义
     *
     * @param c 字符
     * @return 转义后的文本
     */
    public static String escape(char c) {
        final StringBuilder builder = new StringBuilder();
        if (RE_KEYS.contains(c)) {
            builder.append('\\');
        }
        builder.append(c);
        return builder.toString();
    }

    /**
     * 转义字符串，将正则的关键字转义
     *
     * @param content 文本
     * @return 转义后的文本
     */
    public static String escape(CharSequence content) {
        if (StringUtil.isBlank(content)) {
            return StringUtil.str(content);
        }

        final StringBuilder builder = new StringBuilder();
        int len = content.length();
        char current;
        for (int i = 0; i < len; i++) {
            current = content.charAt(i);
            if (RE_KEYS.contains(current)) {
                builder.append('\\');
            }
            builder.append(current);
        }
        return builder.toString();
    }

    /**
     * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
     *
     * @param regex 正则表达式
     * @return {@link Pattern}
     */
    public static Pattern get(String regex) {
        return get(regex, 0);
    }

    /**
     * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
     *
     * @param regex 正则表达式
     * @param flags 正则标识位集合 {@link Pattern}
     * @return {@link Pattern}
     */
    public static Pattern get(String regex, int flags) {
        final RegexWithFlag regexWithFlag = new RegexWithFlag(regex, flags);

        Pattern pattern = POOL.get(regexWithFlag);
        if (null == pattern) {
            pattern = Pattern.compile(regex, flags);
            POOL.put(regexWithFlag, pattern);
        }
        return pattern;
    }

    /**
     * 移除缓存
     *
     * @param regex 正则
     * @param flags 标识
     * @return 移除的{@link Pattern}，可能为{@code null}
     */
    public static Pattern remove(String regex, int flags) {
        return POOL.remove(new RegexWithFlag(regex, flags));
    }

    /**
     * 清空缓存池
     */
    public static void clear() {
        POOL.clear();
    }

    /**
     * 正则表达式和正则标识位的包装
     *
     * @author Looly
     */
    private static class RegexWithFlag {
        private String regex;
        private int    flag;

        /**
         * 构造
         *
         * @param regex 正则
         * @param flag  标识
         */
        public RegexWithFlag(String regex, int flag) {
            this.regex = regex;
            this.flag = flag;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + flag;
            result = prime * result + ((regex == null) ? 0 : regex.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            RegexWithFlag other = (RegexWithFlag) obj;
            if (flag != other.flag) {
                return false;
            }
            if (regex == null) {
                return other.regex == null;
            } else {
                return regex.equals(other.regex);
            }
        }

    }
}