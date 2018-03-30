package ca.unb.cs.cs2063g8.birdcall;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.Arrays;

import ca.unb.cs.cs2063g8.birdcall.database.BlackListDBHelper;
import ca.unb.cs.cs2063g8.birdcall.database.DBHelper;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;

/**
 * Created by jason on 16/03/18.
 */

public class BlackListActivity extends AppCompatActivity {

    private BlackListDBHelper blackListDBHelper;
    private Button addButton;
    private EditText nameEditText;
    private RecyclerView recyclerView;
    private static final String TAG = "BlackListActivity";
    private Spinner typeSpinner;

    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blacklist_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addButton = findViewById(R.id.add_button);
        nameEditText = findViewById(R.id.name_edit_text);
        typeSpinner = findViewById(R.id.type_spinner);

        populateTypeSpinner();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTask addTask = new AddTask();
                addTask.execute(typeSpinner.getSelectedItem().toString(),nameEditText.getText().toString());
            }
        });


    }

    private void populateTypeSpinner(){
        final String TYPES[] = {"Course","Professor","Faculty"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, TYPES);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
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

    /*
    So the game plan is going to be a hard coded seperate thing at the top that looks like the list below think about it as two seperate things.

     */

    /*private class addToBlackList extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            String url = params[0];
            try {
                description = new Description(url);
                description.getDescription();//description.getDescription()
                description.getPrereqs();//description.getPrereqs()
            }catch(MalformedURLException e){
                Toast.makeText(getApplicationContext(), "Unable to access URL", Toast.LENGTH_LONG).show();
                courseDescription.setText("Unable to access URL");
                prereqs.setText("Unable to access URL");
            }
            return "Download Complete";

        }
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
            Log.i(TAG, "setting desc: " + description.getDescription());
            courseDescription.setText(description.getDescription());
            Log.i(TAG, "setting prereq: " + description.getPrereqs());
            prereqs.setText(description.getPrereqs());
        }
    }*/


}
