package andorid_dev_2017.navigation_drawer;

import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import andorid_dev_2017.navigation_drawer.fragments.FragmentStatisticsSessionChart;
import andorid_dev_2017.navigation_drawer.fragments.FragmentStatisticsRoutesChart;
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.StatisticsRecyclerViewItem;

public class StatisticsActivity extends AppCompatActivity {

    private final static String LOGTAG = "statistics";


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerView statSet1RecyclerView;
    private ExpandableHeightGridView statSet2GridView;
    private RecyclerView statSet3RecyclerView;
    private ExpandableHeightGridView statSet4GridView;
    private ExpandableHeightGridView statSet5GridView;
    private ExpandableHeightGridView statSet6GridView;


    private String loggedInUser;
    private SQLiteDbEntryContract sqLiteDbEntryContract;


    private ArrayList<StatGridViewItem> arrayOfGridViewItems;
    private StatisticsGridViewAdapter statSet2GridViewAdapter;
    private StatisticsGridViewAdapter statSet4GridViewAdapter;
    private StatisticsGridViewAdapter statSet5GridViewAdapter;
    private StatisticsGridViewAdapter statSet6GridViewAdapter;


    private StatisticsRecyclerViewAdapter StatSet1RecyclerViewAdapter;
    private StatisticsRecyclerViewAdapter StatSet3RecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //setup database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getApplicationContext());

        //get the extras from the Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }


        //setup views
        tabLayout = findViewById(R.id.statistic_tab_layout_id);
        viewPager = findViewById(R.id.statistic_viewPager_id);
        statSet1RecyclerView = findViewById(R.id.statistics_stat_set_1_recycler_view_id);
        statSet2GridView = findViewById(R.id.statistics_stat_set_2_grid_view_id);
        statSet3RecyclerView = findViewById(R.id.statistics_stat_set_3_recycler_view_id);
        statSet4GridView = findViewById(R.id.statistics_stat_set_4_grid_view_id);
        statSet5GridView = findViewById(R.id.statistics_stat_set_5_grid_view_id);
        statSet6GridView = findViewById(R.id.statistics_stat_set_6_grid_view_id);


        //setup viewPagerAdapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Adding Fragments
        adapter.AddFragment(new FragmentStatisticsSessionChart(), "Sessions");
        adapter.AddFragment(new FragmentStatisticsRoutesChart(), "Routes");

        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        //setup statSet1
        statSet1RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        StatSet1RecyclerViewAdapter = new StatisticsRecyclerViewAdapter(getApplicationContext(), createEntriesRecyclerViewSet1());
        statSet1RecyclerView.setAdapter(StatSet1RecyclerViewAdapter);


        //setup statSet2
        arrayOfGridViewItems = new ArrayList<>();
        statSet2GridViewAdapter = new StatisticsGridViewAdapter(getApplicationContext(), arrayOfGridViewItems);
        createEntriesGridViewSet2(statSet2GridViewAdapter);
        statSet2GridView.setAdapter(statSet2GridViewAdapter);


        //setup statSet3
        statSet3RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        StatSet3RecyclerViewAdapter = new StatisticsRecyclerViewAdapter(getApplicationContext(), createEntriesRecyclerViewSet3());
        statSet3RecyclerView.setAdapter(StatSet3RecyclerViewAdapter);

        //setup statSet4
        arrayOfGridViewItems = new ArrayList<>();
        statSet4GridViewAdapter = new StatisticsGridViewAdapter(getApplicationContext(), arrayOfGridViewItems);
        createEntriesGridViewSet4(statSet4GridViewAdapter);
        statSet4GridView.setAdapter(statSet4GridViewAdapter);

        //setup statSet5
        arrayOfGridViewItems = new ArrayList<>();
        statSet5GridViewAdapter = new StatisticsGridViewAdapter(getApplicationContext(), arrayOfGridViewItems);
        createEntriesGridViewSet5(statSet5GridViewAdapter);
        statSet5GridView.setAdapter(statSet5GridViewAdapter);

