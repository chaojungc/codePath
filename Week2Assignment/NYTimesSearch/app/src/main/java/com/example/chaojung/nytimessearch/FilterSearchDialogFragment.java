package com.example.chaojung.nytimessearch;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.parceler.Parcels;


/**
 * Created by ChaoJung on 2017/2/24.
 */

public class FilterSearchDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private EditText etBeginDate;
    private Spinner spinSortOrder;
    private CheckBox cbArt;
    private CheckBox cbFashion;
    private CheckBox cbSport;
    private Button btnSave;
    Filter filter;
    private FilterSearchDialogListener mCallback;

    public FilterSearchDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public interface FilterSearchDialogListener {
        void onFinishFilterDialog(Filter filter);
    }


    public static FilterSearchDialogFragment newInstance(Filter filter) {
        FilterSearchDialogFragment frag = new FilterSearchDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("filter", Parcels.wrap(filter));
        frag.setArguments(args);
        return frag;
    }

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

        btnSave = (Button) view.findViewById(R.id.btnSave);

        // Fetch arguments from bundle and set title
        filter = (Filter) Parcels.unwrap(getArguments().getParcelable("filter"));
        //getDialog().setTitle(title);

        //etBeginDate.requestFocus();

        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        if (filter.getBeginDate() != null) {
            etBeginDate.setText(filter.getBeginDate());
        } else {
            etBeginDate.setText("");
        }

        if (filter.getSortOrder() == null ||filter.getSortOrder().equals("oldest"))
            spinSortOrder.setSelection(0);
        else
            spinSortOrder.setSelection(1);

        if (filter.getCheckArt() != null)
            cbArt.setChecked(filter.checkArt);
        if (filter.getCheckFashion() != null)
            cbFashion.setChecked(filter.checkFashion);
        if (filter.getCheckSport() != null)
            cbSport.setChecked(filter.checkSport);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.setBeginDate(etBeginDate.getText().toString());
                filter.setSortOrder(spinSortOrder.getSelectedItem().toString());
                // Check if the checkbox is checked
                filter.setCheckArt(cbArt.isChecked());
                filter.setCheckFashion(cbFashion.isChecked());
                filter.setCheckSport(cbSport.isChecked());

                FilterSearchDialogListener listener = (FilterSearchDialogListener) getActivity();

                listener.onFinishFilterDialog(filter);
                dismiss();
            }
        });

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            if (context instanceof FilterSearchDialogListener){
                mCallback = (FilterSearchDialogListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear += 1;
        String day = dayOfMonth >= 10 ? Integer.toString(dayOfMonth) : "0" + Integer.toString(dayOfMonth);
        String month = monthOfYear >= 10 ? Integer.toString(monthOfYear) : "0" + Integer.toString(monthOfYear);
        etBeginDate.setText(Integer.toString(year) + month + day);


        // store the values selected into a Calendar instance
//        final Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, monthOfYear);
//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }


}
