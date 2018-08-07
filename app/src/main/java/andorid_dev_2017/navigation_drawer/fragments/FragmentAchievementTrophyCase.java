package andorid_dev_2017.navigation_drawer.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import andorid_dev_2017.navigation_drawer.R;

public class FragmentAchievementTrophyCase extends Fragment {

    View view;
    ImageView imageView;

    public FragmentAchievementTrophyCase() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.achievement_trophy_case_fragment, container, false);

        imageView = view.findViewById(R.id.achievement_1_id);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getContext());
                View mView = getLayoutInflater().inflate(R.layout.achievement_1_description_dialog, null);
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                //show the background color of the XML
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //change dialog window size - width and height

                dialog.show();


            }
        });


        return view;
    }
}
