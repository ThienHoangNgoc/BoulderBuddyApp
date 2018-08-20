package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class SQLiteDbEntryContract {

    SQLiteDbHelper sqLiteDbHelper;

    public SQLiteDbEntryContract(Context context) {
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




    public void insertEntry(String location, String date, String startTime,
                            String endTime,
                            String veryEasy, String easy, String advanced,
                            String hard, String veryHard, String extremelyHard,
                            String surprising,
                            String rating, String exp, String creator) {

        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BoulderEntry.COLUMN_NAME_LOCATION, location);
        values.put(BoulderEntry.COLUMN_NAME_DATE, date);
        values.put(BoulderEntry.COLUMN_NAME_START_TIME, startTime);
        values.put(BoulderEntry.COLUMN_NAME_END_TIME, endTime);
        values.put(BoulderEntry.COLUMN_NAME_VERY_EASY, veryEasy);
        values.put(BoulderEntry.COLUMN_NAME_EASY, easy);
        values.put(BoulderEntry.COLUMN_NAME_ADVANCED, advanced);
        values.put(BoulderEntry.COLUMN_NAME_HARD, hard);
        values.put(BoulderEntry.COLUMN_NAME_VERY_HARD, veryHard);
        values.put(BoulderEntry.COLUMN_NAME_EXTREMELY_HARD, extremelyHard);
        values.put(BoulderEntry.COLUMN_NAME_SURPRISING, surprising);
        values.put(BoulderEntry.COLUMN_NAME_RATING, rating);
        values.put(BoulderEntry.COLUMN_NAME_EXP, exp);
        values.put(BoulderEntry.COLUMN_NAME_CREATOR, creator);


        long newRowID;
        newRowID = db.insert(
                BoulderEntry.TABLE_NAME,
                null,
                values);

    }


    public void deleteRow(String rowID) {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.delete(BoulderEntry.TABLE_NAME, "Id=" + rowID, null);

    }

    public void deleteAllEntries() {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.execSQL(sqLiteDbHelper.SQL_DELETE_ENTRIES);
        db.execSQL(sqLiteDbHelper.SQL_CREATE_ENTRIES);
    }


    public Cursor readEntry() {
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();
        String[] projection = {
                BoulderEntry.COLUMN_NAME_ENTRY_ID,
                BoulderEntry.COLUMN_NAME_LOCATION,
                BoulderEntry.COLUMN_NAME_DATE,
                BoulderEntry.COLUMN_NAME_START_TIME,
                BoulderEntry.COLUMN_NAME_END_TIME,
                BoulderEntry.COLUMN_NAME_VERY_EASY,
                BoulderEntry.COLUMN_NAME_EASY,
                BoulderEntry.COLUMN_NAME_ADVANCED,
                BoulderEntry.COLUMN_NAME_HARD,
                BoulderEntry.COLUMN_NAME_VERY_HARD,
                BoulderEntry.COLUMN_NAME_EXTREMELY_HARD,
                BoulderEntry.COLUMN_NAME_SURPRISING,
                BoulderEntry.COLUMN_NAME_RATING,
                BoulderEntry.COLUMN_NAME_EXP,
                BoulderEntry.COLUMN_NAME_CREATOR,
        };

        String sortOrder = BoulderEntry.COLUMN_NAME_ENTRY_ID + " ASC";
        Cursor c = db.query(
                BoulderEntry.TABLE_NAME,
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
