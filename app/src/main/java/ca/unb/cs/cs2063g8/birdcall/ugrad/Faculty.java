package ca.unb.cs.cs2063g8.birdcall.ugrad;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author nmagee
 * date: 2018-01-20
 * curl command to get faculty list:
 *     curl -X POST -F 'level=UG' http://es.unb.ca/apps/timetable/ajax/get-subjects.cgi
 *
 *     this command returns json of all unb faculties
 */

public class Faculty {
    private final static String TAG = "Faculty.class";
    private final static String FACULTY_SOURCE_URL =
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
     * accesses UNB's online resources to find all of the facutly options available for undergrads
     *     at the url: http://es.unb.ca/apps/timetable/ajax/get-subjects.cgi. Uses org.json to parse
     *     the json response from the server.
     * @return faculties a list of faculties
     */
    public static List<Faculty> getFaculties(){
        List<Faculty> faculties = new ArrayList<>();
        try{
            Log.i(TAG, "accessing faculty list at" + FACULTY_SOURCE_URL);
            URL url = new URL(FACULTY_SOURCE_URL);
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("level", "UG");

            //building the request body
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            Log.i(TAG, "sending request to url");
            //building the request headers
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            //send body
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            //parsing the response from the server
            BufferedReader response = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String json = "";
            for (int character; (character = response.read()) >= 0;){
                json = json + (char) character;
            }

            JSONArray jsonArray = new JSONObject(json).getJSONArray("subjects");

            //parse the json output and use it to make the faculty list
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonFacutly = jsonArray.getJSONObject(i);
                faculties.add(new Faculty(jsonFacutly.getString("name"), jsonFacutly.getString("id")));
            }
        } catch (Exception e) {
            Log.e(TAG, "unable to gather faculty information", e);
        }

        return faculties;
    }
}
