package com.developgmail.mitroshin.yandextranslate.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.developgmail.mitroshin.yandextranslate.R;

public class LanguagePickerDialogFragment extends DialogFragment {

    public static LanguagePickerDialogFragment newInstance() {
        return new LanguagePickerDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choose_language, null);
        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }
}
