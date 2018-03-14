package com.moxi.palmhealer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.widget.VerticalSeekBar;


/**
 * Created by yinlu on 2016/4/16.
 */
public class TestActivity extends Activity {
    private final static String TAG = "--TestActivity---";
    TextView controler_bar_forhead;
    TextView main_bar_tx;
    float percent;
    VerticalSeekBar bar_time;
    float bar_time_height_percent;
    boolean isReceiveData = true;
    TextView time_float_mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        controler_bar_forhead = (TextView) findViewById(R.id.controler_bar_forhead);
        main_bar_tx = (TextView) findViewById(R.id.main_bar_tx);
    }


    @Override
    protected void onResume() {
        super.onResume();


        main_bar_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) controler_bar_forhead.getLayoutParams();
                params2.setMargins(0, 285, 0, 0);
                controler_bar_forhead.setLayoutParams(params2);

            }
        });

//        time_float_mark = (TextView) findViewById(R.id.time_float_mark);
//        bar_time = (VerticalSeekBar) findViewById(R.id.controler_bar_time);
//        ViewTreeObserver vto = bar_time.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                bar_time.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                int height = bar_time.getHeight();
//                bar_time_height_percent = height / 100;
//                int weight = bar_time.getWidth();
//                LogUtils.debug(TAG, "高度为=" + height + ",宽度为=" + weight);
//
//            }
//        });
//
//
//        bar_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                isReceiveData = false;
//                LogUtils.debug(TAG, "当前进度" + progress + "%");
//                LogUtils.debug(TAG, "当前margin" + (int) (bar_time_height_percent * progress));
//
//
//                AutoRelativeLayout.LayoutParams params2 = (AutoRelativeLayout.LayoutParams) time_float_mark.getLayoutParams();
//                params2.setMargins(296, 680 - (int) (bar_time_height_percent * progress), 0, 0);
//                time_float_mark.setLayoutParams(params2);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                isReceiveData = false;
//                LogUtils.debug(TAG, "开始拖动");
//
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                isReceiveData = false;
//                LogUtils.debug(TAG, "拖动停止");
//            }
//        });
//
    }


}
