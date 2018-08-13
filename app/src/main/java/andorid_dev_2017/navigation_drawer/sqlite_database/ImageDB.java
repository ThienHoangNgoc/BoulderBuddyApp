package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.provider.BaseColumns;

public abstract class ImageDB implements BaseColumns {

    public static final String TABLE_NAME = "image_db";
    public static final String COLUMN_NAME_IMAGE_ID = "Id";
    public static final String COLUMN_NAME_CREATOR = "Creator";
    public static final String COLUMN_NAME_ENTRY_NUMBER = "EntryNumber";
    public static final String COLUMN_NAME_IMAGE = "Image";


}
