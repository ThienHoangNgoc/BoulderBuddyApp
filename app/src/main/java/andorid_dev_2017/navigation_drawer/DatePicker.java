package andorid_dev_2017.navigation_drawer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Thien Hoang on 23.05.2018.
 */

public class DatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    EditText dateText;
    private static int DAY;
    private static int MONTH;
    private static int YEAR;
    private Context context;
    private static String monthString;


    public DatePicker(Context context, int editTextViewID) {
        Activity activity = (Activity) context;
        this.dateText = (EditText) activity.findViewById(editTextViewID);
        this.dateText.setOnClickListener(this);
        this.context = context;
    }


    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        YEAR = year;
        MONTH = month;
        DAY = dayOfMonth;
        updateDisplay();


    }

    @Override
    public void onClick(View v) {
        java.util.Calendar calendar = java.util.Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    private void updateDisplay() {

        switch (MONTH + 1) {
            case 1:
                monthString = "Januar";
                break;
            case 2:
                monthString = "Februar";
                break;
            case 3:
                monthString = "März";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "Mai";
                break;
            case 6:
                monthString = "Juni";
                break;
            case 7:
                monthString = "Juli";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "Oktober";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "Dezember";
                break;
            default:
                break;

        }




        dateText.setText(new StringBuilder().append(DAY).append(".").append(monthString).append(" ")
                .append(YEAR));
    }
}
