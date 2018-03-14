package com.moxi.palmhealer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moxi.palmhealer.R;

/**
 * Created by yinlu on 2016/6/4.
 */
public class ConsultFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.consult_layout, container, false);
    }
}
