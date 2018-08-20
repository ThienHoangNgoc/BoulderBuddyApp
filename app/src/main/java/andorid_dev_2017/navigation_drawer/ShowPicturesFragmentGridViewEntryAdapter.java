package andorid_dev_2017.navigation_drawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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

        return convertView;
    }
}
