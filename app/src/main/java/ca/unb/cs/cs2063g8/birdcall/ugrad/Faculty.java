package ca.unb.cs.cs2063g8.birdcall.ugrad;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.unb.cs.cs2063g8.birdcall.web.UNBTimetableAccess;

/**
 * @author nmagee
 * date: 2018-01-20
 * Faculty object representation
 */

public class Faculty {
    private static final String TAG = "Faculty";
    private static final String FACULTY_SOURCE_URL =
            "http://es.unb.ca/apps/timetable/ajax/get-subjects.cgi";

    private String name;
    private String prefix;

    public Faculty(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }

    /**
     * accesses UNB's online resources to find all of the faculty options available for undergrads
     *     at the url denoted by FACULTY_SOURCE_URL. Uses org.json to parse the json response from
     *     the server.
     * @return a list of UNB faculties
     */
    public static List<Faculty> getFaculties(){
        List<Faculty> faculties = new ArrayList<>();
        try {
            Map<String, String> formParams = new HashMap<>();
            formParams.put("level", "UG");
            String response = UNBTimetableAccess.getResponse(
                    formParams, new URL(FACULTY_SOURCE_URL), UNBTimetableAccess.Expected.JSON);
            JSONArray jsonArray = new JSONObject(response).getJSONArray("subjects");

            //parse the json output and use it to make the faculty list
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonFacutly = jsonArray.getJSONObject(i);
                faculties.add(new Faculty(jsonFacutly.getString("name"), jsonFacutly.getString("id")));
            }

            return faculties;
        } catch (MalformedURLException e) {
            Log.e(TAG, "problem building the URL for: " + FACULTY_SOURCE_URL, e);
            return faculties;
        } catch (JSONException e){
            Log.e(TAG, "error parsing json response from server", e);
            return faculties;
        } catch (NullPointerException e) {
            Log.e(TAG, "null value detected. likely error in request", e);
            return faculties;
        }
    }

    /**
     * returns a random faculty from the parsed list
     * @return a random faculty
     */
    public static Faculty getRandomFaculty(){
        List<Faculty> faculties = Faculty.getFaculties();
        return faculties.get(new Random().nextInt(faculties.size()));
    }
}
