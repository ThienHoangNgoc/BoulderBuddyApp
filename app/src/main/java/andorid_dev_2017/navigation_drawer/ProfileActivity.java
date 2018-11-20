package andorid_dev_2017.navigation_drawer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.ImageDB;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbImageDBContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.User;
import andorid_dev_2017.navigation_drawer.sqlite_database.UserEntry;

public class ProfileActivity extends AppCompatActivity {

    private String loggedInUser;
    final int REQUEST_CODE_GALLERY = 999;
    private int toastDuration = Toast.LENGTH_SHORT;
    private static final String LOGTAG = "profileActivity";

    private ImageView profileImage;
    private TextView usernameText;
    private TextView levelText;
    private TextView totalExpText;
    private TextView neededExpText;
    private TextView rankText;
    private TextView rankPointsText;
    private Button rankInfoBtn;
    private Button editUserNameBtn;
    private boolean profileDataChanged;


    private SQLiteDbUserContract sqLiteDbUserContract;
    private SQLiteDbEntryContract sqLiteDbEntryContract;
    private SQLiteDbImageDBContract sqLiteDbImageDBContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //setup database
        sqLiteDbUserContract = new SQLiteDbUserContract(getApplicationContext());
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getApplicationContext());
        sqLiteDbImageDBContract = new SQLiteDbImageDBContract(getApplicationContext());

        //Get String from extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }

        //setup views
        profileImage = findViewById(R.id.profile_image_id);
        usernameText = findViewById(R.id.profile_name_text_id);
        levelText = findViewById(R.id.profile_lvl_text_view_id);
        totalExpText = findViewById(R.id.profile_total_exp_value_text_id);
        neededExpText = findViewById(R.id.profile_needed_exp_value_text_id);
        rankText = findViewById(R.id.profile_rank_tier_text_id);
        rankPointsText = findViewById(R.id.profile_rank_points_value_text_id);
        rankInfoBtn = findViewById(R.id.profile_rank_info_btn_id);
        editUserNameBtn = findViewById(R.id.profile_edit_btn_id);


        //onClickListeners
        editUserNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button okBtn;
                Button cancelBtn;
                Button resetBtn;
                final EditText newUsernameEditText;
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Light_NoTitleBar);
                View aView = getLayoutInflater().inflate(R.layout.profile_edit_username_dialog, null);
                okBtn = aView.findViewById(R.id.profile_edit_username_dialog_select_btn_id);
                cancelBtn = aView.findViewById(R.id.profile_edit_username_dialog_cancel_btn_id);
                resetBtn = aView.findViewById(R.id.profile_reset_edit_username_btn_id);
                newUsernameEditText = aView.findViewById(R.id.profile_edit_username_dialog_edit_view_id);

                builder.setView(aView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newUsername = newUsernameEditText.getText().toString();
                        Cursor cursor = sqLiteDbUserContract.readEntry();
                        cursor.moveToFirst();
                        if (newUsernameEditText.getText().toString().equals("")) {
                            toastCreator("Username cannot be empty.");
                        } else if (cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME)).equals(newUsername)) {
                            toastCreator("Username is already taken.");
                        } else {
                            sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_USERNAME, newUsername, getUserEntry(loggedInUser).getId());
                            changeCreatorInImageDbOnUsernameChange(loggedInUser, newUsername);
                            changeCreatorOnUsernameChange(loggedInUser, newUsername);
                            usernameText.setText(newUsername);
                            loggedInUser = newUsername;
                            profileDataChanged = true;
                            dialog.cancel();
                        }

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                resetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newUsernameEditText.setText("");
                    }
                });
            }
        });
        rankInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Light_NoTitleBar);
                View aView = getLayoutInflater().inflate(R.layout.profile_rank_info_dialog, null);
                builder.setView(aView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        ProfileActivity.this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                        }, REQUEST_CODE_GALLERY

                );

                profileDataChanged = true;

            }
        });


        setupProfileData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (profileDataChanged) {
            Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
            intent.putExtra("username_key", loggedInUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                //resize image
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                profileImage.setImageBitmap(bitmap);
                updateProfilePicture();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                toastCreator("no permission");
            }
            return;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateProfilePicture() {
        Cursor cursor = sqLiteDbUserContract.readEntry();
        int i;
        if (cursor.getCount() <= 0) return;
        cursor.moveToFirst();
        for (i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME)).equals(loggedInUser)) {
                Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                String imagePath = getImagePath(bitmap);
                sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_PROFILE_PICTURE, imagePath, getUserEntry(loggedInUser).getId());
            }
            cursor.moveToNext();
        }

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

    //changes the creator in all entries of the user to the new username
    public void changeCreatorOnUsernameChange(String oldUsername, String newUsername) {
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR)).equals(oldUsername)) {
                    sqLiteDbEntryContract.updateOneColumn(
                            BoulderEntry.COLUMN_NAME_CREATOR,
                            newUsername,
                            cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID)));
                }
                cursor.moveToNext();
            }


        }
    }

    //changes the creator in all entries of the user to the new username
    public void changeCreatorInImageDbOnUsernameChange(String oldUsername, String newUsername) {
        Cursor cursor = sqLiteDbImageDBContract.readEntry();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(ImageDB.COLUMN_NAME_CREATOR)).equals(oldUsername)) {
                    sqLiteDbImageDBContract.updateOneColumn(
                            ImageDB.COLUMN_NAME_CREATOR,
                            newUsername,
                            cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID)));
                }
                cursor.moveToNext();
            }


        }
    }

    public void setupProfileData() {
        User user = getUserEntry(loggedInUser);

        //exp calculations
        String totalExpString = user.getExp();
        double totalExp = Double.parseDouble(totalExpString);
        double expForNextLvl = 0;
        double expCount = 0;

        int lvlCount = 1;

        //simulates the lvlUps until expCount > totalExp
        for (int i = 1; expCount < totalExp; i++) {
            expCount = expCount + levelUpFunction(i);
            if (expCount < totalExp) {
                lvlCount++;
            }
        }

        if (totalExp == 0) {
            lvlCount = 1;
        } else if (expCount == totalExp) {
            lvlCount++;
            expForNextLvl = levelUpFunction(lvlCount);
        } else {
            //expCount is always greater than totalEXP or equal to totalEXP
            expForNextLvl = expCount - totalExp;
        }

        if (user.getImagePath().equals("")) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dummy_profile_picture);
            profileImage.setImageBitmap(bitmap);
        } else {
            profileImage.setImageBitmap(getBitmapFromImagePath(user.getImagePath()));
        }
        usernameText.setText(loggedInUser);
        levelText.setText(lvlCount + "");
        totalExpText.setText(((int) totalExp) + "");
        neededExpText.setText(((int) expForNextLvl) + "");
        rankText.setText(user.getRank());
        rankPointsText.setText(user.getRankPoints() + " LP");


    }

    //function to calculate how much exp is needed for the next lvl up
    public double levelUpFunction(int lvl) {
        return Math.round(0.2 * (Math.pow(lvl, 2) / 8) + lvl * 2);
    }

    public void toastCreator(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, toastDuration).show();
    }

    public void createLog(String text) {
        Log.d(LOGTAG, text);
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


}
