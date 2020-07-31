
# GridViewPager3.x

GridViewPager3.0组件：采用RecycleView + FlexBoxLayout + PagerSnapHelper实现方式，轻松实现类似美团首页分类多页展示。也可用于表情面板的展示。
链式调用，属性配置，几行代码轻松搞定。

v3.3.0 开始采用 viewpager2 + FlexBoxLayout 实现

  ① 应用的首页经常需要用到这样的分类**多页展示**的效果，还有些消息输入框需要这样的**表情面板**。
  
  ② 既然是常用的，作为懒惰的我，肯定不会每次都去写一遍。网上也找了很多类似的例子，但始终不是我想要的**简洁接入**使用的方式。要么就是加载图片有限制，要么就是样式限制的太死，还得改源码，我不喜欢，我得造一个轮子。。。必须封装一个简单好用的组件，做到几行代码就可实现效果才行。于是乎，**GridViewPager组件**就诞生了。

#  GridViewPager组件效果

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020062920115094.gif#pic_center)

#  如何使用GridViewPager组件

# 1、在根目录 build.gradle 添加:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

# 2、在app项目下的build.gradle中添加：

[![](https://jitpack.io/v/mtjsoft/GridPager.svg)](https://jitpack.io/#mtjsoft/GridPager)

```
dependencies {
	        implementation 'com.github.mtjsoft:GridPager: v3.3.0'
	}
```


# 3、在需要使用的布局xml中添加GridViewPager组件，根据需要设置相关属性

```
    <cn.mtjsoft.www.gridviewpager_recycleview.GridViewPager
        android:id="@+id/gridviewpager"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:pager_MarginTop="10dp"
        app:pager_MarginBottom="0dp"
        app:verticalSpacing="10dp"
        app:img_width="44dp"
        app:img_height="44dp"
        app:text_color="@color/white"
        app:text_size="12sp"
        app:imgtext_margin="5dp"
        app:row_count="2"
        app:column_count="4"
        app:point_is_show="true"
        app:point_width="15dp"
        app:point_height="2dp"
        app:point_is_circle="false"
        app:point_margin="2dp"
        app:point_normal_color="@color/white"
        app:point_select_color="#f00"
        app:point_margin_page="10dp"
        app:point_margin_bottom="10dp"
        app:background_color="@color/white">
    </cn.mtjsoft.www.gridviewpager_recycleview.GridViewPager>

    <cn.mtjsoft.www.gridviewpager_recycleview.GridViewPager
        android:id="@+id/gridviewpager2"
        android:layout_marginTop="10dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </cn.mtjsoft.www.gridviewpager_recycleview.GridViewPager>
```
# 4、GridViewPager组件的版本及属性说明

V3.3.0
--------------------------
更换 viewpager2 + FlexBoxLayout 实现

V3.2.0
--------------------------
修复已知问题

V3.1.0
--------------------------
修复布局设置margin显示不全的问题


V3.0.0
--------------------------

3.0.0 属性  | 属性说明 
------------- | ------------- 
pager_MarginTop  | 设置每页的上边距 默认10dp 单位dp 
pager_MarginBottom | 设置每页的下边距 默认10dp 单位dp 
verticalSpacing  | 设置item的纵向间距 默认10dp 单位dp 
img_width  | 设置图片宽度 默认50dp 单位dp
img_height  | 设置图片高度 默认50dp 单位dp
text_color  | 设置文字颜色 默认黑色
imgtext_margin  | 设置文字与图片的间距 默认5dp 单位dp
text_size  | 设置文字大小 默认10sp 单位sp
row_count  | 设置每页行数 默认2
column_count  | 设置每页列数 默认4
point_is_show  | 是否展示指示器,默认true展示
point_width  | 设置指示器的item宽度 默认8dp 单位dp
point_height  | 设置指示器的item高度 默认8dp 单位dp
point_margin  | 设置指示器的item的间距 默认8dp 单位dp
point_normal_color  | 指示器item未选中的颜色 默认灰色
point_select_color  | 指示器item选中的颜色 默认红色
point_is_circle  | 指示器的item是否为圆形，默认圆形直径取宽高的最小值
point_margin_page  | 设置指示器与page的间距,默认是verticalSpacing的值
point_margin_bottom  | 设置指示器与底部的间距,默认是verticalSpacing的值
background_color  | 设置组件背景颜色，默认白色
notifyItemChanged(int position)  |  刷新指定页数据


# 5、代码实现。链式调用，只需要设置总数量即可。数据绑定完全自定义，不受任何图片加载框架限制，更加自由。

```
        GridViewPager gridViewPager = findViewById(R.id.gridviewpager);
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
```

# 6、代码中也可直接设置属性（如果xml与代码都设置了， 最终以代码设置为准）

```
        GridViewPager gridViewPager2 = findViewById(R.id.gridviewpager2);
        gridViewPager2
                // 设置数据总数量
                .setDataAllCount(titles.length)
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
                .setRowCount(2)
                // 设置每页列数
                .setColumnCount(5)
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
```
# 7、刷新数据

**设置新属性，刷新**

```
                gridViewPager
		        // 数据大小有变化时，得设置
                        .setDataAllCount(titles.length)
			// 动态改变其他属性
                        .setRowCount(3)
                        .setColumnCount(3)
			// 再次show()，gridViewPager不为空时，走notifyDataSetChanged()刷新数据
                        .show();
```

**刷新指定页码数据（每页行列数、数据总数不变，只有某个数据的值改变时使用）**

```
                // 页码从0开始，例如下面是只刷新第二页数据
                gridViewPager.notifyItemChanged(1)
```

## 实现就是如此简单

**添加我个人微信号交流，记得添加时备注一下哦**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200629201307276.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI4Nzc5MDgz,size_16,color_FFFFFF,t_70)

**本人公众号，也可关注一波，共同交流吧。**

![在这里插入图片描述](https://img-blog.csdnimg.cn/2019012509485178.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI4Nzc5MDgz,size_16,color_FFFFFF,t_70)

