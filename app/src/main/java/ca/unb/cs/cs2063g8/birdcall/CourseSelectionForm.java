package ca.unb.cs.cs2063g8.birdcall;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Location;

import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class CourseSelectionForm extends AppCompatActivity {
    private static final String TAG = "CourseSelectionForm";

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
    }

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, locationNames);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
    }

    private void populateFacultySpinner(){

    }
}
