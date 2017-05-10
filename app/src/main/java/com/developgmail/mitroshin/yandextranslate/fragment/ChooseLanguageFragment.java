package com.developgmail.mitroshin.yandextranslate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.developgmail.mitroshin.yandextranslate.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseLanguageFragment extends Fragment {
    private static final String TAG = "ChooseLanguageFragment";
    private static final String ARG_ARRAY_OF_LANGUAGES = "crime_id";

    private List<String> mLanguageGroup = new ArrayList<>();
    private Callbacks mCallbacks;

    private View mViewLayout;
    private RecyclerView mRecyclerViewLanguages;

    public interface Callbacks {
        void sendResultLanguage(String resultLanguage);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public static ChooseLanguageFragment newInstance(String[] arrayOfLanguages) {
        Bundle argumentsOfFragment = new Bundle();
        argumentsOfFragment.putSerializable(ARG_ARRAY_OF_LANGUAGES, arrayOfLanguages);
        ChooseLanguageFragment chooseLanguageFragment = new ChooseLanguageFragment();
        chooseLanguageFragment.setArguments(argumentsOfFragment);
        return chooseLanguageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        String[] arrayOfLanguages = (String[]) getArguments().getSerializable(ARG_ARRAY_OF_LANGUAGES);
        mLanguageGroup = Arrays.asList(arrayOfLanguages);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewLayout = inflater.inflate(R.layout.fragment_choose_language, container, false);
        initializeLayout();
        return mViewLayout;
    }

    private void initializeLayout() {
        initViewLanguageRecyclerView();
    }

    private void initViewLanguageRecyclerView() {
        mRecyclerViewLanguages = (RecyclerView) mViewLayout.findViewById(R.id.dialog_choose_language_recycler_view_languages);
        mRecyclerViewLanguages.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerViewLanguages.setAdapter(new LanguageAdapter(mLanguageGroup));
        }
    }

    private class LanguageAdapter extends RecyclerView.Adapter<LanguageHolder> {
        private List<String> mAdapterLanguageGroup;

        public LanguageAdapter(List<String> languageGroup) {
            mAdapterLanguageGroup = languageGroup;
        }

        @Override
        public LanguageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View layoutLanguageItem = inflater.inflate(R.layout.item_language, parent, false);
            return new LanguageHolder(layoutLanguageItem);
        }

        @Override
        public void onBindViewHolder(LanguageHolder holder, int position) {
            String nameOfLanguage = mAdapterLanguageGroup.get(position);
            holder.bindLanguage(nameOfLanguage);
        }

        @Override
        public int getItemCount() {
            return mAdapterLanguageGroup.size();
        }
    }

    private class LanguageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mButtonNameOfLanguage;

        public LanguageHolder(View itemView) {
            super(itemView);
            mButtonNameOfLanguage = (Button) itemView.findViewById(R.id.item_language_button_name_of_language);
            mButtonNameOfLanguage.setOnClickListener(this);
        }

        public void bindLanguage(String nameOfLanguage) {
            mButtonNameOfLanguage.setText(nameOfLanguage);
        }

        @Override
        public void onClick(View v) {
            String selectedLanguage = (String) mButtonNameOfLanguage.getText();
            mCallbacks.sendResultLanguage(selectedLanguage);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
