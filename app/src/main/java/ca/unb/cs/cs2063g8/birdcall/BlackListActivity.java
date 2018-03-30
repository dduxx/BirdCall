package ca.unb.cs.cs2063g8.birdcall;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ca.unb.cs.cs2063g8.birdcall.database.BlackListDBHelper;

/**
 * Created by jason on 16/03/18.
 */

public class BlackListActivity extends AppCompatActivity {

    private BlackListDBHelper blackListDBHelper;
    private Button addButton;
    private EditText nameEditText;
    private static final String TAG = "BlackListActivity";
    private Spinner typeSpinner;
    private ListView listView;


    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blacklist_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addButton = findViewById(R.id.add_button);
        addButton.setText("Add");
        nameEditText = findViewById(R.id.name_edit_text);
        typeSpinner = findViewById(R.id.type_spinner);
        blackListDBHelper = new BlackListDBHelper(this);
        populateTypeSpinner();

        listView = findViewById(R.id.listview);
        final PopulateListTask populateListTask = new PopulateListTask();
        populateListTask.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(BlackListActivity.this).create();
                alertDialog.setTitle("Delete from Blacklist");
                alertDialog.setMessage("Are you sure that you want to delete the item from the blacklist?");
                alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteTask deleteTask = new DeleteTask();

                                RelativeLayout row = (RelativeLayout) listView.getChildAt(index);

                                TextView typeView = (TextView) row.getChildAt(0);
                                TextView nameView = (TextView) row.getChildAt(1);

                                String name = nameView.getText().toString();
                                String type = typeView.getText().toString();

                                Log.i(TAG, "name: " + name + " type: " + type);

                                deleteTask.execute(type, name);

                                Toast.makeText(getApplicationContext(),"Item Deleted from the Blacklist",Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                            }
                        });
                alertDialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "NO",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(nameEditText.getText().toString().equals(""))){
                    AddTask addTask = new AddTask();
                    addTask.execute(typeSpinner.getSelectedItem().toString(),nameEditText.getText().toString());
                    Toast.makeText(getApplicationContext(),"Item added to blacklist", Toast.LENGTH_LONG).show();

                    PopulateListTask updateList = new PopulateListTask();
                    updateList.execute();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please add input",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void populateTypeSpinner(){
        final String TYPES[] = {"Course","Professor","Faculty"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, TYPES);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

    }


    private class AddTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... params) {
            String name = params[0];
            String type = params[1];
            ContentValues contentValues = new ContentValues();
            contentValues.put(BlackListDBHelper.NAME,name);
            contentValues.put(BlackListDBHelper.TYPE,type);

            SQLiteDatabase db = blackListDBHelper.getWritableDatabase();
            db.insert(blackListDBHelper.TABLE_NAME,null,contentValues);
            return null;
        }

        protected void onPostExecute(Void result) {
            nameEditText.setText("");
            typeSpinner.setSelection(0);
        }
    }

    private class DeleteTask extends AsyncTask<String,Void,Void>{

        protected Void doInBackground(String... params) {
            SQLiteDatabase db = blackListDBHelper.getWritableDatabase();

            String[] args = {params[0],
                    params[1],
                    };

            String deletion = BlackListDBHelper.NAME
                    + " = ? AND " + BlackListDBHelper.TYPE
                    + " = ?";

            int deletedRows = db.delete(BlackListDBHelper.TABLE_NAME, deletion, args);
            Log.i(TAG, "removed: " + deletedRows + " from the database");
            return null;
        }

        protected void onPostExecute(Void result){
            PopulateListTask updateList = new PopulateListTask();
            updateList.execute();

        }

    }


    private class PopulateListTask extends AsyncTask<String, Void, Cursor> {
        protected Cursor doInBackground(String... params) {

            SQLiteDatabase db = blackListDBHelper.getReadableDatabase();

            return db.rawQuery("SELECT * FROM " + BlackListDBHelper.TABLE_NAME, null);

        }

        protected void onPostExecute(Cursor result) {
            if(!(result.getCount() == 0)) {
                String[] cols = {BlackListDBHelper.TYPE, BlackListDBHelper.NAME};
                int[] views = {R.id.name_text_view, R.id.type_text_view};
                final SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(),
                        R.layout.blacklist_list_view,
                        result,
                        cols,
                        views,
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

                listView.setAdapter(simpleCursorAdapter);

            }
            else{
                Toast.makeText(getApplicationContext(),"No results found",Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
