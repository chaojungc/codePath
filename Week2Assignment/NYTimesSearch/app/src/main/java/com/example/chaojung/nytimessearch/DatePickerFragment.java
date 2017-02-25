package com.example.chaojung.nytimessearch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by ChaoJung on 2017/2/25.
 */

public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getParentFragment();


        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getContext(), listener, year, month, day);


    }

    public interface OnDateSetListener {
        // handle the date selected
        void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }
}
