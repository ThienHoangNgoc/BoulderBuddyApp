package andorid_dev_2017.navigation_drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;


public class AchievementProgressRecyclerViewAdapter extends RecyclerView.Adapter<AchievementProgressRecyclerViewAdapter.ViewHolder> {

    private List<AchievementProgressRecyclerViewItem> listData;
    private LayoutInflater mInflater;
    private StatisticsRecyclerViewAdapter.ItemClickListener mClickListener;

    public AchievementProgressRecyclerViewAdapter(Context context, List<AchievementProgressRecyclerViewItem> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.listData = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.achievement_progress_recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AchievementProgressRecyclerViewItem recyclerViewItem = listData.get(position);
        holder.labelText.setText(recyclerViewItem.getLabel());
        holder.progressBar.setProgress(recyclerViewItem.getProgress());
        if (recyclerViewItem.isDoneStatus()) {
            holder.doneText.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView labelText;
        ProgressBar progressBar;
        TextView doneText;


        ViewHolder(View itemView) {
            super(itemView);
            labelText = itemView.findViewById(R.id.achievement_progress_fragment_label_text_id);
            progressBar = itemView.findViewById(R.id.achievement_progress_fragment_progressBar_id);
            doneText = itemView.findViewById(R.id.achievement_progress_done_status_text_id);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
