package ca.unb.cs.cs2063g8.birdcall;

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
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;

/**
 * Created by jason on 16/03/18.
 */

public class BlackListActivity extends AppCompatActivity {

    private BlackListDBHelper blackListDBHelper;
    private Button addButton;
    private EditText name;
    private RecyclerView recyclerView;
    private static final String TAG = "BlackListActivity";

    private Drawable add;
    private Drawable remove;

    protected void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blacklist_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addButton = findViewById(R.id.add_remove);
        name = findViewById(R.id.name);

        populateTypeSpinner();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void populateTypeSpinner(){
        Spinner type = (Spinner) findViewById(R.id.type);

        final String TYPES[] = {"Course","Professor","Faculty"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, TYPES);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type.setAdapter(adapter);

    }

    private void addOrRemove(Button button, Boolean isFirst){
        if(isFirst){
            button.setBackgroundDrawable(add);
        }
        else{
            button.setBackgroundDrawable(remove);
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
