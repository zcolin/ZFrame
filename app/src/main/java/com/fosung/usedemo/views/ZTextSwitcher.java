/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-19 下午1:10
 **********************************************************/
package com.fosung.usedemo.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.fosung.frame.utils.DisplayUtil;

import java.util.ArrayList;

/**
 * 可以定时滚动的TextSwitcher，自动截取文字
 */
public class ZTextSwitcher extends TextSwitcher {
    private TextHandler textHandler    = new TextHandler();
    private MyRunnable  myRunnable     = new MyRunnable();
    private Paint       mPaint         = new Paint();
    private int         textColor      = Color.WHITE;
    private int         index          = 0;
    private int         textLine       = 2;//固定行数
    private long        switchInterval = 2000;//切换间隔
    private int               textSize;
    private String            text;
    private ArrayList<String> listText;
    private int               width;
    private boolean           isStart;
    private boolean           isSetFactory;

    public ZTextSwitcher(Context context) {
        this(context, null);
    }

    public ZTextSwitcher(Context context, AttributeSet attr) {
        super(context, attr);
        textSize = DisplayUtil.dip2px(context, 16);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        if (width > 0 && !isStart) {
            startSwitcher();
        }
    }

    /**
     * 使用dp
     */
    public ZTextSwitcher setSwitcherTextSize(int textSize) {
        this.textSize = DisplayUtil.dip2px(getContext(), textSize);
        mPaint.setTextSize(this.textSize);
        return this;
    }

    /**
     * 设置循环的text
     */
    public ZTextSwitcher setSwitcherText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 设置字体颜色
     */
    public ZTextSwitcher setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * 设置固定行数
     */
    public ZTextSwitcher setTextLine(int textLine) {
        this.textLine = textLine;
        return this;
    }

    /**
     * 设置切换间隔
     */
    public ZTextSwitcher setSwitchInterval(long switchInterval) {
        this.switchInterval = switchInterval;
        return this;
    }

    /**
     * 设置出动画
     */
    public ZTextSwitcher setOutAnima(Context context, int anim) {
        super.setOutAnimation(context, anim);
        return this;
    }

    /**
     * 设置进动画
     */
    public ZTextSwitcher setInAnima(Context context, int anim) {
        super.setInAnimation(context, anim);
        return this;
    }


    /**
     * 开始循环播放
     */
    public void startSwitcher() {
        if (!TextUtils.isEmpty(text) && width > 0 && !isStart) {
            isStart = true;
            if (listText == null || listText.size() == 0) {
                listText = getTextList(text, getMeasuredWidth());
            }

            if (!isSetFactory) {
                isSetFactory = true;
                setFactory(new ViewFactory() {
                    @Override
                    public android.view.View makeView() {
                        TextView textView = new TextView(getContext());
                        textView.setMaxLines(textLine);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        textView.setTextColor(textColor);
                        LayoutParams lp = new LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        lp.gravity = Gravity.CENTER;
                        textView.setLayoutParams(lp);
                        return textView;
                    }
                });
            }
            textHandler.post(myRunnable);
        }
    }

    /**
     * 停止循环播放
     */
    public void stopSwitcher() {
        if (isStart) {
            isStart = false;
            textHandler.removeCallbacks(myRunnable);
            listText = null;
        }
    }

    public boolean isStart() {
        return isStart;
    }


    public boolean isInit()
    {
        return isSetFactory && listText != null;
    }
    /*
    根据数据拼接每屏显示的字符串
     */
    private ArrayList<String> getTextList(String text, float viewWidth) {
        /*获取每行数据*/
        ArrayList<String> textList = new ArrayList<>();
        float length = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (length < viewWidth) {
                builder.append(text.charAt(i));
                length += mPaint.measureText(text.substring(i, i + 1));
                if (i == text.length() - 1) {
                    textList.add(builder.toString());
                }
            } else {
                textList.add(builder.substring(0, builder.length() - 1));
                builder.delete(0, builder.length() - 1);
                length = mPaint.measureText(text.substring(i, i + 1));
                i--;
            }
        }
            
        /*合并每两行的数据*/
        ArrayList<String> newTextList = new ArrayList<>();
        String lastStr = null;
        for (int i = 0; i < textList.size(); i++) {
            if (i % textLine != 0) {
                lastStr += textList.get(i);
                //到最大行数
                if (i % textLine == textLine - 1) {
                    newTextList.add(lastStr);
                }
            } else {
                lastStr = textList.get(i);
                if (i == textList.size() - 1) {
                    newTextList.add(lastStr);
                }
            }
        }
        return newTextList;
    }


    class TextHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            setText(listText.get(index));

            /*当数据1屏时才开始循环播放*/
            if (listText.size() > 1) {
                index++;
                if (index == listText.size()) {
                    index = 0;
                }
                textHandler.postDelayed(myRunnable, switchInterval);
            }
        }
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            textHandler.sendEmptyMessage(0);
        }
    }
}