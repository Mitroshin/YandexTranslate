package com.developgmail.mitroshin.yandextranslate.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.developgmail.mitroshin.yandextranslate.R;
import com.developgmail.mitroshin.yandextranslate.utility.YandexFetcher;

public class TranslateFragment extends Fragment {

    private View mViewLayout;
    private TextView mTextViewSourceLanguage;
    private EditText mEditTextSourceText;

    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewLayout = inflater.inflate(R.layout.fragment_translate, container, false);
        initializeLayout();
        return mViewLayout;
    }

    private void initializeLayout() {
        initializeTextViewSourceLanguage();
        initializeEditTextSourceText();
    }

    private void initializeTextViewSourceLanguage() {
        mTextViewSourceLanguage = (TextView) mViewLayout.findViewById(R.id.fragment_translate_text_view_source_language);
    }

    private void initializeEditTextSourceText() {
        mEditTextSourceText = (EditText) mViewLayout.findViewById(R.id.fragment_translate_edit_text_source_text);
        mEditTextSourceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new DetermineLanguageOfText().execute(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private class DetermineLanguageOfText extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String currentSourceText = params[0];
            if (currentSourceText.equals("")) {
                return null;
            } else {
                return new YandexFetcher().determineLanguageOfText(currentSourceText);
            }
        }

        @Override
        protected void onPostExecute(String sourceLanguage) {
            mTextViewSourceLanguage.setText(sourceLanguage);
        }
    }
}