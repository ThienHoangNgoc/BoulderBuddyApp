package andorid_dev_2017.navigation_drawer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.XAxisFormatter;
import andorid_dev_2017.navigation_drawer.YAxisValueFormatter;
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.Entry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class FragmentShowDetailsCharts extends android.support.v4.app.Fragment {

    private View view;
    private String entryId;
    private HorizontalBarChart barChart;
    private SQLiteDbEntryContract sqLiteDbEntryContract;

    public FragmentShowDetailsCharts() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.show_details_chart_fragment, container, false);


        //setup database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getContext());

        //get the extras from the Activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            entryId = extras.getString("entry_id_key");
        }


        //setup barChart
        barChart = view.findViewById(R.id.horBarChart_02);
        setupBarEntries(barChart);

        return view;


    }


    public void setupBarEntries(HorizontalBarChart barChart) {
        Entry boulderEntry = getBoulderEntry(entryId);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setMaxVisibleValueCount(100);
        barChart.setTouchEnabled(false);


        ArrayList<BarEntry> horiBarEntries = new ArrayList<>();
        //1.easy ... 7.surprising
        horiBarEntries.add(new BarEntry(0, stringToFloat(boulderEntry.getDiff1())));
        horiBarEntries.add(new BarEntry(1, stringToFloat(boulderEntry.getDiff2())));
        horiBarEntries.add(new BarEntry(2, stringToFloat(boulderEntry.getDiff3())));
        horiBarEntries.add(new BarEntry(3, stringToFloat(boulderEntry.getDiff4())));
        horiBarEntries.add(new BarEntry(4, stringToFloat(boulderEntry.getDiff5())));
        horiBarEntries.add(new BarEntry(5, stringToFloat(boulderEntry.getDiff6())));
        horiBarEntries.add(new BarEntry(6, stringToFloat(boulderEntry.getDiff7())));

        BarDataSet horiBarDataSet = new BarDataSet(horiBarEntries, "Data 123");
        /*horiBarDataSet.setColors(new int[]{R.color.colorWhite},getApplicationContext());*/
        horiBarDataSet.setColors(new int[]{R.color.Diff1, R.color.Diff2,
                R.color.Diff3, R.color.Diff4,
                R.color.Diff5, R.color.Diff6,
                R.color.Diff7}, view.getContext());
        BarData horiBarData = new BarData(horiBarDataSet);
        horiBarData.setValueTextColor(getResources().getColor(R.color.colorWhite));
        horiBarData.setBarWidth(0.6f);
        horiBarData.setValueTextSize(15f);
        horiBarData.setValueFormatter(new YAxisValueFormatter());

        barChart.setData(horiBarData);

        String xd[] = new String[]{"Very Easy", "Easy", "Advanced", "Hard", "Very Hard", "Extremely Hard", "Surprising"};
        XAxis xAxis1 = barChart.getXAxis();
        xAxis1.setGranularity(1f);
        xAxis1.setDrawGridLines(false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setValueFormatter(new XAxisFormatter(xd));
        xAxis1.setDrawAxisLine(false);
        xAxis1.setTextColor(getResources().getColor(R.color.colorWhite));
        xAxis1.setTextSize(15f);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawLabels(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);

    }

    //gets an entry from the db based on the id
    public Entry getBoulderEntry(String id) {
        Entry entry;
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID)).equals(id)) {
                entry = new Entry(id,
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_DATE)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_START_TIME)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_END_TIME)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_EASY)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_EASY)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ADVANCED)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_EXTREMELY_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_SURPRISING)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_RATING)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_EXP)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR))
                );
                return entry;
            }
            cursor.moveToNext();
        }
        return null;
    }

    public float stringToFloat(String number) {
        return Float.parseFloat(number);
    }


}
