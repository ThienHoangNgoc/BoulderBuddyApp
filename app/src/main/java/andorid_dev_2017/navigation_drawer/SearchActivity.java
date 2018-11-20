package andorid_dev_2017.navigation_drawer;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.Entry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;

public class SearchActivity extends AppCompatActivity {


    private final static String LOGTAG = "SearchActivity";

    private int toastDuration = Toast.LENGTH_SHORT;
    private String loggedInUser;
    private int searchMonth;
    private int searchYear;

    private SQLiteDbEntryContract sqLiteDbEntryContract;
    private SQLiteDbUserContract sqLiteDbUserContract;
    public EntryAdapter entryAdapter;


    private ListView searchResultListView;
    private EditText searchEditText;

    private Button searchBtn;
    private Button searchClearBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //setup database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getApplicationContext());
        sqLiteDbUserContract = new SQLiteDbUserContract(getApplicationContext());


        //Get String from extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }

        //setup views
        searchResultListView = findViewById(R.id.search_list_view_id);
        searchEditText = findViewById(R.id.search_edit_text_search_date_id);
        searchBtn = findViewById(R.id.search_search_btn_id);
        searchClearBtn = findViewById(R.id.search_reset_btn_id);


        //Setup EntryAdapter
        ArrayList<EntryItem> arrayOfEntryItems = new ArrayList<>();
        entryAdapter = new EntryAdapter(this, arrayOfEntryItems);


        //setup OnClickListeners
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.search_dialog, null);
                final NumberPicker monthPicker = mView.findViewById(R.id.search_dialog_month_picker_id);
                String[] data = getResources().getStringArray(R.array.months);
                monthPicker.setMinValue(0);
                monthPicker.setMaxValue(data.length - 1);
                monthPicker.setValue(getCurrentMonth());
                monthPicker.setDisplayedValues(data);
                ArrayList<String> numbers = new ArrayList<>();
                for (int i = 1900; i <= 2200; i++) {
                    numbers.add(i + "");
                }
                final NumberPicker yearPicker = mView.findViewById(R.id.search_dialog_year_picker_id);
                String[] data2 = numbers.toArray(new String[numbers.size()]);
                yearPicker.setMinValue(1900);
                yearPicker.setMaxValue(2200);
                yearPicker.setValue(getCurrentYear());
                yearPicker.setDisplayedValues(data2);

                Button selectBtn = mView.findViewById(R.id.search_dialog_select_btn_id);
                Button cancelBtn = mView.findViewById(R.id.search_dialog_cancel_btn_id);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                selectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchEditText.setText(intToMonth(monthPicker) + " " + yearPicker.getValue());
                        searchMonth = monthPicker.getValue();
                        searchYear = yearPicker.getValue();
                        dialog.cancel();
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

                    }
                });

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getTextFromView(searchEditText).equals("")) {
                    toastCreator("Pls enter a date.");
                } else {
                    createEmptyList(entryAdapter);
                    createEntries(entryAdapter, getMonthYearDate(searchMonth, searchYear));
                    if (entryAdapter.isEmpty()) {
                        toastCreator("There are no entries for the given date.");
                    } else {
                        searchResultListView.setAdapter(entryAdapter);
                    }

                }


            }
        });

        searchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText("");
            }
        });


    }

    //call this in adapter
    public void onDeleteClick(EntryItem entryItem) {
        entryAdapter.remove(entryItem);
        entryAdapter.notifyDataSetChanged();
    }


    //create dummy entries
    public void createEntries(EntryAdapter entryAdapter, String date) {
        ArrayList<EntryItem> entryList = new ArrayList<>();
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser) &&
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_DATE)).contains(date)) {
                    Entry boulderEntry = getBoulderEntry(cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID)));
                    EntryItem entryItem = new EntryItem(boulderEntry.getId(), boulderEntry.getLocation(),
                            boulderEntry.getDate(), Float.parseFloat(boulderEntry.getRating()));
                    entryList.add(entryItem);
                }
                cursor.moveToNext();

            }

        }

        for (int j = 0; j < entryList.size(); j++) {
            entryAdapter.add(entryList.get(j));
        }


    }

    public void createEmptyList(EntryAdapter entryAdapter) {
        entryAdapter.clear();
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


    public String intToMonth(NumberPicker picker) {
        String date = "";
        switch (picker.getValue() + 1) {
            case 1:
                date = "January";
                break;
            case 2:
                date = "February";
                break;
            case 3:
                date = "March";
                break;
            case 4:
                date = "April";
                break;
            case 5:
                date = "May";
                break;
            case 6:
                date = "June";
                break;
            case 7:
                date = "July";
                break;
            case 8:
                date = "August";
                break;
            case 9:
                date = "September";
                break;
            case 10:
                date = "October";
                break;
            case 11:
                date = "November";
                break;
            case 12:
                date = "December";
                break;


        }

        return date;

    }

    public String getMonthYearDate(int month, int year) {
        String monthString;

        switch (month + 1) {
            case 1:
                monthString = "Jan.";
                break;
            case 2:
                monthString = "Feb.";
                break;
            case 3:
                monthString = "Mar.";
                break;
            case 4:
                monthString = "Apr.";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "Jun.";
                break;
            case 7:
                monthString = "Jul.";
                break;
            case 8:
                monthString = "Aug.";
                break;
            case 9:
                monthString = "Sep.";
                break;
            case 10:
                monthString = "Oct.";
                break;
            case 11:
                monthString = "Nov.";
                break;
            case 12:
                monthString = "Dec.";
                break;
            default:
                monthString = "";
                break;

        }

        return monthString + " " + year;

    }

    //Gets the String from a EditText
    public String getTextFromView(EditText editText) {
        return editText.getText().toString();
    }


    public void toastCreator(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, toastDuration).show();
    }

    public int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }
}
