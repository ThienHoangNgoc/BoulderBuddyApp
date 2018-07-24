package andorid_dev_2017.navigation_drawer;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StatisticsActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        tabLayout = (TabLayout) findViewById(R.id.Statistic_table_layout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.Statistic_appBar_id);
        viewPager = (ViewPager) findViewById(R.id.Statistic_viewPager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new FragmentStatisticsBasicInfo(),"Basic Stats");
        adapter.AddFragment(new FragmentStatisticsRankProgress(),"Progress");
        adapter.AddFragment(new FragmentStatisticsRankProgress(),"Rank");


        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
