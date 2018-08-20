package andorid_dev_2017.navigation_drawer;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import andorid_dev_2017.navigation_drawer.fragments.FragmentShowDetailsBasicInfo;
import andorid_dev_2017.navigation_drawer.fragments.FragmentShowDetailsCharts;
import andorid_dev_2017.navigation_drawer.fragments.FragmentShowDetailsMoreInfo;

public class ShowDetailsActivity extends AppCompatActivity {



    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);



        tabLayout = (TabLayout) findViewById(R.id.tablelayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar_id);
        viewPager = (ViewPager) findViewById(R.id.viewPager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new FragmentShowDetailsBasicInfo(),"Basic Info");
        adapter.AddFragment(new FragmentShowDetailsCharts(),"Chart");
        adapter.AddFragment(new FragmentShowDetailsMoreInfo(), "Pictures");
        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //chart Setup




    }


}
