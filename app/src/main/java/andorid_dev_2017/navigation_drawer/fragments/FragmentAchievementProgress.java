package andorid_dev_2017.navigation_drawer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import andorid_dev_2017.navigation_drawer.AchievementProgressRecyclerViewAdapter;
import andorid_dev_2017.navigation_drawer.AchievementProgressRecyclerViewItem;
import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.Entry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class FragmentAchievementProgress extends Fragment {

    View view;
    RecyclerView progressRecyclerView;

    AchievementProgressRecyclerViewAdapter recyclerViewAdapter;
    SQLiteDbEntryContract sqLiteDbEntryContract;
    String loggedInUser;


    public FragmentAchievementProgress() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.achievement_progress_fragment, container, false);

        //get the extras from the Activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }

        //setup views
        progressRecyclerView = view.findViewById(R.id.achievement_progress_fragment_recycler_view_id);

        //setup database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getContext());

        //setup recyclerViewAdapter
        progressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new AchievementProgressRecyclerViewAdapter(getContext(), createEntries());
        progressRecyclerView.setAdapter(recyclerViewAdapter);


        return view;
    }

    private ArrayList<AchievementProgressRecyclerViewItem> createEntries() {
        ArrayList<AchievementProgressRecyclerViewItem> itemList = new ArrayList<>();


        // in ein 2. array dann prüfen ob null wenn nein dann add
        if (createFirstEntryAchievement() != null) {
            itemList.add(createFirstEntryAchievement());
        }

        return itemList;
    }

    public AchievementProgressRecyclerViewItem createFirstEntryAchievement() {
        AchievementProgressRecyclerViewItem item = null;
        if (getTotalSessions() > 0) {
            item = new AchievementProgressRecyclerViewItem("First Entry", 100, true);
        } else {
            item = new AchievementProgressRecyclerViewItem("First Entry", 0, false);
        }
        return item;
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

    //gets an entry from the db based on the id
    public Entry getBoulderEntry(String id) {
        Entry entry;
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID)).equals(id)) {
                entry = new Entry(id,
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_DATE)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_START_TIME)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_END_TIME)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_EASY)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_EASY)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ADVANCED)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_EXTREMELY_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_SURPRISING)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_RATING)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_EXP)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR))
                );
                return entry;
            }
            cursor.moveToNext();
        }
        return null;
    }
}
