package ca.unb.cs.cs2063g8.birdcall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Location;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Semester;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Boolean monday = false;
    private Boolean tuesday = false;
    private Boolean wednesday = false;
    private Boolean thursday = false;
    private Boolean friday = false;

    private Spinner levelSpinner;
    private Spinner locationSpinner;
    private Spinner facultySpinner;
    private Spinner timeSpinner;
    private Button submitButton;
    private Button mondayButton;
    private Button tuesdayButton;
    private Button wednesdayButton;
    private Button thursdayButton;
    private Button fridayButton;
    private Spinner semesterSpinner;

    private Drawable defaultDOWButton;
    private Drawable selectedDOWButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        defaultDOWButton = getResources().getDrawable(R.drawable.day_of_week_button);
        selectedDOWButton = getResources().getDrawable(R.drawable.day_of_week_selected_button);
        submitButton = findViewById(R.id.submit_button);

        mondayButton = findViewById(R.id.day_of_week_mon);
        tuesdayButton = findViewById(R.id.day_of_week_tue);
        wednesdayButton = findViewById(R.id.day_of_week_wed);
        thursdayButton = findViewById(R.id.day_of_week_thurs);
        fridayButton = findViewById(R.id.day_of_week_fri);

        populateLocationSpinner();
        populateLevelSpinner();
        populateFacultySpinner();
        populateTimeSpinner();
        populateSemesterSpinner();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SuggestionListActivity.class);
                startActivity(intent);
            }
        });

        mondayButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                monday = dayOfWeekSelect(mondayButton, monday);
                Log.i(TAG, "Days selected: mon=" + monday + " tue=" + tuesday
                        + " wed=" + wednesday + " thur=" + thursday + " fri=" + friday);
            }
        });
        tuesdayButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                tuesday = dayOfWeekSelect(tuesdayButton, tuesday);
                Log.i(TAG, "Days selected: mon=" + monday + " tue=" + tuesday
                        + " wed=" + wednesday + " thur=" + thursday + " fri=" + friday);
            }
        });
        wednesdayButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                wednesday = dayOfWeekSelect(wednesdayButton, wednesday);
                Log.i(TAG, "Days selected: mon=" + monday + " tue=" + tuesday
                        + " wed=" + wednesday + " thur=" + thursday + " fri=" + friday);
            }
        });
        thursdayButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                thursday = dayOfWeekSelect(thursdayButton, thursday);
                Log.i(TAG, "Days selected: mon=" + monday + " tue=" + tuesday
                        + " wed=" + wednesday + " thur=" + thursday + " fri=" + friday);
            }
        });
        fridayButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                friday = dayOfWeekSelect(fridayButton, friday);
                Log.i(TAG, "Days selected: mon=" + monday + " tue=" + tuesday
                        + " wed=" + wednesday + " thur=" + thursday + " fri=" + friday);
            }
        });
    }

    private boolean dayOfWeekSelect(Button button, Boolean isSelected){
        if(isSelected){
            button.setBackgroundDrawable(defaultDOWButton);
        }
        else{
            button.setBackgroundDrawable(selectedDOWButton);
        }
        return !isSelected;
    }

    private void populateSemesterSpinner(){
        semesterSpinner = findViewById(R.id.semester_spinner);
        List<String> semesters = new ArrayList<>();

        for(Semester s : Semester.getSemesters()){
            semesters.add(s.getName());
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, semesters);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        semesterSpinner.setAdapter(adapter);
    }

    /**
     * populates the level drop down. ie: 1000, 2000, etc. populates it from a list in resources
     */
    private void populateLevelSpinner(){
        levelSpinner = findViewById(R.id.level_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.level_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        levelSpinner.setAdapter(adapter);
    }

    /**
     * populates the locations. these are hard coded right now. in the future this should
     *     try to detect location
     */
    private void populateLocationSpinner(){
        List<Location> locations = Location.getAllLocations();

        ArrayList<String> locationNames = new ArrayList<>();

        for(Location l : locations){
            locationNames.add(l.getName());
        }
        locationSpinner = findViewById(R.id.location_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, locationNames);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
    }

    /**
     * populates the faculty spinner. must use an async task. if no connection this will fail and
     *     a toast notifying the user that they have no network is displayed
     */
    private void populateFacultySpinner(){
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            new FacultyDownloader().execute();
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "No Network Detected", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * populate the time drop down menu from values stored in xml
     */
    private void populateTimeSpinner(){
        timeSpinner = findViewById(R.id.time_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        timeSpinner.setAdapter(adapter);
    }

    public class FacultyDownloader extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... requestParams) {
            facultyList = Faculty.getFaculties("level=UG");
            return "Faculty download complete";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
            ArrayList<String> faculties = new ArrayList<>();

            for(Faculty f : facultyList){
                faculties.add(f.getName());
            }

            facultySpinner = findViewById(R.id.faculty_spinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    MainActivity.this, android.R.layout.simple_spinner_item, faculties);

            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            facultySpinner.setAdapter(adapter);
        }
    }
}
