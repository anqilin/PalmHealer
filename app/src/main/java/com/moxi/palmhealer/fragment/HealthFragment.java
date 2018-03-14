package com.moxi.palmhealer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxi.palmhealer.R;
import com.moxi.palmhealer.utils.XmlParser;

/**
 * Created by yinlu on 2016/6/29.
 */
public class HealthFragment extends BaseFragment implements View.OnClickListener {
    ImageView commonDisease;
    ImageView acupuncture;
    DiseaseFragment diseaseFragment;
    AcupunctureListFragment acupunctureListFragment;
    private TextView main_bar_tx;
    private ImageView left_back;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.disease_acupuncture_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Fragment target = getActivity().getSupportFragmentManager().findFragmentByTag("AcupunctureListFragment");
        if (target == null) {
            acupunctureListFragment = new AcupunctureListFragment();
        } else {
            acupunctureListFragment = (AcupunctureListFragment) target;
        }

        Fragment diseaseFragmenttarget = getActivity().getSupportFragmentManager().findFragmentByTag("DiseaseFragment");
        if (diseaseFragmenttarget == null) {
            diseaseFragment = new DiseaseFragment();
        } else {
            diseaseFragment = (DiseaseFragment) diseaseFragmenttarget;
        }

        if (XmlParser.pointMap.isEmpty()) {
            try {
                XmlParser.getInstance().parsePoint(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initView();
    }

    private void initView() {
        commonDisease = (ImageView) getView().findViewById(R.id.common_disease_bt);
        acupuncture = (ImageView) getView().findViewById(R.id.acupuncture_bt);
        commonDisease.setOnClickListener(this);
        acupuncture.setOnClickListener(this);

        main_bar_tx = (TextView) getView().findViewById(R.id.main_bar_tx);
        main_bar_tx.setText("养生所");
        left_back = (ImageView) getView().findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.common_disease_bt:
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                transaction.replace(R.id.health_maincontent, diseaseFragment, "DiseaseFragment");
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.acupuncture_bt:
                FragmentTransaction acupuntransaction = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.right_in, R.anim.left_out);
                acupuntransaction.replace(R.id.health_maincontent, acupunctureListFragment, "AcupunctureListFragment");
                acupuntransaction.addToBackStack(null);
                acupuntransaction.commit();
                break;
            case R.id.left_back:
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}


