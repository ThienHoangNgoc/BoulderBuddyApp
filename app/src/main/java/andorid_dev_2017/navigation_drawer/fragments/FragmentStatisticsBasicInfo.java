package andorid_dev_2017.navigation_drawer.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

import andorid_dev_2017.navigation_drawer.ImageGridViewItem;
import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.ShowPicturesFragmentGridViewEntryAdapter;
import andorid_dev_2017.navigation_drawer.StatGridViewItem;
import andorid_dev_2017.navigation_drawer.StatisticsFragmentGridViewAdapter;
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class FragmentStatisticsBasicInfo extends Fragment {

    View view;
    private String loggedInUser;
    private SQLiteDbEntryContract sqLiteDbEntryContract;

    private GridView sessionStatsGridView;
    private GridView difficultyStatsGridView;
    private ArrayList<StatGridViewItem> arrayOfGridViewItems;
    private StatisticsFragmentGridViewAdapter gridViewAdapter;

    public FragmentStatisticsBasicInfo() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistics_basic_info_fragment, container, false);


        //setup database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(view.getContext());

        //get the extras from the Activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }


        //setup views
        sessionStatsGridView = view.findViewById(R.id.statistics_grid_view_sessions_id);
        difficultyStatsGridView = view.findViewById(R.id.statistics_grid_view_difficulty_id);

        //setup gridViewAdapter
        arrayOfGridViewItems = new ArrayList<>();
        gridViewAdapter = new StatisticsFragmentGridViewAdapter(getContext(), arrayOfGridViewItems);
        createEntries(gridViewAdapter);
        sessionStatsGridView.setAdapter(gridViewAdapter);
        difficultyStatsGridView.setAdapter(gridViewAdapter);

        return view;
    }


    public void createEntries(StatisticsFragmentGridViewAdapter adapter) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();

        if (cursor.getCount() > 0) {
            //creating the items
            int counter = 0;
            int routesCounter = 0;
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    routesCounter = routesCounter + getIntFromDb(cursor, BoulderEntry.COLUMN_NAME_VERY_EASY) +
                            getIntFromDb(cursor, BoulderEntry.COLUMN_NAME_EASY) +
                            getIntFromDb(cursor, BoulderEntry.COLUMN_NAME_ADVANCED) +
                            getIntFromDb(cursor, BoulderEntry.COLUMN_NAME_HARD) +
                            getIntFromDb(cursor, BoulderEntry.COLUMN_NAME_VERY_HARD) +
                            getIntFromDb(cursor, BoulderEntry.COLUMN_NAME_EXTREMELY_HARD) +
                            getIntFromDb(cursor, BoulderEntry.COLUMN_NAME_SURPRISING);
                    counter++;
                }
                cursor.moveToNext();
            }
            StatGridViewItem sessionQuantity = new StatGridViewItem(counter + "", "Sessions");
            StatGridViewItem routesQuantity = new StatGridViewItem(routesCounter + "", "Routes");
            StatGridViewItem averageRoutesSession = new StatGridViewItem((routesCounter / counter) + "", "Routes/Session");
            adapter.add(sessionQuantity);
            adapter.add(routesQuantity);
            adapter.add(averageRoutesSession);
        }


    }

    public int getIntFromDb(Cursor cursor, String columnName) {
        String difficulty = cursor.getString(cursor.getColumnIndex(columnName));
        if (!difficulty.equals("")) {
            return Integer.parseInt(difficulty);
        }
        return 0;
    }


}
