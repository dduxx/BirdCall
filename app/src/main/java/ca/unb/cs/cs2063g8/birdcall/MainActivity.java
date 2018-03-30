package ca.unb.cs.cs2063g8.birdcall;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Location;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Semester;
import ca.unb.cs.cs2063g8.birdcall.web.UNBAccess;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

/**
 * @author nmagee
 * date: 2018-02-21
 * main activity. provides input boxes for user. populates the faculties option via async task
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private final String ACTION = "action=index";
    private final String NON_CREDIT = "noncredit=0";
    private final String LEVEL= "level=UG";
    private final String FORMAT = "format=class";

    private List<Faculty> facultyList;

    //used to identify if the day of the week has been selected
    private Boolean monday = false;
    private Boolean tuesday = false;
    private Boolean wednesday = false;
    private Boolean thursday = false;
    private Boolean friday = false;

    private Spinner levelSpinner;
    private Spinner locationSpinner;
    private Spinner facultySpinner;
    private Spinner startTimeSpinner;
    private Spinner endTimeSpinner;
    private Button submitButton;
    private Button blacklistButton;
    private Button favouriteButton;
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
        blacklistButton = findViewById(R.id.blacklist_button);
        favouriteButton = findViewById(R.id.fav_button);

        mondayButton = findViewById(R.id.day_of_week_mon);
        tuesdayButton = findViewById(R.id.day_of_week_tue);
        wednesdayButton = findViewById(R.id.day_of_week_wed);
        thursdayButton = findViewById(R.id.day_of_week_thurs);
        fridayButton = findViewById(R.id.day_of_week_fri);

        populateLocationSpinner();
        populateLevelSpinner();
        populateFacultySpinner();
        populateTimeSpinners();
        populateSemesterSpinner();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SuggestionListActivity.class);
                intent.putExtra(UNBAccess.ACTION, ACTION);
                intent.putExtra(UNBAccess.NON_CREDIT, NON_CREDIT);
                intent.putExtra(UNBAccess.LEVEL, LEVEL);
                intent.putExtra(UNBAccess.FORMAT, FORMAT);

                String location = "location=" + Location.getLocationByName(
                        locationSpinner.getSelectedItem().toString()).getId();
                intent.putExtra(UNBAccess.LOCATION, location);

                String term = "term=" + new Semester(
                        semesterSpinner.getSelectedItem().toString()).getTag();
                intent.putExtra(UNBAccess.TERM, term);

                String subject = "subject=" + facultyList.get(
                        facultySpinner.getSelectedItemPosition()).getPrefix();
                intent.putExtra(UNBAccess.SUBJECT, subject);
                intent.putExtra(Course.COURSE_LEVEL, levelSpinner.getSelectedItem().toString());
                String days = setDays();
                if(!days.equals("ALL")){
                    intent.putExtra(Course.DAYS_OFFERED, days);
                }

                String time = startTimeSpinner.getSelectedItem().toString() + "-" +
                        endTimeSpinner.getSelectedItem().toString();
                if(!time.contains("Any Time")){
                    intent.putExtra(Course.TIME_SLOT, time);
                }

                if((time.split("-")[0].equals("Any Time")
                        && !time.split("-")[1].equals("Any Time"))
                        || (!time.split("-")[0].equals("Any Time")
                        && time.split("-")[1].equals("Any Time"))){
                    Toast.makeText(getApplicationContext(),
                            "If specifying a time you must use both a start and end.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra(SuggestionListActivity.SEARCH, true);
                startActivity(intent);
            }
        });

        blacklistButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(getApplicationContext(), "Blacklist not yet complete", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BlackListActivity.class);
                startActivity(intent);
            }
        });

        favouriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SuggestionListActivity.class);
                intent.putExtra(SuggestionListActivity.SEARCH, false);
                intent.putExtra(Course.COURSE_LEVEL, SuggestionListActivity.ANY_LEVEL);
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

    }//end onCreate()

    /**
     * checks for network connectivity. if non, notify the user and close the app.
     */
    private boolean checkNetwork(){
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if(isConnected){
            Log.i(TAG, "network connection found.");
            return true;
        }
        else{
            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Connection Issue");
            alertDialog.setMessage("This application requires the use of a network. Please ensure " +
                    "that you are connected to a network.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.show();
            final Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            LinearLayout.LayoutParams buttonLL = (LinearLayout.LayoutParams) button.getLayoutParams();
            buttonLL.gravity = Gravity.CENTER;
            button.setLayoutParams(buttonLL);
            return false;
        }
    }

    private String setDays(){
        String days = "";
        if(monday && tuesday && wednesday && thursday && friday){//all selected
            days = "ALL";
        }
        else if((monday || tuesday || wednesday || thursday || friday) == false){//none selected
            days = "ALL";
        }
        else{
            if(monday){
                days = days + "M";
            }

            if(tuesday){
                days = days + "T";
            }

            if(wednesday){
                days = days + "W";
            }

            if(thursday){
                days = days + "Th";
            }

            if(friday){
                days = days + "F";
            }
        }
        return days;
    }

    private boolean dayOfWeekSelect(Button button, Boolean isSelected){
        if(isSelected){
            button.setBackground(defaultDOWButton);
        }
        else{
            button.setBackground(selectedDOWButton);
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
        Log.i(TAG, "starting location task");
        requestPermissions();
    }

    /**
     * populates the faculty spinner. must use an async task. if no connection this will fail and
     *     a toast notifying the user that they have no network is displayed
     */
    private void populateFacultySpinner(){
        if(checkNetwork()){
            new FacultyDownloader().execute();
        }
    }

    /**
     * populate the time drop down menu from values stored in xml
     */
    private void populateTimeSpinners(){
        startTimeSpinner = findViewById(R.id.start_time_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        startTimeSpinner.setAdapter(adapter);

        endTimeSpinner = findViewById(R.id.end_time_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> endAdapter = ArrayAdapter.createFromResource(this,
                R.array.end_time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        endAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        endTimeSpinner.setAdapter(endAdapter);
    }

    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "requestPermissions: No permissions");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Log.i(TAG, "requestPermissions: Have permissions");
            new LocationTask().execute(Location.getAllLocations());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: Granted");
                    new LocationTask().execute(Location.getAllLocations());
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: Denied");
                    new LocationTask().execute(Location.getAllLocations());
                }
            }
        }
    }

    public class FacultyDownloader extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... requestParams) {
            List<Faculty> list = new ArrayList<>();
            list.add(new Faculty("Any Subject", "ALLSUBJECTS"));
            list.addAll(Faculty.getFaculties("level=UG"));
            facultyList = list;
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

    public class LocationTask extends AsyncTask<List<Location>, Void, List<String>>{


        @Override
        protected List<String> doInBackground(List<Location>... allLocations) {
            List<String> locationNames = new ArrayList<>();

            List<Location> locations = allLocations[0];
            for(int i=0; i<locations.size(); i++){
                locations.get(i).setDistance(MainActivity.this, new Listener());
            }

            Location nearest = locations.get(0);
            for(int i=0; i<locations.size(); i++){
                if(nearest.getDistance() > locations.get(i).getDistance()){
                    nearest = locations.get(i);
                }
            }

            locationNames.add(nearest.getName());

            for(Location l : locations){
                if(!l.equals(nearest)){
                    locationNames.add(l.getName());
                }
            }

            return locationNames;
        }

        @Override
        protected void onPostExecute(List<String> result){
            locationSpinner = findViewById(R.id.location_spinner);

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(MainActivity.this,
                            android.R.layout.simple_spinner_item, result);

            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);
        }
    }

    public class Listener implements OnSuccessListener<android.location.Location> {
        private float distance;
        private android.location.Location campus;

        @Override
        public void onSuccess(android.location.Location location) {
            this.distance=location.distanceTo(campus);
            Log.i(TAG, "calculated distance: " + getDistance());
        }

        public float getDistance(){
            return this.distance;
        }

        public void setCampus(android.location.Location campus){
            this.campus = campus;
        }
    }
}
