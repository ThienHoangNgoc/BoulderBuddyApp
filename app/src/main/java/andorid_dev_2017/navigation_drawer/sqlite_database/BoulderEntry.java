package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.provider.BaseColumns;

public abstract class BoulderEntry implements BaseColumns{

    public static final String TABLE_NAME = "boulder_entries";
    public static final String COLUMN_NAME_ENTRY_ID = "Id";
    public static final String COLUMN_NAME_LOCATION = "Location";
    public static final String COLUMN_NAME_DATE = "Date";
    public static final String COLUMN_NAME_START_TIME = "StartTime";
    public static final String COLUMN_NAME_END_TIME = "EndTime";
    public static final String COLUMN_NAME_VERY_EASY = "VeryEasy";
    public static final String COLUMN_NAME_EASY = "Easy";
    public static final String COLUMN_NAME_ADVANCED = "Advanced";
    public static final String COLUMN_NAME_HARD = "Hard";
    public static final String COLUMN_NAME_VERY_HARD = "VeryHard";
    public static final String COLUMN_NAME_EXTREMELY_HARD = "ExtremelyHard";
    public static final String COLUMN_NAME_SURPRISING = "Surprising";
    public static final String COLUMN_NAME_RATING = "Rating";
    public static final String COLUMN_NAME_EXP = "Exp";
    public static final String COLUMN_NAME_NOTES = "Notes";


}
