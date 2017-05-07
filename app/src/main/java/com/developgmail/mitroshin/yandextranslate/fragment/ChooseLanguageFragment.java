package com.developgmail.mitroshin.yandextranslate.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developgmail.mitroshin.yandextranslate.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseLanguageFragment extends Fragment {
    private static final String TAG = "ChooseLanguageFragment";
    private static final String ARG_ARRAY_OF_LANGUAGES = "crime_id";

    private List<String> mGlobalLanguageGroup = new ArrayList<>();

    private View mViewLayout;
    private RecyclerView mLanguageRecyclerView;

    public static ChooseLanguageFragment newInstance(String[] arrayOfLanguages) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ARRAY_OF_LANGUAGES, arrayOfLanguages);
        ChooseLanguageFragment chooseLanguageFragment = new ChooseLanguageFragment();
        chooseLanguageFragment.setArguments(args);
        return chooseLanguageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        String[] arrayOfLanguages = (String[]) getArguments().getSerializable(ARG_ARRAY_OF_LANGUAGES);
        mGlobalLanguageGroup = Arrays.asList(arrayOfLanguages);
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
        mLanguageRecyclerView = (RecyclerView) mViewLayout.findViewById(R.id.dialog_choose_language_recycler_view_languages);
        mLanguageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
    }

    private void setupAdapter() {
        if (isAdded()) {
            mLanguageRecyclerView.setAdapter(new LanguageAdapter(mGlobalLanguageGroup));
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
            View itemLanguageView = inflater.inflate(R.layout.item_language, parent, false);
            return new LanguageHolder(itemLanguageView);
        }

        @Override
        public void onBindViewHolder(LanguageHolder holder, int position) {
            String languageItem = mAdapterLanguageGroup.get(position);
            holder.bindLanguage(languageItem);
        }

        @Override
        public int getItemCount() {
            return mAdapterLanguageGroup.size();
        }
    }

    private class LanguageHolder extends RecyclerView.ViewHolder {

        private TextView mLanguageTextView;

        public LanguageHolder(View itemView) {
            super(itemView);
            mLanguageTextView = (TextView) itemView.findViewById(R.id.item_language_text_view_language);
        }

        public void bindLanguage(String languageItem) {
            mLanguageTextView.setText(languageItem);
        }
    }
}
