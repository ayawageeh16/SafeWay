package com.example.lenovo.trafficservices;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by jahid on 12/10/15.
 */
public class TimePickerFragment extends DialogFragment {
    OnTimeSetListener ontimeSet;

    public TimePickerFragment() {
    }

    public void setCallBack(OnTimeSetListener ontime) {
        ontimeSet = ontime;
    }


    private int Hour, Minute;
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        Hour = args.getInt("Hour");
        Minute = args.getInt("Minute");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), ontimeSet, Hour, Minute,
                DateFormat.is24HourFormat(getActivity()));
    }

}
