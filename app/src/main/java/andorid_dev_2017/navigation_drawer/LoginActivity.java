package andorid_dev_2017.navigation_drawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.User;
import andorid_dev_2017.navigation_drawer.sqlite_database.UserEntry;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private TextView createAccTextView;

    private SQLiteDbUserContract sqLiteDbUserContract;

    private User user;
    private String lastActiveUserId;

    private int toastDuration = Toast.LENGTH_SHORT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //User Database Setup
        sqLiteDbUserContract = new SQLiteDbUserContract(getApplicationContext());

        //setup views
        usernameEditText = findViewById(R.id.login_username_edit_text_id);
        passwordEditText = findViewById(R.id.login_password_edit_text_id);
        loginBtn = findViewById(R.id.login_login_btn_id);
        createAccTextView = findViewById(R.id.login_create_new_acc_text_id);


        //Check if last user wanted to stay logged in
        Cursor cursor = sqLiteDbUserContract.readEntry();
        if (cursor.getCount() > 0) {
            SharedPreferences prefsUserId = this.getSharedPreferences(
                    "lastUserId", Context.MODE_PRIVATE);
            lastActiveUserId = prefsUserId.getString("user_id_key", "");
            if (!lastActiveUserId.equals("")) {
                user = getUserEntry(lastActiveUserId);
                if (user.getLoginStatus().equals("stayLogged")) {
                    Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                    intent.putExtra("username_key", getUserEntry(lastActiveUserId).getUsername());
                    startActivity(intent);
                    finish();
                }
            }
        }


        //onClickListener
        createAccTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateAccountClick();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick();
            }
        });


    }

    //gets a user from the db based on the id
    public User getUserEntry(String id) {
        User user;
        Cursor cursor = sqLiteDbUserContract.readEntry();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USER_ID)).equals(id)) {
                user = new User(id,
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME)),
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

    public void onLoginClick() {
        Cursor cursor = sqLiteDbUserContract.readEntry();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Boolean loginSuccess = false;


        //check if one or both fields are empty
        if (username.equals("") || password.equals("")) {
            toastCreator("Some fields are still empty.");
        } else {

            //check whether accounts exists
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (username.equals(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME))) &&
                            password.equals(cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_PASSWORD)))) {
                        loginSuccess = true;
                    }
                    cursor.moveToNext();
                }

                //if there is a matching username and password
                if (loginSuccess) {
                    toastCreator("Login successful");
                    Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
                    //put the username into extra to use it in MainScreenActivity
                    intent.putExtra("username_key", username);
                    startActivity(intent);
                    finish();

                } else {
                    toastCreator("Wrong username or password.");
                }

            } else {
                toastCreator("there are no User");

            }

        }

    }


    public void onCreateAccountClick() {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }


    public void toastCreator(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, toastDuration).show();
    }


}
