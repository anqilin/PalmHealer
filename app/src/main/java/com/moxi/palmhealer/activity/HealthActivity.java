package com.moxi.palmhealer.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.fragment.HealthFragment;

/**
 * Created by yinlu on 2016/6/7.
 */
public class HealthActivity extends AppCompatActivity {
    HealthFragment healthFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_main_layout);
        initView();
    }

    private void initView() {
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);


        Fragment target = getSupportFragmentManager().findFragmentByTag("HealthFragment");
        if (target == null) {
            healthFragment = new HealthFragment();
        } else {
            healthFragment = (HealthFragment) target;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.health_maincontent, healthFragment, "HealthFragment");
        transaction.commit();
    }

}
