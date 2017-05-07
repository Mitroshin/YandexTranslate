package com.developgmail.mitroshin.yandextranslate.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.developgmail.mitroshin.yandextranslate.R;
import com.developgmail.mitroshin.yandextranslate.activity.ChooseLanguageActivity;
import com.developgmail.mitroshin.yandextranslate.utility.YandexFetcher;

import java.util.HashMap;
import java.util.Map;

public class TranslateFragment extends Fragment {
    private static final String TAG = "TranslateFragment";

    private static final int REQUEST_RESULT_LANGUAGE = 0;

    private Map<String, String> mGlobalLanguageGroup = new HashMap<>();

    private View mViewLayout;
    private TextView mTextViewSourceLanguage;
    private EditText mEditTextSourceText;
    private Button mButtonChooseResultLanguage;

    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new ListOfLanguages().execute();
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
        initializeButtonChooseResultLanguage();
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
        protected void onPostExecute(String codeOfSourceLanguage) {
            String nameOfSourceLanguage = mGlobalLanguageGroup.get(codeOfSourceLanguage);
            mTextViewSourceLanguage.setText(nameOfSourceLanguage);
        }
    }

    private void initializeButtonChooseResultLanguage() {
        mButtonChooseResultLanguage = (Button) mViewLayout.findViewById(R.id.fragment_translate_button_choose_result_language);
        mButtonChooseResultLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchChooseLanguageFragment();
            }
        });
    }

    private void launchChooseLanguageFragment() {
        Intent intent = ChooseLanguageActivity.newIntent(getActivity());
        startActivityForResult(intent, REQUEST_RESULT_LANGUAGE);
    }

    private class ListOfLanguages extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return new YandexFetcher().getLanguageGroup();
        }

        @Override
        protected void onPostExecute(Map<String, String> languageGroup) {
            mGlobalLanguageGroup = languageGroup;
        }
    }
}