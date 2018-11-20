package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 35;
    public static final String DATABASE_NAME = "boulder_entries.db";

    public static final String COMMA_SEP = ",";
    public static final String TEXT_TYPE = " TEXT";
    public static final String IMAGE_TYPE = " MEDIUMBLOB";
    public static final String INTEGER_TYPE = " INTEGER";


    //Table for entries
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BoulderEntry.TABLE_NAME + " (" +
                    BoulderEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
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
                    BoulderEntry.COLUMN_NAME_CREATOR + TEXT_TYPE +
                    " )";

    //Create Table String for User_entries
    public static final String SQL_CREATE_ENTRIES_USER =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry.COLUMN_NAME_USER_ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_LAST_LOGIN + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_EXP + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_RANK + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_RANK_POINTS + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_PROFILE_PICTURE + IMAGE_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_LOGIN_STATUS + TEXT_TYPE +
                    " )";

    //Create Table String for Image_db
    public static final String SQL_CREATE_IMAGE_DB =
            "CREATE TABLE " + ImageDB.TABLE_NAME + " (" +
                    ImageDB.COLUMN_NAME_IMAGE_ID + " INTEGER PRIMARY KEY," +
                    ImageDB.COLUMN_NAME_CREATOR + TEXT_TYPE + COMMA_SEP +
                    ImageDB.COLUMN_NAME_ENTRY_NUMBER + TEXT_TYPE + COMMA_SEP +
                    ImageDB.COLUMN_NAME_IMAGE + IMAGE_TYPE +
                    " )";


    //Create Table String for Achievement_db

    public static final String SQL_CREATE_ACHIEVEMENT_DB =
            "CREATE TABLE " + AchievementEntry.TABLE_NAME + " (" +
                    AchievementEntry.COLUMN_NAME_ACHIEVEMENT_ID + " INTEGER PRIMARY KEY," +
                    AchievementEntry.COLUMN_NAME_ACHIEVEMENT_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    AchievementEntry.COLUMN_NAME_ACHIEVEMENT_STATUS_ID + INTEGER_TYPE + COMMA_SEP +
                    AchievementEntry.COLUMN_NAME_ACHIEVEMENT_CREATOR_ID + TEXT_TYPE +
                    " )";


    //Delete Table String for entries
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BoulderEntry.TABLE_NAME;

    //Delete Table String for User_entries
    public static final String SQL_DELETE_ENTRIES_USER =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    //Delete Table String for Image_entries
    public static final String SQL_DELETE_IMAGE_DB =
            "DROP TABLE IF EXISTS " + ImageDB.TABLE_NAME;

    //Delete Table String for Achievement_entries
    public static final String SQL_DELETE_ACHIEVEMENT_DB =
            "DROP TABLE IF EXISTS " + AchievementEntry.TABLE_NAME;


    public SQLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_USER);
        db.execSQL(SQL_CREATE_IMAGE_DB);
        db.execSQL(SQL_CREATE_ACHIEVEMENT_DB);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES_USER);
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_IMAGE_DB);
        db.execSQL(SQL_DELETE_ACHIEVEMENT_DB);
        onCreate(db);
    }
}
