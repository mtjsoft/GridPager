package cn.mtjsoft.www.gridpager.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.security.InvalidParameterException;

import cn.mtjsoft.www.gridpager.R;

/**
 * 显示小圆点，表示当前选择的位置
 *
 * @author yuan
 */
@SuppressLint("Recycle")
public class AndSelectCircleView extends RadioGroup {

    private static final String tag = AndSelectCircleView.class.getSimpleName();
    //默认的子控件的大小，单位是dp
    private static final int DEAFULT_CHILD_SIZE = 8;
    //默认情况下子控件之间的间距，单位是dp
    private static final int DEFAULT_CHILD_MARGIN = 8;
    //默认显示的颜色值
    private static final int DEFAULT_NORMAL_COLOR = Color.GRAY;
    //默认选中显示的颜色值
    private static final int DEFAULT_SELECT_COLOR = Color.RED;
    //子控件显示的宽度
    private int mChildWidth = 0;
    //子控件显示的高度
    private int mChildHeight = 0;
    //两个子控件之间的间距
    private int mChildMargin = 0;
    //正常情况下显示的颜色
    private int mNormalColor = DEFAULT_NORMAL_COLOR;
    //选中的时候现实的颜色
    private int mSelectColor = DEFAULT_SELECT_COLOR;
    //正常情况下显示的drawable
    private Drawable mNormalDrawable = null;
    //选中情况下显示的drawable
    private Drawable mSelectDrawable = null;
    //是否显示圆点
    private boolean mIsCircle = true;
    //
    private PointCheckedChangeListener pointCheckedChangeListener;

    public AndSelectCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AndSelectCircleView);
        mChildWidth = attributes.getDimensionPixelSize(R.styleable.AndSelectCircleView_child_width, AndDensityUtils.dip2px(context, DEAFULT_CHILD_SIZE));
        mChildHeight = attributes.getDimensionPixelSize(R.styleable.AndSelectCircleView_child_height, AndDensityUtils.dip2px(context, DEAFULT_CHILD_SIZE));
        mChildMargin = attributes.getDimensionPixelSize(R.styleable.AndSelectCircleView_child_margin, AndDensityUtils.dip2px(context, DEFAULT_CHILD_MARGIN));
        mIsCircle = attributes.getBoolean(R.styleable.AndSelectCircleView_is_circle, true);
        mNormalColor = attributes.getColor(R.styleable.AndSelectCircleView_normal_color, DEFAULT_NORMAL_COLOR);
        mSelectColor = attributes.getColor(R.styleable.AndSelectCircleView_select_color, DEFAULT_SELECT_COLOR);
        attributes.recycle();
    }

    private Drawable getSpecialDrawable(boolean isNormal) {
        if (mIsCircle) {
            int width = Math.min(mChildHeight, mChildWidth);
            RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(getResources(), getCircleDrawableBitmap(isNormal, width));
            create.setCircular(true);
            return create;
        } else {
            ColorDrawable drawable = new ColorDrawable(isNormal ? mNormalColor : mSelectColor);
            drawable.setBounds(0, 0, mChildWidth, mChildHeight);
            return drawable;
        }
    }

    private Bitmap getCircleDrawableBitmap(boolean isNormal, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, width, Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(isNormal ? mNormalColor : mSelectColor);
        return bitmap;
    }

    public AndSelectCircleView(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mChildHeight = AndDensityUtils.dip2px(getContext(), DEAFULT_CHILD_SIZE);
        mChildWidth = AndDensityUtils.dip2px(getContext(), DEAFULT_CHILD_SIZE);
        mChildMargin = AndDensityUtils.dip2px(getContext(), DEFAULT_CHILD_MARGIN);
    }

    /**
     * 设置宽度
     *
     * @param mChildWidth
     * @return
     */
    public AndSelectCircleView setmChildWidth(int mChildWidth) {
        this.mChildWidth = mChildWidth;
        return this;
    }

    public AndSelectCircleView setmChildHeight(int mChildHeight) {
        this.mChildHeight = mChildHeight;
        return this;
    }

    public AndSelectCircleView setmIsCircle(boolean mIsCircle) {
        this.mIsCircle = mIsCircle;
        return this;
    }

    public AndSelectCircleView setmChildMargin(int mChildMargin) {
        this.mChildMargin = mChildMargin;
        return this;
    }

    public AndSelectCircleView setmNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
        return this;
    }

    public AndSelectCircleView setmSelectColor(int mSelectColor) {
        this.mSelectColor = mSelectColor;
        return this;
    }

    public AndSelectCircleView setPointCheckedChangeListener(PointCheckedChangeListener pointCheckedChangeListener) {
        this.pointCheckedChangeListener = pointCheckedChangeListener;
        return this;
    }

    /**
     * 添加子View
     *
     * @param count 添加的数量
     */
    @SuppressWarnings("deprecation")
    public void addChild(int count) {
        clear();
        if (count < 1) {
            throw new InvalidParameterException("count must be biger than 0");
        }
        mNormalDrawable = getSpecialDrawable(true);
        mSelectDrawable = getSpecialDrawable(false);
        for (int index = 0; index < count; index++) {
            RadioButton button = new RadioButton(getContext());
            button.setId(index);
            StateListDrawable drawable = new StateListDrawable();
            drawable.addState(new int[]{android.R.attr.state_checked}, mSelectDrawable);
            drawable.addState(new int[]{}, mNormalDrawable);
            button.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            button.setBackgroundDrawable(drawable);
            LayoutParams params = new LayoutParams(mChildWidth, mChildHeight);
            if (index == 0) {
                button.setChecked(true);
                params.leftMargin = 0;
            } else {
                button.setChecked(false);
                params.leftMargin = mChildMargin;
            }
            addView(button, index, params);
        }
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (pointCheckedChangeListener != null) {
                    pointCheckedChangeListener.checkedChange(checkedId);
                }
            }
        });
    }

    /**
     * 设置选中的位置
     *
     * @param position
     */
    public void setSelectPosition(int position) {
        if (position < getChildCount()) {
            RadioButton button = (RadioButton) getChildAt(position);
            button.setChecked(true);
        }
    }

    /**
     * 清除所有
     */
    public void clear() {
        removeAllViews();
    }

    public interface PointCheckedChangeListener {
        void checkedChange(int position);
    }

}
