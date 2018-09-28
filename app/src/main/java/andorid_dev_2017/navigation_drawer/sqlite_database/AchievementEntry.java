package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.provider.BaseColumns;

public abstract class AchievementEntry implements BaseColumns {

    public static final String TABLE_NAME = "achievement_entries";
    public static final String COLUMN_NAME_ACHIEVEMENT_ID = "Id";
    public static final String COLUMN_NAME_ACHIEVEMENT_NAME_ID = "Name";
    public static final String COLUMN_NAME_ACHIEVEMENT_STATUS_ID = "Status";
    public static final String COLUMN_NAME_ACHIEVEMENT_CREATOR_ID = "Creator";

}
