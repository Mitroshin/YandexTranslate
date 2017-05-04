package com.developgmail.mitroshin.yandextranslate.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.developgmail.mitroshin.yandextranslate.R;
import com.developgmail.mitroshin.yandextranslate.fragment.dialog.LanguagePickerDialogFragment;
import com.developgmail.mitroshin.yandextranslate.utility.YandexFetcher;

public class TranslateFragment extends Fragment {

    private static final int REQUEST_RESULT_LANGUAGE = 0;
    private static final String DIALOG_CHOOSE_RESULT_LANGUAGE = "DialogChooseResultLanguage";

    private View mViewLayout;
    private TextView mTextViewSourceLanguage;
    private EditText mEditTextSourceText;
    private Button mButtonChooseResultLanguage;

    private FragmentManager mFragmentManager;

    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewLayout = inflater.inflate(R.layout.fragment_translate, container, false);
        mFragmentManager = getFragmentManager();
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

    private void initializeButtonChooseResultLanguage() {
        mButtonChooseResultLanguage = (Button) mViewLayout.findViewById(R.id.fragment_translate_button_choose_result_language);
        mButtonChooseResultLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDialogToChooseResultLanguage();
            }
        });
    }

    private void launchDialogToChooseResultLanguage() {
        LanguagePickerDialogFragment languagePickerDialogFragment = LanguagePickerDialogFragment.newInstance();
        languagePickerDialogFragment.setTargetFragment(TranslateFragment.this, REQUEST_RESULT_LANGUAGE);
        languagePickerDialogFragment.show(mFragmentManager, DIALOG_CHOOSE_RESULT_LANGUAGE);
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