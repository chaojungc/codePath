package com.example.chaojung.nytimessearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by ChaoJung on 2017/2/24.
 */

public class FilterSearchDialogFragment extends DialogFragment{

    private EditText etBeginDate;
    private Spinner spinSortOrder;
    private CheckBox cbArt;
    private CheckBox cbFashion;
    private CheckBox cbSport;

    public FilterSearchDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface FilterSearchDialogListener {
        void onFinishFilterDialog(String inputText);
    }


    /*public static FilterSearchDialogFragment newInstance(String title) {
        FilterSearchDialogFragment frag = new FilterSearchDialogFragment();
        Bundle args = new Bundle();
        //args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_search, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);
        spinSortOrder = (Spinner) view.findViewById(R.id.spinSortOrder);
        cbArt = (CheckBox) view.findViewById(R.id.cbArt);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbSport = (CheckBox) view.findViewById(R.id.cbSport);

        /*
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        //Show soft keyboard automatically and request focus to field
        etBeginDate.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                .LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        */
    }

    public boolean onFilterAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            FilterSearchDialogListener listener = (FilterSearchDialogListener) getActivity();
            listener.onFinishFilterDialog(etBeginDate.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }


}
