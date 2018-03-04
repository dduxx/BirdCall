package ca.unb.cs.cs2063g8.birdcall;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;

/**
 * @author jason
 * date: 2018-02-18
 * once criteria are selected this activity will display the list of courses
 */

public class SuggestionListActivity extends AppCompatActivity {
    private final String TAG = "SuggestionListActivity";
    private List<Course> courseList;
    private Button rerollButton;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_course_suggestion);

        mListView = findViewById(R.id.course_list);
        rerollButton = findViewById(R.id.reroll_button);

        rerollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populate();
            }
        });
        populate();
    }

    private void populate(){
        new CourseDownloader().execute("action=index",
                "noncredit=0",
                "term=2018/WI",
                "level=UG",
                "subject=ANTH",
                "location=FR",
                "format=CLASS");
    }


    /**
     * @author nmagee
     * date: 2018-02-21
     * async task to download the course list from the UNB site
     */
    private class CourseDownloader extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... requestParams) {
            courseList = Course.getCourseList(
                    requestParams);//this is hardcoded for now. should be passed via the intent later

            return "Download Complete";
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Not yet implemented onPreExecute()");
        }

        @Override
        protected void onPostExecute(String result){
            List<Course> suggestionList = Course.randomizer(courseList);
            CourseListAdapter courseListAdapter = new CourseListAdapter(getApplicationContext(), R.id.course_list,suggestionList);
            mListView.setAdapter(courseListAdapter);


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
                        intent.putExtra(Course.DESCRIPTION,courses.get(position).getDescription().getDescriptionUrl().toString());
                        startActivity(intent);

                    }
                });
            }

            return v;
        }

    }
}
