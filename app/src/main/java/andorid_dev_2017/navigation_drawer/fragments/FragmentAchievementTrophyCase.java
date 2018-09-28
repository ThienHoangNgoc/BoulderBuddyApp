package andorid_dev_2017.navigation_drawer.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.sqlite_database.Achievement;
import andorid_dev_2017.navigation_drawer.sqlite_database.AchievementEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbAchievementContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class FragmentAchievementTrophyCase extends Fragment {

    View view;
    ImageView achievementFirstEntry;
    ImageView achievementSecondEntry;

    SQLiteDbAchievementContract sqLiteDbAchievementContract;
    SQLiteDbEntryContract sqLiteDbEntryContract;
    String loggedInUser;

    static final String LOGTAG = "trophyCase";

    public FragmentAchievementTrophyCase() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.achievement_trophy_case_fragment, container, false);

        //setup database
        sqLiteDbAchievementContract = new SQLiteDbAchievementContract(getContext());
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getContext());

        //get the extras from the Activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }

        //setup views
        achievementFirstEntry = view.findViewById(R.id.achievement_1_id);
        achievementSecondEntry = view.findViewById(R.id.achievement_2_id);


        setVisibilityIfEligible("A New Journey", achievementFirstEntry, R.layout.achievement_1_description_dialog);
        setVisibilityIfEligible("Very Easy Grandmaster", achievementSecondEntry, R.layout.achievement_2_description_dialog);


        return view;
    }


    public void setOnclickAndCreateDialog(ImageView achievement, final int layout_id) {
        achievement.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                                               View mView = getLayoutInflater().inflate(layout_id, null);
                                               mBuilder.setView(mView);
                                               AlertDialog dialog = mBuilder.create();
                                               //show the background color of the XML
                                               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                               dialog.show();
                                           }
                                       }
        );
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


    public void setVisibilityIfEligible(String achievementName, ImageView achievementView, int layout_id) {
        //if there are no entries show no achievements ----- Note:Emergency solution or right one? needs more testing
        Cursor cursor = sqLiteDbEntryContract.readEntry();

        if (cursor.getCount() > 0) {
            if (isInAchievementDB(achievementName, loggedInUser)) {
                Achievement achievement = getAchievementEntryByName(achievementName);
                if (achievement.getStatus() == 1) {
                    setOnclickAndCreateDialog(achievementView, layout_id);
                    achievementView.setVisibility(View.VISIBLE);

                }
            }
        }


    }

    public void createLog(String text) {
        Log.d(LOGTAG, text);
    }

}
