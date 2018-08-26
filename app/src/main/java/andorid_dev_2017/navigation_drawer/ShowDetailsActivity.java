package andorid_dev_2017.navigation_drawer;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import andorid_dev_2017.navigation_drawer.fragments.FragmentShowDetailsBasicInfo;
import andorid_dev_2017.navigation_drawer.fragments.FragmentShowDetailsCharts;
import andorid_dev_2017.navigation_drawer.fragments.FragmentShowDetailsPictures;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class ShowDetailsActivity extends AppCompatActivity {


    private String entryId;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView headerText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);




        //Get String from extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            entryId = extras.getString("entry_id_key");
        }


        headerText = findViewById(R.id.show_details_header_text_id);
        tabLayout = findViewById(R.id.tablelayout_id);
        viewPager = findViewById(R.id.viewPager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new FragmentShowDetailsBasicInfo(), "Basic Info");
        adapter.AddFragment(new FragmentShowDetailsCharts(), "Chart");
        adapter.AddFragment(new FragmentShowDetailsPictures(), "Pictures");
        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //headerText
        headerText.setText("Eintrag #" + entryId);


    }


}
