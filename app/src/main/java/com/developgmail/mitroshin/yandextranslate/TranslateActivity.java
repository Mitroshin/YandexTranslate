package com.developgmail.mitroshin.yandextranslate;

import android.support.v4.app.Fragment;

public class TranslateActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return TranslateFragment.newInstance();
    }
}