package cn.mtjsoft.www.gridviewpager_recycleview.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 页面上下进入效果
 */
public class TopOrDownPageTransformer implements ViewPager2.PageTransformer {
    //初始
    private static final float MIN_SCALE = 0.5f;

    private ModeType modeType;

    public TopOrDownPageTransformer(ModeType modeType) {
        this.modeType = modeType;
    }

    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        if (position <= 0) { // [-1,0]
            view.setTranslationX(0);
            view.setTranslationY(0);
            view.setAlpha(1 + position);
            view.setScaleX(1);
            view.setScaleY(1);
        } else { // (0,1]
            float SCALE = 0.5f - position / 2;
            view.setScaleX(MIN_SCALE + SCALE);
            view.setScaleY(MIN_SCALE + SCALE);
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
            if (modeType != null) {
                if (modeType.getType() == ModeType.MODE_TOP.getType()) {
                    view.setTranslationY(pageHeight * -position);
                } else {
                    view.setTranslationY(pageHeight * position);
                }
            }
        }
        view.setVisibility(position == 1 || position == -1 ? View.GONE : View.VISIBLE);
    }


    public enum ModeType {

        MODE_TOP(1, "自上而下"),
        MODE_DOWN(2, "自下而上");

        private int type;
        private String name;

        ModeType(int type, String name) {
            this.type = type;
            this.name = name;
        }


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
