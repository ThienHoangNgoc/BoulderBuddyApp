package andorid_dev_2017.navigation_drawer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<EntryItem> {


    public EntryAdapter(Context context, ArrayList<EntryItem> entryList) {
        super(context, 0, entryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final EntryItem entryItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_list_item, parent, false);
        }

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar_inMain_id);
        TextView entryNumber = (TextView) convertView.findViewById(R.id.entry_number_id);
        TextView hallName = (TextView) convertView.findViewById(R.id.hall_id);
        TextView date = (TextView) convertView.findViewById(R.id.date_id);
        Button deleteBtn = (Button) convertView.findViewById(R.id.delete_btn_inMain_id);
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.list_item_main_rLayout_id);

        ratingBar.setRating(entryItem.getRating());
        entryNumber.setText("Eintrag #" + entryItem.getId());
        hallName.setText(entryItem.getHall());
        date.setText(entryItem.getDate());
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final AlertDialog dialog = builder.setMessage("Eintrag löschen?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "Eintrag wurde gelöscht.(Dummy Text)", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", null).create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getColor(R.color.colorGrayDark));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                    }
                });


                dialog.show();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), show_details_v2.class);
                getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}
