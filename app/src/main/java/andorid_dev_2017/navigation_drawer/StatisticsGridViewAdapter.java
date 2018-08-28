package andorid_dev_2017.navigation_drawer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class StatisticsGridViewAdapter extends ArrayAdapter<StatGridViewItem> {


    public StatisticsGridViewAdapter(@NonNull Context context, @NonNull List<StatGridViewItem> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final StatGridViewItem statGridViewItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.statistics_grid_view_item, parent, false);
        }

        TextView statValue = convertView.findViewById(R.id.statistics_grid_item_text_value_id);
        TextView statLabel = convertView.findViewById(R.id.statistics_grid_item_text_label_id);

        statValue.setText(statGridViewItem.getStatValue());
        statLabel.setText(statGridViewItem.getStatLabel());


        return convertView;
    }
}
