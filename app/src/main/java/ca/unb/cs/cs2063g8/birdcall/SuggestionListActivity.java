package ca.unb.cs.cs2063g8.birdcall;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ca.unb.cs.cs2063g8.birdcall.ugrad.Course;
import ca.unb.cs.cs2063g8.birdcall.ugrad.Faculty;

/**
 * @author jason
 * date: 2018-02-18
 * once criteria are selected this activity will display the list of courses
 */

public class SuggestionListActivity {
    private final String TAG = "SuggestionListActivity";
    private List<Course> courseList;


    /**
     * @author nmagee
     * date: 2018-02-21
     * async task to download the course list from the UNB site
     */
    public class CourseDownloader extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... requestParams) {
            courseList = Course.getCourseList(
                    "action=index",
                    "noncredit=0",
                    "term=2018/WI",
                    "level=UG",
                    "subject=ANTH",
                    "location=FR",
                    "format=CLASS");//this is hardcoded for now. should be passed via the intent later

            //add something else here for selection and whatnot.
            return "Faculty download complete";
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Not yet implemented onPreExecute()");
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
