package andorid_dev_2017.navigation_drawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBasicInfo extends android.support.v4.app.Fragment {


    View view;
    public FragmentBasicInfo(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.basic_info_fragment,container,false);
        return view;
    }
}
