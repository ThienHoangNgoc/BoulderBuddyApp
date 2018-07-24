package andorid_dev_2017.navigation_drawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import andorid_dev_2017.navigation_drawer.R;

public class ProfileActivity extends AppCompatActivity {

    private Button rankInfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //AlertDialog for RankInfo
        rankInfoBtn = findViewById(R.id.rank_info_btn);
        rankInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
