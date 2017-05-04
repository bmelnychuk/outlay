package app.outlay.view.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import app.outlay.utils.DeviceUtils;

import java.util.Calendar;

/**
 * Created by Bogdan Melnychuk on 1/29/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog.OnDateSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), app.outlay.R.style.DatePicker, this, year, month, day);
        if (DeviceUtils.supportV5()) {
            dialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        }
        return dialog;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (listener != null) {
            listener.onDateSet(view, year, monthOfYear, dayOfMonth);
        }
    }

}
