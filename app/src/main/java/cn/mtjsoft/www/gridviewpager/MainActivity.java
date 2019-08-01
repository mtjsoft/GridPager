package cn.mtjsoft.www.gridviewpager;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.mtjsoft.www.gridpager.GridPager;

public class MainActivity extends AppCompatActivity {

    private String[] titles = {"美食", "电影", "酒店住宿", "休闲娱乐", "外卖", "自助餐", "KTV", "机票/火车票", "周边游", "美甲美睫",
            "火锅", "生日蛋糕", "甜品饮品", "水上乐园", "汽车服务", "美发", "丽人", "景点", "足疗按摩", "运动健身", "健身", "超市", "买菜",
            "今日新单", "小吃快餐", "面膜", "洗浴/汗蒸", "母婴亲子", "生活服务", "婚纱摄影", "学习培训", "家装", "结婚", "全部分配"};

    private int[] iconS = new int[titles.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        GridPager gridPager = findViewById(R.id.gridpager);

        gridPager
                // 设置item的纵向间距
                .setVerticalSpacing(20)
                // 设置图片宽度
                .setImageWidth(80)
                // 设置图片高度
                .setImageHeight(80)
                // 设置文字与图片的间距
                .setTextImgMargin(10)
                // 设置文字颜色
                .setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimaryDark))
                // 设置文字大小
                .setTextSize(12)
                // 设置每页行数
                .setRowCount(2)
                // 设置每页列数
                .setColumnCount(4)
                // 设置数量总条数
                .setDataAllCount(titles.length)
                // 设置指示器的item宽度
                .setPointChildWidth(15)
                // 设置指示器的item高度
                .setPointChildHeight(3)
                // 设置指示器的item的间距
                .setPointChildMargin(5)
                // 指示器的item是否为圆形，默认圆形直径取宽高的最小值
                .setPointIsCircle(false)
                // 指示器item未选中的颜色
                .setPointNormalColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary))
                // 指示器item选中的颜色
                .setPointSelectColor(ContextCompat.getColor(getBaseContext(),R.color.colorAccent))
                // 数据绑定
                .setItemBindDataListener(new GridPager.ItemBindDataListener() {
                    @Override
                    public void BindData(ImageView imageView, TextView textView, int position) {
                        // 自己进行数据的绑定，灵活度更高，不受任何限制
                        imageView.setImageResource(iconS[position]);
                        textView.setText(titles[position]);
                    }
                })
                // Item点击
                .setGridItemClickListener(new GridPager.GridItemClickListener() {
                    @Override
                    public void click(int position) {
                        Toast.makeText(getBaseContext(), "点击了" + titles[position], Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
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
}
