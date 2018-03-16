package ca.unb.cs.cs2063g8.birdcall.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by jason on 16/03/18.
 */

public class BlackListDBHelper extends DBHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "blacklist";
    final static String _ID = "_id";
    public static final String TYPE = "type";
    public static final String NAME = "name";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TYPE + " TEXT, " +
                    NAME + " TEXT);";

    private static final String TAG = "BlackListDBHelper";
    final static String[] COLUMNS = { _ID, TYPE, NAME };


    public BlackListDBHelper(Context context) {
        super(context);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");
    }
}
