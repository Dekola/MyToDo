//package com.kola.mytodo.Fragment;
//
//
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.app.Fragment;
//import android.os.Bundle;
//import android.widget.DatePicker;
//
//import java.util.Calendar;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class DatePickerFragment extends DialogFragment {
//
//    public DatePickerFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the current time as the default values for the picker
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
//        // Activity needs to implement this interface
////        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();
//
//        // Create a new instance of TimePickerDialog and return it
////        return new DatePickerDialog(getActivity(), listener, year, month, day);
//        return null;
//    }
//
//    public void onDateSet(DatePicker view, int year, int month, int day) {
//        // Do something with the date chosen by the user
//    }
//}