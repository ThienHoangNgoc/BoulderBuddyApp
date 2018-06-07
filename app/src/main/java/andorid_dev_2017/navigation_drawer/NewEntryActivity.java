package andorid_dev_2017.navigation_drawer;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class NewEntryActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private final String LOGTAG = "NEW_ENTRY";

    private AutoCompleteTextView autoCompleteTextView;
    private String[] hall_names;
    private EditText veryEasyValue;
    private EditText easyValue;
    private EditText startValue;
    private EditText endValue;
    private Button veryEasyPlus;
    private Button veryEasyMinus;
    private Button easyPlus;
    private Button easyMinus;
    private Button cancel_btn;
    private String hour;
    private String minute;

    private DatePicker datePicker;



    private int i;
    private boolean isPlus;
    private boolean timepickerOnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);


        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.test_autocomplete);
        hall_names = getResources().getStringArray(R.array.hallen);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hall_names);
        autoCompleteTextView.setAdapter(adapter);

        datePicker = new DatePicker(this, findViewById(R.id.nE_date).getId());

        cancel_btn = (Button) findViewById(R.id.cancel_btn_nE);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        startValue = (EditText) findViewById(R.id.nE_start);
        startValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerOnStart = true;
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), "time picker");
            }
        });

        endValue = (EditText) findViewById(R.id.nE_end);
        endValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timepickerOnStart = false;
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), "time picker");
            }
        });

        veryEasyPlus = (Button) findViewById(R.id.btn_plus);
        veryEasyMinus = (Button) findViewById(R.id.btn_minus);
        veryEasyValue = (EditText) findViewById(R.id.vE_EdTex);

        easyPlus = (Button) findViewById(R.id.btn_plus1);
        easyMinus = (Button) findViewById(R.id.btn_minus1);
        easyValue = (EditText) findViewById(R.id.vE_EdTex1);

        easyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlus = true;
                onOperatorClick(easyValue);
            }
        });

        easyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlus = false;
                onOperatorClick(easyValue);
            }
        });

        veryEasyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlus = true;
                onOperatorClick(veryEasyValue);
            }
        });

        veryEasyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlus = false;
                onOperatorClick(veryEasyValue);
            }
        });


    }


    private void onOperatorClick(EditText difValue) {
        if (isPlus) {
            i = Integer.parseInt(difValue.getText() + "");
            i++;
            difValue.setText(i + "");
        } else {
            if (i > 0) {
                i = Integer.parseInt(difValue.getText() + "");
                i--;
            }

            difValue.setText(i + "");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        hour = i + "";
        minute = i1 + "";

        if (i < 10) {
            hour = "0" + i;
        }
        if (i1 < 10) {
            minute = "0" + i1;
        }
        if (timepickerOnStart) {
            startValue.setText(hour + ":" + minute);
        } else {
            endValue.setText(hour + ":" + minute);
        }

    }
}
