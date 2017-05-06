package com.developgmail.mitroshin.yandextranslate.activity;

import android.support.v4.app.Fragment;

import com.developgmail.mitroshin.yandextranslate.fragment.ChooseLanguageFragment;

public class ChooseLanguageActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return ChooseLanguageFragment.newInstance();
    }
}
