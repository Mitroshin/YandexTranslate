package com.developgmail.mitroshin.yandextranslate.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developgmail.mitroshin.yandextranslate.R;
import com.developgmail.mitroshin.yandextranslate.model.LanguageItem;
import com.developgmail.mitroshin.yandextranslate.utility.YandexFetcher;

import java.util.List;

public class LanguagePickerDialogFragment extends DialogFragment {

    private static final String TAG = "LanguagePickerDialogFragment";

    private List<LanguageItem> mLanguageGroup;

    private RecyclerView mLanguageRecyclerView;
    private View mViewLayout;

    public static LanguagePickerDialogFragment newInstance() {
        return new LanguagePickerDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new ListOfLanguages().execute();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mViewLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choose_language, null);
        initializeLayout();
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.text_choose_result_language)
                .setView(mViewLayout)
                .create();
    }

    private void initializeLayout() {
        initViewLanguageRecyclerView();
    }

    private void initViewLanguageRecyclerView() {
        mLanguageRecyclerView = (RecyclerView) mViewLayout.findViewById(R.id.dialog_choose_language_recycler_view_languages);
        mLanguageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupAdapter() {
        if (isAdded()) {
            mLanguageRecyclerView.setAdapter(new LanguageAdapter(mLanguageGroup));
        }
    }

    private class ListOfLanguages extends AsyncTask<Void, Void, List<LanguageItem>> {
        @Override
        protected List<LanguageItem> doInBackground(Void... params) {
            return new YandexFetcher().getLanguageGroup();
        }

        @Override
        protected void onPostExecute(List<LanguageItem> languageGroup) {
            mLanguageGroup = languageGroup;
            for (LanguageItem languageItem: mLanguageGroup) {
                Log.i(TAG, languageItem.toString());
            }
            setupAdapter();
        }
    }

    private class LanguageAdapter extends RecyclerView.Adapter<LanguageHolder> {
        private List<LanguageItem> mLanguageGroupAdapter;

        public LanguageAdapter(List<LanguageItem> languageGroup) {
            mLanguageGroupAdapter = languageGroup;
        }

        @Override
        public LanguageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_language, parent, false);
            return new LanguageHolder(view);
        }

        @Override
        public void onBindViewHolder(LanguageHolder holder, int position) {
            LanguageItem languageItem = mLanguageGroupAdapter.get(position);
            holder.bindLanguage(languageItem);
        }

        @Override
        public int getItemCount() {
            return mLanguageGroupAdapter.size();
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
