package ca.unb.cs.cs2063g8.birdcall;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.unb.cs.cs2063g8.birdcall.database.BlacklistItem;
import ca.unb.cs.cs2063g8.birdcall.database.FavouriteDBHelper;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;
import ca.unb.cs.cs2063g8.birdcall.web.UNBAccess;

/**
 * @author jason
 * date: 2018-02-18
 * once criteria are selected this activity will display the list of courses
 */

public class SuggestionListActivity extends AppCompatActivity {
    private final String TAG = "SuggestionListActivity";
    public static final String ANY_LEVEL = "Any Level";
    public static final String SEARCH = "SEARCH";
    public static final String BLACKLIST = "BLACKLIST";
    private boolean allowWeight;
    private List<Course> courseList;
    private Button rerollButton;
    private ListView mListView;
    private ProgressBar progress;
    private TextView count;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeEventHandler mShakeEventHandler;

    private List<BlacklistItem> blacklistItems;

    @Override
    public void onResume(){
        super.onResume();
        mSensorManager.registerListener(mShakeEventHandler, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        if(!getIntent().getBooleanExtra(SEARCH, false)){
            new FavouritesTask().execute();
        }
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeEventHandler);
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_course_suggestion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allowWeight = getIntent().getStringExtra(Course.COURSE_LEVEL).equals(ANY_LEVEL);
        Log.i(TAG, "weighted selection is: " + allowWeight);
        mListView = findViewById(R.id.course_list);
        rerollButton = findViewById(R.id.reroll_button);
        progress = findViewById(R.id.progress_bar);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeEventHandler = new ShakeEventHandler();

        rerollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populate();
            }
        });
        count = findViewById(R.id.count_id);

        mShakeEventHandler.setOnShakeListener(new ShakeEventHandler.OnShakeListener() {

            @Override
            public void onShake(int count)
            {
                populate();
            }
        });

        populate();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void populate(){
        Log.i(TAG, "search value is: " + getIntent().getBooleanExtra(SEARCH, false));
        if(courseList == null && getIntent().getBooleanExtra(SEARCH, false) == true){
            new CourseDownloader().execute(
                    getIntent().getStringExtra(UNBAccess.ACTION),
                    getIntent().getStringExtra(UNBAccess.NON_CREDIT),
                    getIntent().getStringExtra(UNBAccess.TERM),
                    getIntent().getStringExtra(UNBAccess.LEVEL),
                    getIntent().getStringExtra(UNBAccess.SUBJECT),
                    getIntent().getStringExtra(UNBAccess.LOCATION),
                    getIntent().getStringExtra(UNBAccess.FORMAT));
        }
        else if(courseList == null && getIntent().getBooleanExtra(this.SEARCH, false) == false){
            rerollButton.setEnabled(false);
            rerollButton.setVisibility(View.INVISIBLE);
            new FavouritesTask().execute();
        }
        else{
            CourseListAdapter courseListAdapter = new CourseListAdapter(getApplicationContext(),
                    R.id.course_list, Course.randomizer(courseList
                            .toArray(new Course[courseList.size()]), allowWeight));
            mListView.setAdapter(courseListAdapter);
        }
    }


    /**
     * @author nmagee
     * date: 2018-02-21
     * async task to download the course list from the UNB site
     */
    private class CourseDownloader extends AsyncTask<String, Integer, String> {

        @Override
        public void onPreExecute(){
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... requestParams) {
            Map<String, String> filters = new HashMap<>();
            filters.put(Course.COURSE_LEVEL, getIntent().getStringExtra(Course.COURSE_LEVEL));

            if(getIntent().getStringExtra(Course.DAYS_OFFERED) != null){
                filters.put(Course.DAYS_OFFERED, getIntent().getStringExtra(Course.DAYS_OFFERED));
            }
            if(getIntent().getStringExtra(Course.TIME_SLOT) != null){
                filters.put(Course.TIME_SLOT, getIntent().getStringExtra(Course.TIME_SLOT));
            }

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<BlacklistItem>>(){}.getType();
            blacklistItems = gson.fromJson(getIntent().getStringExtra(BLACKLIST), type);
            courseList = Course.getCourseList(filters, blacklistItems, requestParams);

            return "Download Complete";
        }

        @Override
        protected void onPostExecute(String result){
            List<Course> suggestionList = Course.randomizer(courseList
                    .toArray(new Course[courseList.size()]), allowWeight);
            CourseListAdapter courseListAdapter = new CourseListAdapter(getApplicationContext(), R.id.course_list, suggestionList);
            mListView.setAdapter(courseListAdapter);
            progress.setVisibility(View.INVISIBLE);
            if(courseList != null){
                count.setText("Courses Found: " + courseList.size());
                count.setTextColor(Color.BLACK);
            }
            else{
                count.setText("Courses Found: 0");
                count.setTextColor(Color.BLACK);
            }
        }

    }

    private class FavouritesTask extends AsyncTask<String, Void, Cursor>{

        @Override
        protected  void onPreExecute(){
            Log.i(TAG, "Loading favourites");
            progress.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Cursor doInBackground(String... strings) {
            SQLiteDatabase db = new FavouriteDBHelper(getApplicationContext()).getReadableDatabase();

            return db.rawQuery("SELECT * FROM " + FavouriteDBHelper.TABLE_NAME, null);
        }

        @Override
        protected  void onPostExecute(Cursor result){
            List<Course> favourites = new ArrayList<>();

            if (result.moveToFirst()) {
                while (!result.isAfterLast()) {
                    try{
                        Course course = new Course(
                                result.getString(result.getColumnIndex(FavouriteDBHelper.COURSE_ID)),
                                result.getString(result.getColumnIndex(FavouriteDBHelper.NAME)),
                                result.getInt(result.getColumnIndex(FavouriteDBHelper.OPEN_SEATS)),
                                new Description(result.getString(result.getColumnIndex(FavouriteDBHelper.URL))),
                                result.getString(result.getColumnIndex(FavouriteDBHelper.DAYS_OFFERED)),
                                result.getString(result.getColumnIndex(FavouriteDBHelper.TIME_SLOT)),
                                result.getString(result.getColumnIndex(FavouriteDBHelper.PROFESSOR)));
                        favourites.add(course);
                        result.moveToNext();
                    } catch(MalformedURLException e){
                        result.moveToNext();
                    }
                }
            }
            courseList = favourites;
            CourseListAdapter courseListAdapter = new CourseListAdapter(getApplicationContext(), R.id.course_list, courseList);
            mListView.setAdapter(courseListAdapter);
            if(courseList != null){
                count.setText("Courses Found: " + courseList.size());
                count.setTextColor(Color.BLACK);
            }
            else{
                count.setText("Courses Found: " + 0);
                count.setTextColor(Color.BLACK);
            }
        }
    }

    private class CourseListAdapter extends ArrayAdapter<Course> {
        private List<Course> courses;

        public CourseListAdapter(Context context, int textViewResourceId, List<Course> courses) {
            super(context, textViewResourceId, courses);
            this.courses = courses;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_view, null);
            }

            if (courses.get(position) != null) {
                TextView id = v.findViewById(R.id.course_id_list_text);
                TextView name = v.findViewById(R.id.course_name_list_text);
                TextView seats = v.findViewById(R.id.course_seats_list_text);

                id.setText(courses.get(position).getId());
                name.setText(courses.get(position).getName());
                seats.setText("" + courses.get(position).getOpenSeats());

                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent intent = new Intent(getApplicationContext(), CourseDescriptionActivity.class);
                        intent.putExtra(Course.COURSE_ID, courses.get(position).getId());
                        intent.putExtra(Course.COURSE_NAME,courses.get(position).getName());
                        intent.putExtra(Course.SEATS_OPEN, courses.get(position).getOpenSeats().toString());
                        intent.putExtra(Course.DESCRIPTION,courses.get(position).getDescription().getDescriptionUrl());
                        intent.putExtra(Course.PROFESSOR, courses.get(position).getProfessor());
                        intent.putExtra(Course.DAYS_OFFERED, courses.get(position).getDaysOffered());
                        intent.putExtra(Course.TIME_SLOT, courses.get(position).getTimeSlot());
                        startActivity(intent);
                    }
                });
            }
            return v;
        }

    }
}
