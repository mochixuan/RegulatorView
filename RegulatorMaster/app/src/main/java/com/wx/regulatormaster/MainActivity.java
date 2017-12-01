package com.wx.regulatormaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wx.regulatorview.OnProgressChangeListener;
import com.wx.regulatorview.RegulatorView;

public class MainActivity extends AppCompatActivity {

    private RegulatorView mRegulatorView;
    private String TAG = this.getClass().getSimpleName();
    private int mTemperature = 10;
    private int mColorIndex = 1;
    private int[] mColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegulatorView = findViewById(R.id.regulator_view);
        mColors = new int[]{0xff2ab62d,0xff2ab62d, 0xff56f318, 0xff8ff318, 0xffd2f318, 0xfff3b318, 0xfff36a18, 0xffe73046, 0xffff0000,0xffff0000};
        mRegulatorView.setThreeCircleColors(mColors);
        mRegulatorView.setSecondCircleShadowColor(mColors[mColorIndex]);
        mRegulatorView.setPointerColor(mColors[mColorIndex]);
        mRegulatorView.setIsOpenBacklightAnim(true);
        mRegulatorView.setProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onProgress(float progress) {
                int value = (int) (10 + 32*progress);
                if (mTemperature != value) {
                    mTemperature = value;
                    mRegulatorView.setCenterTitle(mTemperature+"");
                    int index = Math.round(progress*7+1);
                    if (mColorIndex != index) {
                        mColorIndex = index;
                        mRegulatorView.setSecondCircleShadowColor(mColors[mColorIndex]);
                        mRegulatorView.setPointerColor(mColors[mColorIndex]);
                    }
                }
            }
        });

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegulatorView.setCurAngle(0f,true);
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegulatorView.setCurAngle(1f,true);
            }
        });

        final Button button3 = findViewById(R.id.btn_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRegulatorView.isForbidSlide()) {
                    button3.setText("禁止滑动操控");
                    mRegulatorView.setIsForbidSlide(false);
                } else {
                    button3.setText("打开滑动操控");
                    mRegulatorView.setIsForbidSlide(true);
                }
            }
        });

    }
}
