/***********************************************************
 * author   colin
 * company  fosung
 * email    wanglin2046@126.com
 * date     16-7-15 上午9:54
 **********************************************************/
package com.fosung.usedemo.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fosung.usedemo.R;


/**
 * 自定义上有横条动画的TabView
 */
public class ZTabView extends RelativeLayout implements OnClickListener, OnPageChangeListener {

    public static final int TAB_MODE_LINE = 1;
    public static final int TAB_MODE_ICON = 2;

    private DisplayMetrics dm             = new DisplayMetrics();
    private int            curTab         = 0;                      //当前停留的Tab Index
    private int            tabMode        = TAB_MODE_LINE;
    private boolean        isSmoothScroll = false;

    private ImageView            tabLine;                        //横条View, 如果Mode为TAB_MODE_ICON，tabLine不加载
    private LinearLayout         llTabLay;                        //盛放TabView的容器
    private int                  tabWidth;                        //每个Tab的宽度
    private ZTabListener         tabListener;                    //tab切换时回调接口
    private OnPageChangeListener pagerChangeListener;            //ViewPager切换时回调
    private ViewPager            pager;                            //盛放内容的ViewPager
    private Matrix matrix = new Matrix();
    private Context context;

    public ZTabView(Context context) {
        this(context, null);
    }

    public ZTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * 增加tab选中回调
     */
    public void addZTabListener(ZTabListener tabListener) {
        this.tabListener = tabListener;
    }

