package andorid_dev_2017.navigation_drawer.sqlite_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;


import java.io.ByteArrayOutputStream;

public class SQLiteDbImageDBContract {

    SQLiteDbHelper sqLiteDbHelper;

    public SQLiteDbImageDBContract(Context context) {
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

    public void insertEntry(String creator, String entryNumber,
                            Bitmap image) {

        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        int id = getNumberOfEntries(ImageDB.TABLE_NAME) + 1;
        ContentValues values = new ContentValues();
        values.put(ImageDB.COLUMN_NAME_IMAGE_ID, id);
        values.put(ImageDB.COLUMN_NAME_CREATOR, creator);
        values.put(ImageDB.COLUMN_NAME_ENTRY_NUMBER, entryNumber);
        values.put(ImageDB.COLUMN_NAME_IMAGE, getBitmapAsByteArray(image));


        long newRowID;
        newRowID = db.insert(
                ImageDB.TABLE_NAME,
                null,
                values);

    }

    public void deleteRow(String rowID) {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.delete(ImageDB.TABLE_NAME, "Id=" + rowID, null);

    }

    public void deleteAllEntires() {
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();
        db.execSQL(SQLiteDbHelper.SQL_DELETE_IMAGE_DB);
        db.execSQL(SQLiteDbHelper.SQL_CREATE_IMAGE_DB);
    }


    public Cursor readEntry() {
        SQLiteDatabase db = sqLiteDbHelper.getReadableDatabase();
        String[] projection = {
                ImageDB.COLUMN_NAME_IMAGE_ID,
                ImageDB.COLUMN_NAME_CREATOR,
                ImageDB.COLUMN_NAME_ENTRY_NUMBER,
                ImageDB.COLUMN_NAME_IMAGE,
        };

        String sortOrder = ImageDB.COLUMN_NAME_IMAGE_ID + " ASC";
        Cursor c = db.query(
                ImageDB.TABLE_NAME,
                projection,
                null,               //columns for the WHERE clause
                null,               //values for the WHERE clause
                null,               //don't group the rows
                null,               //don't filter by row groups
                sortOrder
        );
        return c;
    }


    //Bitmap to ByteArray for BLOB
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }
    }


}
