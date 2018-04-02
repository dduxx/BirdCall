package ca.unb.cs.cs2063g8.birdcall;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.net.MalformedURLException;

import ca.unb.cs.cs2063g8.birdcall.database.FavouriteDBHelper;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
    private Boolean isFavourite = false;
    private ProgressBar progress;

    private FavouriteDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new FavouriteDBHelper(this);

        setContentView(R.layout.course_description_activity);
        courseDescription = findViewById(R.id.course_full_description_id);
        progress = findViewById(R.id.progress_bar);
        progress.setVisibility(View.INVISIBLE);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CheckFavTask check = new CheckFavTask();

        check.execute(intent.getStringExtra(Course.COURSE_ID),
                intent.getStringExtra(Course.COURSE_NAME),
                intent.getStringExtra(Course.SEATS_OPEN),
                intent.getStringExtra(Course.DAYS_OFFERED),
                intent.getStringExtra(Course.PROFESSOR),
                intent.getStringExtra(Course.TIME_SLOT),
                intent.getStringExtra(Course.DESCRIPTION));

        courseName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isFavourite){
                    new AddFavTask().execute(getIntent().getStringExtra(Course.COURSE_ID),
                            getIntent().getStringExtra(Course.COURSE_NAME),
                            getIntent().getStringExtra(Course.SEATS_OPEN),
                            getIntent().getStringExtra(Course.DAYS_OFFERED),
                            getIntent().getStringExtra(Course.PROFESSOR),
                            getIntent().getStringExtra(Course.TIME_SLOT),
                            getIntent().getStringExtra(Course.DESCRIPTION));
                }
                else{
                    new DeleteTask().execute(getIntent().getStringExtra(Course.COURSE_ID),
                            getIntent().getStringExtra(Course.COURSE_NAME),
                            getIntent().getStringExtra(Course.SEATS_OPEN),
                            getIntent().getStringExtra(Course.DAYS_OFFERED),
                            getIntent().getStringExtra(Course.PROFESSOR),
                            getIntent().getStringExtra(Course.TIME_SLOT),
                            getIntent().getStringExtra(Course.DESCRIPTION));
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public class DescriptionDownloader extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute(){
            progress.setVisibility(View.VISIBLE);
        }

        @Override
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

       @Override
       protected void onPostExecute(String result) {
            Log.i(TAG, result);
            Log.i(TAG, "setting desc: " + description.getDescription());
            courseDescription.setText(description.getDescription());
            Log.i(TAG, "setting prereq: " + description.getPrereqs());
            prereqs.setText(description.getPrereqs());
            progress.setVisibility(View.INVISIBLE);
       }
    }

    private class CheckFavTask extends AsyncTask<String, Void, Cursor>{
        @Override
        protected  void onPreExecute(){
            //disable the favorite button until we resolve if it is in the db.
            courseName.setEnabled(false);
        }

        @Override
        protected Cursor doInBackground(String... params) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] cols = FavouriteDBHelper.COLUMNS;
            String[] args = {params[0],
                    params[1],
                    params[2],
                    params[3],
                    params[4],
                    params[5],
                    params[6]};

            String selection = FavouriteDBHelper.COURSE_ID
                    + " = ? AND " + FavouriteDBHelper.NAME
                    + " = ? AND " + FavouriteDBHelper.OPEN_SEATS
                    + " = ? AND " + FavouriteDBHelper.DAYS_OFFERED
                    + " = ? AND " + FavouriteDBHelper.PROFESSOR
                    + " = ? AND " + FavouriteDBHelper.TIME_SLOT
                    + " = ? AND " + FavouriteDBHelper.URL + " = ?";

            String order = FavouriteDBHelper.COURSE_ID;

            return db.query(FavouriteDBHelper.TABLE_NAME, cols, selection, args, null, null, order);

        }

        @Override
        protected  void onPostExecute(Cursor result){
            Drawable notFav = getResources().getDrawable(R.drawable.not_fav_icon);
            Drawable isFav = getResources().getDrawable(R.drawable.is_fav_icon);
            if(result.getCount() == 0){
                Log.i(TAG, "Course not found in favorites");
                courseName.setTextColor(Color.BLACK);
                isFavourite = false;
            }
            else{
                Log.i(TAG, "Course is in the favourites");
                courseName.setTextColor(Color.RED);
                isFavourite = true;
            }
            courseName.setEnabled(true);
        }
    }

    private class AddFavTask extends AsyncTask<String, Void, Void>{
        @Override
        protected  void onPreExecute(){
            courseName.setEnabled(false);
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "adding course to favourite");
            String courseId = params[0];
            String name = params[1];
            String openSeats = params[2];
            String daysOffered = params[3];
            String professor = params[4];
            String timeSlot = params[5];
            String url = params[6];

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(FavouriteDBHelper.COURSE_ID, courseId);
            values.put(FavouriteDBHelper.NAME, name);
            values.put(FavouriteDBHelper.OPEN_SEATS, openSeats);
            values.put(FavouriteDBHelper.DAYS_OFFERED, daysOffered);
            values.put(FavouriteDBHelper.PROFESSOR, professor);
            values.put(FavouriteDBHelper.TIME_SLOT, timeSlot);
            values.put(FavouriteDBHelper.URL, url);
            db.insert(FavouriteDBHelper.TABLE_NAME, null, values);
            return null;
        }

        @Override
        protected  void onPostExecute(Void result){
            Drawable isFav = getResources().getDrawable(R.drawable.is_fav_icon);
            courseName.setTextColor(Color.RED);
            isFavourite = true;
            courseName.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
        }
    }

    private class DeleteTask extends AsyncTask<String, Void, Void>{
        @Override
        protected  void onPreExecute(){
            courseName.setEnabled(false);
        }

        @Override
        protected Void doInBackground(String... params) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] args = {params[0],
                    params[1],
                    params[2],
                    params[3],
                    params[4],
                    params[5],
                    params[6]};

            String deletion = FavouriteDBHelper.COURSE_ID
                    + " = ? AND " + FavouriteDBHelper.NAME
                    + " = ? AND " + FavouriteDBHelper.OPEN_SEATS
                    + " = ? AND " + FavouriteDBHelper.DAYS_OFFERED
                    + " = ? AND " + FavouriteDBHelper.PROFESSOR
                    + " = ? AND " + FavouriteDBHelper.TIME_SLOT
                    + " = ? AND " + FavouriteDBHelper.URL + " = ?";

            int deletedRows = db.delete(FavouriteDBHelper.TABLE_NAME, deletion, args);
            Log.i(TAG, "removed: " + deletedRows + " from the database");
            return null;
        }

        @Override
        protected  void onPostExecute(Void result){
            courseName.setTextColor(Color.BLACK);
            isFavourite = false;
            courseName.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
        }
    }

}
