package cn.mtjsoft.www.gridviewpager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import cn.mtjsoft.www.gridviewpager_recycleview.GridViewPager;

public class MainActivity extends AppCompatActivity {

    private String[] titles = {"美食", "电影", "酒店住宿", "休闲娱乐", "外卖", "自助餐", "KTV", "机票/火车票", "周边游", "美甲美睫",
            "火锅", "生日蛋糕", "甜品饮品", "水上乐园", "汽车服务", "美发", "丽人", "景点", "足疗按摩", "运动健身", "健身", "超市", "买菜",
            "今日新单", "小吃快餐", "面膜", "洗浴/汗蒸", "母婴亲子", "生活服务", "婚纱摄影", "学习培训", "家装", "结婚", "全部分配"};

    private int[] iconS = new int[titles.length];

    private String[] titles2 = new String[titles.length];
    private int[] iconS2 = new int[titles.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        final GridViewPager gridViewPager = findViewById(R.id.gridviewpager);
        gridViewPager
                // 设置数据总数量
                .setDataAllCount(titles.length)
                // 设置背景图片(此时设置的背景色无效，以背景图片为主)
                .setBackgroundImageLoader(new GridViewPager.BackgroundImageLoaderInterface() {
                    @Override
                    public void setBackgroundImg(ImageView bgImageView) {
                        bgImageView.setImageResource(R.drawable.ic_launcher_background);
                    }
                })
                // 数据绑定
                .setImageTextLoaderInterface(new GridViewPager.ImageTextLoaderInterface() {
                    @Override
                    public void displayImageText(ImageView imageView, TextView textView, int position) {
                        // 自己进行数据的绑定，灵活度更高，不受任何限制
                        imageView.setImageResource(iconS[position]);
                        textView.setText(titles[position]);
                    }
                })
                // Item点击
                .setGridItemClickListener(new GridViewPager.GridItemClickListener() {
                    @Override
                    public void click(int position) {
                        Toast.makeText(getBaseContext(), "点击了" + titles[position] + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

        // 刷新
        Button button = findViewById(R.id.btu_page);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 改变数据
                for (int i = 0; i < titles.length; i++) {
                    titles[i] = titles[i] + "_1";
                }
                // 刷新
                gridViewPager.setDataAllCount(titles.length).setRowCount(3).setColumnCount(3).show();
            }
        });

        /**
         * 指定刷新页
         */
        int rowCount = 2;
        int columnCount = 5;
        final GridViewPager gridViewPager2 = findViewById(R.id.gridviewpager2);
        gridViewPager2
                // 设置数据总数量
                .setDataAllCount(titles2.length)
                // 设置背景色，默认白色
                .setGridViewPagerBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                // 设置item的纵向间距
                .setVerticalSpacing(10)
                // 设置上边距
                .setPagerMarginTop(10)
                // 设置下边距
                .setPagerMarginBottom(10)
                // 设置图片宽度
                .setImageWidth(50)
                // 设置图片高度
                .setImageHeight(50)
                // 设置文字与图片的间距
                .setTextImgMargin(5)
                // 设置文字颜色
                .setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                // 设置文字大小
                .setTextSize(12)
                // 设置每页行数
                .setRowCount(rowCount)
                // 设置每页列数
                .setColumnCount(columnCount)
                // 设置是否显示指示器
                .setPointIsShow(true)
                // 设置指示器与page的间距
                .setPointMarginPage(10)
                // 设置指示器与底部的间距
                .setPointMarginBottom(10)
                // 设置指示器的item宽度
                .setPointChildWidth(8)
                // 设置指示器的item高度
                .setPointChildHeight(8)
                // 设置指示器的item的间距
                .setPointChildMargin(8)
                // 指示器的item是否为圆形，默认圆形直径取宽高的最小值
                .setPointIsCircle(true)
                // 指示器item未选中的颜色
                .setPointNormalColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                // 指示器item选中的颜色
                .setPointSelectColor(ContextCompat.getColor(getBaseContext(), R.color.black_text))
                // 设置背景图片(此时设置的背景色无效，以背景图片为主)
                .setBackgroundImageLoader(new GridViewPager.BackgroundImageLoaderInterface() {
                    @Override
                    public void setBackgroundImg(ImageView bgImageView) {
                        bgImageView.setImageResource(R.drawable.ic_launcher_background_red);
                    }
                })
                // 数据绑定
                .setImageTextLoaderInterface(new GridViewPager.ImageTextLoaderInterface() {
                    @Override
                    public void displayImageText(ImageView imageView, TextView textView, int position) {
                        // 自己进行数据的绑定，灵活度更高，不受任何限制
                        imageView.setImageResource(iconS2[position]);
                        textView.setText(titles2[position]);
                    }
                })
                // Item点击
                .setGridItemClickListener(new GridViewPager.GridItemClickListener() {
                    @Override
                    public void click(int position) {
                        Toast.makeText(getBaseContext(), "点击了" + titles2[position] + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

        // 刷新指定的第二页
        final int pageSize = columnCount * rowCount;
        Button button2 = findViewById(R.id.btu_page2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 改变第二页的数据
                for (int i = 0; i < titles2.length; i++) {
                    if (i >= pageSize && i < pageSize * 2) {
                        titles2[i] = titles2[i] + "_2";
                    }
                }
                // 刷新第二页的数据
                gridViewPager2.notifyItemChanged(1);
            }
        });


        Button button3 = findViewById(R.id.btu_list);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ListActivity.class));
            }
        });
    }


    /**
     * 初始化数据源，这里使用本地图标作为示例
     */
    private void initData() {
        for (int i = 0; i < titles.length; i++) {
            //动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
            int imageId = getResources().getIdentifier("ic_category_" + i, "mipmap", getPackageName());
            iconS[i] = imageId;
            //
            titles2[i] = titles[i];
            iconS2[i] = imageId;
        }
    }
}
