# RegulatorView

![image](https://github.com/mochixuan/RegulatorView/blob/master/RegulatorMaster/img/main.jpg)
![image](https://github.com/mochixuan/RegulatorView/blob/master/RegulatorMaster/img/slide.gif)
![image](https://github.com/mochixuan/RegulatorView/blob/master/RegulatorMaster/img/sweep.gif)

## 1. 主要功能
>- 背光灯渐变
>- 背光灯调色
>- 控制环的颜色
>- 控制环形的度数
>- 平滑实现调节
>- 里面所以参数都可以微调

## 2. 用途
>- 空调遥控器
>- 各种挡位调节器
>- 热水器调节器

## 3. 使用

> 主要接受一下圆命名大家就知道什么意思了从中心开始一共三个圆分别是 第一个圆 实心圆:firstCircle 第二个圆环: secondCircle 第三个圆:环形彩色圆弧: threeCircle

#####  attrs
```
      <attr name="three_circle_radius" format="dimension"/>
        
        <attr name="first_circle_color" format="color"/>
        <attr name="second_circle_color" format="color"/>
        <attr name="secondcircle_shadow_color" format="color"/>
        <attr name="three_circle_color" format="color"/>
        
        <attr name="second_circle_width" format="dimension"/>
        <attr name="secondcircle_shadow_width" format="dimension"/>
        <attr name="three_circle_width" format="dimension"/>
        <attr name="gap1_width" format="dimension"/>
        
        <attr name="three_ring_angle" format="integer"/>
        <attr name="second_scale" format="float"/>
        
        <attr name="pointer_color" format="color"/>
        <attr name="pointer_scale" format="float"/>
        <attr name="pointer_width" format="dimension"/>

        <attr name="symbol" format="string"/>
        <attr name="center_title" format="string"/>
        <attr name="bottom_title" format="string"/>

        <attr name="symbol_color" format="color"/>
        <attr name="center_title_color" format="color"/>
        <attr name="bottom_title_color" format="color"/>

        <attr name="symbol_size" format="dimension"/>
        <attr name="center_title_size" format="dimension"/>
        <attr name="bottom_title_size" format="dimension"/>

        <attr name="bottomcenter_gap" format="dimension"/>
        <attr name="symbolcenter_gap" format="dimension"/>
        <attr name="symbolmovetop_gap" format="dimension"/>

        <attr name="backlight_durtion" format="integer"/>
        <attr name="isopen_backlightanim" format="boolean"/>
        <attr name="is_forbid_slide" format="boolean"/>
```

##### xml 
```
<!--其他参数自己看名字就知道，可以实现微调每一个参数-->
<com.wx.regulatorview.RegulatorView
    android:id="@+id/regulator_view"
    android:layout_width="match_parent"
    android:layout_height="360dp"
    app:symbol="°"
    app:center_title="10"
    app:bottom_title="Auto" />
```

##### java

```
mRegulatorView = findViewById(R.id.regulator_view);
 mColors = new int[]{0xff2ab62d,0xff2ab62d, 0xff56f318, 0xff8ff318, 0xffd2f318, 0xfff3b318, 0xfff36a18, 0xffe73046, 0xffff0000,0xffff0000};
mRegulatorView.setThreeCircleColors(mColors);
mRegulatorView.setProgressChangeListener(new OnProgressChangeListener() {
     @Override
     public void onProgress(float progress) {
                
     }
});
```

## 4.其他

这里没有将代码上传到jcenter，如果你项目需要引用应该也要小范围修改，而且代码其实就一个核心类，项目引用直接粘贴更合适。


