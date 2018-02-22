package ca.unb.cs.cs2063g8.birdcall;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Location;

import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author nmagee
 * date: 2018-02-21
 * main activity. provides input boxes for user. populates the faculties option via async task
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<Faculty> facultyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        actionBar.setTitle(Html.fromHtml("<font color='"
                + Color.BLACK + "'>BirdCall</font>"));

        populateLocationSpinner();
        populateLevelSpinner();
        populateFacultySpinner();
    }

    /**
     * populates the level drop down. ie: 1000, 2000, etc. populates it from a list in resources
     */
    private void populateLevelSpinner(){
        Spinner levelSpinner = findViewById(R.id.level_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.level_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        levelSpinner.setAdapter(adapter);
    }

    private void populateLocationSpinner(){
        List<Location> locations = Location.getAllLocations();

        ArrayList<String> locationNames = new ArrayList<>();

        for(Location l : locations){
            locationNames.add(l.getName());
        }
        Spinner locationSpinner = findViewById(R.id.location_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, locationNames);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
    }

    private void populateFacultySpinner(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            FacultyDownloader downloaderTask = new FacultyDownloader();
            downloaderTask.execute();
        }
        else{
            CharSequence text = "No Network Detected";
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(getApplicationContext(), text, duration).show();
        }
    }

    public class FacultyDownloader extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... requestParams) {
            facultyList = Faculty.getFaculties("level=UG");
            return "Faculty download complete";
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Not yet implemented onPreExecute()");
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
            ArrayList<String> faculties = new ArrayList<>();

            for(Faculty f : facultyList){
                faculties.add(f.getName());
            }

            Spinner facultySpinner = findViewById(R.id.faculty_spinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    MainActivity.this, android.R.layout.simple_spinner_item, faculties);

            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            facultySpinner.setAdapter(adapter);
        }
    }
}
