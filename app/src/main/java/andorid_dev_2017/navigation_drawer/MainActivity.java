package andorid_dev_2017.navigation_drawer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawlayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView nView;
    private View headerView;
    private ProgressBar progressBar;
    private int duration = Toast.LENGTH_SHORT;
    private FloatingActionButton fab;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawlayout = (DrawerLayout) findViewById(R.id.drawer);
        nView = (NavigationView) findViewById(R.id.navigation_view);
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        listView = (ListView) findViewById(R.id.latest_entries_listView);


        mToggle = new ActionBarDrawerToggle(this, mDrawlayout, R.string.open, R.string.close);
        mDrawlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawerContent(nView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewEntryV2.class);
                startActivity(intent);
            }
        });

        headerView = nView.getHeaderView(0);
        progressBar = (ProgressBar) headerView.findViewById(R.id.progressBar);
        progressBar.setProgress(45);


        //create Entries

        ArrayList<EntryItem> arrayOfEntryItems = new ArrayList<>();
        EntryAdapter entryAdapter = new EntryAdapter(this, arrayOfEntryItems);
        createEntries(entryAdapter);
        listView.setAdapter(entryAdapter);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void drawItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.profile:
                /*progressBar.setProgress(10);
                Toast.makeText(getApplicationContext(), "event", duration).show();*/
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.achievements:
                intent = new Intent(getApplicationContext(), AchievementActivity.class);
                startActivity(intent);
                break;
            case R.id.statistics:
                intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                intent = new Intent(getApplicationContext(), NewEntryActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        mDrawlayout.closeDrawers();

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawItemSelected(item);
                return true;
            }
        });
    }

    public void createEntries(EntryAdapter entryAdapter) {
        final ArrayList<EntryItem> entryCounter = new ArrayList<>();

        EntryItem item1 = new EntryItem("1", "Südbloc", "28.01.17", 2.5f);
        EntryItem item2 = new EntryItem("2", "Ostbloc", "04.03.17", 3f);
        EntryItem item3 = new EntryItem("3", "Südbloc", "07.04.17", 3.5f);
        EntryItem item4 = new EntryItem("4", "Südbloc", "28.04.17", 3f);

        entryCounter.add(item1);
        entryCounter.add(item2);
        entryCounter.add(item3);
        entryCounter.add(item4);


        int j;
        for (j = entryCounter.size() - 1; j >= 0; j--) {
            entryAdapter.add(entryCounter.get(j));
        }


    }


}
