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

    private static final String[] columns =
            {ID, COURSE_ID, NAME, OPEN_SEATS, DAYS_OFFERED, PROFESSOR, TIME_SLOT, URL};

    private static final String TAG = "FavouriteDBHelper";

    public FavouriteDBHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    package mobiledev.unb.ca.trydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

    public class MainActivity extends AppCompatActivity {

        private DBHelper mDBHelper;
        private Button mAddButton;
        private EditText mSearchEditText;
        private EditText mItemEditText;
        private EditText mNumberEditText;
        private TextView mResultsTextView;
        private static final String TAG = "TAG";
        private ListView mListview;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // TODO Nothing to do here, just note that a DBHelper has been instantiated
            mDBHelper = new DBHelper(this);
            mAddButton = (Button) findViewById(R.id.add_button);
            mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
            mItemEditText = (EditText) findViewById(R.id.item_edit_text);
            mNumberEditText = (EditText) findViewById(R.id.number_edit_text);
            mResultsTextView = (TextView) findViewById(R.id.results_text_view);
            mListview = (ListView) findViewById(R.id.listview);

            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO Check if some text has been entered in both the item and number
                    // EditTexts. If so, create and execute an AddTask, passing its
                    // doInBackground method the text from these EditTetxs. If not,
                    // display a toast indicating that the data entered was incomplete.
                    if((mItemEditText.getText().toString().equals("")) &&
                            mNumberEditText.getText().toString().equals("")){
                        int duration = Toast.LENGTH_SHORT;
                        CharSequence text = "Data incomplete";
                        Toast toast = Toast.makeText(getApplicationContext(),text,duration);
                        toast.show();
                    }
                    else{
                        AddTask addTask = new AddTask();
                        addTask.execute(mItemEditText.getText().toString(), mNumberEditText.getText().toString());
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(mNumberEditText.getWindowToken(), 0);
                        mgr.hideSoftInputFromWindow(mItemEditText.getWindowToken(), 0);
                    }




                }
            });

            mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        // TODO v is the search EditText. (EditText is a subclass of TextView.)
                        // Get the text from this view. Create and execute a QueryTask passing
                        // its doInBackground method this text.
                        QueryTask queryTask = new QueryTask();
                        queryTask.execute(v.getText().toString());


                    }
                    return false;
                }
            });
        }


        private class AddTask extends AsyncTask<String, Void, Void> {

            protected Void doInBackground(String... params) {
                // TODO Get the item and number that were passed to this method
                // as params. Add a corresponding row to the the database.
                String item = params[0];
                String number = params[1];
                ContentValues contentValues = new ContentValues();
                contentValues.put(mDBHelper.ITEM,item);
                contentValues.put(mDBHelper.NUM, number);

                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                db.insert(mDBHelper.TABLE_NAME,null,contentValues);


                return null;
            }

            protected void onPostExecute(Void result) {

                // TODO You will need to write a bit of extra code to get the
                // UI to behave nicely, e.g., showing and hiding the keyboard
                // at the right time, clearing text fields appropriately. Some
                // of that code will likely go here, but you might also make
                // changes elsewhere in the app. Exactly how you make the
                // UI behave is up to you, but you should make reasonable
                // choices.
                mNumberEditText.setText("");
                mItemEditText.setText("");





            }
        }


        private class QueryTask extends AsyncTask<String, Void, Cursor> {
            protected Cursor doInBackground(String... params) {
                // TODO Get the query String from params. Query the database to
                // retrieve all rows that have an item that matches this query,
                // and return this Cursor object. Make sure that the results
                // are sorted appropriately.

                // Remove this return statement when you're done this part
                SQLiteDatabase db = mDBHelper.getReadableDatabase();
                String[] projection = DBHelper.COLUMNS;

                String selection = mDBHelper.ITEM + " LIKE ?" ;

                String[] selectionArgs = {params[0]};

                String orderby = mDBHelper.NUM;

                Cursor cursor = db.query(
                        mDBHelper.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        orderby
                );

                return cursor;

            }

            protected void onPostExecute(Cursor result) {
                // TODO Use a SimpleCursorAdapter to set the adapter for
                // the ListView (mListview) to be the Cursor passed
                // to onPostExecute. If there are no results, set the
                // results TextView to indicate that there are no results.
                //
                // Again, you might need to write a bit of extra code here,
                // or elsewhere, to get the UI to behave nicely
                if(!(result.getCount() == 0)) {
                    String[] cols = {DBHelper.ITEM, DBHelper.NUM};
                    int[] views = {R.id.item_textview, R.id.num_textview};
                    SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),
                            R.layout.list_layout,
                            result,
                            cols,
                            views,
                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                    mListview.setAdapter(simpleCursorAdapter);
                    mResultsTextView.setText("");
                    mSearchEditText.clearFocus();
                }
                else{
                    String[] message = {"No results found"};
                    //ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_layout,message);
                    //mListview.setAdapter(stringArrayAdapter);
                    mResultsTextView.setText(message[0]);
                    mListview.setVisibility(View.INVISIBLE);
                    mSearchEditText.clearFocus();


                }


            }
        }
    }


}


