package andorid_dev_2017.navigation_drawer.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.Entry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;

public class FragmentShowDetailsBasicInfo extends android.support.v4.app.Fragment {


    View view;
    TextView locationText;
    TextView dateText;
    TextView startText;
    TextView endText;
    TextView expText;
    RatingBar ratingBar;

    String entryId;

    SQLiteDbEntryContract sqLiteDbEntryContract;

    public FragmentShowDetailsBasicInfo() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.show_details_basic_info_fragment, container, false);

        //setup database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getContext());

        //get the extras from the Activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            entryId = extras.getString("entry_id_key");
        }



        //Setup views;
        locationText = view.findViewById(R.id.show_details_fragment_1_location_text_id);
        dateText = view.findViewById(R.id.show_details_fragment_1_date_text_id);
        startText = view.findViewById(R.id.show_details_fragment_1_start_text_id);
        endText = view.findViewById(R.id.show_details_fragment_1_end_text_id);
        expText = view.findViewById(R.id.show_details_fragment_1_exp_text_id);
        ratingBar = view.findViewById(R.id.show_details_fragment_1_rating_bar_id);

        setupData();


        return view;
    }

    public void setupData() {
        Entry boulderEntry = getBoulderEntry(entryId);
        locationText.setText(boulderEntry.getLocation());
        dateText.setText(boulderEntry.getDate());
        startText.setText(boulderEntry.getStartTime());
        endText.setText(boulderEntry.getEndTime());
        expText.setText(boulderEntry.getExp());
        ratingBar.setRating(Float.parseFloat(boulderEntry.getRating()));


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
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_RATING))
                );
                return entry;
            }
            cursor.moveToNext();
        }
        return null;
    }
}
