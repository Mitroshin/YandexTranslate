package com.developgmail.mitroshin.yandextranslate.activity;

import android.support.v4.app.Fragment;

import com.developgmail.mitroshin.yandextranslate.fragment.TranslateFragment;

public class TranslateActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return TranslateFragment.newInstance();
    }
}