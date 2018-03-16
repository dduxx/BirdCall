package ca.unb.cs.cs2063g8.birdcall;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.MalformedURLException;

import ca.unb.cs.cs2063g8.birdcall.database.FavouriteDBHelper;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private Button favourite;

    private FavouriteDBHelper dbHelper;

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
        favourite = findViewById(R.id.set_fav);

        new DescriptionDownloader().execute(intent.getStringExtra(Course.DESCRIPTION));
        courseID.setText(intent.getStringExtra(Course.COURSE_ID));
        courseName.setText(intent.getStringExtra(Course.COURSE_NAME));
        seatsOpen.setText(intent.getStringExtra(Course.SEATS_OPEN));
        daysOffered.setText(intent.getStringExtra(Course.DAYS_OFFERED));
        timeSlot.setText(intent.getStringExtra(Course.TIME_SLOT));
        professor.setText(intent.getStringExtra(Course.PROFESSOR));

        favourite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Favourites not yet complete", Toast.LENGTH_SHORT).show();
            }
        });
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

    public class CheckFavTask extends AsyncTask<String, Void, Cursor>{
        @Override
        protected  void onPreExecute(){
            //disable the favorites button
        }

        @Override
        protected Cursor doInBackground(String... strings) {
            return null;
        }

        @Override
        protected  void onPostExecute(Cursor result){
            //re enable the favorites button
        }
    }

    public class AddFavTask extends AsyncTask<String, Void, Void>{
        @Override
        protected  void onPreExecute(){

        }

        @Override
        protected Void doInBackground(String... strings) {
            return null;
        }

        @Override
        protected  void onPostExecute(Void result){
            
        }
    }

}
