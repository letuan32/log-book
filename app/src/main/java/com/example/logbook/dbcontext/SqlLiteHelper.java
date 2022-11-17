package com.example.logbook.dbcontext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.logbook.dbcontext.contract.PictureContract.PictureEntry;
import com.example.logbook.dbcontext.models.PictureModel;

import java.util.ArrayList;
import java.util.List;

public class SqlLiteHelper extends SQLiteOpenHelper {
//    private Context _context;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "picture.db";

    public SqlLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        _context = context;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);

    }

    public long addImageUrl( ContentValues values)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.insert( PictureEntry.TABLE_NAME, null, values );
        }
        catch (Exception e)
        {
            Log.d("Failed insert picture", e.getMessage());
            throw e;
        }
    }

    public ArrayList<PictureModel> getImageUrls()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorPicture = db.rawQuery(SQL_QUERY_ENTRIES, null);
        ArrayList<PictureModel> pictureModelArrayList = new ArrayList<>();
        if(cursorPicture.moveToFirst())
        {
            do
            {
                pictureModelArrayList.add(new PictureModel(cursorPicture.getLong(1), cursorPicture.getString(0)));
            }
            while (cursorPicture.moveToNext());
        }

        return pictureModelArrayList;
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PictureEntry.TABLE_NAME + " (" +
                    PictureEntry.COLUMN_NAME_URL + " TEXT," +
                    PictureEntry._ID + " INTEGER PRIMARY KEY)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PictureEntry.TABLE_NAME;

    private static final String SQL_QUERY_ENTRIES =
            "SELECT * FROM " + PictureEntry.TABLE_NAME + " ORDER BY " + PictureEntry._ID + " DESC";
}


