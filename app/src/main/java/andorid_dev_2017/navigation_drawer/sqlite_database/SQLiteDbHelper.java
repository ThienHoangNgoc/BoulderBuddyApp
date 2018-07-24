package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "boulder_entries.db";

    public static final String COMMA_SEP = ",";
    public static final String TEXT_TYPE = " TEXT";


    //Table for entries
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BoulderEntry.TABLE_NAME + " (" +
                    BoulderEntry.COLUMN_NAME_ENTRY_ID + " INTENGER PRIMARY KEY," +
                    BoulderEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_START_TIME + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_END_TIME + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_VERY_EASY + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_EASY + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_ADVANCED + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_HARD + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_VERY_HARD + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_EXTREMELY_HARD + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_SURPRISING + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_RATING + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_EXP + TEXT_TYPE + COMMA_SEP +
                    BoulderEntry.COLUMN_NAME_NOTES + TEXT_TYPE +
                    " )";

    //Create Table String for User_entries
    public static final String SQL_CREATE_ENTRIES_USER =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry.COLUMN_NAME_ENTRY_ID + " INTENGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_LAST_LOGIN + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_EXP + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_RANK + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_RANK_POINTS + TEXT_TYPE +
                    " )";

    //Delete Table String for entries
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BoulderEntry.TABLE_NAME;


    //Delete Table String for User_entries
    public static final String SQL_DELETE_ENTRIES_USER =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


    public SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_USER);
    }
}
