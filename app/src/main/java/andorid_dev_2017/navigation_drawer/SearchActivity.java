package andorid_dev_2017.navigation_drawer;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    EditText editText;
    Button searchBtn;
    Button clearBtn;
    Button backBtn;
    final String LOGTAG = "SearchActivity";
    private ListView listView;
    private int duration = Toast.LENGTH_SHORT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backBtn = findViewById(R.id.search_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        clearBtn = (Button) findViewById(R.id.reset_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

        editText = findViewById(R.id.edit_text_search_date);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SearchActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.search_dialog, null);
                final NumberPicker picker = (NumberPicker) mView.findViewById(R.id.picker);
                String[] data = getResources().getStringArray(R.array.monate);
                picker.setMinValue(0);
                picker.setMaxValue(data.length - 1);
                picker.setDisplayedValues(data);

                ArrayList<String> numbers = new ArrayList<>();
                for (int i = 1900; i <= 2200; i++) {
                    numbers.add(i + "");
                }
                final NumberPicker picker2 = (NumberPicker) mView.findViewById(R.id.picker2);
                String[] data2 = numbers.toArray(new String[numbers.size()]);
                picker2.setMinValue(1900);
                picker2.setMaxValue(2200);
                picker2.setValue(2018);
                picker2.setDisplayedValues(data2);

                Button btn = (Button) mView.findViewById(R.id.select_btn_1);
                Button btn2 = (Button) mView.findViewById(R.id.cancel_btn_1);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //cancel has to be after create() and show().

                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

                    }
                });

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editText.setText(intToMonth(picker) + " " + picker2.getValue() );
                        dialog.cancel();
                    }
                });


            }
        });


        searchBtn = (Button) findViewById(R.id.search_btn_go);


        //create Entries

        listView = (ListView) findViewById(R.id.search_list_view);
        ArrayList<EntryItem> arrayOfEntryItems = new ArrayList<>();
        final EntryAdapter entryAdapter = new EntryAdapter(this, arrayOfEntryItems);



        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Pls enter a date.", duration).show();
                }else{

                    //empties the list
                    createEmptyList(entryAdapter);
                    listView.setAdapter(entryAdapter);


                    createEntries(entryAdapter);
                    listView.setAdapter(entryAdapter);
                }



            }
        });





    }



    //create dummy entries

    public void createEntries(EntryAdapter entryAdapter) {
        final ArrayList<EntryItem> entryCounter = new ArrayList<>();

        EntryItem item1 = new EntryItem("1", "Südbloc", "28.01.17", 2.5f);
        EntryItem item2 = new EntryItem("2", "Ostbloc", "04.03.17", 3f);
        EntryItem item3 = new EntryItem("3", "Südbloc", "07.04.17", 3.5f);
        EntryItem item4 = new EntryItem("4", "Südbloc", "28.04.17", 3f);

        entryCounter.add(item1);
        entryCounter.add(item2);
        entryCounter.add(item3);
        entryCounter.add(item4);


        int j;
        for (j = entryCounter.size() - 1; j >= 0; j--) {
            entryAdapter.add(entryCounter.get(j));
        }


    }

    public void createEmptyList(EntryAdapter entryAdapter){
        entryAdapter.clear();

    }


    public String intToMonth(NumberPicker picker){
        String date = "";

        switch (picker.getValue() + 1){
            case 1:
                date = "Januar";
                break;
            case 2:
                date = "Februar";
                break;
            case 3:
                date = "März";
                break;
            case 4:
                date = "April";
                break;
            case 5:
                date = "Mai";
                break;
            case 6:
                date = "Juni";
                break;
            case 7:
                date = "Juli";
                break;
            case 8:
                date = "August";
                break;
            case 9:
                date = "September";
                break;
            case 10:
                date = "Oktober";
                break;
            case 11:
                date = "November";
                break;
            case 12:
                date = "Dezember";
                break;
            default:
                break;


        }

        return date;


    }
}
