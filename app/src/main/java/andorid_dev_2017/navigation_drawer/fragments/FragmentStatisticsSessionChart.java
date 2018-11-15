package andorid_dev_2017.navigation_drawer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import andorid_dev_2017.navigation_drawer.LineChartValueFormatter;
import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.XAxisFormatter;
import andorid_dev_2017.navigation_drawer.YAxisValueFormatter;
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;


public class FragmentStatisticsSessionChart extends Fragment {

    View view;
    private String loggedInUser;
    private SQLiteDbEntryContract sqLiteDbEntryContract;
    private LineChart lineChart;
    private EditText yearPickerEditText;

    public FragmentStatisticsSessionChart() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistics_session_chart_fragment, container, false);


        //setup database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getContext());

        //get the extras from the Activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }

        //setup views
        lineChart = view.findViewById(R.id.statistics_sessions_line_chart_id);
        yearPickerEditText = view.findViewById(R.id.statistics_session_chart_fragment_edit_text_id);

        //other setups
        yearPickerEditText.setText(getCurrentYear() + "");
        setupChartEntries(lineChart);
        setupOnClickListener(yearPickerEditText);


        return view;
    }

    public void setupChartEntries(LineChart lineChart) {

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(false);

        ArrayList<Entry> lineChartEntries = new ArrayList<>();

        //get the highest value in the chart and setup its data
        int highestValue = setUpData(lineChartEntries, Integer.parseInt(yearPickerEditText.getText().toString()));

        //modify the data curve
        LineDataSet lineDataSet = new LineDataSet(lineChartEntries, "LineChart Data");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(getColorValue(R.color.colorPrimaryDark));
        lineDataSet.setLineWidth(2f);

        LineData lineData = new LineData(lineDataSet);
        lineData.setValueTextColor(getColorValue(R.color.colorWhite));
        lineData.setValueFormatter(new LineChartValueFormatter());
        lineChart.setData(lineData);

        String xAxisStringLabel[] = new String[]{"Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun.",
                "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."};

        //Chart Design
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisRight = lineChart.getAxisRight();
        YAxis yAxisLeft = lineChart.getAxisLeft();

        //X-Axis
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XAxisFormatter(xAxisStringLabel));
        xAxis.setLabelCount(11);
        xAxis.setAxisLineColor(getColorValue(R.color.colorBlack));
        xAxis.setTextColor(getColorValue(R.color.colorWhite));

        //Right Y-Axis
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawLabels(false);

        //Left Y-Axis
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisLineColor(getColorValue(R.color.colorBlack));
        yAxisLeft.setTextColor(getColorValue(R.color.colorWhite));
        if (highestValue <= 10) {
            yAxisLeft.setAxisMaximum(10f);
        }


    }

    //gets Data based on year and returns the highest value in order to format the lineChart correctly
    public int setUpData(ArrayList<Entry> dataList, int year) {
        ArrayList<String> monthList = new ArrayList<>(Arrays.asList("Jan. ", "Feb. ", "Mar. ", "Apr. ", "May ", "Jun. ",
                "Jul. ", "Aug. ", "Sep. ", "Oct. ", "Nov. ", "Dec. "));
        int highestValue = 0;
        int sessionQuantity = 0;
        for (int i = 0; i < monthList.size(); i++) {
            sessionQuantity = getNumberOfSession(year, monthList.get(i));
            dataList.add(new Entry(i, sessionQuantity));
            if (sessionQuantity > highestValue) {
                highestValue = sessionQuantity;
            }
        }
        return highestValue;
    }


    public int getNumberOfSession(int year, String month) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        int sessionCount = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; cursor.getCount() > i; i++) {
                if (getStringFromDbCursor(cursor, BoulderEntry.COLUMN_NAME_DATE).contains(month + year) &&
                        getStringFromDbCursor(cursor, BoulderEntry.COLUMN_NAME_CREATOR).equals(loggedInUser)) {
                    sessionCount++;
                }
                cursor.moveToNext();
            }
        }
        return sessionCount;
    }

    public String getStringFromDbCursor(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public void setupOnClickListener(EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                view = getLayoutInflater().inflate(R.layout.statistics_session_number_picker_dialog, null);
                final NumberPicker yearPicker = view.findViewById(R.id.statistics_session_chart_fragment_dialog_year_picker_id);
                ArrayList<String> yearList = new ArrayList<>();
                for (int i = 2000; i <= 2300; i++) {
                    yearList.add(i + "");
                }

                String[] yearData = yearList.toArray(new String[yearList.size()]);
                yearPicker.setMinValue(2000);
                yearPicker.setMaxValue(2300);
                yearPicker.setValue(getCurrentYear());
                yearPicker.setDisplayedValues(yearData);

                Button selectBtn = view.findViewById(R.id.statistics_session_chart_fragment_dialog_select_btn_id);
                Button cancelBtn = view.findViewById(R.id.statistics_session_chart_fragment_dialog_cancel_btn_id);

                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                selectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        yearPickerEditText.setText(yearPicker.getValue() + "");
                        //refresh chart if data changed
                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                        setupChartEntries(lineChart);
                        dialog.cancel();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

                    }
                });

            }
        });

    }

    public int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public int getColorValue(int colorID) {
        return ResourcesCompat.getColor(getResources(), colorID, null);
    }


}
