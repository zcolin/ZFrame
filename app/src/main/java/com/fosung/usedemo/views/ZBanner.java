/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-18 上午10:00
 * *********************************************************
 */
package com.fosung.usedemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fosung.usedemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 带轮播的vierpager banner
 * 一般来说 banner不会有很多图片，所以imageViews在一开始加载的时候就把所有的imageview加载进来
 * 如果有很多图片，比如有1000，此类需要再更改
 */
public class ZBanner extends FrameLayout {
    public static final int NOT_INDICATOR          = 0;//无指示器
    public static final int CIRCLE_INDICATOR       = 1;//圆点指示器
    public static final int NUM_INDICATOR          = 2;//数字指示器
    public static final int NUM_INDICATOR_TITLE    = 3;//数字和文字
    public static final int CIRCLE_INDICATOR_TITLE = 4;//圆点和文字

    public static final int LEFT   = 5;//指示器位置
    public static final int CENTER = 6;//指示器位置
    public static final int RIGHT  = 7;//指示器位置

    private int mIndicatorMargin = 5;
    private int mIndicatorWidth  = 8;
    private int mIndicatorHeight = 8;

    private int mIndicatorSelectedResId   = R.drawable.banner_gray_radius;
    private int mIndicatorUnselectedResId = R.drawable.banner_white_radius;

    private long    delayTime   = 2000;
    private int     gravity     = -1;
    private int     bannerStyle = CIRCLE_INDICATOR;
    private Handler handler     = new Handler();

    private ArrayList<Object>    listUrl       = new ArrayList<>();
    private ArrayList<ImageView> listIndicator = new ArrayList<>();
    private ArrayList<String> listTitle;

    private int     dataSize;
    private int     startPosition;//无限循环，startPosition是MAX_VALUE的中间值
    private int     currentItem;    //当前的ItemPosition, 真实位置，
    private boolean isResumePlay;
    private boolean isAutoPlay;

    private ViewPager             viewPager;
    private PageChangedListener   onPageChangedListener;
    private ImageView.ScaleType   scaleType;
    private LinearLayout          indicator;
    private OnBannerClickListener listener;
    private TextView              bannerTitle;
    private TextView              numIndicator;
    private Context               context;

    private int MAX_VALUE = 10000;

    public ZBanner(Context context) {
        this(context, null);
    }

