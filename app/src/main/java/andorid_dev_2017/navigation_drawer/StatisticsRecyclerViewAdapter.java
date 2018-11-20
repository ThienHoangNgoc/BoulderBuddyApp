package andorid_dev_2017.navigation_drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.ViewHolder> {

    private List<StatisticsRecyclerViewItem> listData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public StatisticsRecyclerViewAdapter(Context context, List<StatisticsRecyclerViewItem> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.listData = itemList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.statistics_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatisticsRecyclerViewItem recyclerViewItem = listData.get(position);
        holder.valueText.setText(recyclerViewItem.getValue());
        holder.labelText.setText(recyclerViewItem.getLabel());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView valueText;
        TextView labelText;


        ViewHolder(View itemView) {
            super(itemView);
            valueText = itemView.findViewById(R.id.statistics_stat_set_1_text_value_id);
            labelText = itemView.findViewById(R.id.statistics_stat_set_1_text_label_id);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught, call this in parent activity
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
