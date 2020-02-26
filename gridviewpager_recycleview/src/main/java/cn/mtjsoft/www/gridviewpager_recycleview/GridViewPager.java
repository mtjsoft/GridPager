package cn.mtjsoft.www.gridviewpager_recycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import cn.mtjsoft.www.gridviewpager_recycleview.view.AndDensityUtils;
import cn.mtjsoft.www.gridviewpager_recycleview.view.AndSelectCircleView;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * recycleview的方式来实现美团app的首页标签效果
 */
public class GridViewPager extends FrameLayout {

    private RecyclerView recyclerView;
    private ImageView bgImageView;

    private GridViewPagerAdapter pagerAdapter;
    private LinearLayoutManager linearLayoutManager;
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
    // 数据列表
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

    /**
     * item点击监听
     */
    private GridItemClickListener gridItemClickListener;

    private ImageTextLoaderInterface imageTextLoaderInterface;

    private BackgroundImageLoaderInterface backgroundImageLoaderInterface;

    public GridViewPager(Context context) {
        this(context, null);
    }

    public GridViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        recyclerView = view.findViewById(R.id.recycleview);
        andSelectCircleView = view.findViewById(R.id.scv);
        addView(view);
        // 设置分页滑动
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        //
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        // 滚动监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        // recyclerview已经停止滚动
                        // 设置指示点
                        int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                        andSelectCircleView.setSelectPosition(firstVisibleItem);
                        break;
                    case SCROLL_STATE_DRAGGING:
                        // recyclerview正在被拖拽
                        break;
                    case SCROLL_STATE_SETTLING:
                        // recyclerview正在依靠惯性滚动
                        break;
                }
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
        imageWidth = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_img_width, AndDensityUtils.dip2px(getContext(), imageWidth));
        imageHeight = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_img_height, AndDensityUtils.dip2px(getContext(), imageHeight));
        textColor = typedArray.getColor(R.styleable.GridViewPager_text_color, Color.BLACK);
        textSize = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_text_size, AndDensityUtils.sp2px(getContext(), textSize));
        textImgMargin = typedArray.getDimensionPixelSize(R.styleable.GridViewPager_imgtext_margin, AndDensityUtils.dip2px(getContext(), textImgMargin));
        rowCount = typedArray.getInt(R.styleable.GridViewPager_row_count, rowCount);
        columnCount = typedArray.getInt(R.styleable.GridViewPager_column_count, columnCount);
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
     * 设置 图片加载
     *
     * @param imageTextLoaderInterface
     */
    public GridViewPager setImageTextLoaderInterface(ImageTextLoaderInterface imageTextLoaderInterface) {
        this.imageTextLoaderInterface = imageTextLoaderInterface;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param backgroundImageLoaderInterface
     * @return
     */
    public GridViewPager setBackgroundImageLoader(BackgroundImageLoaderInterface backgroundImageLoaderInterface) {
        this.backgroundImageLoaderInterface = backgroundImageLoaderInterface;
        return this;
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
        recyclerView.setLayoutParams(rl);
        // 每页数据大小
        pageSize = rowCount * columnCount;
        // 总页数
        final int page = dataAllCount / pageSize + (dataAllCount % pageSize > 0 ? 1 : 0);
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
                                linearLayoutManager.scrollToPositionWithOffset(position, 0);
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
    private List<String> stringList = new ArrayList<>();

    private void setAdapter(int page) {
        stringList.clear();
        for (int i = 0; i < page; i++) {
            stringList.add(i + "");
        }
        if (pagerAdapter == null) {
            pagerAdapter = new GridViewPagerAdapter(R.layout.gridpager_item_layout, stringList);
            recyclerView.setAdapter(pagerAdapter);
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
        }
    }

    /**
     * 刷新指定页码数据（每页行列数、数据总数不变，只有某个数据的值改变时使用）
     *
     * @param position
     */
    public void notifyItemChanged(int position) {
        // 总页数
        int page = dataAllCount / pageSize + (dataAllCount % pageSize > 0 ? 1 : 0);
        if (position >= 0 && position < page && pagerAdapter != null) {
            pagerAdapter.notifyItemChanged(position);
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
     * 图片加载
     */
    public interface ImageTextLoaderInterface {
        void displayImageText(ImageView imageView, TextView textView, int position);
    }

    public interface BackgroundImageLoaderInterface {
        void setBackgroundImg(ImageView bgImageView);
    }

    /**
     * adapter
     */
    public class GridViewPagerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        private ViewGroup.LayoutParams layoutParamsMatch;
        private LinearLayout.LayoutParams imageLp;
        private LinearLayout.LayoutParams textLp;
        private int widthPixels;

        public GridViewPagerAdapter(int layoutResId, List<String> data) {
            super(layoutResId, data);
            widthPixels = getResources().getDisplayMetrics().widthPixels;
            setChanged();
        }

        public void setChanged() {
            layoutParamsMatch = new ViewGroup.LayoutParams(widthPixels / columnCount, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageLp = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            textLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textLp.topMargin = textImgMargin;
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, String item) {
            //
            final int position = helper.getLayoutPosition();
            FlexboxLayout flexboxLayout = helper.getView(R.id.flex_layout);
            flexboxLayout.removeAllViews();
            // 循环添加每页数据
            int pageSizeCount = pageSize;
            // 如果是最后一页，判断最后一页是否够每页的大小
            if (position == getItemCount() - 1) {
                pageSizeCount = dataAllCount % pageSize > 0 ? dataAllCount % pageSize : pageSize;
            }
            for (int i = 0; i < pageSizeCount; i++) {
                View view = View.inflate(getContext(), R.layout.gridpager_item, null);
                LinearLayout layout = view.findViewById(R.id.ll_layout);
                layout.setLayoutParams(layoutParamsMatch);
                ImageView imageView = view.findViewById(R.id.item_image);
                imageView.setLayoutParams(imageLp);
                TextView textView = view.findViewById(R.id.item_text);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                textView.setTextColor(textColor);
                textView.setLayoutParams(textLp);
                if (imageTextLoaderInterface != null) {
                    imageTextLoaderInterface.displayImageText(imageView, textView, position * pageSize + i);
                }
                layout.setOnClickListener(new myClick(position, i));
                flexboxLayout.addView(view);
            }

        }

        private class myClick implements OnClickListener {
            int position;
            int pageCount;

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
    }
}
