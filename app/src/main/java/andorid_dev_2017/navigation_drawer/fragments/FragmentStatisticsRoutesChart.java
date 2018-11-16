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
import android.widget.TextView;

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
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class FragmentStatisticsRoutesChart extends Fragment {

    View view;

    private String loggedInUser;
    private SQLiteDbEntryContract sqLiteDbEntryContract;
    private LineChart lineChart;
    private EditText levelPickerEditText;
    private EditText yearPickerEditText;

    public FragmentStatisticsRoutesChart() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistics_year_chart_fragment, container, false);

        //setup database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getContext());

        //get the extras from the Activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }

        //setup views
        lineChart = view.findViewById(R.id.statistics_routes_line_chart_id);
        levelPickerEditText = view.findViewById(R.id.statistics_routes_fragment_edit_text_level_id);
        yearPickerEditText = view.findViewById(R.id.statistics_routes_fragment_edit_text_date_id);


        //other setups
        levelPickerEditText.setText("Level 1");
        yearPickerEditText.setText(getCurrentYear() + "");
        setupChartEntries(lineChart);

        //setup for clickListener
        String[] levelData = getResources().getStringArray(R.array.level);
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = 2000; i <= 2300; i++) {
            yearList.add(i + "");
        }
        String[] yearData = yearList.toArray(new String[yearList.size()]);


        //clickListener
        setupOnClickListener(levelPickerEditText, "Select Level", "Level",
                levelData, 0, levelData.length - 1, 0);
        setupOnClickListener(yearPickerEditText, "Select Year", "Year",
                yearData, 2000, 2300, stringToInt(yearPickerEditText.getText().toString()));


        return view;
    }

    public void setupChartEntries(LineChart lineChart) {

        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(false);

        ArrayList<Entry> lineChartEntries = new ArrayList<>();

        //data setup
        setUpData(lineChartEntries, getStringFromView(levelPickerEditText), getStringFromView(yearPickerEditText));

        //modify the data curve
        LineDataSet lineDataSet = new LineDataSet(lineChartEntries, "LineChart Data");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawCircles(false);
        //changes curve depending on the level
        lineDataSet.setColor(getColorValue(getLevelColor(levelPickerEditText.getText().toString())));
        lineDataSet.setLineWidth(3f);

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
        yAxisLeft.setLabelCount(10);


    }

    //gets Data based on year and difficulty
    public void setUpData(ArrayList<Entry> dataList, String level, String year) {
        ArrayList<String> monthList = new ArrayList<>(Arrays.asList("Jan. ", "Feb. ", "Mar. ", "Apr. ", "May ", "Jun. ",
                "Jul. ", "Aug. ", "Sep. ", "Oct. ", "Nov. ", "Dec. "));
        int routeQuantity;
        for (int i = 0; i < monthList.size(); i++) {
            routeQuantity = getNumberOfLevel(level, year, monthList.get(i));
            dataList.add(new Entry(i, routeQuantity));

        }

    }

    //get the number of all routes of a specific month and level
    public int getNumberOfLevel(String level, String year, String month) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        int routeCount = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; cursor.getCount() > i; i++) {
                if (getStringFromDbCursor(cursor, BoulderEntry.COLUMN_NAME_DATE).contains(month + year) &&
                        getStringFromDbCursor(cursor, BoulderEntry.COLUMN_NAME_CREATOR).equals(loggedInUser)) {
                    routeCount += getQuantityOfDifficulty(cursor, level);
                }
                cursor.moveToNext();
            }
        }
        return routeCount;

    }

    //returns the quantity of the difficulty at the current cursor position
    public int getQuantityOfDifficulty(Cursor cursor, String level) {
        int numberOfDifficulty = 0;
        String columnName = "";

        switch (level) {
            case "Level 1":
                columnName = BoulderEntry.COLUMN_NAME_VERY_EASY;
                break;
            case "Level 2":
                columnName = BoulderEntry.COLUMN_NAME_EASY;
                break;
            case "Level 3":
                columnName = BoulderEntry.COLUMN_NAME_ADVANCED;
                break;
            case "Level 4":
                columnName = BoulderEntry.COLUMN_NAME_HARD;
                break;
            case "Level 5":
                columnName = BoulderEntry.COLUMN_NAME_VERY_HARD;
                break;
            case "Level 6":
                columnName = BoulderEntry.COLUMN_NAME_EXTREMELY_HARD;
                break;
            case "Surprising":
                columnName = BoulderEntry.COLUMN_NAME_SURPRISING;
                break;
            default:
                break;

        }

        if (!columnName.equals("")) {
            numberOfDifficulty = Integer.parseInt(getStringFromDbCursor(cursor, columnName));
        }

        return numberOfDifficulty;
    }

    public void setupOnClickListener(final EditText editText, final String headerText, final String textLabel,
                                     final String[] dataSet, final int minValue, final int maxValue, final int presetValue) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView headerTextView;
                TextView labelTextView;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                view = getLayoutInflater().inflate(R.layout.statistics_number_picker_dialog, null);
                headerTextView = view.findViewById(R.id.statistics_number_picker_dialog_text_header_id);
                labelTextView = view.findViewById(R.id.statistics_number_picker_dialog_text_label_id);
                headerTextView.setText(headerText);
                labelTextView.setText(textLabel);

                final NumberPicker picker = view.findViewById(R.id.statistics_number_picker_dialog_picker_id);

                picker.setMinValue(minValue);
                picker.setMaxValue(maxValue);
                picker.setValue(presetValue);
                picker.setDisplayedValues(dataSet);

                Button selectBtn = view.findViewById(R.id.statistics_session_chart_fragment_dialog_select_btn_id);
                Button cancelBtn = view.findViewById(R.id.statistics_session_chart_fragment_dialog_cancel_btn_id);

                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                selectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (textLabel.equals("Level")) {
                            editText.setText(intToLevel(picker.getValue()));
                        } else {
                            editText.setText(picker.getValue() + "");
                        }
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

    public String intToLevel(int pickerIndex) {
        String level = "";
        switch (pickerIndex) {
            case 0:
                level = "Level " + (pickerIndex + 1);
                break;
            case 1:
                level = "Level " + (pickerIndex + 1);
                break;
            case 2:
                level = "Level " + (pickerIndex + 1);
                break;
            case 3:
                level = "Level " + (pickerIndex + 1);
                break;
            case 4:
                level = "Level " + (pickerIndex + 1);
                break;
            case 5:
                level = "Level " + (pickerIndex + 1);
                break;
            case 6:
                level = "Surprising";
                break;
        }
        return level;
    }

    public int getLevelColor(String level) {
        int color;
        switch (level) {
            case "Level 1":
                color = R.color.Diff1;
                break;
            case "Level 2":
                color = R.color.Diff2;
                break;
            case "Level 3":
                color = R.color.Diff3;
                break;
            case "Level 4":
                color = R.color.Diff4;
                break;
            case "Level 5":
                color = R.color.Diff5;
                break;
            case "Level 6":
                color = R.color.Diff6;
                break;
            case "Surprising":
                color = R.color.Diff7;
                break;
            default:
                color = R.color.colorPrimaryDark;
                break;
        }

        return color;
    }

    public int stringToInt(String number) {
        return Integer.parseInt(number);
    }

    public String getStringFromView(EditText editText) {
        return editText.getText().toString();
    }


    public String getStringFromDbCursor(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public int getColorValue(int colorID) {
        return ResourcesCompat.getColor(getResources(), colorID, null);
    }

}
