package ca.unb.cs.cs2063g8.birdcall;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.MalformedURLException;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;

/**
 * @author Alex
 * date: 2018-02-24.
 */

public class CourseDescriptionActivity extends AppCompatActivity {
    private static final String TAG = "CourseDescActivity";
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
        setContentView(R.layout.course_description_activity);
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
        courseID.setText(intent.getStringExtra(Course.COURSE_ID));
        courseName.setText(intent.getStringExtra(Course.COURSE_NAME));
        seatsOpen.setText(intent.getStringExtra(Course.SEATS_OPEN));
        daysOffered.setText(intent.getStringExtra(Course.DAYS_OFFERED));
        timeSlot.setText(intent.getStringExtra(Course.TIME_SLOT));
        professor.setText(intent.getStringExtra(Course.PROFESSOR));
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
            Log.i(TAG, result);
            Log.i(TAG, "setting desc: " + description.getDescription());
            courseDescription.setText(description.getDescription());
            Log.i(TAG, "setting prereq: " + description.getPrereqs());
            prereqs.setText(description.getPrereqs());
        }
    }
}
