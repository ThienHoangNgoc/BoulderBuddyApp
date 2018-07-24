package andorid_dev_2017.navigation_drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentStatisticsChartProgress extends Fragment{

    View view;

    public FragmentStatisticsChartProgress(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistics_chart_progress_fragment,container,false);
        return view;
    }
}
