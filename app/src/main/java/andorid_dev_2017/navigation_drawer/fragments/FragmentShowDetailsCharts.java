package andorid_dev_2017.navigation_drawer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class FragmentShowDetailsCharts extends android.support.v4.app.Fragment {

    View view;
    private HorizontalBarChart barChart;

    public FragmentShowDetailsCharts() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.show_details_chart_fragment, container, false);

        barChart = view.findViewById(R.id.horBarChart_02);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setMaxVisibleValueCount(100);
        barChart.setTouchEnabled(false);


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
        return view;


    }
}
