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
                monthString = "Jan.";
                break;
            case 2:
                monthString = "Feb.";
                break;
            case 3:
                monthString = "Mar.";
                break;
            case 4:
                monthString = "Apr.";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "Jun.";
                break;
            case 7:
                monthString = "Jul.";
                break;
            case 8:
                monthString = "Aug.";
                break;
            case 9:
                monthString = "Sep.";
                break;
            case 10:
                monthString = "Oct.";
                break;
            case 11:
                monthString = "Nov.";
                break;
            case 12:
                monthString = "Dec.";
                break;
            default:
                break;

        }




        dateText.setText(new StringBuilder().append(DAY).append(".").append(monthString).append(" ")
                .append(YEAR));
    }
}
