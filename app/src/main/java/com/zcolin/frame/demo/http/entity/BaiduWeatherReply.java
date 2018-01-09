/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午9:59
 * ********************************************************
 */
package com.zcolin.frame.demo.http.entity;


import java.util.List;

/**
 * 信息实体，此处demo为User的信息
 */
public class BaiduWeatherReply extends HttpBaseReplyBean {


    public String            date;
    public List<ResultsBean> results;

    public static class ResultsBean {
        public String                currentCity;
        public String                pm25;
        public List<IndexBean>       index;
        public List<WeatherDataBean> weather_data;
    }

    public static class IndexBean {
        public String title;
        public String zs;
        public String tipt;
        public String des;
    }

    public static class WeatherDataBean {
        public String date;
        public String dayPictureUrl;
        public String nightPictureUrl;
        public String weather;
        public String wind;
        public String temperature;
    }

    /**
     * {
     "error": 0,
     "status": "success",
     "date": "2016-10-26",
     "results": [
     {
     "currentCity": "济南",
     "pm25": "142",
     "index": [
     {
     "title": "穿衣",
     "zs": "较舒适",
     "tipt": "穿衣指数",
     "des": "建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"
     },
     {
     "title": "洗车",
     "zs": "较适宜",
     "tipt": "洗车指数",
     "des": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
     },
     {
     "title": "旅游",
     "zs": "适宜",
     "tipt": "旅游指数",
     "des": "天气较好，温度适宜，总体来说还是好天气哦，这样的天气适宜旅游，您可以尽情地享受大自然的风光。"
     },
     {
     "title": "感冒",
     "zs": "较易发",
     "tipt": "感冒指数",
     "des": "天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"
     },
     {
     "title": "运动",
     "zs": "较适宜",
     "tipt": "运动指数",
     "des": "阴天，较适宜进行各种户内外运动。"
     },
     {
     "title": "紫外线强度",
     "zs": "最弱",
     "tipt": "紫外线强度指数",
     "des": "属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"
     }
     ],
     "weather_data": [
     {
     "date": "周三 10月26日 (实时：13℃)",
     "dayPictureUrl": "http://api.map.baidu.com/images/weather/day/yin.png",
     "nightPictureUrl": "http://api.map.baidu.com/images/weather/night/yin.png",
     "weather": "阴",
     "wind": "东北风微风",
     "temperature": "17 ~ 11℃"
     },
     {
     "date": "周四",
     "dayPictureUrl": "http://api.map.baidu.com/images/weather/day/xiaoyu.png",
     "nightPictureUrl": "http://api.map.baidu.com/images/weather/night/xiaoyu.png",
     "weather": "小雨",
     "wind": "东北风3-4级",
     "temperature": "14 ~ 10℃"
     },
     {
     "date": "周五",
     "dayPictureUrl": "http://api.map.baidu.com/images/weather/day/duoyun.png",
     "nightPictureUrl": "http://api.map.baidu.com/images/weather/night/duoyun.png",
     "weather": "多云",
     "wind": "东北风3-4级",
     "temperature": "14 ~ 7℃"
     },
     {
     "date": "周六",
     "dayPictureUrl": "http://api.map.baidu.com/images/weather/day/qing.png",
     "nightPictureUrl": "http://api.map.baidu.com/images/weather/night/qing.png",
     "weather": "晴",
     "wind": "北风微风",
     "temperature": "14 ~ 8℃"
     }
     ]
     }
     ]
     }
     */
}
