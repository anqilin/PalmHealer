package com.moxi.palmhealer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoRelativeLayout;


/**
 * 闪屏页，对应进入应用程序首页
 */

public class SplashActivity extends Activity implements View.OnClickListener {
    ImageView splashPic;
    TextView splashEnter;
    Handler mHandler = new Handler();
    Context context;
    AutoFrameLayout activity_splash_content;
    AutoRelativeLayout splash_enter_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        setContentView(R.layout.activity_splash_main);
        initView();
        mHandler.postDelayed(runnable, 2000);
    }

    private void initView() {
        activity_splash_content = (AutoFrameLayout) findViewById(R.id.activity_splash_content);
        splash_enter_page = (AutoRelativeLayout) View.inflate(this, R.layout.activity_splash, null);

    }

    private void initView2() {
        splashPic = (ImageView) findViewById(R.id.splashPic);
        splashEnter = (TextView) findViewById(R.id.splashEnter);
        splashEnter.setOnClickListener(this);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //启动页修改，先显示一个页面，2秒后进入含有点击进入的页面
            AutoRelativeLayout.LayoutParams prams = new AutoRelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            activity_splash_content.removeAllViews();
            activity_splash_content.addView(splash_enter_page, prams);
            initView2();
        }
    };


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.splashEnter:
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                mHandler.removeCallbacks(runnable);
                finish();
                break;
            default:
                break;
        }
    }


}
