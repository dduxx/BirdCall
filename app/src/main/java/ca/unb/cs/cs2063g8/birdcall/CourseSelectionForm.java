package ca.unb.cs.cs2063g8.birdcall;

import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;
import android.util.Log;

/**
 * this is the curl command to get all course information:
 *
 */
public class CourseSelectionForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);
        //TODO: this is super bad. do not do this. fix this later once testing is done. should
        //run on another thread.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Course.getCourseList();
        List<Faculty> f = Faculty.getFaculties();

        for(Faculty fac : f){
            Log.i("BASE", fac.toString());
        }
    }
}
