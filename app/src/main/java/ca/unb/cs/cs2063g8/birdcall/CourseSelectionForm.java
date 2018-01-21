package ca.unb.cs.cs2063g8.birdcall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Location;

public class CourseSelectionForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);
        Location loc = new Location("UNB Fredericton", "frederictoncourses");
        loc.findAll();
    }
}
