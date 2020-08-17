package cn.mtjsoft.www.gridviewpager_recycleview.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 页面覆盖效果
 */
public class CoverPageTransformer implements ViewPager2.PageTransformer {
    //初始
    private static final float MIN_SCALE = 0.5f;

    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();
        if (position <= 0) { // [-1,0]
            view.setTranslationX(0);
            view.setAlpha(1 + position);
            view.setScaleX(1);
            view.setScaleY(1);
        } else { // (0,1]
            float SCALE = 0.5f - position / 2;
            view.setScaleX(MIN_SCALE + SCALE);
            view.setScaleY(MIN_SCALE + SCALE);
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
        }
        view.setVisibility(position == 1 || position == -1 ? View.GONE : View.VISIBLE);
    }
}
