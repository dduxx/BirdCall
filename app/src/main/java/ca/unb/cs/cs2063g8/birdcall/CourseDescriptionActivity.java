package ca.unb.cs.cs2063g8.birdcall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Location;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

/**
 * Created by Alex on 2018-02-24.
 */

public class CourseDescriptionActivity extends AppCompatActivity {
    private TextView courseID;
    private TextView courseName;
    private TextView seatsOpen;
    private TextView courseDescription;
    private TextView prereqs;
    private TextView daysOffered;
    private TextView timeSlot;
    private TextView professor;
    private Description description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        courseDescription = findViewById(R.id.course_full_description_id);
        prereqs = findViewById(R.id.prereqs_id);
        Intent intent = this.getIntent();
        courseID = findViewById(R.id.course_id);
        courseName = findViewById(R.id.course_name_id);
        seatsOpen = findViewById(R.id.seats_open_id);
        daysOffered = findViewById(R.id.days_offered_id);
        timeSlot = findViewById(R.id.time_slot_id);
        professor = findViewById(R.id.professor_id);



        new DescriptionDownloader().execute(intent.getStringExtra(Course.DESCRIPTION));
        courseID.setText(Course.COURSE_ID);
        courseName.setText(Course.COURSE_NAME);
        seatsOpen.setText(Course.SEATS_OPEN);
        daysOffered.setText("NA");
        timeSlot.setText("NA");
        professor.setText("NA");



    }
    public class DescriptionDownloader extends AsyncTask<String, Integer, String> {
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
            courseDescription.setText(description.getDescription());
            prereqs.setText(description.getPrereqs());
        }
    }
}
