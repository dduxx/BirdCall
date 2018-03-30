package ca.unb.cs.cs2063g8.birdcall.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author nmagee
 * date: 2018-03-14
 * gets items for the favourites menu from the database
 */

public class FavouriteDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "fav_db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "favourites";

    public static final String ID = "id";
    public static final String COURSE_ID = "course_id";
    public static final String NAME = "name";
    public static final String OPEN_SEATS = "open_seats";
    public static final String DAYS_OFFERED= "days_offered";
    public static final String PROFESSOR = "professor";
    public static final String TIME_SLOT = "time_slot";
    public static final String URL = "url";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_ID + " TEXT, " +
                    NAME + " TEXT, " +
                    OPEN_SEATS + " INT, " +
                    DAYS_OFFERED + " TEXT, " +
                    PROFESSOR + " TEXT, " +
                    TIME_SLOT + " TEXT, " +
                    URL + " TEXT);";

    public static final String[] COLUMNS =
            {ID, COURSE_ID, NAME, OPEN_SEATS, DAYS_OFFERED, PROFESSOR, TIME_SLOT, URL};

    private static final String TAG = "FavouriteDBHelper";

    public FavouriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");
    }
}


