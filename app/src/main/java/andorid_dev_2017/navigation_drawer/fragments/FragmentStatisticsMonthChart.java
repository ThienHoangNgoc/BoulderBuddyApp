package andorid_dev_2017.navigation_drawer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.StatGridViewItem;
import andorid_dev_2017.navigation_drawer.StatisticsGridViewAdapter;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class FragmentStatisticsMonthChart extends Fragment {

    View view;
    private String loggedInUser;
    private SQLiteDbEntryContract sqLiteDbEntryContract;

    private GridView sessionStatsGridView;
    private GridView difficultyStatsGridView;
    private ArrayList<StatGridViewItem> arrayOfGridViewItems;
    private StatisticsGridViewAdapter gridViewAdapter;

    public FragmentStatisticsMonthChart() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistics_month_chart_fragment, container, false);


        return view;
    }




}
