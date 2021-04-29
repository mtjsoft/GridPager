package cn.mtjsoft.www.gridviewpager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.AlignContent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Random;

import cn.mtjsoft.www.gridviewpager_recycleview.GridViewPager;
import cn.mtjsoft.www.gridviewpager_recycleview.transformer.TopOrDownPageTransformer;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private String[] titles = {"美食", "电影", "酒店住宿", "休闲娱乐", "外卖", "自助餐", "KTV", "机票/火车票", "周边游", "美甲美睫",
            "火锅", "生日蛋糕", "甜品饮品", "水上乐园", "汽车服务", "美发", "丽人", "景点", "足疗按摩", "运动健身", "健身", "超市", "买菜",
            "今日新单", "小吃快餐", "面膜", "洗浴/汗蒸", "母婴亲子", "生活服务", "婚纱摄影", "学习培训", "家装", "结婚", "全部分配"};

    private int[] iconS = new int[titles.length];

    private int rowCount = 3;
    private int columnCount = 4;
    // 指定刷新某一页数据
    private int page = 0;

    //
    private GridViewPager gridViewPager;
    private TextView tv_row;
    private TextView tv_column;
    private TextView tv_page;


    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();


        gridViewPager = findViewById(R.id.gridviewpager);
        gridViewPager
                // 设置数据总数量
                .setDataAllCount(18)
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
                // 设置是否显示文本
                .setIsShowText(true)
                // 设置子VIEW对齐方式
                .setAlignContent(AlignContent.FLEX_START)
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
                // 设置无限循环
                .setPageLoop(true)
                // 设置是否显示指示器
                .setPointIsShow(true)
                // 设置指示器与page的间距
                .setPointMarginPage(0)
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
                        imageView.setImageResource(iconS[position]);
                        if (textView != null) {
                            textView.setText(titles[position]);
                        }
                    }
                })
                // Item点击
                .setGridItemClickListener(new GridViewPager.GridItemClickListener() {
                    @Override
                    public void click(int position) {
                        Toast.makeText(getBaseContext(), "点击了" + titles[position].split("_")[0], Toast.LENGTH_SHORT).show();
                    }
                })
                // 设置Item长按
                .setGridItemLongClickListener(new GridViewPager.GridItemLongClickListener() {
                    @Override
                    public void longClick(int position) {
                        Toast.makeText(getBaseContext(), "长按了" + titles[position].split("_")[0], Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

        //
        tv_row = findViewById(R.id.tv_row);
        tv_column = findViewById(R.id.tv_column);
        tv_page = findViewById(R.id.tv_page);
        SeekBar sb_row = findViewById(R.id.sb_row);
        SeekBar sb_column = findViewById(R.id.sb_column);
        SeekBar sb_page = findViewById(R.id.sb_page);
        sb_row.setOnSeekBarChangeListener(this);
        sb_column.setOnSeekBarChangeListener(this);
        sb_page.setOnSeekBarChangeListener(this);

        // 刷新
        Button button = findViewById(R.id.btu_page);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 改变数据
                int x = random.nextInt(5);
                for (int i = 0; i < titles.length; i++) {
                    titles[i] = titles[i].split("_")[0] + "_" + x;
                }
                // 刷新
                gridViewPager.setDataAllCount(titles.length).setRowCount(rowCount).setColumnCount(columnCount).show();
            }
        });
        // 刷新指定的第page页
        Button button2 = findViewById(R.id.btu_page2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageSize = gridViewPager.getOnePageSize();
                int x = random.nextInt(5) + 5;
                // 改变第page页的数据
                for (int i = 0; i < titles.length; i++) {
                    if (i >= pageSize * page && i < pageSize * (page + 1)) {
                        titles[i] = titles[i].split("_")[0] + "_" + x;
                    }
                }
                // 刷新第page页的数据
                gridViewPager.notifyItemChanged(page);
            }
        });

        // 测试翻页效果
        final GridViewPager gridViewPager2 = findViewById(R.id.gridviewpager2);
        gridViewPager2
                // 设置数据总数量
                .setDataAllCount(titles.length)
                // 设置内置的覆盖翻页效果
                .setCoverPageTransformer()
                // 设置内置的上下进入效果
//                .setTopOrDownPageTransformer(TopOrDownPageTransformer.ModeType.MODE_DOWN)
                // 设置内置的画廊效果
//                .setGalleryPageTransformer()
                // 数据绑定
                .setImageTextLoaderInterface(new GridViewPager.ImageTextLoaderInterface() {
                    @Override
                    public void displayImageText(ImageView imageView, TextView textView, int position) {
                        // 自己进行数据的绑定，灵活度更高，不受任何限制
                        imageView.setImageResource(iconS[position]);
                        textView.setText(titles[position].split("_")[0]);
                    }
                })
                // Item点击
                .setGridItemClickListener(new GridViewPager.GridItemClickListener() {
                    @Override
                    public void click(int position) {
                        Toast.makeText(getBaseContext(), "点击了" + titles[position].split("_")[0], Toast.LENGTH_SHORT).show();
                    }
                })
                // 设置Item长按
                .setGridItemLongClickListener(new GridViewPager.GridItemLongClickListener() {
                    @Override
                    public void longClick(int position) {
                        Toast.makeText(getBaseContext(), "长按了" + titles[position].split("_")[0], Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

        // 测试列表
        Button button3 = findViewById(R.id.btu_list);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ListActivity.class));
            }
        });

        // 测试嵌套滑动
        findViewById(R.id.btu_list2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), CoordinatorActivity.class));
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
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.sb_row:
                if (i < 1) {
                    Toast.makeText(getBaseContext(), "最少设置一行", Toast.LENGTH_SHORT).show();
                } else {
                    tv_row.setText("设置行数：" + i);
                    rowCount = i;
                }
                break;
            case R.id.sb_column:
                if (i < 1) {
                    Toast.makeText(getBaseContext(), "最少设置一列", Toast.LENGTH_SHORT).show();
                } else {
                    tv_column.setText("设置列数：" + i);
                    columnCount = i;
                }
                break;
            case R.id.sb_page:
                if (i > gridViewPager.getPageSize() - 1) {
                    Toast.makeText(getBaseContext(), "超出页码", Toast.LENGTH_SHORT).show();
                } else {
                    tv_page.setText("指定刷新页：" + i);
                    page = i;
                }
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
