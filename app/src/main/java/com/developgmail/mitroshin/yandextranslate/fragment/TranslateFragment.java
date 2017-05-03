package com.developgmail.mitroshin.yandextranslate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developgmail.mitroshin.yandextranslate.R;

public class TranslateFragment extends Fragment {

    private View mViewLayout;

    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewLayout = inflater.inflate(R.layout.fragment_translate, container, false);
        return mViewLayout;
    }
}