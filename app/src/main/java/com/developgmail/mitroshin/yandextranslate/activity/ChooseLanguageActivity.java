package com.developgmail.mitroshin.yandextranslate.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developgmail.mitroshin.yandextranslate.fragment.ChooseLanguageFragment;

import java.util.List;

public class ChooseLanguageActivity extends SingleFragmentActivity {
    private static final String EXTRA_ARRAY_OF_LANGUAGES = "com.developgmail.mitroshin.yandextranslate.activity.languages";

    public static Intent newIntent(Context packageContext, List<String> listOfLanguages) {
        Intent intent = new Intent(packageContext, ChooseLanguageActivity.class);
        String[] arrayOfLanguages = new String[listOfLanguages.size()];
        arrayOfLanguages = listOfLanguages.toArray(arrayOfLanguages);
        intent.putExtra(EXTRA_ARRAY_OF_LANGUAGES, arrayOfLanguages);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String[] arrayOfLanguages = (String[]) getIntent().getSerializableExtra(EXTRA_ARRAY_OF_LANGUAGES);
        return ChooseLanguageFragment.newInstance(arrayOfLanguages);
    }
}