    /**
     * 设置ViewPager的回调接口
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        pagerChangeListener = listener;
    }

    /**
     * 增加tab
     */
    public void addZTab(ZTab tab) {
        llTabLay.addView(tab);
        tabWidth = dm.widthPixels / llTabLay.getChildCount();
        tab.setOnClickListener(this);
        tab.tabIndex = llTabLay.getChildCount() - 1;
        tab.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));

        //默认选中第一个
        if (llTabLay.getChildCount() == 1) {
            llTabLay.getChildAt(0)
                    .setSelected(true);
        }
    }

    /**
     * 获取新建的Tab对象
     */
    public ZTab getNewTextTab(String text) {
        return new ZTab(context, text);
    }
    /**
     * 获取新建的Tab对象
     */
    public ZTab getNewIconTab(int stateListDrawable, String text) {
        return new ZTab(context, stateListDrawable, text);
    }
    
    /**
     * 获取新建的Tab对象
     */
    public ZTab getNewIconTab(int drawAbleCommon, int drawAbleSelected, String text) {
        return new ZTab(context, drawAbleCommon, drawAbleSelected, text);
    }

    /**
     * 获取新建的Tab对象
     */
    public ZTab getNewIconTab(Drawable drawAbleCommon, Drawable drawAbleSelected, String text) {
        return new ZTab(context, drawAbleCommon, drawAbleSelected, text);
    }

    /**
     * 初始化为TabLine模式,使用默认图像
     */
    public void initAsTabLine(ViewPager pager) {
        initAsTabLine(pager, 0);
    }

    /**
     * 初始化为TabLine模式
     */
    public void initAsTabLine(ViewPager pager, int tabLineRes) {
        tabMode = TAB_MODE_LINE;
        initViewPager(pager);

        LayoutInflater.from(context)
                      .inflate(R.layout.view_tabview, this);
        llTabLay = (LinearLayout) findViewById(R.id.ll_tabview);
        tabLine = (ImageView) findViewById(R.id.iv_tabivew);
        ((Activity) context).getWindowManager()
                            .getDefaultDisplay()
                            .getMetrics(dm);

        if (tabLineRes != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), tabLineRes);
            int width = tabWidth > bitmap.getWidth() ? bitmap.getWidth() : tabWidth;
            Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, width, bitmap.getHeight());//设置tab的宽度和高度
            tabLine.setImageBitmap(b);
        }
    }

    /**
     * 初始化为TabIcon模式
     */
    public void initAsTabIcon(ViewPager pager) {
        tabMode = TAB_MODE_ICON;
        initViewPager(pager);

        llTabLay = new LinearLayout(context);
        llTabLay.setOrientation(LinearLayout.HORIZONTAL);
        addView(llTabLay, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }


    private void initViewPager(ViewPager pager) {
        if (this.pager == pager) {
            return;
        }

        if (this.pager != null) {
            this.pager.setOnPageChangeListener(null);
        }

        final PagerAdapter adapter = pager.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        this.pager = pager;
        pager.setOnPageChangeListener(this);
        adapter.notifyDataSetChanged();
    }

    public void setSmoothScroll(boolean isSmoothScroll) {
        this.isSmoothScroll = isSmoothScroll;
    }


    /**
     * 选中某个Tab 不会调用onTabSelect
     *
     * @param tab 选中的tabIndex
     */
    public void selectTab(int tab) {
        if (curTab == tab) {
            return;
        }

        int childCount = llTabLay.getChildCount();
        if (tab > childCount) {
            tab = childCount - 1;
        }

        if (tab < 0) {
            tab = 0;
        }

        for (int i = 0; i < childCount; i++) {
            if (tab != i) {
                llTabLay.getChildAt(i)
                        .setSelected(false);
            } else {
                llTabLay.getChildAt(i)
                        .setSelected(true);
            }
        }
        curTab = tab;
    }

    /**
     * 获取当前Tab Position
     */
    public int getCurZTab() {
        return curTab;
    }


    /*
     * 调用Tab进行滚动，一般是viewpager的onPagerScroll来调用
     *
     * @param tabIndex 当前的tabIndex
     * @param arg1     滚动的百分比
     */
    private void tabLineScoller(int tabIndex, float arg1) {
        if (tabLine != null) {
            // 平移的目的地
            matrix.setTranslate(tabWidth * tabIndex, 0);
            // 在滑动的过程中，计算出激活条应该要滑动的距离
            float t = (tabWidth) * arg1;
            // 平移的距离
            matrix.postTranslate(t, 0);
            tabLine.setImageMatrix(matrix);
        }
    }


    @Override
    public void onClick(View v) {
        if (v instanceof ZTab) {
            if (pager != null) {
                pager.setCurrentItem(((ZTab) v).tabIndex, isSmoothScroll);
            }

            if (tabListener != null) {
                tabListener.onTabSelected((ZTab) v, ((ZTab) v).tabIndex);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (pagerChangeListener != null) {
            pagerChangeListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        tabLineScoller(arg0, arg1);
        if (pagerChangeListener != null) {
            pagerChangeListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        selectTab(arg0);
        if (pagerChangeListener != null) {
            pagerChangeListener.onPageSelected(arg0);
        }
    }

    /**
     * Tab选中回调
     */
    public interface ZTabListener {
        void onTabSelected(ZTab arg0, int index);
    }

    /**
     * 自定义TAB
     */
    public class ZTab extends TextView {
        int tabIndex;

        private ZTab(Context context) {
            super(context);
        }

        private ZTab(Context context, String text) {
            super(context);
            this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            setText(text);
        }
        private ZTab(Context context, int stateListDrawable, String text) {
            super(context);
            this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            setCompoundDrawablesWithIntrinsicBounds(0, stateListDrawable, 0, 0);
            setText(text);
        }

        private ZTab(Context context, int iconCommon, int iconSelected, String text) {
            super(context);
            this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            setCompoundDerawableTop(getResources().getDrawable(iconCommon), getResources().getDrawable(iconSelected));
            setText(text);
        }

        private ZTab(Context context, Drawable iconCommon, Drawable iconSelected, String text) {
            super(context);
            this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

            setCompoundDerawableTop(iconCommon, iconSelected);
            setText(text);
        }

        private void setCompoundDerawableTop(Drawable iconCommon, Drawable iconSelected) {
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{android.R.attr.state_selected},
                    iconSelected);
            drawable.addState(new int[]{},
                    iconCommon);
            setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }
}
