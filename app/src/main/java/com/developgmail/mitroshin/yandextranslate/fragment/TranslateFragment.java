package com.developgmail.mitroshin.yandextranslate.fragment;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.developgmail.mitroshin.yandextranslate.R;
import com.developgmail.mitroshin.yandextranslate.activity.ChooseLanguageActivity;
import com.developgmail.mitroshin.yandextranslate.utility.YandexFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslateFragment extends Fragment {
    private static final String TAG = "TranslateFragment";
    private static final String YANDEX_TRANSLATOR_URL = "http://translate.yandex.ru/";

    private static final int REQUEST_RESULT_LANGUAGE = 0;

    private Map<String, String> mGlobalLanguageGroup = new HashMap<>();
    private String currentResultLanguageCode;

    private View mViewLayout;
    private TextView mTextViewSourceLanguage;
    private TextView mTextViewResultLanguage;
    private TextView mTextViewResultText;
    private EditText mEditTextSourceText;
    private Button mButtonChooseResultLanguage;
    private Button mButtonYandexTranslator;

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
        initializeTextViewResultLanguage();
        initializeTextViewResultText();
        initializeEditTextSourceText();
        initializeButtonChooseResultLanguage();
        initializeButtonYandexTranslator();
    }

    private void initializeTextViewSourceLanguage() {
        mTextViewSourceLanguage = (TextView) mViewLayout.findViewById(R.id.fragment_translate_text_view_source_language);
    }

    private void initializeTextViewResultLanguage() {
        mTextViewResultLanguage = (TextView) mViewLayout.findViewById(R.id.fragment_translate_text_view_result_language);
    }

    private void initializeTextViewResultText() {
        mTextViewResultText = (TextView) mViewLayout.findViewById(R.id.fragment_translate_text_view_result_text);
    }

    private void initializeEditTextSourceText() {
        mEditTextSourceText = (EditText) mViewLayout.findViewById(R.id.fragment_translate_edit_text_source_text);
        mEditTextSourceText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new DetermineLanguageOfText().execute(s.toString());
                translateCurrentText(s.toString());
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
        Intent intent = ChooseLanguageActivity.newIntent(getActivity(), getListOfLanguagesNameFromMap());
        startActivityForResult(intent, REQUEST_RESULT_LANGUAGE);
    }

    private List<String> getListOfLanguagesNameFromMap() {
        List<String> listOfLanguagesItem = new ArrayList<>();
        for (String itemCode: mGlobalLanguageGroup.keySet()) {
            listOfLanguagesItem.add(mGlobalLanguageGroup.get(itemCode));
        }
        return listOfLanguagesItem;
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

    private void initializeButtonYandexTranslator() {
        mButtonYandexTranslator = (Button) mViewLayout.findViewById(R.id.fragment_translate_button_yandex_translate);
        mButtonYandexTranslator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchBrowserByUrl();
            }
        });
    }

    private void launchBrowserByUrl() {
        Uri urlToYandexTranslator = Uri.parse(YANDEX_TRANSLATOR_URL);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, urlToYandexTranslator);
        startActivity(launchBrowser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_RESULT_LANGUAGE:
                setCurrentLanguageFromResultIntent(data);
                if (mEditTextSourceText.getText() != null) {
                    translateCurrentText(mEditTextSourceText.getText().toString());
                }
                break;
        }
    }

    private void translateCurrentText(String text) {
        if (currentResultLanguageCode == null) {
            Toast.makeText(getActivity(), "You should choose result language", Toast.LENGTH_SHORT).show();
        } else {
            new TranslateText().execute(text.toString(), currentResultLanguageCode);
        }
    }

    private void setCurrentLanguageFromResultIntent(Intent resultIntent) {
        if (resultIntent == null) {
            return;
        } else {
            String resultLanguage = ChooseLanguageActivity.getLanguageFromResultIntent(resultIntent);
            currentResultLanguageCode = getLanguageCodeByName(resultLanguage);
            mTextViewResultLanguage.setText(resultLanguage);
        }
    }

    private String getLanguageCodeByName(String nameOfLanguage) {
        for (String code: mGlobalLanguageGroup.keySet()) {
            if (nameOfLanguage.equals(mGlobalLanguageGroup.get(code))) {
                return code;
            }
        }
        return null;
    }

    private class TranslateText extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            if (!params[0].equals("")) {
                return new YandexFetcher().translateText(params[0], params[1]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mTextViewResultText.setText(s);
        }
    }
}