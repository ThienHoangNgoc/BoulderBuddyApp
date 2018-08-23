package andorid_dev_2017.navigation_drawer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

public class ShowPicturesFragmentGridViewEntryAdapter extends ArrayAdapter<ImageGridViewItem> {
    public ShowPicturesFragmentGridViewEntryAdapter(@NonNull Context context, @NonNull List<ImageGridViewItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ImageGridViewItem imageGridViewItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.show_details_pictures_grid_view_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.show_details_pictures_image_item_id);
        imageView.setImageBitmap(imageGridViewItem.getBitmap());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Theme_Black_NoTitleBar -> Fullscreen but show statusBar
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), android.R.style.Theme_Black_NoTitleBar);
                //in adapter
                View aView = LayoutInflater.from(getContext()).inflate(R.layout.show_details_image_full_screen_dialog, null);
                ImageView image = aView.findViewById(R.id.show_details_fullscreen_image_id);
                image.setImageBitmap(imageGridViewItem.getBitmap());
                builder.setView(aView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return convertView;
    }
}
