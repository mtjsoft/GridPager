package cn.mtjsoft.www.gridviewpager_recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import cn.mtjsoft.www.gridviewpager_recycleview.transformer.CoverPageTransformer;
import cn.mtjsoft.www.gridviewpager_recycleview.transformer.GalleryPageTransformer;
import cn.mtjsoft.www.gridviewpager_recycleview.transformer.TopOrDownPageTransformer;
import cn.mtjsoft.www.gridviewpager_recycleview.view.AndDensityUtils;
import cn.mtjsoft.www.gridviewpager_recycleview.view.AndSelectCircleView;


/**
 * recycleview的方式来实现美团app的首页标签效果
 */
public class GridViewPager extends FrameLayout {

    private ViewPager2 viewPager2;
    private ImageView bgImageView;

    private PagerAdapter pagerAdapter;

    private int widthPixels = 0;
    /**
     * 指示点
     */
    private AndSelectCircleView andSelectCircleView;
    //子控件显示的宽度
    private int mChildWidth = 8;
    //子控件显示的高度
    private int mChildHeight = 8;
    //两个子控件之间的间距
    private int mChildMargin = 8;
    //正常情况下显示的颜色
    private int mNormalColor = Color.GRAY;
    //选中的时候现实的颜色
    private int mSelectColor = Color.RED;
    // 是否是圆形的指示点
    private boolean mIsCircle = true;
    // 是否需要显示指示器
    private boolean mIsShow = true;
    // 指示器与page间距
    private int pointMarginPage = 10;
    // 指示器与底部间距
    private int pointMarginBottom = 10;

    /**
     * GridViewPager
     */
    // page上间距
    private int pagerMarginTop = 10;
    // page下间距
    private int pagerMarginBottom = 10;
    // 行间距间距
    private int verticalSpacing = 10;
    // icon 宽度
    private int imageWidth = 50;
    // icon 高度
    private int imageHeight = 50;
    // 文字颜色
    private int textColor = Color.BLACK;
    // 文字大小
    private int textSize = 10;
    // icon 文字 的间距
    private int textImgMargin = 5;
    // 行数
    private int rowCount = 2;
    // 列数
    private int columnCount = 4;
    // 每页大小
    private int pageSize = 8;
    // 数据总数
    private int dataAllCount = 0;
    // 背景颜色
    private int backgroundColor = Color.WHITE;
    // item背景颜色
    private int itemBackgroundColor = Color.TRANSPARENT;
    // 是否开启无限循环(页数大于1才有效)
    private boolean pageLoop = false;

    // 用于切换动画
    private ViewPager2.PageTransformer pageTransformer;

    /**
     * item点击监听
     */
    private GridItemClickListener gridItemClickListener;
    /**
     * item长按监听
     */
    private GridItemLongClickListener gridItemLongClickListener;

    private ImageTextLoaderInterface imageTextLoaderInterface;

    private BackgroundImageLoaderInterface backgroundImageLoaderInterface;

    private float startX;
    private float startY;

    public GridViewPager(Context context) {
        this(context, null);
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        handleTypedArray(context, attrs);
        initView();
        setBackgroundColor(backgroundColor);
    }

    /**
     * 添加布局
     */

