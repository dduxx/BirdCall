package ca.unb.cs.cs2063g8.birdcall;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;
import android.util.Log;

/**
 * this is the curl command to get all course information:
 *     curl -X POST -F 'action=index' -F 'noncredit=0' -F 'term=2018/WI' -F 'level=UG' -F 'subject=ALLSUBJECTS' -F 'location=FR' -F 'format=CLASS' http://es.unb.ca/apps/timetable/index.cgi
 */
public class CourseSelectionForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);

        //TODO: this is super bad. do not do this. fix this later once testing is done. shoudl
        //run on another thread.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        List<Faculty> faculties = Faculty.getFaculties();

        for(Faculty faculty : faculties){
            Log.i("Base", faculty.toString());
        }
    }
}
