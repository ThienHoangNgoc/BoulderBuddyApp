package andorid_dev_2017.navigation_drawer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import andorid_dev_2017.navigation_drawer.R;

public class FragmentShowDetailsBasicInfo extends android.support.v4.app.Fragment {


    View view;
    public FragmentShowDetailsBasicInfo(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.basic_info_fragment,container,false);
        return view;
    }
}
