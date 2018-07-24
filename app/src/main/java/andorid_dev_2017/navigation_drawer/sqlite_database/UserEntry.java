package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.provider.BaseColumns;

public abstract class UserEntry implements BaseColumns {

    public static final String TABLE_NAME = "user_entries";
    public static final String COLUMN_NAME_ENTRY_ID = "Id";
    public static final String COLUMN_NAME_USERNAME = "Name";
    public static final String COLUMN_NAME_PASSWORD = "Password";
    public static final String COLUMN_NAME_LAST_LOGIN  = "LastLogin";
    public static final String COLUMN_NAME_EXP = "Exp";
    public static final String COLUMN_NAME_RANK = "Rank";
    public static final String COLUMN_NAME_RANK_POINTS = "RankPoints";


}
