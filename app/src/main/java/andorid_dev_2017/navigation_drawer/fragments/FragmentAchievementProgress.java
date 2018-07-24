package andorid_dev_2017.navigation_drawer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andorid_dev_2017.navigation_drawer.R;

public class FragmentAchievementProgress extends Fragment {

    View view;

    public FragmentAchievementProgress(){


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.achievement_progress_fragment,container,false);
        return view;
    }
}
