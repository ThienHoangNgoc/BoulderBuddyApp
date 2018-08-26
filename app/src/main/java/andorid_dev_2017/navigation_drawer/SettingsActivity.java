package andorid_dev_2017.navigation_drawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.User;
import andorid_dev_2017.navigation_drawer.sqlite_database.UserEntry;

public class SettingsActivity extends AppCompatActivity {

    private SQLiteDbUserContract sqLiteDbUserContract;
    private int toastDuration = Toast.LENGTH_SHORT;
    private static final String LOGTAG = "settingsActivity";
    private String loggedInUser;

    private Switch stayLogSwitch;
    private Button changePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //setup database
        sqLiteDbUserContract = new SQLiteDbUserContract(getApplicationContext());

        //Get String from extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("username_key");
        }


        //setup views
        stayLogSwitch = findViewById(R.id.setting_login_status_switch_btn_id);
        changePasswordBtn = findViewById(R.id.settings_change_password_btn_id);

        //check switch
        if (getUserEntry(loggedInUser).getLoginStatus().equals("stayLogged")) {
            stayLogSwitch.setChecked(true);
        } else {
            stayLogSwitch.setChecked(false);
        }


        //setOnclickListeners
        stayLogSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if (isOn) {
                    sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_LOGIN_STATUS, "stayLogged", getUserEntry(loggedInUser).getId());
                } else {
                    sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_LOGIN_STATUS, "logout", getUserEntry(loggedInUser).getId());
                }
            }
        });
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button okBtn;
                Button cancelBtn;
                Button oldPwResetBtn;
                Button newPwResetBtn;
                Button confNewPwResetBtn;
                final EditText oldPasswordEditText;
                final EditText newPasswordEditText;
                final EditText confPasswordEditText;
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, android.R.style.Theme_Light_NoTitleBar);
                View aView = getLayoutInflater().inflate(R.layout.settings_change_password_dialog, null);
                okBtn = aView.findViewById(R.id.settings_change_password_dialog_select_btn_id);
                cancelBtn = aView.findViewById(R.id.settings_change_password_dialog_cancel_btn_id);
                oldPwResetBtn = aView.findViewById(R.id.settings_reset_old_password_btn_id);
                newPwResetBtn = aView.findViewById(R.id.settings_reset_new_password_btn_id);
                confNewPwResetBtn = aView.findViewById(R.id.settings_reset_confirm_new_password_btn_id);
                oldPasswordEditText = aView.findViewById(R.id.settings_change_password_dialog_old_password_edit_view_id);
                newPasswordEditText = aView.findViewById(R.id.settings_change_password_dialog_new_password_edit_view_id);
                confPasswordEditText = aView.findViewById(R.id.settings_change_password_dialog_confirm_new_password_edit_view_id);

                builder.setView(aView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (getTextFromView(oldPasswordEditText).equals("") ||
                                getTextFromView(newPasswordEditText).equals("") ||
                                getTextFromView(confPasswordEditText).equals("")) {
                            toastCreator("Some fields are still empty.");
                        } else if (!getUserEntry(loggedInUser).getPassword().equals(getTextFromView(oldPasswordEditText))) {
                            toastCreator("Old password is not correct.");

                        } else if (!getTextFromView(newPasswordEditText).equals(getTextFromView(confPasswordEditText))) {
                            toastCreator("New passwords are not matching.");
                        } else {
                            sqLiteDbUserContract.updateOneColumn(UserEntry.COLUMN_NAME_PASSWORD,
                                    getTextFromView(newPasswordEditText),
                                    getUserEntry(loggedInUser).getId());
                            dialog.cancel();
                            toastCreator("Password was changed successfully.");


                        }

                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                oldPwResetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oldPasswordEditText.setText("");
                    }
                });

                newPwResetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newPasswordEditText.setText("");

                    }
                });

                confNewPwResetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confPasswordEditText.setText("");

                    }
                });
            }
        });


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


    public void toastCreator(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, toastDuration).show();
    }

    public void createLog(String text) {
        Log.d(LOGTAG, text);
    }

    //Gets the String from a EditText
    public String getTextFromView(EditText editText) {
        return editText.getText().toString();
    }
}
