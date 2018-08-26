package andorid_dev_2017.navigation_drawer;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.ViewPagerAdapter;
import andorid_dev_2017.navigation_drawer.fragments.FragmentStatisticsBasicInfo;
import andorid_dev_2017.navigation_drawer.fragments.FragmentStatisticsChartProgress;
import andorid_dev_2017.navigation_drawer.fragments.FragmentStatisticsRankProgress;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class StatisticsActivity extends AppCompatActivity {



    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        //setup views
        tabLayout = (TabLayout) findViewById(R.id.statistic_table_layout_id);
        viewPager = (ViewPager) findViewById(R.id.statistic_viewPager_id);

        //setup adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new FragmentStatisticsBasicInfo(), "Stats");
        adapter.AddFragment(new FragmentStatisticsChartProgress(), "Progress");
        adapter.AddFragment(new FragmentStatisticsRankProgress(), "Rank");


        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