    private void initView() {
        View view = View.inflate(getContext(), R.layout.gridpager_layout, null);
        bgImageView = view.findViewById(R.id.iv_bg);
        viewPager2 = view.findViewById(R.id.viewPager2);
        andSelectCircleView = view.findViewById(R.id.scv);
        addView(view);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int page = getPageSize();
                if (pageLoop && page > 1) {
                    if (position == 0) {
                        viewPager2.setCurrentItem(integerList.size() - 2, false);
                    } else if (position == integerList.size() - 1) {
                        viewPager2.setCurrentItem(1, false);
                    }
                    andSelectCircleView.setSelectPosition(position - 1);
                } else {
                    andSelectCircleView.setSelectPosition(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridViewPager);
        pagerMarginTop = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_pager_MarginTop, AndDensityUtils.dip2px(getContext(), pagerMarginTop));
        pagerMarginBottom = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_pager_MarginBottom, AndDensityUtils.dip2px(getContext(), pagerMarginBottom));
        verticalSpacing = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_verticalSpacing, AndDensityUtils.dip2px(getContext(), verticalSpacing));
        backgroundColor = typedArray.getColor(R.styleable.GridViewPager_background_color, Color.WHITE);
        itemBackgroundColor = typedArray.getColor(R.styleable.GridViewPager_item_background_color, Color.TRANSPARENT);
        imageWidth = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_img_width, AndDensityUtils.dip2px(getContext(), imageWidth));
        imageHeight = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_img_height, AndDensityUtils.dip2px(getContext(), imageHeight));
        textColor = typedArray.getColor(R.styleable.GridViewPager_text_color, Color.BLACK);
        textSize = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_text_size, AndDensityUtils.sp2px(getContext(), textSize));
        textImgMargin = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_imgtext_margin, AndDensityUtils.dip2px(getContext(), textImgMargin));
        rowCount = typedArray.getInt(R.styleable.GridViewPager_row_count, rowCount);
        columnCount = typedArray.getInt(R.styleable.GridViewPager_column_count, columnCount);
        pageLoop = typedArray.getBoolean(R.styleable.GridViewPager_pager_loop, false);
        // 指示点
        mChildWidth = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_point_width, AndDensityUtils.dip2px(getContext(), mChildWidth));
        mChildHeight = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_point_height, AndDensityUtils.dip2px(getContext(), mChildHeight));
        mChildMargin = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_point_margin, AndDensityUtils.dip2px(getContext(), mChildMargin));
        mNormalColor = typedArray.getColor(R.styleable.GridViewPager_point_normal_color, Color.GRAY);
        mSelectColor = typedArray.getColor(R.styleable.GridViewPager_point_select_color, Color.RED);
        mIsCircle = typedArray.getBoolean(R.styleable.GridViewPager_point_is_circle, true);
        mIsShow = typedArray.getBoolean(R.styleable.GridViewPager_point_is_show, true);
        pointMarginPage = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_point_margin_page, verticalSpacing);
        pointMarginBottom = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_point_margin_bottom, verticalSpacing);
        typedArray.recycle();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - startX) < Math.abs(ev.getY() - startY)) {
                    return false;
                } else {
                    return super.dispatchTouchEvent(ev);
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置数据总数
     *
     * @param dataAllCount
     * @return
     */
    public GridViewPager setDataAllCount(int dataAllCount) {
        if (dataAllCount > 0) {
            this.dataAllCount = dataAllCount;
        }
        return this;
    }

    /**
     * 设置列数
     *
     * @param columnCount
     * @return
     */
    public GridViewPager setColumnCount(int columnCount) {
        if (columnCount > 0) {
            this.columnCount = columnCount;
        }
        return this;
    }

    /**
     * 设置是否无限循环（true 页码大于1时才有效）
     *
     * @param pageLoop
     * @return
     */
    public GridViewPager setPageLoop(boolean pageLoop) {
        this.pageLoop = pageLoop;
        return this;
    }

    /**
     * 设置行数
     *
     * @param rowCount
     * @return
     */
    public GridViewPager setRowCount(int rowCount) {
        if (rowCount > 0) {
            this.rowCount = rowCount;
        }
        return this;
    }

    /**
     * 上下边距
     *
     * @param pagerMarginTop
     */
    public GridViewPager setPagerMarginTop(int pagerMarginTop) {
        this.pagerMarginTop = AndDensityUtils.dip2px(getContext(), pagerMarginTop);
        return this;
    }

    public GridViewPager setPagerMarginBottom(int pagerMarginBottom) {
        this.pagerMarginBottom = AndDensityUtils.dip2px(getContext(), pagerMarginBottom);
        return this;
    }

    /**
     * 设置 纵向间距
     *
     * @param verticalSpacing
     * @return
     */
    public GridViewPager setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = AndDensityUtils.dip2px(getContext(), verticalSpacing);
        return this;
    }

    /**
     * 设置 icon 宽度
     *
     * @param imageWidth
     * @return
     */
    public GridViewPager setImageWidth(int imageWidth) {
        this.imageWidth = AndDensityUtils.dip2px(getContext(), imageWidth);
        return this;
    }

    /**
     * 设置 icon 高度
     *
     * @param imageHeight
     * @return
     */
    public GridViewPager setImageHeight(int imageHeight) {
        this.imageHeight = AndDensityUtils.dip2px(getContext(), imageHeight);
        return this;
    }

    /**
     * 设置 字体颜色
     *
     * @param textColor
     * @return
     */
    public GridViewPager setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * 设置 背景颜色
     *
     * @param backgroundColor
     * @return
     */
    public GridViewPager setGridViewPagerBackgroundColor(int backgroundColor) {
        setBackgroundColor(backgroundColor);
        return this;
    }

    /**
     * 单独设置item的背景
     *
     * @param itemBackgroundColor
     * @return
     */
    public GridViewPager setItemBackgroundColor(int itemBackgroundColor) {
        this.itemBackgroundColor = itemBackgroundColor;
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     * @return
     */
    public GridViewPager setTextSize(int textSize) {
        this.textSize = AndDensityUtils.sp2px(getContext(), textSize);
        return this;
    }

    /**
     * 设置字体与icon的间距
     *
     * @param textImgMargin
     * @return
     */
    public GridViewPager setTextImgMargin(int textImgMargin) {
        this.textImgMargin = AndDensityUtils.dip2px(getContext(), textImgMargin);
        return this;
    }

    /**
     * 设置指示器的宽度
     *
     * @param mChildWidth
     * @return
     */
    public GridViewPager setPointChildWidth(int mChildWidth) {
        this.mChildWidth = AndDensityUtils.dip2px(getContext(), mChildWidth);
        return this;
    }

    /**
     * 设置指示器的高度
     *
     * @param mChildHeight
     * @return
     */
    public GridViewPager setPointChildHeight(int mChildHeight) {
        this.mChildHeight = AndDensityUtils.dip2px(getContext(), mChildHeight);
        return this;
    }

    /**
     * 设置指示器的间距
     *
     * @param mChildMargin
     * @return
     */
    public GridViewPager setPointChildMargin(int mChildMargin) {
        this.mChildMargin = AndDensityUtils.dip2px(getContext(), mChildMargin);
        return this;
    }

    /**
     * 设置指示器是否为圆形
     *
     * @param mIsCircle
     * @return
     */
    public GridViewPager setPointIsCircle(boolean mIsCircle) {
        this.mIsCircle = mIsCircle;
        return this;
    }

    /**
     * 设置指示器未选中颜色
     *
     * @param mNormalColor
     * @return
     */
    public GridViewPager setPointNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
        return this;
    }

    /**
     * 设置指示器选中的颜色
     *
     * @param mSelectColor
     * @return
     */
    public GridViewPager setPointSelectColor(int mSelectColor) {
        this.mSelectColor = mSelectColor;
        return this;
    }

    /**
     * 设置指示器是否显示
     *
     * @param mIsShow
     * @return
     */
    public GridViewPager setPointIsShow(boolean mIsShow) {
        this.mIsShow = mIsShow;
        return this;
    }

    /**
     * 设置指示器与page的间距
     *
     * @param pointMarginPage
     * @return
     */
    public GridViewPager setPointMarginPage(int pointMarginPage) {
        this.pointMarginPage = AndDensityUtils.dip2px(getContext(), pointMarginPage);
        return this;
    }

    /**
     * 设置指示器与底部的间距
     *
     * @param pointMarginBottom
     * @return
     */
    public GridViewPager setPointMarginBottom(int pointMarginBottom) {
        this.pointMarginBottom = AndDensityUtils.dip2px(getContext(), pointMarginBottom);
        return this;
    }

    /**
     * 设置 Item 点击监听
     *
     * @param gridItemClickListener
     */
    public GridViewPager setGridItemClickListener(GridItemClickListener gridItemClickListener) {
        this.gridItemClickListener = gridItemClickListener;
        return this;
    }

    /**
     * 设置 Item 长按监听
     *
     * @param gridItemLongClickListener
     */
    public GridViewPager setGridItemLongClickListener(GridItemLongClickListener gridItemLongClickListener) {
        this.gridItemLongClickListener = gridItemLongClickListener;
        return this;
    }

    /**
     * 设置 图片加载
     *
     * @param imageTextLoaderInterface
     */
    public GridViewPager setImageTextLoaderInterface(ImageTextLoaderInterface
                                                             imageTextLoaderInterface) {
        this.imageTextLoaderInterface = imageTextLoaderInterface;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param backgroundImageLoaderInterface
     * @return
     */
    public GridViewPager setBackgroundImageLoader(BackgroundImageLoaderInterface
                                                          backgroundImageLoaderInterface) {
        this.backgroundImageLoaderInterface = backgroundImageLoaderInterface;
        return this;
    }

    /**
     * 设置自定义 PageTransformer
     *
     * @param pageTransformer
     * @return
     */
    public GridViewPager setCustomPageTransformer(ViewPager2.PageTransformer pageTransformer) {
        this.pageTransformer = pageTransformer;
        if (pageTransformer != null) {
            viewPager2.setPageTransformer(pageTransformer);
        }
        return this;
    }

    /**
     * 设置内置的覆盖效果的 PageTransformer
     *
     * @return
     */
    public GridViewPager setCoverPageTransformer() {
        this.pageTransformer = new CoverPageTransformer();
        viewPager2.setPageTransformer(pageTransformer);
        return this;
    }

    /**
     * 设置内置的画廊效果的 PageTransformer
     *
     * @return
     */
    public GridViewPager setGalleryPageTransformer() {
        this.pageTransformer = new GalleryPageTransformer();
        viewPager2.setPageTransformer(pageTransformer);
        return this;
    }

    /**
     * 设置内置的上下进入效果的 PageTransformer
     *
     * @return
     */
    public GridViewPager setTopOrDownPageTransformer(TopOrDownPageTransformer.ModeType modeType) {
        this.pageTransformer = new TopOrDownPageTransformer(modeType);
        viewPager2.setPageTransformer(pageTransformer);
        return this;
    }

    /**
     * 获取页码大小
     *
     * @return
     */
    public int getPageSize() {
        pageSize = rowCount * columnCount;
        return dataAllCount / pageSize + (dataAllCount % pageSize > 0 ? 1 : 0);
    }

    /**
     * 获取一页大小
     *
     * @return
     */
    public int getOnePageSize() {
        return pageSize;
    }

    /**
     * 显示
     */
    public void show() {
        if (dataAllCount == 0) {
            return;
        }
        // 设置高度
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getAutoHeight());
        rl.topMargin = pagerMarginTop;
        rl.bottomMargin = pagerMarginBottom;
        viewPager2.setLayoutParams(rl);
        // 总页数
        final int page = getPageSize();
        // 显示指示器
        andSelectCircleView.setVisibility((mIsShow && page > 1) ? View.VISIBLE : View.GONE);
        if (mIsShow && page > 1) {
            RelativeLayout.LayoutParams pointParams = (RelativeLayout.LayoutParams) andSelectCircleView.getLayoutParams();
            pointParams.topMargin = pointMarginPage;
            pointParams.bottomMargin = pointMarginBottom;
            andSelectCircleView.setLayoutParams(pointParams);
            // 设置指示点
            andSelectCircleView
                    .setmChildWidth(mChildWidth)
                    .setmChildHeight(mChildHeight)
                    .setmChildMargin(mChildMargin)
                    .setmIsCircle(mIsCircle)
                    .setmNormalColor(mNormalColor)
                    .setmSelectColor(mSelectColor)
                    .setPointCheckedChangeListener(new AndSelectCircleView.PointCheckedChangeListener() {
                        @Override
                        public void checkedChange(int position) {
                            if (position >= 0 && position < page) {
                                // 指示点点击，滚动到对应的页
                                if (pageLoop) {
                                    viewPager2.setCurrentItem(position + 1, true);
                                } else {
                                    viewPager2.setCurrentItem(position, true);
                                }
                            }
                        }
                    })
                    .addChild(page);
        }
        // 设置背景图片
        if (backgroundImageLoaderInterface != null) {
            RelativeLayout.LayoutParams bgImageViewRl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getAllHeight());
            bgImageView.setLayoutParams(bgImageViewRl);
            backgroundImageLoaderInterface.setBackgroundImg(bgImageView);
        }
        // 设置数据
        setAdapter(page);
    }

    /**
     * 设置数据
     */
    private List<Integer> integerList = new ArrayList<>();

    private void setAdapter(final int page) {
        integerList.clear();
        for (int i = 0; i < page; i++) {
            integerList.add(i);
        }
        // 如果开启了循环模式，首位各多添加一页
        if (pageLoop && page > 1) {
            integerList.add(0, integerList.get(integerList.size() - 1));
            integerList.add(0);
        }
        if (pagerAdapter == null) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    // post最后调用，可获取测量后的宽度
                    widthPixels = getMeasuredWidth();
                    pagerAdapter = new PagerAdapter(viewPager2.getContext(), R.layout.gridpager_item_layout, integerList);
                    viewPager2.setAdapter(pagerAdapter);
                    viewPager2.setOffscreenPageLimit(1);
                    if (pageLoop && page > 1) {
                        viewPager2.setCurrentItem(1, false);
                    }
                }
            });
        } else {
            notifyDataSetChanged();
        }
    }

    /**
     * 刷新数据
     */
    private void notifyDataSetChanged() {
        if (pagerAdapter != null) {
            pagerAdapter.setChanged();
            pagerAdapter.notifyDataSetChanged();
            if (pageLoop && getPageSize() > 1) {
                viewPager2.setCurrentItem(1, false);
            }
        }
    }

    /**
     * 刷新指定页码数据（每页行列数、数据总数不变，只有某个数据的值改变时使用）
     *
     * @param position
     */
    public void notifyItemChanged(int position) {
        // 总页数
        int page = getPageSize();
        if (position >= 0 && position < page && pagerAdapter != null) {
            if (pageLoop && page > 1) {
                pagerAdapter.notifyItemChanged(position + 1);
            } else {
                pagerAdapter.notifyItemChanged(position);
            }
        }
    }

    /**
     * 计算recycleview高度
     *
     * @return
     */
    private int getAutoHeight() {
        return getOnesHeight() * rowCount + (rowCount - 1) * verticalSpacing;
    }

    /**
     * 获取一行的高度
     *
     * @return
     */
    private int getOnesHeight() {
        return (int) (imageHeight + textImgMargin + textSize * 1.133);
    }

    /**
     * 计算总高度
     *
     * @return
     */
    private int getAllHeight() {
        // 总高
        int page = dataAllCount / pageSize + (dataAllCount % pageSize > 0 ? 1 : 0);
        int recycleviewH = getAutoHeight();
        if (mIsShow && page > 1) {
            recycleviewH += pagerMarginTop + pagerMarginBottom + pointMarginPage + pointMarginBottom + mChildHeight;
        } else {
            recycleviewH += pagerMarginTop + pagerMarginBottom;
        }
        return recycleviewH;
    }

    /**
     * item点击回调
     */
    public interface GridItemClickListener {
        void click(int position);
    }

    /**
     * item点击回调
     */
    public interface GridItemLongClickListener {
        void longClick(int position);
    }

    /**
     * 图片加载
     */
    public interface ImageTextLoaderInterface {
        void displayImageText(ImageView imageView, TextView textView, int position);
    }

    public interface BackgroundImageLoaderInterface {
        void setBackgroundImg(ImageView bgImageView);
    }

    /**
     * PagerAdapter
     */
    public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.Holder> {

        private Context context;
        private int layoutResId;
        private List<Integer> data;
        private ViewGroup.LayoutParams layoutParamsMatch;
        private LinearLayout.LayoutParams imageLp;
        private LinearLayout.LayoutParams textLp;

        public PagerAdapter(Context context, int layoutResId, List<Integer> data) {
            this.context = context;
            this.layoutResId = layoutResId;
            this.data = data;
            setChanged();
        }

        public void setChanged() {
            layoutParamsMatch = new ViewGroup.LayoutParams(widthPixels / columnCount, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageLp = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            textLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textLp.topMargin = textImgMargin;
        }

        @Override
        public int getItemCount() {
            return data == null || data.size() == 0 ? 0 : data.size();
        }

        @NonNull
        @Override
        public PagerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PagerAdapter.Holder holder, int position) {
            holder.flexboxLayout.removeAllViews();
            int posi = position;
            int page = getPageSize();
            if (pageLoop && page > 1) {
                if (position == 0) { // 第一页，展示实际最后一页的内容
                    posi = page - 1;
                } else if (position == getItemCount() - 1) { // 最后一页，展示实际第一页的内容
                    posi = 0;
                } else { // 否则展示 position - 1 的实际内容
                    posi = posi - 1;
                }
            }
            // 循环添加每页数据
            int pageSizeCount = pageSize;
            // 如果是最后一页，判断最后一页是否够每页的大小
            if (posi == page - 1) {
                pageSizeCount = dataAllCount % pageSize > 0 ? dataAllCount % pageSize : pageSize;
            }
            for (int i = 0; i < pageSizeCount; i++) {
                View view = View.inflate(getContext(), R.layout.gridpager_item, null);
                LinearLayout layout = view.findViewById(R.id.ll_layout);
                layout.setLayoutParams(layoutParamsMatch);
                layout.setBackgroundColor(itemBackgroundColor);
                ImageView imageView = view.findViewById(R.id.item_image);
                imageView.setLayoutParams(imageLp);
                TextView textView = view.findViewById(R.id.item_text);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                textView.setTextColor(textColor);
                textView.setLayoutParams(textLp);
                if (imageTextLoaderInterface != null) {
                    imageTextLoaderInterface.displayImageText(imageView, textView, posi * pageSize + i);
                }
                layout.setOnClickListener(new myClick(posi, i));
                layout.setOnLongClickListener(new myLongClick(posi, i));
                holder.flexboxLayout.addView(view);
            }
        }

        public class Holder extends RecyclerView.ViewHolder {
            private FlexboxLayout flexboxLayout;

            public Holder(@NonNull View itemView) {
                super(itemView);
                flexboxLayout = itemView.findViewById(R.id.flex_layout);
            }
        }
    }

    private class myClick implements OnClickListener {
        private int position;
        private int pageCount;

        public myClick(int position, int pageCount) {
            this.position = position;
            this.pageCount = pageCount;
        }

        @Override
        public void onClick(View v) {
            if (gridItemClickListener != null) {
                gridItemClickListener.click(position * pageSize + pageCount);
            }
        }
    }

    private class myLongClick implements OnLongClickListener {

        private int position;
        private int pageCount;

        public myLongClick(int position, int pageCount) {
            this.position = position;
            this.pageCount = pageCount;
        }

        @Override
        public boolean onLongClick(View v) {
            if (gridItemLongClickListener != null) {
                gridItemLongClickListener.longClick(position * pageSize + pageCount);
            }
            return true;
        }
    }
}
