package andorid_dev_2017.navigation_drawer;

import android.Manifest;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
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

    final static String LOGTAG = "NewEntryAct";


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
    private GridView imageGridlayout;


    private String loggedInUser;

    private ArrayList<NewEntryGridViewItem> arrayOfEntryItems;
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
        imageGridlayout = findViewById(R.id.step_3_image_grid_view_id);


        //Setup GridView
        arrayOfEntryItems = new ArrayList<>();
        gridViewEntryAdapter = new NewEntryGridViewEntryAdapter(this, arrayOfEntryItems);
        imageGridlayout.setAdapter(gridViewEntryAdapter);


        //location autocomplete setup
        location_names = getResources().getStringArray(R.array.hallen);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, location_names);
        location.setAdapter(adapter);

        //setup Datepicker
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
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConfirmClick();
            }
        });
        addGridItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        NewEntryActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                        }, REQUEST_CODE_GALLERY

                );
            }
        });

        //Dialog Setup for every Difficulty
        alertBuilder(veryEasyEditText, R.layout.new_entry_very_easy_dialog, R.id.veryEasy_number_picker_id,
                R.id.new_entry_dialog_veryEasy_select_btn_id, R.id.new_entry_dialog_veryEasy_cancel_btn_id, R.id.reset_btn_veryEasy);

        alertBuilder(easyEditText, R.layout.new_entry_easy_dialog, R.id.easy_number_picker_id,
                R.id.new_entry_dialog_easy_select_btn_id, R.id.new_entry_dialog_easy_cancel_btn_id, R.id.reset_btn_easy);

        alertBuilder(advancedEditText, R.layout.new_entry_advanced_dialog, R.id.advanced_number_picker_id,
                R.id.new_entry_dialog_advanced_select_btn_id, R.id.new_entry_dialog_advanced_cancel_btn_id, R.id.reset_btn_advanced);

        alertBuilder(hardEditText, R.layout.new_entry_hard_dialog, R.id.hard_number_picker_id,
                R.id.new_entry_dialog_hard_select_btn_id, R.id.new_entry_dialog_hard_cancel_btn_id, R.id.reset_btn_hard);

        alertBuilder(veryHardEditText, R.layout.new_entry_very_hard_dialog, R.id.very_hard_number_picker_id,
                R.id.new_entry_dialog_very_hard_select_btn_id, R.id.new_entry_dialog_very_hard_cancel_btn_id, R.id.reset_btn_veryHard);

        alertBuilder(extremelyHardEditText, R.layout.new_entry_extremely_hard_dialog, R.id.extremely_hard_number_picker_id,
                R.id.new_entry_dialog_extremely_hard_select_btn_id, R.id.new_entry_dialog_extremely_hard_cancel_btn_id, R.id.reset_btn_extremelyHard);

        alertBuilder(surprisingEditText, R.layout.new_entry_surprising_dialog, R.id.surprising_number_picker_id,
                R.id.new_entry_dialog_surprising_select_btn_id, R.id.new_entry_dialog_surprising_cancel_btn_id, R.id.reset_btn_surprising);


    }

    private void addImageToGrid(NewEntryGridViewEntryAdapter adapter, Bitmap bitmap) {
        NewEntryGridViewItem gridViewItem = new NewEntryGridViewItem(bitmap);
        adapter.add(gridViewItem);
        adapter.notifyDataSetChanged();
    }

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

    //create a dialog with a numberPicker and a reset button to clear the content
    public void alertBuilder(EditText editTextView, final int rlayoutId, final int numberPickerId, final int selectBtnId, final int cancelBtnId, int resetBtnId) {
        final EditText editText = editTextView;
        Button btn = findViewById(resetBtnId);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewEntryActivity.this);
                final View mView = getLayoutInflater().inflate(rlayoutId, null);
                final NumberPicker picker = mView.findViewById(numberPickerId);
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
                Button sBtn = mView.findViewById(selectBtnId);
                Button cBtn = mView.findViewById(cancelBtnId);
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

    public void onConfirmClick() {

        if (getTextFromView(location).equals("") || getTextFromView(dateText).equals("")
                || getTextFromView(startTime).equals("") || getTextFromView(endTime).equals("")) {
            toastCreator("Fields from Step 1 must no be empty.");
        } else if (getTextFromView(veryEasyEditText).equals("") && getTextFromView(easyEditText).equals("") &&
                getTextFromView(advancedEditText).equals("") && getTextFromView(hardEditText).equals("") &&
                getTextFromView(veryHardEditText).equals("") && getTextFromView(extremelyHardEditText).equals("") &&
                getTextFromView(surprisingEditText).equals("")) {
            toastCreator("At least 1 difficulty has to be filled.");
        } else {
            addExpToUser();
            addImageToDb(gridViewEntryAdapter);
            insertEntry();
            Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
            intent.putExtra("username_key", loggedInUser);
            startActivity(intent);
            finish();
        }


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

    //get the exp Values from all the difficulties
    public int getExp() {
        int exp = getExpValue(veryEasyEditText) +
                (getExpValue(easyEditText) * 3) +
                (getExpValue(advancedEditText) * 5) +
                (getExpValue(hardEditText) * 10) +
                (getExpValue(veryHardEditText) * 15) +
                (getExpValue(extremelyHardEditText) * 25) +
                (getExpValue(surprisingEditText) * 7);

        return exp;
    }

    //adds the new gained exp to the total exp
    public void addExpToUser() {
        User user = getUserEntry(loggedInUser);
        createLog("User total EXP: " + user.getExp());
        createLog("exp gained: " + getExp());
        double newExp = 0.0;
        newExp = Double.parseDouble(user.getExp()) + (double) getExp();
        sqLiteDbUserContract.updateOneColum(UserEntry.COLUMN_NAME_EXP, newExp + "", user.getId());
    }

    //checks if the EditText is not empty and gets the value as an int
    public int getExpValue(EditText editText) {
        int exp = 0;
        if (!getTextFromView(editText).equals("")) {
            exp = Integer.parseInt(getTextFromView(editText));

        }
        return exp;
    }

    //Gets the String from a EditText
    public String getTextFromView(EditText editText) {
        return editText.getText().toString();
    }

    //get number from diff
    public String getDiffFromView(EditText editText) {
        if (editText.getText().toString().equals("")) {
            return "0";
        }
        return editText.getText().toString();
    }


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
                        BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex(UserEntry.COLUMN_NAME_PROFILE_PICTURE)), 0,
                                (cursor.getBlob(cursor.getColumnIndex(UserEntry.COLUMN_NAME_PROFILE_PICTURE)).length)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_LOGIN_STATUS))
                );
                return user;
            }
            cursor.moveToNext();
        }

        return null;

    }


    //get the last entry Number and adds 1 to get the current entry number
    public String getEntryNumber() {
        int lastEntry;
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        if (cursor.getCount() < 0) {
            return "1";
        } else {
            cursor.moveToLast();
            lastEntry = Integer.parseInt(cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID))) + 1;

        }
        return lastEntry + "";
    }

    public void addImageToDb(NewEntryGridViewEntryAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            sqLiteDbImageDBContract.insertEntry(loggedInUser, getEntryNumber(), adapter.getItem(i).getBitmap());
        }
    }

    public void toastCreator(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, toastDuration).show();
    }

    public void createLog(String text) {
        Log.d(LOGTAG, text);
    }


}
