package cn.mtjsoft.www.gridviewpager_recycleview.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 页面画廊效果
 */
public class GalleryPageTransformer implements ViewPager2.PageTransformer {
    //初始
    private static final float MIN_SCALE = 0.85f;

    @Override
    public void transformPage(@NonNull View view, float position) {
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        float rotate = 25 * Math.abs(position);
        if (position > 0) {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setRotationY(-rotate);
        } else {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setRotationY(rotate);
        }
    }
}
