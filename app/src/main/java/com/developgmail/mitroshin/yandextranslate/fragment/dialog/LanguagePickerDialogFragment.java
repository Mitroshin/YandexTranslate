package com.developgmail.mitroshin.yandextranslate.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.developgmail.mitroshin.yandextranslate.R;
import com.developgmail.mitroshin.yandextranslate.utility.YandexFetcher;

import java.util.Map;

public class LanguagePickerDialogFragment extends DialogFragment {

    private static final String TAG = "LanguagePickerDialogFragment";

    public static LanguagePickerDialogFragment newInstance() {
        return new LanguagePickerDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choose_language, null);
        new ListOfLanguages().execute();
        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }

    private class ListOfLanguages extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... params) {
            return new YandexFetcher().getMapOfLanguages();
        }

        @Override
        protected void onPostExecute(Map<String, String> mapOfLanguages) {
            for (String key: mapOfLanguages.keySet()) {
                Log.i(TAG, "Key = " + key + " : value = " + mapOfLanguages.get(key));
            }
        }
    }
}
