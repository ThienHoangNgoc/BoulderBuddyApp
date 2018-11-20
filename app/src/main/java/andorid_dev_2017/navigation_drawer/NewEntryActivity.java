package andorid_dev_2017.navigation_drawer;

import android.Manifest;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbImageDBContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.User;
import andorid_dev_2017.navigation_drawer.sqlite_database.UserEntry;

public class NewEntryActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {


    SQLiteDbEntryContract sqLiteDbEntryContract;
    SQLiteDbUserContract sqLiteDbUserContract;
    SQLiteDbImageDBContract sqLiteDbImageDBContract;

    private String[] location_names;
    private DatePicker datepicker;
    final int REQUEST_CODE_GALLERY = 999;
    private int toastDuration = Toast.LENGTH_SHORT;

    private final static String LOGTAG = "NewEntryAct";


    private AutoCompleteTextView location;
    private EditText dateText;
    private EditText startTime;
    private EditText endTime;
    private EditText veryEasyEditText;
    private EditText easyEditText;
    private EditText advancedEditText;
    private EditText hardEditText;
    private EditText veryHardEditText;
    private EditText extremelyHardEditText;
    private EditText surprisingEditText;
    private RatingBar ratingBar;
    private Button confirmBtn;
    private Button addGridItemBtn;
    private Button showLevelInfoBtn;
    private ExpandableHeightGridView imageGridView;


    private String loggedInUser;

    private ArrayList<ImageGridViewItem> arrayOfImages;
    private NewEntryGridViewEntryAdapter gridViewEntryAdapter;


