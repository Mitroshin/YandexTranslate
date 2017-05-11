package com.developgmail.mitroshin.yandextranslate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developgmail.mitroshin.yandextranslate.fragment.ChooseLanguageFragment;

import java.util.List;

public class ChooseLanguageActivity extends SingleFragmentActivity implements ChooseLanguageFragment.Callbacks {
    private static final String EXTRA_ARRAY_OF_LANGUAGES = "com.developgmail.mitroshin.yandextranslate.activity.languages";
    private static final String EXTRA_RESULT_LANGUAGE = "com.developgmail.mitroshin.yandextranslate.activity.result_language";

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

    @Override
    public void sendResultLanguage(String resultLanguage) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT_LANGUAGE, resultLanguage);
        this.setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public static String getLanguageFromResultIntent(Intent intent) {
        return intent.getStringExtra(EXTRA_RESULT_LANGUAGE);
    }
}
