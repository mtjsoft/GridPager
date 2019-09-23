package cn.mtjsoft.www.gridpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.mtjsoft.www.gridpager.view.AndDensityUtils;
import cn.mtjsoft.www.gridpager.view.AndSelectCircleView;
import cn.mtjsoft.www.gridpager.view.AtMostGridView;
import cn.mtjsoft.www.gridpager.view.AtMostViewPager;

/**
 * 采用ViewPager+GridView的方式来实现美团app的首页标签效果
 */
@SuppressLint("Recycle")
public class GridPager extends FrameLayout implements ViewPager.OnPageChangeListener {

    //
    private LinearLayout linearLayout;
    // ViewPager
    private AtMostViewPager viewPager;
    /**
     * 指示点
     */
    private AndSelectCircleView andSelectCircleView;
    //子控件显示的宽度
    private int mChildWidth = 0;
    //子控件显示的高度
    private int mChildHeight = 0;
    //两个子控件之间的间距
    private int mChildMargin = 0;
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
     * GridPager
     */
    // 竖直方向的间距
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
     * 监听
     */
    private ItemBindDataListener itemBindDataListener;
    private GridItemClickListener gridItemClickListener;

    public GridPager(Context context) {
        this(context, null);
    }

    public GridPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.WHITE);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        handleTypedArray(context, attrs);
        View view = View.inflate(getContext(), R.layout.gridpager_layout, null);
        linearLayout = view.findViewById(R.id.ll_layout);
        viewPager = view.findViewById(R.id.viewpager);
        andSelectCircleView = view.findViewById(R.id.scv);
        addView(view);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridPager);
        backgroundColor = typedArray.getColor(R.styleable.GridPager_background_color, Color.WHITE);
        verticalSpacing = typedArray.getDimensionPixelSize(R.styleable.GridPager_verticalSpacing, AndDensityUtils.dip2px(getContext(), 10));
        imageWidth = typedArray.getDimensionPixelSize(R.styleable.GridPager_img_width, AndDensityUtils.dip2px(getContext(), 50));
        imageHeight = typedArray.getDimensionPixelSize(R.styleable.GridPager_img_height, AndDensityUtils.dip2px(getContext(), 50));
        textColor = typedArray.getColor(R.styleable.GridPager_text_color, Color.BLACK);
        textSize = typedArray.getDimensionPixelSize(R.styleable.GridPager_text_size, AndDensityUtils.sp2px(getContext(), 10));
        textImgMargin = typedArray.getDimensionPixelSize(R.styleable.GridPager_imgtext_margin, AndDensityUtils.dip2px(getContext(), 5));
        rowCount = typedArray.getInt(R.styleable.GridPager_row_count, 2);
        columnCount = typedArray.getInt(R.styleable.GridPager_column_count, 4);
        // 指示点
        mChildWidth = typedArray.getDimensionPixelSize(R.styleable.GridPager_point_width, AndDensityUtils.dip2px(getContext(), 8));
        mChildHeight = typedArray.getDimensionPixelSize(R.styleable.GridPager_point_height, AndDensityUtils.dip2px(getContext(), 8));
        mChildMargin = typedArray.getDimensionPixelSize(R.styleable.GridPager_point_margin, AndDensityUtils.dip2px(getContext(), 8));
        mNormalColor = typedArray.getColor(R.styleable.GridPager_point_normal_color, Color.GRAY);
        mSelectColor = typedArray.getColor(R.styleable.GridPager_point_select_color, Color.RED);
        mIsCircle = typedArray.getBoolean(R.styleable.GridPager_point_is_circle, true);
        mIsShow = typedArray.getBoolean(R.styleable.GridPager_point_is_show, true);
        pointMarginPage = typedArray.getDimensionPixelSize(R.styleable.GridPager_point_margin_page, verticalSpacing);
        pointMarginBottom = typedArray.getDimensionPixelSize(R.styleable.GridPager_point_margin_bottom, verticalSpacing);
        typedArray.recycle();
    }

    /**
     * 设置数据
     *
     * @param allCount
     * @return
     */
    public GridPager setDataAllCount(int allCount) {
        if (allCount >= 0) {
            this.dataAllCount = allCount;
        }
        return this;
    }

    /**
     * 设置列数
     *
     * @param columnCount
     * @return
     */
    public GridPager setColumnCount(int columnCount) {
        if (columnCount >= 0) {
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
    public GridPager setRowCount(int rowCount) {
        if (rowCount >= 0) {
            this.rowCount = rowCount;
        }
        return this;
    }

    /**
     * 设置 纵向间距
     *
     * @param verticalSpacing
     * @return
     */
    public GridPager setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = AndDensityUtils.dip2px(getContext(), verticalSpacing);
        return this;
    }

    /**
     * 设置 icon 宽度
     *
     * @param imageWidth
     * @return
     */
    public GridPager setImageWidth(int imageWidth) {
        this.imageWidth = AndDensityUtils.dip2px(getContext(), imageWidth);
        return this;
    }

    /**
     * 设置 icon 高度
     *
     * @param imageHeight
     * @return
     */
    public GridPager setImageHeight(int imageHeight) {
        this.imageHeight = AndDensityUtils.dip2px(getContext(), imageHeight);
        return this;
    }

    /**
     * 设置 字体颜色
     *
     * @param textColor
     * @return
     */
    public GridPager setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * 设置 背景颜色
     *
     * @param backgroundColor
     * @return
     */
    public GridPager setGridPagerBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param textSize
     * @return
     */
    public GridPager setTextSize(int textSize) {
        this.textSize = AndDensityUtils.sp2px(getContext(), textSize);
        return this;
    }

    /**
     * 设置字体与icon的间距
     *
     * @param textImgMargin
     * @return
     */
    public GridPager setTextImgMargin(int textImgMargin) {
        this.textImgMargin = AndDensityUtils.dip2px(getContext(), textImgMargin);
        return this;
    }

    /**
     * 设置指示器的宽度
     *
     * @param mChildWidth
     * @return
     */
    public GridPager setPointChildWidth(int mChildWidth) {
        this.mChildWidth = AndDensityUtils.dip2px(getContext(), mChildWidth);
        return this;
    }

    /**
     * 设置指示器的高度
     *
     * @param mChildHeight
     * @return
     */
    public GridPager setPointChildHeight(int mChildHeight) {
        this.mChildHeight = AndDensityUtils.dip2px(getContext(), mChildHeight);
        return this;
    }

    /**
     * 设置指示器的间距
     *
     * @param mChildMargin
     * @return
     */
    public GridPager setPointChildMargin(int mChildMargin) {
        this.mChildMargin = AndDensityUtils.dip2px(getContext(), mChildMargin);
        return this;
    }

    /**
     * 设置指示器是否为圆形
     *
     * @param mIsCircle
     * @return
     */
    public GridPager setPointIsCircle(boolean mIsCircle) {
        this.mIsCircle = mIsCircle;
        return this;
    }

    /**
     * 设置指示器未选中颜色
     *
     * @param mNormalColor
     * @return
     */
    public GridPager setPointNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
        return this;
    }

    /**
     * 设置指示器选中的颜色
     *
     * @param mSelectColor
     * @return
     */
    public GridPager setPointSelectColor(int mSelectColor) {
        this.mSelectColor = mSelectColor;
        return this;
    }

    /**
     * 设置指示器是否显示
     *
     * @param mIsShow
     * @return
     */
    public GridPager setPointIsShow(boolean mIsShow) {
        this.mIsShow = mIsShow;
        return this;
    }

    /**
     * 设置指示器与page的间距
     *
     * @param pointMarginPage
     * @return
     */
    public GridPager setPointMarginPage(int pointMarginPage) {
        this.pointMarginPage = AndDensityUtils.dip2px(getContext(), pointMarginPage);
        return this;
    }

    /**
     * 设置指示器与底部的间距
     *
     * @param pointMarginBottom
     * @return
     */
    public GridPager setPointMarginBottom(int pointMarginBottom) {
        this.pointMarginBottom = AndDensityUtils.dip2px(getContext(), pointMarginBottom);
        return this;
    }

    /**
     * 设置 Item 点击监听
     *
     * @param gridItemClickListener
     */
    public GridPager setGridItemClickListener(GridItemClickListener gridItemClickListener) {
        this.gridItemClickListener = gridItemClickListener;
        return this;
    }

    /**
     * 绑定数据
     *
     * @param itemBindDataListener
     */
    public GridPager setItemBindDataListener(ItemBindDataListener itemBindDataListener) {
        this.itemBindDataListener = itemBindDataListener;
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        if (dataAllCount == 0) {
            return;
        }
        linearLayout.setBackgroundColor(backgroundColor);
        viewPager.setBackgroundColor(backgroundColor);
        andSelectCircleView.setBackgroundColor(backgroundColor);
        setBackgroundColor(backgroundColor);
        pageSize = rowCount * columnCount;
        // 设置viewPager
        if (verticalSpacing > 0) {
            LinearLayout.LayoutParams viewPagerParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
            viewPagerParams.topMargin = verticalSpacing;
            viewPagerParams.bottomMargin = verticalSpacing;
            viewPager.setLayoutParams(viewPagerParams);
        }
        viewPager.setAdapter(new GridAdapter());
        viewPager.setCurrentItem(0);
        if (mIsShow) {
            // 显示指示器
            viewPager.addOnPageChangeListener(this);
            andSelectCircleView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams pointParams = (LinearLayout.LayoutParams) andSelectCircleView.getLayoutParams();
            pointParams.topMargin = pointMarginPage;
            pointParams.bottomMargin = pointMarginBottom;
            andSelectCircleView.setLayoutParams(pointParams);
            // 设置指示点
            final int page = dataAllCount / pageSize + (dataAllCount % pageSize > 0 ? 1 : 0);
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
                                viewPager.setCurrentItem(position);
                            }
                        }
                    })
                    .addChild(page);
        } else {
            andSelectCircleView.setVisibility(View.GONE);
        }
    }

    /**
     * viewPager 页面切换监听
     *
     * @param i
     * @param v
     * @param i1
     */
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        andSelectCircleView.setSelectPosition(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * GridAdapter
     */
    private class GridAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return dataAllCount / pageSize + (dataAllCount % pageSize > 0 ? 1 : 0);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            AtMostGridView atMostGridView = (AtMostGridView) View.inflate(getContext(), R.layout.gridpager_mostgridview, null);
            atMostGridView.setNumColumns(columnCount);
            if (verticalSpacing > 0) {
                atMostGridView.setVerticalSpacing(verticalSpacing);
            }
            atMostGridView.setAdapter(new AtMostGridViewAdapter(getContext(), position));
            container.addView(atMostGridView);
            return atMostGridView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * AtMostGridViewAdapter
     */
    private class AtMostGridViewAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Context context;
        private int pageindex;
        private LinearLayout.LayoutParams imageParams;

        public AtMostGridViewAdapter(Context context, int position) {
            this.context = context;
            pageindex = position;
            inflater = LayoutInflater.from(context);
            imageParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        }

        @Override
        public int getCount() {
            return dataAllCount > (pageindex + 1) * pageSize ? pageSize : (dataAllCount - pageindex * pageSize);
        }

        @Override
        public Object getItem(int position) {
            return position + pageindex * pageSize;
        }

        @Override
        public long getItemId(int position) {
            return position + pageindex * pageSize;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gridpager_item, parent, false);
                holder = new ViewHolder();
                holder.linearLayout = convertView.findViewById(R.id.ll_layout);
                holder.iconImageView = convertView.findViewById(R.id.item_image);
                holder.iconNameTextView = convertView.findViewById(R.id.item_text);
                holder.iconImageView.setLayoutParams(imageParams);
                LinearLayout.LayoutParams textParams = (LinearLayout.LayoutParams) holder.iconNameTextView.getLayoutParams();
                textParams.topMargin = textImgMargin;
                holder.iconNameTextView.setLayoutParams(textParams);
                holder.linearLayout.setBackgroundColor(backgroundColor);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final int pos = position + pageindex * pageSize;
            holder.iconNameTextView.setTextColor(textColor);
            holder.iconNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            // 绑定数据
            if (itemBindDataListener != null) {
                itemBindDataListener.BindData(holder.iconImageView, holder.iconNameTextView, pos);
            }
            // item点击
            holder.linearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gridItemClickListener != null) {
                        gridItemClickListener.click(pos);
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private LinearLayout linearLayout;
            private TextView iconNameTextView;
            private ImageView iconImageView;
        }
    }

    /**
     * 绑定数据
     */
    public interface ItemBindDataListener {
        void BindData(ImageView imageView, TextView textView, int position);
    }

    /**
     * item点击回调
     */
    public interface GridItemClickListener {
        void click(int position);
    }
}