    //lets onTimeSet know for which view it has to set the text
    private boolean timeViewPosition = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        //Setup Database
        sqLiteDbUserContract = new SQLiteDbUserContract(getApplicationContext());
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getApplicationContext());
        sqLiteDbImageDBContract = new SQLiteDbImageDBContract(getApplicationContext());

        //get String from extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }

        //Setup views
        location = findViewById(R.id.step_1_location_autoComTextV_id);
        dateText = findViewById(R.id.step_1_date_editText_id);
        startTime = findViewById(R.id.step_1_startTime_editText_id);
        endTime = findViewById(R.id.step_1_endTime_editText_id);
        easyEditText = findViewById(R.id.step_2_easy_editText_id);
        veryEasyEditText = findViewById(R.id.step_2_veryEasy_editText_id);
        advancedEditText = findViewById(R.id.step_2_advanced_editText_id);
        hardEditText = findViewById(R.id.step_2_hard_editText_id);
        veryHardEditText = findViewById(R.id.step_2_veryHard_editText_id);
        extremelyHardEditText = findViewById(R.id.step_2_extremelyHard_editText_id);
        surprisingEditText = findViewById(R.id.step_2_surprising_editText_id);
        ratingBar = findViewById(R.id.step_4_ratingBar_id);
        confirmBtn = findViewById(R.id.new_entry_confirm_btn_id);
        addGridItemBtn = findViewById(R.id.step_3_image_grid_view_add_btn_id);
        showLevelInfoBtn = findViewById(R.id.step_2_level_info_btn_id);
        imageGridView = findViewById(R.id.step_3_image_grid_view_id);


        //Setup GridView
        arrayOfImages = new ArrayList<>();
        gridViewEntryAdapter = new NewEntryGridViewEntryAdapter(this, arrayOfImages);
        imageGridView.setAdapter(gridViewEntryAdapter);


        //location autocomplete setup
        location_names = getResources().getStringArray(R.array.hallen);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, location_names);
        location.setAdapter(adapter);

        //setup DatePicker
        datepicker = new DatePicker(this, findViewById(R.id.step_1_date_editText_id).getId());

        //OnClickListeners
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeViewPosition = true;
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), "time picker");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeViewPosition = false;
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), "time picker");
            }
        });
        buttonEffect(confirmBtn, R.color.colorPrimaryDark);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirmClick();
            }
        });
        addGridItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location.clearFocus();
                addGridItemBtn.requestFocus();
                ActivityCompat.requestPermissions(
                        NewEntryActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                        }, REQUEST_CODE_GALLERY

                );
            }
        });

        showLevelInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.new_entry_level_info_dialog, null);
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        //Dialog Setup for every Difficulty
        alertBuilder(veryEasyEditText, R.id.reset_btn_veryEasy);
        alertBuilder(easyEditText, R.id.reset_btn_easy);
        alertBuilder(advancedEditText, R.id.reset_btn_advanced);
        alertBuilder(hardEditText, R.id.reset_btn_hard);
        alertBuilder(veryHardEditText, R.id.reset_btn_veryHard);
        alertBuilder(extremelyHardEditText, R.id.reset_btn_surprising);
        alertBuilder(surprisingEditText, R.id.reset_btn_surprising);


    }

    //create a dialog with a numberPicker and a reset button to clear the content
    public void alertBuilder(EditText editTextView, int resetBtnId) {
        final EditText editText = editTextView;
        Button btn = findViewById(resetBtnId);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewEntryActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.new_entry_difficulty_quantity_dialog, null);
                final NumberPicker picker = mView.findViewById(R.id.new_entry_quantity_number_picker_id);
                //create the range for the NumberPicker
                ArrayList<String> numbers = new ArrayList<>();
                for (int i = 0; i <= 100; i++) {
                    numbers.add(i + "");
                }
                String[] data = numbers.toArray(new String[numbers.size()]);
                picker.setMinValue(0);
                picker.setMaxValue(99);
                if (!editText.getText().toString().equals("")) {
                    picker.setValue(Integer.parseInt(editText.getText().toString()));
                }
                picker.setDisplayedValues(data);

                //initialize select and cancel Buttons with mView
                Button sBtn = mView.findViewById(R.id.new_entry_dialog_select_btn_id);
                Button cBtn = mView.findViewById(R.id.new_entry_dialog_cancel_btn_id);
                //create the Dialog
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                sBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText.setText(picker.getValue() + "");
                        dialog.cancel();
                    }
                });
                cBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

                    }
                });

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

    }

    private void addImageToGrid(NewEntryGridViewEntryAdapter adapter, Bitmap bitmap) {
        ImageGridViewItem gridViewItem = new ImageGridViewItem(bitmap);
        adapter.add(gridViewItem);
        adapter.notifyDataSetChanged();
    }

    public void addImageToDb(NewEntryGridViewEntryAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            sqLiteDbImageDBContract.insertEntry(loggedInUser, getEntryNumber(), adapter.getItem(i).getBitmap());
        }
    }

    //get the last entry Number and adds 1 to get the current entry number
    public String getEntryNumber() {
        int lastEntry;

        Cursor cursor = sqLiteDbEntryContract.readEntry();
        createLog(" cursor count" + cursor.getCount());
        if (cursor.getCount() <= 0) {
            return "1";
        } else {
            cursor.moveToLast();
            lastEntry = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID))) + 1;

        }
        return lastEntry + "";
    }

    //get permission for gallery
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), " no permission", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //use object from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                addImageToGrid(gridViewEntryAdapter, bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //get the exp Values from all the difficulties
    public int getExp() {
        int exp = getQuantityValue(veryEasyEditText) +
                (getQuantityValue(easyEditText) * 3) +
                (getQuantityValue(advancedEditText) * 5) +
                (getQuantityValue(hardEditText) * 10) +
                (getQuantityValue(veryHardEditText) * 15) +
                (getQuantityValue(extremelyHardEditText) * 25) +
                (getQuantityValue(surprisingEditText) * 7);

        return exp;
    }

    //adds the new gained exp to the total exp
    public void addExpToUser() {
        User user = getUserEntry(loggedInUser);
        createLog("User total EXP: " + user.getExp());
        createLog("exp gained: " + getExp());
        double newExp = 0.0;
        newExp = Double.parseDouble(user.getExp()) + (double) getExp();
        sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_EXP, newExp + "", user.getId());
    }


    public void insertEntry() {
        sqLiteDbEntryContract.insertEntry(
                getTextFromView(location),
                getTextFromView(dateText),
                getTextFromView(startTime),
                getTextFromView(endTime),
                getDiffFromView(veryEasyEditText),
                getDiffFromView(easyEditText),
                getDiffFromView(advancedEditText),
                getDiffFromView(hardEditText),
                getDiffFromView(veryHardEditText),
                getDiffFromView(extremelyHardEditText),
                getDiffFromView(surprisingEditText),
                ratingBar.getRating() + "",
                getExp() + "",
                loggedInUser);
    }


    public void onConfirmClick() {

        if (getTextFromView(location).equals("") || getTextFromView(dateText).equals("")
                || getTextFromView(startTime).equals("") || getTextFromView(endTime).equals("")) {
            toastCreator("Fields from Step 1 must no be empty.");
        } else if (!eligibleTime(getTextFromView(startTime), getTextFromView(endTime))) {
            toastCreator("The end time has to be later than the start time.");
        } else if (getTextFromView(veryEasyEditText).equals("") && getTextFromView(easyEditText).equals("") &&
                getTextFromView(advancedEditText).equals("") && getTextFromView(hardEditText).equals("") &&
                getTextFromView(veryHardEditText).equals("") && getTextFromView(extremelyHardEditText).equals("") &&
                getTextFromView(surprisingEditText).equals("")) {
            toastCreator("At least 1 difficulty has to be filled.");
        } else {
            addExpToUser();
            addImageToDb(gridViewEntryAdapter);
            insertEntry();
            rankUpdater();
            Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
            intent.putExtra("username_key", loggedInUser);
            startActivity(intent);
            MainScreenActivity.mainScreenActivityReference.finish();
            finish();
        }


    }


    public boolean eligibleTime(String startTime, String endTime) {
        String[] startTimeParts = startTime.split(":");
        String[] endTimeParts = endTime.split(":");

        //end hour < start hour
        if (getInt(endTimeParts[0]) < getInt(startTimeParts[0])) {
            return false;
        }
        // end hour = start hour and end minutes < start minutes
        if (getInt(endTimeParts[0]) == getInt(startTimeParts[0]) && getInt(endTimeParts[1]) < getInt(startTimeParts[1])) {
            return false;
        }

        if (startTime.equals(endTime)) {
            return false;
        }

        return true;

    }


    //updates the rank and adjusts it according to the users performance after the session
    public void rankUpdater() {
        User user = getUserEntry(loggedInUser);
        String currentRank = user.getRank();
        String currentRankPoints = user.getRankPoints();

        //note: at league division 1 you need to meet the rank up condition for the next higher rank e.g. for silver 1 the rankIndex changes to 1
        switch (currentRank) {
            case "Placement":
                placementCalculator();
                break;
            case "Silver 5":
                rankCalculator(rankChecker(0), currentRank, currentRankPoints, "Silver 4", "LastRank");
                break;
            case "Silver 4":
                rankCalculator(rankChecker(0), currentRank, currentRankPoints, "Silver 3", "Silver 5");
                break;
            case "Silver 3":
                rankCalculator(rankChecker(0), currentRank, currentRankPoints, "Silver 2", "Silver 4");
                break;
            case "Silver 2":
                rankCalculator(rankChecker(0), currentRank, currentRankPoints, "Silver 1", "Silver 3");
                break;
            case "Silver 1":
                rankCalculator(rankChecker(1), currentRank, currentRankPoints, "Gold 5", "Silver 2");
                break;
            case "Gold 5":
                rankCalculator(rankChecker(1), currentRank, currentRankPoints, "Gold 4", "Silver 1");
                break;
            case "Gold 4":
                rankCalculator(rankChecker(1), currentRank, currentRankPoints, "Gold 3", "Gold 5");
                break;
            case "Gold 3":
                rankCalculator(rankChecker(1), currentRank, currentRankPoints, "Gold 2", "Gold 4");
                break;
            case "Gold 2":
                rankCalculator(rankChecker(1), currentRank, currentRankPoints, "Gold 1", "Gold 3");
                break;
            case "Gold 1":
                rankCalculator(rankChecker(2), currentRank, currentRankPoints, "Amethyst 5", "Gold 2");
                break;
            case "Amethyst 5":
                rankCalculator(rankChecker(2), currentRank, currentRankPoints, "Amethyst 4", "Gold 1");
                break;
            case "Amethyst 4":
                rankCalculator(rankChecker(2), currentRank, currentRankPoints, "Amethyst 3", "Amethyst 5");
                break;
            case "Amethyst 3":
                rankCalculator(rankChecker(2), currentRank, currentRankPoints, "Amethyst 2", "Amethyst 4");
                break;
            case "Amethyst 2":
                rankCalculator(rankChecker(2), currentRank, currentRankPoints, "Amethyst 1", "Amethyst 3");
                break;
            case "Amethyst 1":
                rankCalculator(rankChecker(3), currentRank, currentRankPoints, "Ruby 5", "Amethyst 2");
                break;
            case "Ruby 5":
                rankCalculator(rankChecker(3), currentRank, currentRankPoints, "Ruby 4", "Amethyst 1");
                break;
            case "Ruby 4":
                rankCalculator(rankChecker(3), currentRank, currentRankPoints, "Ruby 3", "Ruby 5");
                break;
            case "Ruby 3":
                rankCalculator(rankChecker(3), currentRank, currentRankPoints, "Ruby 2", "Ruby 4");
                break;
            case "Ruby 2":
                rankCalculator(rankChecker(3), currentRank, currentRankPoints, "Ruby 1", "Ruby 3");
                break;
            case "Ruby 1":
                rankCalculator(rankChecker(4), currentRank, currentRankPoints, "Emerald 5", "Ruby 2");
                break;
            case "Emerald 5":
                rankCalculator(rankChecker(4), currentRank, currentRankPoints, "Emerald 4", "Ruby 1");
                break;
            case "Emerald 4":
                rankCalculator(rankChecker(4), currentRank, currentRankPoints, "Emerald 3", "Emerald 5");
                break;
            case "Emerald 3":
                rankCalculator(rankChecker(4), currentRank, currentRankPoints, "Emerald 2", "Emerald 4");
                break;
            case "Emerald 2":
                rankCalculator(rankChecker(4), currentRank, currentRankPoints, "Emerald 1", "Emerald 3");
                break;
            case "Emerald 1":
                rankCalculator(rankChecker(5), currentRank, currentRankPoints, "Diamond", "Emerald 2");
                break;
            case "Diamond":
                rankCalculator(rankChecker(5), currentRank, currentRankPoints, "HighestRank", "Emerald 1");
                break;
            default:
                break;

        }


    }

    public void rankCalculator(boolean rankChecker, String currentRank,
                               String currentRankPoints,
                               String rankUp, String rankDown) {

        String newRankPoints;
        String newRank;
        User user = getUserEntry(loggedInUser);

        if (rankChecker) {
            if (currentRankPoints.equals("100")) {
                if (rankUp.equals("HighestRank")) {
                    newRankPoints = "100";
                    newRank = "Diamond";
                } else {
                    newRankPoints = "0";
                    newRank = rankUp;
                }

            } else {
                newRankPoints = (Integer.parseInt(currentRankPoints) + 20) + "";
                newRank = currentRank;
            }
        } else {
            if (!currentRankPoints.equals("0")) {
                newRankPoints = (Integer.parseInt(currentRankPoints) - 20) + "";
                newRank = currentRank;
            } else {
                //for Silver 5, because there is no rank below Silver 5
                if (rankDown.equals("LastRank")) {
                    newRankPoints = "0";
                    newRank = "Silver 5";
                } else {
                    newRankPoints = "80";
                    newRank = rankDown;
                }

            }
        }

        sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_RANK, newRank, user.getId());
        sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_RANK_POINTS, newRankPoints, user.getId());

    }

    //checks if the rankUp condition is met (complete at least 3 routes according to your rank or higher)
    //index help: silver(0), gold(1), ..., diamond(5), surprising(6)
    //only until the rank ruby(4)(excluding ruby) surprising counts towards a rankUP
    public boolean rankChecker(int rankIndex) {
        ArrayList<String> list = diffValueListCreator();
        int count = 0;
        if (rankIndex < 4) {
            for (int i = rankIndex; i < list.size(); i++) {
                count += Integer.parseInt(list.get(i));
            }
        } else {
            for (int i = rankIndex; i < list.size() - 1; i++) {
                count += Integer.parseInt(list.get(i));
            }
        }

        if (count >= 3) {
            return true;
        }
        return false;
    }

    //get the placement rank according to what you have completed after your first session
    //for more info check placement notes.
    public void placementCalculator() {
        User user = getUserEntry(loggedInUser);
        String rank = "";
        int completedRoutes = getQuantityValue(veryEasyEditText) +
                getQuantityValue(easyEditText) +
                getQuantityValue(advancedEditText) +
                getQuantityValue(hardEditText) +
                getQuantityValue(veryHardEditText) +
                getQuantityValue(extremelyHardEditText) +
                getQuantityValue(surprisingEditText);

        int placementValue = (getQuantityValue(veryEasyEditText) * 3) +
                (getQuantityValue(easyEditText) * 8) +
                (getQuantityValue(advancedEditText) * 13) +
                (getQuantityValue(hardEditText) * 18) +
                (getQuantityValue(veryHardEditText) * 23) +
                (getQuantityValue(extremelyHardEditText) * 26) +
                (getQuantityValue(surprisingEditText) * 14);

        int placementIndex = (placementValue / completedRoutes);

        switch (placementIndex) {
            case 1:
                rank = "Silver 5";
                break;
            case 2:
                rank = "Silver 4";
                break;
            case 3:
                rank = "Silver 3";
                break;
            case 4:
                rank = "Silver 2";
                break;
            case 5:
                rank = "Silver 1";
                break;
            case 6:
                rank = "Gold 5";
                break;
            case 7:
                rank = "Gold 4";
                break;
            case 8:
                rank = "Gold 3";
                break;
            case 9:
                rank = "Gold 2";
                break;
            case 10:
                rank = "Gold 1";
                break;
            case 11:
                rank = "Amethyst 5";
                break;
            case 12:
                rank = "Amethyst 4";
                break;
            case 13:
                rank = "Amethyst 3";
                break;
            case 14:
                rank = "Amethyst 2";
                break;
            case 15:
                rank = "Amethyst 1";
                break;
            case 16:
                rank = "Ruby 5";
                break;
            case 17:
                rank = "Ruby 4";
                break;
            case 18:
                rank = "Ruby 3";
                break;
            case 19:
                rank = "Ruby 2";
                break;
            case 20:
                rank = "Ruby 1";
                break;
            case 21:
                rank = "Emerald 5";
                break;
            case 22:
                rank = "Emerald 4";
                break;
            case 23:
                rank = "Emerald 3";
                break;
            case 24:
                rank = "Emerald 2";
                break;
            case 25:
                rank = "Emerald 1";
                break;
            case 26:
                rank = "Diamond";
                break;
            default:
                rank = "Placement";
                break;

        }

        sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_RANK, rank, user.getId());


    }


    //----Utility methods----


    //sets the text from the TimePicker
    @Override
    public void onTimeSet(TimePicker timePicker, int h, int m) {

        String hour = h + "";
        String minute = m + "";

        if (h < 10) {
            hour = "0" + h;
        }

        if (m < 10) {
            minute = "0" + m;
        }

        if (timeViewPosition) {
            startTime.setText(hour + ":" + minute);
        } else {
            endTime.setText(hour + ":" + minute);
        }
    }

    //gets a user from the db based on the id
    public User getUserEntry(String username) {
        User user;
        Cursor cursor = sqLiteDbUserContract.readEntry();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME)).equals(username)) {
                user = new User(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USER_ID)),
                        username,
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_LAST_LOGIN)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_EXP)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_RANK)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_RANK_POINTS)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_PROFILE_PICTURE)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_LOGIN_STATUS))
                );
                return user;
            }
            cursor.moveToNext();
        }

        return null;

    }

    //get number from difficulty
    public String getDiffFromView(EditText editText) {
        if (editText.getText().toString().equals("")) {
            return "0";
        }
        return editText.getText().toString();
    }

    //checks if the EditText is empty and if not it gets the value as an int
    public int getQuantityValue(EditText editText) {
        int count = 0;
        if (!getTextFromView(editText).equals("")) {
            count = Integer.parseInt(getTextFromView(editText));

        }
        return count;
    }

    //Gets the String from a EditText
    public String getTextFromView(EditText editText) {
        return editText.getText().toString();
    }


    public void toastCreator(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, toastDuration).show();
    }

    public ArrayList<String> diffValueListCreator() {

        ArrayList<String> list = new ArrayList<>();
        list.add(getDiffFromView(veryEasyEditText));
        list.add(getDiffFromView(easyEditText));
        list.add(getDiffFromView(advancedEditText));
        list.add(getDiffFromView(hardEditText));
        list.add(getDiffFromView(veryHardEditText));
        list.add(getDiffFromView(extremelyHardEditText));
        list.add(getDiffFromView(surprisingEditText));

        return list;

    }

    public void createLog(String text) {
        Log.d(LOGTAG, text);
    }

    public int getInt(String string) {
        return Integer.parseInt(string);
    }

    public void buttonEffect(View button, final int colorId) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(), colorId, null), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }


}
