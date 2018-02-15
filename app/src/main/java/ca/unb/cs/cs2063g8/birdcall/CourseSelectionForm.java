package ca.unb.cs.cs2063g8.birdcall;

import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Description;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;
import android.util.Log;

/**
 * this is the curl command to get all course information:
 *
 */
public class CourseSelectionForm extends AppCompatActivity {
    private static final String TAG = "CourseSelectionForm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);
        //TODO: this is super bad. do not do this. fix this later once testing is done. should
        //run on another thread.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        List<Course> courses = Course.getCourseList();
        Log.i(TAG, "" + courses.size());//just seeing if the list is empty
        Description description = courses.get(0).getDescription();//get a description for a course
        Log.i(TAG, description.getPrereqs());//see if it has prereqs
        Log.i(TAG, description.getDescription());//see if it has description

    }
}
