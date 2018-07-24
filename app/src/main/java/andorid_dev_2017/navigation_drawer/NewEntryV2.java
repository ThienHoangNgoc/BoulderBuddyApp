package andorid_dev_2017.navigation_drawer;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class NewEntryV2 extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private AutoCompleteTextView location;
    private String[] location_names;
    private DatePicker date;
    private EditText startTime;
    private EditText endTime;
    //lets onTimeSet know for which view it has to set the text
    private boolean timeViewPosition = true;
    private EditText veryEasy;
    private Button veryEasyClearBtn;
    private EditText easy;
    private Button easyClearBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry_v2);

        //location autocomplete setup
        location = findViewById(R.id.step_1_location_autoComTextV_id);
        location_names = getResources().getStringArray(R.array.hallen);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, location_names);
        location.setAdapter(adapter);

        //setup Datepicker
        date = new DatePicker(this, findViewById(R.id.step_1_date_editText_id).getId());

        //starting time
        startTime = findViewById(R.id.step_1_startTime_editText_id);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeViewPosition = true;
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), "time picker");
            }
        });

        //ending time
        endTime = findViewById(R.id.step_1_endTime_editText_id);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeViewPosition = false;
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), "time picker");
            }
        });

        //create dialog for veryEasy difficulty
        veryEasy = findViewById(R.id.step_2_veryEasy_editText_id);
        veryEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewEntryV2.this);
                final View mView = getLayoutInflater().inflate(R.layout.new_entry_very_easy_dialog, null);
                final NumberPicker picker = mView.findViewById(R.id.veryEasy_number_picker_id);
                //create the range for the NumberPicker
                ArrayList<String> numbers = new ArrayList<>();
                for (int i = 0; i <= 100; i++) {
                    numbers.add(i + "");
                }
                String[] data = numbers.toArray(new String[numbers.size()]);
                picker.setMinValue(0);
                picker.setMaxValue(99);
                if(!veryEasy.getText().toString().equals("")){
                    picker.setValue(Integer.parseInt(veryEasy.getText().toString()));
                }
                picker.setDisplayedValues(data);
                //initialize select and cancel Buttons with mView
                Button sBtn = mView.findViewById(R.id.new_entry_dialog_veryEasy_select_btn_id);
                Button cBtn = mView.findViewById(R.id.new_entry_dialog_veryEasy_cancel_btn_id);
                //create the Dialog
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                sBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        veryEasy.setText(picker.getValue() + "");
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
        //clear content from very easy
        veryEasyClearBtn = findViewById(R.id.reset_btn_veryEasy);
        veryEasyClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veryEasy.setText("");
            }
        });

        //create dialog for easy difficulty
        easy = findViewById(R.id.step_2_easy_editText_id);
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewEntryV2.this);
                final View mView = getLayoutInflater().inflate(R.layout.new_entry_easy_dialog, null);
                final NumberPicker picker = mView.findViewById(R.id.easy_number_picker_id);
                //create the range for the NumberPicker
                ArrayList<String> numbers = new ArrayList<>();
                for (int i = 0; i <= 100; i++) {
                    numbers.add(i + "");
                }
                String[] data = numbers.toArray(new String[numbers.size()]);
                picker.setMinValue(0);
                picker.setMaxValue(99);
                if(!easy.getText().toString().equals("")){
                    picker.setValue(Integer.parseInt(easy.getText().toString()));
                }
                picker.setDisplayedValues(data);
                //initialize select and cancel Buttons with mView
                Button sBtn = mView.findViewById(R.id.new_entry_dialog_easy_select_btn_id);
                Button cBtn = mView.findViewById(R.id.new_entry_dialog_easy_cancel_btn_id);
                //create the Dialog
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                sBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        easy.setText(picker.getValue() + "");
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
        //clear content from easy
        easyClearBtn = findViewById(R.id.reset_btn_easy);
        easyClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setText("");
            }
        });



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
}
