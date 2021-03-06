package andorid_dev_2017.navigation_drawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.Entry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.User;
import andorid_dev_2017.navigation_drawer.sqlite_database.UserEntry;

public class MainScreenActivity extends AppCompatActivity {

    final static String LOGTAG = "MainScreenAct";

    //create a reference object from MainScreenActivity for NewEntryActivity, to close app properly after creating a new entry
    public static Activity mainScreenActivityReference;

    private DrawerLayout mDrawlayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView nView;
    private View headerView;
    private FloatingActionButton newEntryBtn;
    private ListView entryList;

    //Header Views
    private TextView usernameTextView;
    private TextView userLvlTextView;
    private ProgressBar progressBar;
    private ImageView userProfileImageView;


    private SQLiteDbUserContract sqLiteDbUserContract;
    private SQLiteDbEntryContract sqLiteDbEntryContract;

    private String loggedInUser;
    private boolean logout;

    public EntryAdapter entryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainScreenActivityReference = this;

        //Setup Database
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getApplicationContext());
        sqLiteDbUserContract = new SQLiteDbUserContract(getApplicationContext());


        //Get String from extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }


        //Set the id of the active user to SharedPreferences for next login
        SharedPreferences prefs = this.getSharedPreferences(
                "lastUserId", Context.MODE_PRIVATE);
        prefs.edit().putString("user_id_key", getUserEntry(loggedInUser).getId()).apply();


        //Setup views
        mDrawlayout = findViewById(R.id.main_draw_layout_id);
        nView = findViewById(R.id.main_navigation_view_id);
        newEntryBtn = findViewById(R.id.main_add_new_entry_btn_id);
        entryList = findViewById(R.id.main_latest_entries_listView_id);

        //Setup Drawer
        mToggle = new ActionBarDrawerToggle(this, mDrawlayout, R.string.open, R.string.close);
        mDrawlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nView);

        //Setup Header for the NavigationView and Setup the views in the header
        headerView = nView.getHeaderView(0);
        usernameTextView = headerView.findViewById(R.id.main_header_username_id);
        userLvlTextView = headerView.findViewById(R.id.main_header_user_lvl_text_view_id);
        progressBar = headerView.findViewById(R.id.main_header_user_lvl_progress_id);
        userProfileImageView = headerView.findViewById(R.id.main_header_user_profile_image_view_id);


        //Setup onClickListener
        newEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewEntryActivity.class);
                intent.putExtra("username_key", loggedInUser);
                startActivity(intent);


            }
        });


        //Setup EntryAdapter and create entries
        ArrayList<EntryItem> arrayOfEntryItems = new ArrayList<>();
        entryAdapter = new EntryAdapter(this, arrayOfEntryItems);
        createEntries(entryAdapter);
        entryList.setAdapter(entryAdapter);


        //Display Info in Header
        setProfileData(loggedInUser);

        //Update last login date ( for rank )
        setLastLogin();

        //for logout
        logout = false;


    }


    //method called in EntryAdapter, when entry is deleted
    public void onDeleteClick(EntryItem entryItem) {
        entryAdapter.remove(entryItem);
        entryAdapter.notifyDataSetChanged();
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
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("username_key", loggedInUser);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("username_key", loggedInUser);
                startActivity(intent);
                break;
            case R.id.achievements:
                intent = new Intent(getApplicationContext(), AchievementActivity.class);
                intent.putExtra("username_key", loggedInUser);
                startActivity(intent);
                break;
            case R.id.statistics:
                intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                intent.putExtra("username_key", loggedInUser);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                intent.putExtra("username_key", loggedInUser);
                startActivity(intent);
                break;
            case R.id.logout:
                onLogoutClick();
                break;
            default:
                break;
        }
        mDrawlayout.closeDrawers();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setMessage("Exit app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onSuperBackPressed();
                    }
                }).setNegativeButton("No", null).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getApplicationContext().getColor(R.color.defaultDialogTextColor));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getApplicationContext().getColor(R.color.defaultDialogTextColor));
            }
        });
        dialog.show();

    }

    public void onSuperBackPressed() {
        super.onBackPressed();
    }

    public void onLogoutClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setMessage("Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_LOGIN_STATUS, "logout", getUserEntry(loggedInUser).getId());
                    }
                }).setNegativeButton("No", null).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getApplicationContext().getColor(R.color.defaultDialogTextColor));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getApplicationContext().getColor(R.color.defaultDialogTextColor));
            }
        });
        dialog.show();

    }


    //Setup Drawer Content
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawItemSelected(item);
                return true;
            }
        });
    }

    //create Entries
    public void createEntries(EntryAdapter entryAdapter) {
        ArrayList<EntryItem> entryList = new ArrayList<>();
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(loggedInUser)) {
                    Entry boulderEntry = getBoulderEntry(cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID)));
                    EntryItem entryItem = new EntryItem(
                            boulderEntry.getId(),
                            boulderEntry.getLocation(),
                            boulderEntry.getDate(),
                            Float.parseFloat(boulderEntry.getRating()));
                    entryList.add(entryItem);
                }
                cursor.moveToNext();

            }

        }


        if (entryList.size() > 10) {
            //only show the last 10 entries
            for (int j = entryList.size() - 1; j > entryList.size() - 11; j--) {
                entryAdapter.add(entryList.get(j));
            }

        } else {
            //newest entries first
            for (int j = entryList.size() - 1; j >= 0; j--) {
                entryAdapter.add(entryList.get(j));
            }


        }


    }

    //Set profile data
    public void setProfileData(String username) {
        User user = getUserEntry(username);

        //exp calculations
        String totalExpString = user.getExp();
        double totalExp = Double.parseDouble(totalExpString);
        double expForNextLvl = 0;
        double expCount = 0;
        double percentage;
        int lvlCount = 1;

        //simulates the lvlUps until expCount > totalExp
        for (int i = 1; expCount < totalExp; i++) {
            expCount = expCount + levelUpFunction(i);
            if (expCount < totalExp) {
                lvlCount++;
            }
        }


        //expCount is always greater than totalEXP or equal to totalEXP
        expForNextLvl = expCount - totalExp;

        //if expCount is equal to totalExp, the user just hit the next lvl on point --> add extra lvl
        //therefore to get the exp for the next lvl use levelUpFunction()
        if (expCount == totalExp) {
            lvlCount++;
            expForNextLvl = levelUpFunction(lvlCount);
            progressBar.setProgress(0);
        }

        //get percentage as int *100
        if (expCount > totalExp) {
            //percentage = exp that we already have * 100 / total exp that was needed to get to the next level
            percentage = (expForNextLvl * 100) / levelUpFunction(lvlCount + 1);
            //100 - percentage = exp that the user already has as int percentage
            progressBar.setProgress(100 - (new Double(percentage)).intValue());
        }

        //if totalExp equals 0. the users lvl is 1
        if (totalExp == 0) {
            lvlCount = 1;
        }

        usernameTextView.setText(user.getUsername());
        userLvlTextView.setText(lvlCount + "");
        if (user.getImagePath().equals("")) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dummy_profile_picture);
            userProfileImageView.setImageBitmap(bitmap);
        }else{
            userProfileImageView.setImageBitmap(getBitmapFromImagePath(user.getImagePath()));

        }


    }

    public void setLastLogin() {
        String date = getCurrentDate();
        User user = getUserEntry(loggedInUser);
        sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_LAST_LOGIN, date, user.getId());
    }


    //gets a user from the db based on the username
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


    //function to calculate how much exp is needed for the next lvl up
    public double levelUpFunction(int lvl) {
        return Math.round(0.2 * (Math.pow(lvl, 2) / 8) + lvl * 2);
    }

    public String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    public Bitmap getBitmapFromImagePath(String imagePath) {
        File img = new File(imagePath);
        Bitmap bitmap = null;

        if (img.exists()) {
            bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
        }

        return bitmap;
    }

    public String getImagePath(Bitmap bitmap) {
        return getRealPathFromURI(getImageUri(getApplicationContext(), bitmap));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void createLog(String text) {
        Log.d(LOGTAG, text);
    }


}
