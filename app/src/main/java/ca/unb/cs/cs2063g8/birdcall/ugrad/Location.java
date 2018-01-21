package ca.unb.cs.cs2063g8.birdcall.ugrad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.net.URLConnection;
import java.net.URL;

import android.os.StrictMode;
import android.util.Log;

/**
 * @author nmagee
 * date: 2018-01-20
 */

public class Location {
    private final String ERROR_TAG = "Location.class";
    private String name;
    private String locationTag;
    private final String BASE_URL = "http://www.unb.ca/academics/calendar/undergraduate/current/";

    public Location(String name, String locationTag){
        //TODO: this is super bad... just a test. get rid of this asap
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.name = name;
        this.locationTag = locationTag;
    }

    /**
     * randomly select a faculty and provide suggested courses from it
     * @return
     */
    public List<Course> findAll() {
        try {
            URLConnection site = new URL(BASE_URL + locationTag + "/").openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(site.getInputStream()));

            String src = "";
            String line;
            while ((line = reader.readLine()) != null) {
                //src = src + line;
                Log.i(ERROR_TAG, line);
            }

            return null;
        } catch (MalformedURLException e) {
            Log.e(ERROR_TAG, "Parsing issue at: " + BASE_URL + locationTag + "/", e);
            return new ArrayList<Course>();
        } catch (IOException e) {
            Log.e(ERROR_TAG, "Could not open connection too: "
                    + BASE_URL + locationTag + "/", e);
            return new ArrayList<Course>();
        }
    }
}