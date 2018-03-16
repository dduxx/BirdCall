package ca.unb.cs.cs2063g8.birdcall.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nmagee on 14/03/18.
 */

public abstract class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "birdcall_db";
    public static final int DATABSE_VERIONS = 1;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABSE_VERIONS);
    }

}
