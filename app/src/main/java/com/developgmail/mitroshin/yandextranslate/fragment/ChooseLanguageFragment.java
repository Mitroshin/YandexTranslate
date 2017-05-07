package com.developgmail.mitroshin.yandextranslate.fragment;

import android.os.AsyncTask;
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
import com.developgmail.mitroshin.yandextranslate.model.LanguageItem;
import com.developgmail.mitroshin.yandextranslate.utility.YandexFetcher;

import java.util.ArrayList;
import java.util.List;

public class ChooseLanguageFragment extends Fragment {
    private static final String TAG = "ChooseLanguageFragment";

    private List<LanguageItem> mGlobalLanguageGroup = new ArrayList<>();

    private View mViewLayout;
    private RecyclerView mLanguageRecyclerView;

    public static ChooseLanguageFragment newInstance() {
        return new ChooseLanguageFragment();
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

    private class ListOfLanguages extends AsyncTask<Void, Void, List<LanguageItem>> {
        @Override
        protected List<LanguageItem> doInBackground(Void... params) {
            return new YandexFetcher().getLanguageGroup();
        }

        @Override
        protected void onPostExecute(List<LanguageItem> languageGroup) {
            mGlobalLanguageGroup = languageGroup;
            setupAdapter();
        }
    }

    private class LanguageAdapter extends RecyclerView.Adapter<LanguageHolder> {
        private List<LanguageItem> mAdapterLanguageGroup;

        public LanguageAdapter(List<LanguageItem> languageGroup) {
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
            LanguageItem languageItem = mAdapterLanguageGroup.get(position);
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

        public void bindLanguage(LanguageItem languageItem) {
            mLanguageTextView.setText(languageItem.getName());
        }
    }
}
