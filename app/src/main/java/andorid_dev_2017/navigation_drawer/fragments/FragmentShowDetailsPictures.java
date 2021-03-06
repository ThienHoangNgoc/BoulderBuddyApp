package andorid_dev_2017.navigation_drawer.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import andorid_dev_2017.navigation_drawer.ImageGridViewItem;
import andorid_dev_2017.navigation_drawer.R;
import andorid_dev_2017.navigation_drawer.ShowPicturesFragmentGridViewEntryAdapter;
import andorid_dev_2017.navigation_drawer.sqlite_database.BoulderEntry;
import andorid_dev_2017.navigation_drawer.sqlite_database.Entry;
import andorid_dev_2017.navigation_drawer.sqlite_database.ImageDB;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbEntryContract;
import andorid_dev_2017.navigation_drawer.sqlite_database.SQLiteDbImageDBContract;

public class FragmentShowDetailsPictures extends android.support.v4.app.Fragment {
    View view;
    String entryId;
    String creator;
    TextView noImageText;
    GridView imageGridView;
    SQLiteDbImageDBContract sqLiteDbImageDBContract;
    SQLiteDbEntryContract sqLiteDbEntryContract;
    private final static String LOGTAG = "ShowDetailsPicFragment";


    private ArrayList<ImageGridViewItem> arrayOfImages;
    private ShowPicturesFragmentGridViewEntryAdapter gridViewEntryAdapter;

    public FragmentShowDetailsPictures() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.show_details_pictures_fragment, container, false);

        //Setup Database
        sqLiteDbImageDBContract = new SQLiteDbImageDBContract(getContext());
        sqLiteDbEntryContract = new SQLiteDbEntryContract(getContext());

        //get the extras from the Activity
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            entryId = extras.getString("entry_id_key");
        }

        //Setup views
        noImageText = view.findViewById(R.id.show_details_fragment_3_no_image_text_id);
        imageGridView = view.findViewById(R.id.show_details_fragment_3_image_grid_view_id);

        //Setup gridView
        arrayOfImages = new ArrayList<>();
        gridViewEntryAdapter = new ShowPicturesFragmentGridViewEntryAdapter(getContext(), arrayOfImages);
        imageGridView.setAdapter(gridViewEntryAdapter);


        //checks if there are any images, and then if the images are from the current user
        Cursor cursor = sqLiteDbImageDBContract.readEntry();
        if (cursor.getCount() < 0) {
            return view;
        } else {
            creator = getBoulderEntry(entryId).getCreator();
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if (cursor.getString(cursor.getColumnIndex(ImageDB.COLUMN_NAME_CREATOR)).equals(creator) &&
                        cursor.getString(cursor.getColumnIndex(ImageDB.COLUMN_NAME_ENTRY_NUMBER)).equals(entryId)) {
                    ImageGridViewItem gridViewItem = new ImageGridViewItem(getBitmapFromImagePath(cursor.getString(cursor.getColumnIndex(ImageDB.COLUMN_NAME_IMAGE))));
                    gridViewEntryAdapter.add(gridViewItem);
                    gridViewEntryAdapter.notifyDataSetChanged();
                    noImageText.setVisibility(View.INVISIBLE);

                }
                cursor.moveToNext();
            }

        }


        return view;
    }


    //gets an entry from the db based on the id
    public Entry getBoulderEntry(String id) {
        Entry entry;
        Cursor cursor = sqLiteDbEntryContract.readEntry();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            if (cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ENTRY_ID)).equals(id)) {
                entry = new Entry(id,
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_DATE)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_START_TIME)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_END_TIME)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_EASY)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_EASY)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_ADVANCED)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_VERY_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_EXTREMELY_HARD)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_SURPRISING)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_RATING)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_EXP)),
                        cursor.getString(cursor.getColumnIndex(BoulderEntry.COLUMN_NAME_CREATOR))
                );
                return entry;
            }
            cursor.moveToNext();
        }
        return null;
    }

    public Bitmap getBitmapFromImagePath(String imagePath) {
        File img = new File(imagePath);
        Bitmap bitmap = null;

        if (img.exists()) {
            bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
        }

        return bitmap;
    }

    public String getImagePath(Bitmap bitmap) {
        return getRealPathFromURI(getImageUri(getContext(), bitmap));
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void createLog(String text) {
        Log.d(LOGTAG, text);
    }


}
