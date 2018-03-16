package ca.unb.cs.cs2063g8.birdcall.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author nmagee
 * date: 2018-03-14
 * gets items for the favourites menu from the database
 */

public class FavouriteDBHelper extends DBHelper{
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
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }
}


