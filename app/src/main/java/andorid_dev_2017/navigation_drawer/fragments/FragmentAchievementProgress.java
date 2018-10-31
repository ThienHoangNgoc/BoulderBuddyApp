package andorid_dev_2017.navigation_drawer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import andorid_dev_2017.navigation_drawer.AchievementProgressRecyclerViewAdapter;
import andorid_dev_2017.navigation_drawer.AchievementProgressRecyclerViewItem;
import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.sqlite_database.Achievement;
import andorid_dev_2017.navigation_drawer.sqlite_database.AchievementEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.Entry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbAchievementContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class FragmentAchievementProgress extends Fragment {

    View view;
    RecyclerView progressRecyclerView;

    AchievementProgressRecyclerViewAdapter recyclerViewAdapter;
    SQLiteDbEntryContract sqLiteDbEntryContract;
    SQLiteDbAchievementContract sqLiteDbAchievementContract;
    String loggedInUser;

    static final String LOGTAG = "achievementProgress";


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
        sqLiteDbAchievementContract = new SQLiteDbAchievementContract(getContext());

        //setup recyclerViewAdapter
        progressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAdapter = new AchievementProgressRecyclerViewAdapter(getContext(), createEntries());
        progressRecyclerView.setAdapter(recyclerViewAdapter);


        return view;
    }

    private ArrayList<AchievementProgressRecyclerViewItem> createEntries() {
        ArrayList<AchievementProgressRecyclerViewItem> itemList = new ArrayList<>();


        AchievementProgressRecyclerViewItem achievement1 = createFirstAchievement();
        AchievementProgressRecyclerViewItem achievement2 = createSecondAchievement();


        addIfNotNull(itemList, achievement1);
        addIfNotNull(itemList, achievement2);

        return itemList;
    }

    //creates a  AchievementProgressRecyclerViewItem object and inserts into db if it doesnt already exists
    //Create a Entry
    public AchievementProgressRecyclerViewItem createFirstAchievement() {
        AchievementProgressRecyclerViewItem item = null;
        if (getTotalSessions() > 0) {
            item = new AchievementProgressRecyclerViewItem("A New Journey", 100, true);
            if (!isInAchievementDB("A New Journey", loggedInUser)) {
                sqLiteDbAchievementContract.insertEntry("A New Journey", 1, loggedInUser);
            }
        }
        return item;
    }

    //Complete 100 very Easy
    public AchievementProgressRecyclerViewItem createSecondAchievement() {
        AchievementProgressRecyclerViewItem item = null;
        int count = getNumberOfDifficulty(BoulderEntry.COLUMN_NAME_VERY_EASY);
        if (count > 0) {
            if (count >= 100) {
                item = new AchievementProgressRecyclerViewItem("Level 1 Prodigy", 100, true);
                if (!isInAchievementDB("Level 1 Prodigy", loggedInUser)) {
                    sqLiteDbAchievementContract.insertEntry("Level 1 Prodigy", 1, loggedInUser);
                } else {
                    Achievement achievement = getAchievementEntryByName("Level 1 Prodigy");
                    sqLiteDbAchievementContract.updateStatusColumn(1, achievement.getId());
                }
            }
            if (count < 100) {
                item = new AchievementProgressRecyclerViewItem("Level 1 Prodigy", count, false);
                if (!isInAchievementDB("Level 1 Prodigy", loggedInUser)) {
                    sqLiteDbAchievementContract.insertEntry("Level 1 Prodigy", 0, loggedInUser);

                } else {
                    Achievement achievement = getAchievementEntryByName("Level 1 Prodigy");
                    sqLiteDbAchievementContract.updateStatusColumn(0, achievement.getId());

                }

            }
        }

        return item;

    }


    //get total Sessions
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

    public int getNumberOfDifficulty(String columnName) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        int count = 0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; cursor.getCount() > i; i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    count += getIntFromDifficulty(cursor, columnName);
                }
                cursor.moveToNext();

            }


        }
        return count;
    }

    //for number Strings if != "" return int else return 0 for dataSet1
    public int getIntFromDifficulty(Cursor cursor, String columnName) {
        String difficulty = cursor.getString(cursor.getColumnIndex(columnName));
        if (!difficulty.equals("")) {
            return Integer.parseInt(difficulty);
        }
        return 0;
    }


    //gets a achievementEntry based on the id
    public Achievement getAchievementEntryById(String id) {
        Achievement achievement = null;
        Cursor cursor = sqLiteDbAchievementContract.readEntry();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_ID)).equals(id)) {
                achievement = new Achievement(
                        id,
                        cursor.getString(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_NAME_ID)),
                        cursor.getInt(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_STATUS_ID)),
                        cursor.getString(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_CREATOR_ID)));
            }
            cursor.moveToNext();
        }

        return achievement;

    }

    //gets a achievementEntry based on the name
    public Achievement getAchievementEntryByName(String name) {
        Achievement achievement = null;
        Cursor cursor = sqLiteDbAchievementContract.readEntry();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_NAME_ID)).equals(name)) {
                achievement = new Achievement(
                        cursor.getString(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_ID)),
                        name,
                        cursor.getInt(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_STATUS_ID)),
                        cursor.getString(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_CREATOR_ID)));
                return achievement;
            }
            cursor.moveToNext();
        }

        return achievement;

    }


    public boolean isInAchievementDB(String achievementName, String currentUser) {
        Cursor cursor = sqLiteDbAchievementContract.readEntry();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; cursor.getCount() > i; i++) {
                Achievement achievement = getAchievementEntryById(cursor.getString(cursor.getColumnIndex(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_ID)));
                if (achievement.getName().equals(achievementName) && achievement.getCreator().equals(currentUser)) {
                    return true;
                }
                cursor.moveToNext();

            }
        }
        return false;
    }

    public void addIfNotNull(ArrayList<AchievementProgressRecyclerViewItem> list, AchievementProgressRecyclerViewItem item) {
        if (item != null) {
            list.add(item);
        }

    }

    public void createLog(String text) {
        Log.d(LOGTAG, text);
    }


}
