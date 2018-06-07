package andorid_dev_2017.navigation_drawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentMoreInfo extends android.support.v4.app.Fragment {
    View view;

    public FragmentMoreInfo() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.more_details_fragment,container,false);
        return view;
    }
}