        //setup statSet6
        arrayOfGridViewItems = new ArrayList<>();
        statSet6GridViewAdapter = new StatisticsGridViewAdapter(getApplicationContext(), arrayOfGridViewItems);
        createEntriesGridViewSet6(statSet6GridViewAdapter);
        statSet6GridView.setAdapter(statSet6GridViewAdapter);


    }


    public ArrayList<StatisticsRecyclerViewItem> createEntriesRecyclerViewSet1() {
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        ArrayList<StatisticsRecyclerViewItem> list = new ArrayList<>();

        StatisticsRecyclerViewItem sessionQuantity;
        StatisticsRecyclerViewItem routesQuantity;
        StatisticsRecyclerViewItem averageRoutesSession;

        if (cursor.getCount() > 0) {
            //creating the items
            int counter = 0;
            int routesCounter = 0;
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    routesCounter = routesCounter + getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_VERY_EASY) +
                            getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_EASY) +
                            getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_ADVANCED) +
                            getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_HARD) +
                            getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_VERY_HARD) +
                            getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_EXTREMELY_HARD) +
                            getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_SURPRISING);
                    counter++;
                }
                cursor.moveToNext();
            }

            if (counter == 0) {
                counter = 1;
            }

            sessionQuantity = new StatisticsRecyclerViewItem(getTotalSessions() + "", "Sessions");
            routesQuantity = new StatisticsRecyclerViewItem(routesCounter + "", "Routes");
            averageRoutesSession = new StatisticsRecyclerViewItem((routesCounter / counter) + "", "Routes/Session");

        } else {

            sessionQuantity = new StatisticsRecyclerViewItem("0", "Sessions");
            routesQuantity = new StatisticsRecyclerViewItem("0", "Routes");
            averageRoutesSession = new StatisticsRecyclerViewItem("0", "Routes/Session");

        }

        list.add(sessionQuantity);
        list.add(routesQuantity);
        list.add(averageRoutesSession);

        return list;


    }

    public void createEntriesGridViewSet2(StatisticsGridViewAdapter gridViewAdapter) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();

        StatGridViewItem stat1;
        StatGridViewItem stat2;
        StatGridViewItem stat3;
        StatGridViewItem stat4;

        if (cursor.getCount() > 0) {
            int totalTime = 0;
            int totalStartTime = 0;
            int totalEndTime = 0;
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    totalTime += (getMinutesFromTimeFromDb(cursor, BoulderEntry.COLUMN_NAME_END_TIME) -
                            getMinutesFromTimeFromDb(cursor, BoulderEntry.COLUMN_NAME_START_TIME));
                    totalStartTime += getMinutesFromTimeFromDb(cursor, BoulderEntry.COLUMN_NAME_START_TIME);
                    totalEndTime += getMinutesFromTimeFromDb(cursor, BoulderEntry.COLUMN_NAME_END_TIME);
                }
                cursor.moveToNext();
            }

            int totalSessions = getTotalSessions();
            if (totalSessions == 0) {
                totalSessions = 1;
            }


            stat1 = new StatGridViewItem(timeInHour(totalTime), "Total Time (in hours)");
            stat2 = new StatGridViewItem(timeInHour(totalTime / totalSessions), "Average Time (in hours)");
            stat3 = new StatGridViewItem(convertMinutesToTime(totalStartTime / totalSessions), "Average Start");
            stat4 = new StatGridViewItem(convertMinutesToTime(totalEndTime / totalSessions), "Average End");

        } else {

            stat1 = new StatGridViewItem("0", "Total Time (in hours)");
            stat2 = new StatGridViewItem("0", "Average Time (in hours)");
            stat3 = new StatGridViewItem("00:00", "Average Start");
            stat4 = new StatGridViewItem("00:00", "Average End");

        }

        gridViewAdapter.add(stat1);
        gridViewAdapter.add(stat2);
        gridViewAdapter.add(stat3);
        gridViewAdapter.add(stat4);


    }

    public ArrayList<StatisticsRecyclerViewItem> createEntriesRecyclerViewSet3() {
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        ArrayList<StatisticsRecyclerViewItem> list = new ArrayList<>();

        StatisticsRecyclerViewItem stat1;
        StatisticsRecyclerViewItem stat2;
        StatisticsRecyclerViewItem stat3;
        StatisticsRecyclerViewItem stat4;
        StatisticsRecyclerViewItem stat5;
        StatisticsRecyclerViewItem stat6;
        StatisticsRecyclerViewItem stat7;

        if (cursor.getCount() > 0) {
            int diff1 = 0;
            int diff2 = 0;
            int diff3 = 0;
            int diff4 = 0;
            int diff5 = 0;
            int diff6 = 0;
            int diff7 = 0;
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    diff1 += getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_VERY_EASY);
                    diff2 += getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_EASY);
                    diff3 += getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_ADVANCED);
                    diff4 += getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_HARD);
                    diff5 += getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_VERY_HARD);
                    diff6 += getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_EXTREMELY_HARD);
                    diff7 += getIntFromDifficulty(cursor, BoulderEntry.COLUMN_NAME_SURPRISING);
                }
                cursor.moveToNext();
            }

            stat1 = new StatisticsRecyclerViewItem(diff1 + "", "Level 1");
            stat2 = new StatisticsRecyclerViewItem(diff2 + "", "Level 2");
            stat3 = new StatisticsRecyclerViewItem(diff3 + "", "Level 3");
            stat4 = new StatisticsRecyclerViewItem(diff4 + "", "Level 4");
            stat5 = new StatisticsRecyclerViewItem(diff5 + "", "Level 5");
            stat6 = new StatisticsRecyclerViewItem(diff6 + "", "Level 6");
            stat7 = new StatisticsRecyclerViewItem(diff7 + "", "Surprising");

        } else {

            stat1 = new StatisticsRecyclerViewItem("0", "Level 1");
            stat2 = new StatisticsRecyclerViewItem("0", "Level 2");
            stat3 = new StatisticsRecyclerViewItem("0", "Level 3");
            stat4 = new StatisticsRecyclerViewItem("0", "Level 4");
            stat5 = new StatisticsRecyclerViewItem("0", "Level 5");
            stat6 = new StatisticsRecyclerViewItem("0", "Level 6");
            stat7 = new StatisticsRecyclerViewItem("0", "Surprising");

        }

        list.add(stat1);
        list.add(stat2);
        list.add(stat3);
        list.add(stat4);
        list.add(stat5);
        list.add(stat6);
        list.add(stat7);

        return list;


    }

    public void createEntriesGridViewSet4(StatisticsGridViewAdapter gridViewAdapter) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();

        StatGridViewItem stat1;
        StatGridViewItem stat2;
        StatGridViewItem stat3;

        if (cursor.getCount() > 0) {
            //String key , Integer value ( reverse ) if key is a duplicate increment the value
            HashMap<String, Integer> weekDayCountMap = new HashMap<>();
            HashMap<String, Integer> monthCountMap = new HashMap<>();
            HashMap<String, Integer> yearCountMap = new HashMap<>();


            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    String weekDay = getWeekDay(cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_DATE)));
                    Integer countWD = weekDayCountMap.get(weekDay);
                    if (countWD == null) countWD = new Integer(0);
                    countWD++;
                    weekDayCountMap.put(weekDay, countWD);

                    String month = getMonth(cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_DATE)));
                    Integer countMonth = monthCountMap.get(month);
                    if (countMonth == null) countMonth = new Integer(0);
                    countMonth++;
                    monthCountMap.put(month, countMonth);

                    String year = getYear(cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_DATE)));
                    Integer countYear = yearCountMap.get(year);
                    if (countYear == null) countYear = new Integer(0);
                    countYear++;
                    yearCountMap.put(year, countYear);


                }
                cursor.moveToNext();
            }

            stat1 = new StatGridViewItem(getMostRepeatedComponent(weekDayCountMap).getKey(), "Most Active Weekday");
            stat2 = new StatGridViewItem(getMostRepeatedComponent(monthCountMap).getKey(), "Most Active Month");
            stat3 = new StatGridViewItem(getMostRepeatedComponent(yearCountMap).getKey(), "Most Active Year");

        } else {

            stat1 = new StatGridViewItem("No Data", "Most Active Weekday");
            stat2 = new StatGridViewItem("No Data", "Most Active Month");
            stat3 = new StatGridViewItem("No Data", "Most Active Year");

        }

        gridViewAdapter.add(stat1);
        gridViewAdapter.add(stat2);
        gridViewAdapter.add(stat3);


    }

    public void createEntriesGridViewSet5(StatisticsGridViewAdapter gridViewAdapter) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();

        StatGridViewItem stat1;
        StatGridViewItem stat2;

        if (cursor.getCount() > 0) {
            HashMap<String, Integer> sessionRatingCountMap = new HashMap<>();
            float totalRating = 0f;
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    String sessionRating = cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_RATING));
                    Integer countRating = sessionRatingCountMap.get(sessionRating);
                    if (countRating == null) countRating = new Integer(0);
                    countRating++;
                    sessionRatingCountMap.put(sessionRating, countRating);

                    totalRating += Float.parseFloat(cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_RATING)));

                }
                cursor.moveToNext();
            }


            int totalSessions = getTotalSessions();
            if (totalSessions == 0) {
                totalSessions = 1;
            }

            String averageRating = totalRating / totalSessions + "";
            if (averageRating.length() > 4) {
                averageRating = averageRating.substring(0, 4);
            }

            stat1 = new StatGridViewItem(averageRating + " / 5", "Average Rating");
            stat2 = new StatGridViewItem(getMostRepeatedComponent(sessionRatingCountMap).getKey() + " / 5", "Most Given Rating");

        } else {

            stat1 = new StatGridViewItem("No Data", "Average Rating");
            stat2 = new StatGridViewItem("No Data", "Most Given Rating");

        }

        gridViewAdapter.add(stat1);
        gridViewAdapter.add(stat2);


    }

    public void createEntriesGridViewSet6(StatisticsGridViewAdapter gridViewAdapter) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();

        StatGridViewItem stat1;
        StatGridViewItem stat2;

        if (cursor.getCount() > 0) {
            HashMap<String, Integer> locationsCountMap = new HashMap<>();
            ArrayList<String> locations = new ArrayList<>();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    String location = cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_LOCATION));
                    if (!locations.contains(location)) {
                        locations.add(location);
                    }


                    Integer countLocation = locationsCountMap.get(location);
                    if (countLocation == null) countLocation = new Integer(0);
                    countLocation++;
                    locationsCountMap.put(location, countLocation);
                }
                cursor.moveToNext();
            }


            stat1 = new StatGridViewItem(getMostRepeatedComponent(locationsCountMap).getKey() + " (" + getMostRepeatedComponent(locationsCountMap).getValue() + ")", "Most Visited Location");
            stat2 = new StatGridViewItem(locations.size() + "", "Different Visited Locations");


        } else {

            stat1 = new StatGridViewItem("No Data", "Most Visited Location");
            stat2 = new StatGridViewItem("No Data", "Different Visited Locations");


        }

        gridViewAdapter.add(stat1);
        gridViewAdapter.add(stat2);


    }


    //for number Strings if != "" return int else return 0 for dataSet1
    public int getIntFromDifficulty(Cursor cursor, String columnName) {
        String difficulty = cursor.getString(cursor.getColumnIndex(columnName));
        if (!difficulty.equals("")) {
            return Integer.parseInt(difficulty);
        }
        return 0;
    }

    //get total Sessions for dataSet1 and dataSet2
    public int getTotalSessions() {
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        int counter = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    counter++;
                }
                cursor.moveToNext();
            }
        }

        return counter;
    }

    //get minutes for dataSet2
    public int getMinutesFromTimeFromDb(Cursor cursor, String columnName) {
        int time;
        int hours;
        int minutes;
        String timeText = cursor.getString(cursor.getColumnIndex(columnName));
        if (!timeText.equals("")) {
            String[] timeParts = timeText.split(":");
            hours = Integer.parseInt(timeParts[0]) * 60;
            minutes = Integer.parseInt(timeParts[1]);
            time = hours + minutes;
            return time;
        }
        return 0;
    }

    //convert for dataSet2
    public String convertMinutesToTime(int minutes) {
        String minutesText;
        String hoursText;
        if (minutes != 0) {
            hoursText = minutes / 60 + "";
            minutesText = minutes % 60 + "";
            if (minutesText.equals("0")) {
                minutesText = "00";
            }
            if(minutesText.length() == 1){
                minutesText = "0" + minutesText;
            }
            return hoursText + ":" + minutesText;
        }

        return "00:00";
    }

    //convert for dataSet2
    public String timeInHour(int minutes) {
        if (minutes != 0) {
            String timeText = (minutes / 60.0) + "";
            Log.d("stats", timeText);
            String[] timeParts = timeText.split("\\.");
            Log.d("stats", timeParts[0] + "   " + timeParts[1]);
            if (timeParts[1].equals(".0")) {
                return timeParts[0];
            } else {
                String[] timeParts2 = timeText.split("\\.");
                if (timeParts2[1].length() > 2) {
                    return timeParts2[0] + "." + timeParts2[1].substring(0, 2);
                }
                return timeParts2[0] + "." + timeParts2[1];
            }
        }
        return "0";
    }


    //convert date for dataSet4
    public String getWeekDay(String date) {
        //6.Aug. 2018
        String[] dayAndMonthYear = date.split("\\.", 2);
        //0= 6 ; 1= Aug. 2018
        String[] monthAndYear = dayAndMonthYear[1].split(" ");
        //0 = Aug. 1 = 2018
        String day = dayAndMonthYear[0];
        String month = monthAndYear[0].substring(0, 3);
        String year = monthAndYear[1];
        String inputDate = day + "/" + month + "/" + year;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat1 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        String weekDay = dateFormat1.format(newDate);

        return weekDay;
    }

    //convert date for dataSet4
    public String getMonth(String date) {
        //6.Aug. 2018
        String[] dayAndMonthYear = date.split("\\.", 2);
        //0= 6 ; 1= Aug. 2018
        String[] monthAndYear = dayAndMonthYear[1].split(" ");
        //0 = Aug. 1 = 2018
        String day = dayAndMonthYear[0];
        String month = monthAndYear[0].substring(0, 3);
        String year = monthAndYear[1];
        String inputDate = day + "/" + month + "/" + year;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
        Date newDate = null;
        try {
            newDate = dateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat1 = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        String finalMonth = dateFormat1.format(newDate);

        return finalMonth;
    }

    //convert date for dataSet4
    public String getYear(String date) {
        //6.Aug. 2018
        String[] dayAndMonthYear = date.split("\\.", 2);
        //0= 6 ; 1= Aug. 2018
        String[] monthAndYear = dayAndMonthYear[1].split(" ");
        //0 = Aug. 1 = 2018
        String year = monthAndYear[1];
        return year;
    }

    //get the most repeated date component -> iterate through the String and return the Map Entry with the highest Integer value
    //dataSet 4 and 5
    public Map.Entry<String, Integer> getMostRepeatedComponent(HashMap<String, Integer> countMap) {
        Map.Entry<String, Integer> mostRepeatedComponent = null;
        for (Map.Entry<String, Integer> e : countMap.entrySet()) {
            if (mostRepeatedComponent == null || mostRepeatedComponent.getValue() < e.getValue())
                mostRepeatedComponent = e;
        }
        return mostRepeatedComponent;
    }


}
