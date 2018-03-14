package com.moxi.palmhealer.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;

import com.moxi.palmhealer.utils.LogUtils;


/**
 * Created by yinlu on 2016/4/21.
 */
public class BaseFragment extends Fragment {


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.debug(this.getClass().getSimpleName() + "-----onHiddenChanged");
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        LogUtils.debug(this.getClass().getSimpleName() + "-----startActivityForResult");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.debug(this.getClass().getSimpleName() + "-----onActivityResult");
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        LogUtils.debug(this.getClass().getSimpleName() + "-----onInflate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.debug(this.getClass().getSimpleName() + "-----onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.debug(this.getClass().getSimpleName() + "-----onCreate");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.debug(this.getClass().getSimpleName() + "-----onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.debug(this.getClass().getSimpleName() + "-----onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.debug(this.getClass().getSimpleName() + "-----onResume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.debug(this.getClass().getSimpleName() + "-----onSaveInstanceState");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.debug(this.getClass().getSimpleName() + "-----onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.debug(this.getClass().getSimpleName() + "-----onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.debug(this.getClass().getSimpleName() + "-----onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.debug(this.getClass().getSimpleName() + "-----onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.debug(this.getClass().getSimpleName() + "-----onDetach");
    }


}
