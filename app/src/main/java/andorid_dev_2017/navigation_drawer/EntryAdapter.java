package andorid_dev_2017.navigation_drawer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.Entry;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbUserContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.User;
import andorid_dev_2017.navigation_drawer.sqlite_database.UserEntry;

public class EntryAdapter extends ArrayAdapter<EntryItem> {

    private SQLiteDbEntryContract sqLiteDbEntryContract = new SQLiteDbEntryContract(getContext());


    public EntryAdapter(Context context, ArrayList<EntryItem> entryList) {
        super(context, 0, entryList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        final EntryItem entryItem = getItem(position);
        String entryNumberString;


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_list_item, parent, false);
        }

        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar_inMain_id);
        TextView entryNumber = convertView.findViewById(R.id.entry_number_id);
        TextView hallName = convertView.findViewById(R.id.hall_id);
        TextView date = convertView.findViewById(R.id.date_id);
        Button deleteBtn = convertView.findViewById(R.id.delete_btn_inMain_id);
        RelativeLayout relativeLayout = convertView.findViewById(R.id.list_item_main_rLayout_id);

        ratingBar.setRating(entryItem.getRating());
        entryNumber.setText("Eintrag #" + entryItem.getId());
        hallName.setText(entryItem.getHall());
        date.setText(entryItem.getDate());


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final AlertDialog dialog = builder.setMessage("Delete entry #" + getBoulderEntry(entryItem.getId()).getId() + " ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String entryNumberHolder = getBoulderEntry(entryItem.getId()).getId();
                                sqLiteDbEntryContract.deleteRow(getBoulderEntry(entryItem.getId()).getId());
                                //call method from mainScreenActivity
                                if (getContext() instanceof MainScreenActivity) {
                                    ((MainScreenActivity) getContext()).onDeleteClick(entryItem);
                                }
                                if (getContext() instanceof SearchActivity) {
                                    ((SearchActivity) getContext()).onDeleteClick(entryItem);
                                }
                                Toast.makeText(getContext(), "Entry #" + entryNumberHolder + " has been deleted.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", null).create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getColor(R.color.colorGrayDark));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getContext().getColor(R.color.colorGrayDark));
                    }
                });


                dialog.show();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowDetailsActivity.class);
                intent.putExtra("entry_id_key", entryItem.getId());
                getContext().startActivity(intent);
            }
        });


        return convertView;
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


}