    public ZBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView(context, attrs);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZBanner);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.ZBanner_indicator_width, 8);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.ZBanner_indicator_height, 8);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.ZBanner_indicator_margin, 5);
        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.ZBanner_indicator_drawable_selected, R.drawable.banner_gray_radius);
        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.ZBanner_indicator_drawable_unselected, R.drawable.banner_white_radius);
        typedArray.recycle();
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.view_banner, this, true);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        indicator = (LinearLayout) view.findViewById(R.id.indicator);
        bannerTitle = (TextView) view.findViewById(R.id.bannerTitle);
        numIndicator = (TextView) view.findViewById(R.id.numIndicator);
        handleTypedArray(context, attrs);
    }

    public ZBanner setDelayTime(long delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    /**
     * 设置指示器位置
     *
     * @see #LEFT
     * @see #RIGHT
     * @see #CENTER
     */
    public ZBanner setIndicatorGravity(int type) {
        switch (type) {
            case LEFT:
                this.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case CENTER:
                this.gravity = Gravity.CENTER;
                break;
            case RIGHT:
                this.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        return this;
    }

    public ZBanner setBannerTitle(ArrayList<String> listTitle) {
        this.listTitle = listTitle;
        if (bannerStyle == CIRCLE_INDICATOR_TITLE || bannerStyle == NUM_INDICATOR_TITLE) {
            if (listTitle != null && listTitle.size() > 0) {
                bannerTitle.setVisibility(View.VISIBLE);
                indicator.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                bannerTitle.setText(listTitle.get(0));
            } else {
                numIndicator.setBackgroundResource(R.drawable.bg_banner_numindicator);
            }
        }
        return this;
    }

    /**
     * 设置导航的样式
     *
     * @see #NOT_INDICATOR
     * @see #CIRCLE_INDICATOR
     * @see #NUM_INDICATOR
     * @see #NUM_INDICATOR_TITLE
     * @see #CIRCLE_INDICATOR_TITLE
     */
    public ZBanner setBannerStyle(int bannerStyle) {
        this.bannerStyle = bannerStyle;
        switch (bannerStyle) {
            case CIRCLE_INDICATOR:
                indicator.setVisibility(View.VISIBLE);
                break;
            case NUM_INDICATOR:
                numIndicator.setVisibility(View.VISIBLE);
                numIndicator.setBackgroundResource(R.drawable.bg_banner_numindicator);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 10, 10);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                numIndicator.setLayoutParams(layoutParams);
                numIndicator.setPadding(5, 6, 5, 6);
                break;
            case NUM_INDICATOR_TITLE:
                numIndicator.setVisibility(View.VISIBLE);
                break;
            case CIRCLE_INDICATOR_TITLE:
                indicator.setVisibility(View.VISIBLE);
                break;
        }
        return this;
    }

    /**
     * 设置图片播放容器的PageChangedlistener
     */
    public ZBanner setOnPageChangedListener(PageChangedListener onPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener;
        return this;
    }

    /**
     * 设置子控件ImageView的ScaleType
     */
    public ZBanner setItemScaleType(ImageView.ScaleType itemScaleType) {
        this.scaleType = itemScaleType;
        return this;
    }

    public ZBanner setOnBannerClickListener(OnBannerClickListener listener) {
        this.listener = listener;
        return this;
    }

    public ZBanner setImages(Object[] imagesUrl) {
        if (imagesUrl == null || imagesUrl.length == 0) {
            return this;
        }
        return setImages(Arrays.asList(imagesUrl));
    }

    public ZBanner setImages(List<?> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() == 0) {
            return this;
        }

        setImageList(imagesUrl);
        setData();
        return this;
    }

    /**
     * 程序外部调用StartPlay
     */
    public void startAutoPlay() {
        isAutoPlay = true;
        resumePaly();
    }

    /**
     * 程序外部调用StopPlay
     */
    public void stopAutoPlay() {
        isAutoPlay = false;
        pausePlay();
    }

    public boolean isStart() {
        return isAutoPlay;
    }

    public boolean isInit() {
        return listUrl.size() > 0;
    }

    private void resumePaly() {
        isResumePlay = true;
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    private void pausePlay() {
        isResumePlay = false;
        handler.removeCallbacks(task);
    }

    private boolean setImageList(List<?> imagesUrl) {
        dataSize = imagesUrl.size();
        if (bannerStyle == CIRCLE_INDICATOR || bannerStyle == CIRCLE_INDICATOR_TITLE) {
            createIndicator();
        } else if (bannerStyle == NUM_INDICATOR || bannerStyle == NUM_INDICATOR_TITLE) {
            numIndicator.setText(1 + "/" + dataSize);
        }

        listUrl.clear();
        listUrl.addAll(imagesUrl);
        return false;
    }

    private void createIndicator() {
        listIndicator.clear();
        indicator.removeAllViews();
        for (int i = 0; i < dataSize; i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            indicator.addView(imageView, params);
            listIndicator.add(imageView);
        }
    }


    private void setData() {
        currentItem = startPosition = MAX_VALUE / 2 - (MAX_VALUE / 2) % dataSize;
        viewPager.setAdapter(new BannerPagerAdapter());
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(currentItem);
        viewPager.addOnPageChangeListener(new PageChangedListener());
        if (gravity != -1)
            indicator.setGravity(gravity);
    }

    /**
     * <p>获取在屏保列表中的实际位置</p>
     * 为了无限轮播，location记录的是一个整体的位置，有可能非常大</br>
     * 所以要获取实际的位置。
     */
    public int getRealPosition(int position) {
        int midPosition = position - startPosition;
        if (midPosition >= 0) {
            return midPosition % dataSize;
        } else {
            int i = Math.abs(midPosition) % dataSize;
            return i == 0 ? 0 : dataSize - i;
        }
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isResumePlay && isAutoPlay) {
                currentItem++;
                viewPager.setCurrentItem(currentItem);
                handler.postDelayed(task, delayTime);
            }
        }
    };

    /**
     * PagerAdapter
     */
    class BannerPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(scaleType == null ? ImageView.ScaleType.CENTER_CROP : scaleType);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnBannerClick(v, getRealPosition(position));
                    }
                }
            });
            container.addView(imageView);

            Glide.with(context)
                 .load(listUrl.get(getRealPosition(position)))
                 .into(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            final View view = (View) object;
            container.removeView(view);
        }
    }

    /**
     * OnPageChangeListener
     */
    class PageChangedListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1:
                    pausePlay();
                    break;
                case 2:
                    if (isAutoPlay) {
                        currentItem = viewPager.getCurrentItem();
                        resumePaly();
                    }
                    break;
            }

            if (onPageChangedListener != null) {
                onPageChangedListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (onPageChangedListener != null) {
                onPageChangedListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            int realPosition = getRealPosition(position);
            if (bannerStyle == CIRCLE_INDICATOR || bannerStyle == CIRCLE_INDICATOR_TITLE) {
                for (int i = 0; i < listIndicator.size(); i++) {
                    if (realPosition == i) {
                        listIndicator.get(i)
                                     .setImageResource(mIndicatorSelectedResId);
                    } else {
                        listIndicator.get(i)
                                     .setImageResource(mIndicatorUnselectedResId);
                    }
                }
            }

            switch (bannerStyle) {
                case CIRCLE_INDICATOR:
                    break;
                case NUM_INDICATOR:
                    numIndicator.setText((realPosition + 1) + "/" + dataSize);
                    break;
                case NUM_INDICATOR_TITLE:
                    numIndicator.setText((realPosition + 1) + "/" + dataSize);
                    if (listTitle != null && listTitle.size() > 0 && realPosition < listTitle.size()) {
                        bannerTitle.setText(listTitle.get(realPosition));
                    }
                    break;
                case CIRCLE_INDICATOR_TITLE:
                    if (listTitle != null && listTitle.size() > 0 && realPosition < listTitle.size()) {
                        bannerTitle.setText(listTitle.get(realPosition));
                    }
                    break;
            }

            if (onPageChangedListener != null) {
                onPageChangedListener.onPageSelected(position);
            }
        }
    }

    public interface OnBannerClickListener {
        void OnBannerClick(View view, int position);
    }
}