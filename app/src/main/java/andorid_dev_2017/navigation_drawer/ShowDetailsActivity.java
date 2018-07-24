package andorid_dev_2017.navigation_drawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class ShowDetailsActivity extends AppCompatActivity {


    private BarChart barChart;
    private HorizontalBarChart hBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_details);

        /*barChart = (BarChart) findViewById(R.id.barChart_01);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(0, 1f));
        barEntries.add(new BarEntry(1, 23f));
        barEntries.add(new BarEntry(2, 15f));
        barEntries.add(new BarEntry(3, 1f));
        barEntries.add(new BarEntry(4, 42f));
        barEntries.add(new BarEntry(5, 39f));
        barEntries.add(new BarEntry(6, 1f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Data");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        *//*barDataSet.setColors(new int[]{R.color.colorWhite,});*//*

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.9f);
        data.setValueFormatter(new YAxisValueFormatter());

        barChart.setData(data);
        barChart.invalidate();
        barChart.animateY(500);

        String[] months = new String[]{"Very Easy", "Easy", "Advanced", "Hard", "Very Hard", "Extremely Hard", "Surprising"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new XAxisFormatter(months));*/


        hBarChart = (HorizontalBarChart) findViewById(R.id.horBarChart_01);
        hBarChart.getDescription().setEnabled(false);
        hBarChart.getLegend().setEnabled(false);
        hBarChart.setMaxVisibleValueCount(100);
        hBarChart.setTouchEnabled(false);


        ArrayList<BarEntry> horiBarEntries = new ArrayList<>();

        horiBarEntries.add(new BarEntry(0, 4f));
        horiBarEntries.add(new BarEntry(1, 5f));
        horiBarEntries.add(new BarEntry(2, 6f));
        horiBarEntries.add(new BarEntry(3, 2f));
        horiBarEntries.add(new BarEntry(4, 0f));
        horiBarEntries.add(new BarEntry(5, 0f));
        horiBarEntries.add(new BarEntry(6, 2f));


        BarDataSet horiBarDataSet = new BarDataSet(horiBarEntries, "Data 123");
        /*horiBarDataSet.setColors(new int[]{R.color.colorWhite},getApplicationContext());*/
        horiBarDataSet.setColors(new int[]{R.color.Diff1, R.color.Diff2,
                R.color.Diff3, R.color.Diff4,
                R.color.Diff5, R.color.Diff6,
                R.color.Diff7}, getApplicationContext());
        BarData horiBarData = new BarData(horiBarDataSet);
        horiBarData.setValueTextColor(getResources().getColor(R.color.colorWhite));
        horiBarData.setBarWidth(0.6f);
        horiBarData.setValueTextSize(15f);
        horiBarData.setValueFormatter(new YAxisValueFormatter());

        hBarChart.setData(horiBarData);

        String xd[] = new String[]{"Very Easy", "Easy", "Advanced", "Hard", "Very Hard", "Extremely Hard", "Surprising"};
        XAxis xAxis1 = hBarChart.getXAxis();
        xAxis1.setGranularity(1f);
        xAxis1.setDrawGridLines(false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setValueFormatter(new XAxisFormatter(xd));
        xAxis1.setDrawAxisLine(false);
        xAxis1.setTextColor(getResources().getColor(R.color.colorWhite));
        xAxis1.setTextSize(15f);



        YAxis yAxis = hBarChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawLabels(false);





        hBarChart.getAxisRight().setDrawGridLines(false);
        hBarChart.getAxisRight().setDrawLabels(false);
        hBarChart.getAxisRight().setDrawAxisLine(false);



    }


}
