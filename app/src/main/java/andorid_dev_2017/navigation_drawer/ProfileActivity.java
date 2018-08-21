package andorid_dev_2017.navigation_drawer;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.User;
import andorid_dev_2017.navigation_drawer.sqlite_database.UserEntry;

public class ProfileActivity extends AppCompatActivity {

    private String loggedInUser;

    private ImageView profileImage;
    private TextView usernameText;
    private TextView levelText;
    private TextView totalExpText;
    private TextView neededExpText;
    private TextView rankText;
    private TextView rankPointsText;
    private Button rankInfoBtn;


    private SQLiteDbUserContract sqLiteDbUserContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //setup database
        sqLiteDbUserContract = new SQLiteDbUserContract(getApplicationContext());

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



        //onClickListeners
        rankInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.profile_rank_info_dialog, null);
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.getWindow().setLayout(600,600);
            }
        });

        setupProfileData();
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

    public void setupProfileData() {
        User user = getUserEntry(loggedInUser);

        //exp calculations
        String totalExpString = user.getExp();
        double totalExp = Double.parseDouble(totalExpString);
        double expForNextLvl = 0;
        double expCount = 0;
        double percentage = 0;
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

        }

        //get percentage as int *100
        if (expCount > totalExp) {
            percentage = (expForNextLvl * 100) / levelUpFunction(lvlCount + 1);

        }

        //if totalExp equals 0. the users lvl is 1
        if (totalExp == 0) {
            lvlCount = 1;
        }

        profileImage.setImageBitmap(user.getProfileImage());
        usernameText.setText(loggedInUser);
        levelText.setText(lvlCount + "");
        totalExpText.setText(((int) totalExp) + "");
        neededExpText.setText(((int) percentage) + "");
        rankText.setText(user.getRank());
        rankPointsText.setText(user.getRankPoints() + " LP");




    }

    //function to calculate how much exp is needed for the next lvl up
    public double levelUpFunction(int lvl) {
        return Math.round(0.2 * (Math.pow(lvl, 2) / 8) + lvl * 2);
    }
}
