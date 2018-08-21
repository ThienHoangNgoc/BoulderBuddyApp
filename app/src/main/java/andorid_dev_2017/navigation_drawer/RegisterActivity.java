package andorid_dev_2017.navigation_drawer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.UserEntry;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button createAccBtn;
    private ImageView dummyImageView;

    private int toastDuration = Toast.LENGTH_SHORT;

    SQLiteDbUserContract sqLiteDbUserContract;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Setup user database
        sqLiteDbUserContract = new SQLiteDbUserContract(getApplicationContext());

        //Setup views
        usernameEditText = findViewById(R.id.register_username_edit_text_id);
        passwordEditText = findViewById(R.id.register_password_edit_text_id);
        passwordConfirmEditText = findViewById(R.id.register_confirm_password_edit_text_id);
        createAccBtn = findViewById(R.id.register_register_btn_id);
        dummyImageView = findViewById(R.id.register_dummy_image_id);

        //Get dummy image for the dummyImageView for the user database entry
        dummyImageView.setImageResource(R.drawable.photo);



        //Setup onClickListener
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateClick();
            }
        });


    }

    public void onCreateClick() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmedPassword = passwordConfirmEditText.getText().toString();
        Cursor cursor = sqLiteDbUserContract.readEntry();
        ArrayList<String> nameList = new ArrayList<>();

        //get all Usernames if there are any to prevent doubles
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String userName = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME));
                nameList.add(userName);
                cursor.moveToNext();
            }
        }

        /*
        1. Check if any fields are empty
        2. Check name is already in use
        3. Check if passwords are matching
         */
        if (username.equals("") || password.equals("") || confirmedPassword.equals("")) {
            toastCreator("Some fields are still empty");
        } else if (nameList.contains(username)) {
            toastCreator("Name is already in use.");
        } else if (!password.equals(confirmedPassword)) {
            toastCreator("Passwords are not matching");
        } else {
            sqLiteDbUserContract.insertEntry(username, password, getCurrentDate(), "0",
                    "Placement", "0", ((BitmapDrawable) dummyImageView.getDrawable()).getBitmap(), "logout");
            toastCreator("Your Account was successfully created.");
            Intent intent = new Intent(getApplicationContext(), MainScreenActivity.class);
            intent.putExtra("username_key", username);
            startActivity(intent);
            finish();

        }

    }

    public void toastCreator(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, toastDuration).show();
    }

    public String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
}
