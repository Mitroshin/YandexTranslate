package com.developgmail.mitroshin.yandextranslate.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developgmail.mitroshin.yandextranslate.fragment.ChooseLanguageFragment;

public class ChooseLanguageActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return ChooseLanguageFragment.newInstance();
    }

    public static Intent newIntent(Context pachageContext) {
        Intent intent = new Intent(pachageContext, ChooseLanguageActivity.class);
        return intent;
    }
}
