package cn.mtjsoft.www.gridpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
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
    //是否显示默认指示器
    private boolean mIsShowDefaultIndicator = true;

    /**
     * GridPager
     */
    // 竖直方向的间距
    private int verticalSpacing = 0;
    // icon 宽度
    private int imageWidth = 0;
    // icon 高度
    private int imageHeight = 0;
    // 文字颜色
    private int textColor = Color.BLACK;
    // 文字大小
    private int textSize = 0;
    // icon 文字 的间距
    private int textImgMargin = 0;
    // 行数
    private int rowCount = 2;
    // 列数
    private int columnCount = 4;
    // 每页大小
    private int pageSize = 8;
    // 数据总数
    private int dataAllCount = 0;

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
        viewPager = view.findViewById(R.id.viewpager);
        andSelectCircleView = view.findViewById(R.id.scv);
        addView(view);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GridPager);
        verticalSpacing = typedArray.getDimensionPixelSize(R.styleable.GridPager_verticalSpacing,0);
        imageWidth = typedArray.getDimensionPixelSize(R.styleable.GridPager_img_width, 0);
        imageHeight = typedArray.getDimensionPixelSize(R.styleable.GridPager_img_height, 0);
        textColor = typedArray.getColor(R.styleable.GridPager_text_color, Color.BLACK);
        textSize = typedArray.getDimensionPixelSize(R.styleable.GridPager_text_size, 0);
        textImgMargin = typedArray.getDimensionPixelSize(R.styleable.GridPager_imgtext_margin, 0);
        rowCount = typedArray.getInt(R.styleable.GridPager_row_count, 2);
        columnCount = typedArray.getInt(R.styleable.GridPager_column_count, 4);
        pageSize = rowCount * columnCount;
        // 指示点
        mIsShowDefaultIndicator = typedArray.getBoolean(R.styleable.GridPager_show_default_indicator, true);
        mChildWidth = typedArray.getDimensionPixelSize(R.styleable.GridPager_point_width, AndDensityUtils.dip2px(getContext(), 8));
        mChildHeight = typedArray.getDimensionPixelSize(R.styleable.GridPager_point_height, AndDensityUtils.dip2px(getContext(), 8));
        mChildMargin = typedArray.getDimensionPixelSize(R.styleable.GridPager_point_margin, AndDensityUtils.dip2px(getContext(), 8));
        mNormalColor = typedArray.getColor(R.styleable.GridPager_point_normal_color, Color.GRAY);
        mSelectColor = typedArray.getColor(R.styleable.GridPager_point_select_color, Color.RED);
        mIsCircle = typedArray.getBoolean(R.styleable.GridPager_point_is_circle, true);
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
        this.verticalSpacing = verticalSpacing;
        return this;
    }

    /**
     * 设置 icon 宽度
     *
     * @param imageWidth
     * @return
     */
    public GridPager setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    /**
     * 设置 icon 高度
     *
     * @param imageHeight
     * @return
     */
    public GridPager setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
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
     * 设置字体大小
     *
     * @param textSize
     * @return
     */
    public GridPager setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * 设置字体与icon的间距
     *
     * @param textImgMargin
     * @return
     */
    public GridPager setTextImgMargin(int textImgMargin) {
        this.textImgMargin = textImgMargin;
        return this;
    }

    /**
     * 设置显示默认指示器
     *
     * @param showDefaultIndicator
     * @return
     */
    public GridPager showDefaultIndicator(boolean showDefaultIndicator) {
        this.mIsShowDefaultIndicator = showDefaultIndicator;
        return this;
    }
    /**
     * 设置指示器的宽度
     *
     * @param mChildWidth
     * @return
     */
    public GridPager setPointChildWidth(int mChildWidth) {
        this.mChildWidth = mChildWidth;
        return this;
    }

    /**
     * 设置指示器的高度
     *
     * @param mChildHeight
     * @return
     */
    public GridPager setPointChildHeight(int mChildHeight) {
        this.mChildHeight = mChildHeight;
        return this;
    }

    /**
     * 设置指示器的间距
     *
     * @param mChildMargin
     * @return
     */
    public GridPager setPointChildMargin(int mChildMargin) {
        this.mChildMargin = mChildMargin;
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
        // 设置viewPager
        if (verticalSpacing > 0) {
            LinearLayout.LayoutParams viewPagerParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
            viewPagerParams.topMargin = verticalSpacing;
            viewPagerParams.bottomMargin = verticalSpacing;
            viewPager.setLayoutParams(viewPagerParams);
        }
        viewPager.setAdapter(new GridAdapter());
        viewPager.addOnPageChangeListener(this);
        // 设置指示点
        if(mIsShowDefaultIndicator){
            andSelectCircleView.setVisibility(View.VISIBLE);
            final int page = dataAllCount / pageSize + (dataAllCount % pageSize > 0 ? 1 : 0);
            andSelectCircleView.setmChildWidth(mChildWidth)
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
        }else{
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
        if(mIsShowDefaultIndicator && andSelectCircleView.getVisibility() == View.VISIBLE){
            andSelectCircleView.setSelectPosition(i);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public ViewPager getPageAdapter() {
        return viewPager;
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
            if(imageWidth > 0 && imageHeight > 0){
                imageParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            }else{
                imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gridpager_item, parent, false);
                holder = new ViewHolder();
                holder.linearLayout = convertView.findViewById(R.id.ll_layout);
                holder.iconImageView = convertView.findViewById(R.id.item_image);
                holder.iconNameTextView = convertView.findViewById(R.id.item_text);
                holder.iconImageView.setLayoutParams(imageParams);
                LinearLayout.LayoutParams textParams = (LinearLayout.LayoutParams) holder.iconNameTextView.getLayoutParams();
                if(textImgMargin > 0){
                    textParams.topMargin = textImgMargin;
                }
                holder.iconNameTextView.setLayoutParams(textParams);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final int pos = position + pageindex * pageSize;
            if(textColor > 0){
                holder.iconNameTextView.setTextColor(textColor);
            }
            if(textSize > 0){
                holder.iconNameTextView.setTextSize(textSize);
            }
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
