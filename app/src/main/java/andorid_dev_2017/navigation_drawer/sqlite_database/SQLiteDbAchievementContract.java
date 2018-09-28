package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDbAchievementContract {

    SQLiteDbHelper sqLiteDbHelper;


    public SQLiteDbAchievementContract(Context context) {
        sqLiteDbHelper = new SQLiteDbHelper(context);
    }

    private int getNumberOfEntries(String tableName) {
        String countQuery = "SELECT *FROM " + tableName;
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void insertEntry(String name, int status, String creator) {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        int id = getNumberOfEntries(AchievementEntry.TABLE_NAME) + 1;
        ContentValues values = new ContentValues();
        values.put(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_ID, id);
        values.put(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_NAME_ID, name);
        values.put(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_STATUS_ID, status);
        values.put(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_CREATOR_ID, creator);


        long newRowID;
        newRowID = db.insert(
                AchievementEntry.TABLE_NAME,
                null,
                values);

    }

    public void updateOneStringColumn(String columnName, String newValue, String rowID) {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnName, newValue);
        db.update(AchievementEntry.TABLE_NAME, values, "Id=" + rowID, null);
    }

    public void updateStatusColumn(int newValue, String rowID) {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AchievementEntry.COLUMN_NAME_ACHIEVEMENT_STATUS_ID, newValue);
        db.update(AchievementEntry.TABLE_NAME, values, "Id=" + rowID, null);
    }

    public void deleteRow(String rowID) {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.delete(AchievementEntry.TABLE_NAME, "Id=" + rowID, null);

    }

    public void deleteAllEntires() {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.execSQL(SQLiteDbHelper.SQL_DELETE_ACHIEVEMENT_DB);
        db.execSQL(SQLiteDbHelper.SQL_CREATE_ACHIEVEMENT_DB);
    }

    public Cursor readEntry() {
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();
        String[] projection = {
                AchievementEntry.COLUMN_NAME_ACHIEVEMENT_ID,
                AchievementEntry.COLUMN_NAME_ACHIEVEMENT_NAME_ID,
                AchievementEntry.COLUMN_NAME_ACHIEVEMENT_STATUS_ID,
                AchievementEntry.COLUMN_NAME_ACHIEVEMENT_CREATOR_ID,


        };

        String sortOrder = AchievementEntry.COLUMN_NAME_ACHIEVEMENT_ID + " ASC";
        Cursor c = db.query(
                AchievementEntry.TABLE_NAME,
                projection,
                null,               //columns for the WHERE clause
                null,               //values for the WHERE clause
                null,               //don't group the rows
                null,               //don't filter by row groups
                sortOrder
        );
        return c;
    }

}
