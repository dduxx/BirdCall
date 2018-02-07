package ca.unb.cs.cs2063g8.birdcall.ugrad;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.unb.cs.cs2063g8.birdcall.web.UNBAccess;

/**
 * @author nmagee
 * date: 2018-01-20
 * unb course object
 */

public class Course {
    private static final String TAG = "Course";
    private static final String COURSE_SOURCE_URL =
            "http://es.unb.ca/apps/timetable/index.cgi";

    private String id;
    private Faculty faculty;
    private Integer openSeats;
    private Integer totalSeats;
    private Description description;

    public Course(){

    }

    public Course(
            String id,
            Faculty faculty,
            Integer openSeats,
            Integer totalSeats,
            Description description){
        this.id = id;
        this.faculty = faculty;
        this.openSeats = openSeats;
        this.totalSeats = totalSeats;
        this.description = description;
    }

    public static List<Course> getCourseList(){
        List<Course> courses = new ArrayList<>();
        try{
            Map<String, String> params = new HashMap<>();
            params.put("action", "index");
            params.put("noncredit", "0");
            params.put("term", "2018/WI");
            params.put("level", "UG");
            //params.put("subject", "ALLSUBJECTS");
            params.put("subject", "ADM"); //performance is WAAY better if we force faculty selection
            params.put("location", "FR");
            params.put("format", "CLASS");
            String response = UNBAccess.getResponse(
                    params, new URL(COURSE_SOURCE_URL), UNBAccess.Expected.HTML);

            Document doc = Jsoup.parse(response);
            Iterator<Element> rows = doc.getElementsByTag("tr").iterator();
            while(rows.hasNext()){
                Element row = rows.next();
                Elements cells = row.getElementsByTag("td");
                if(cells.size() == 9){
                    Iterator<Element> cellIterator = cells.iterator();
                    String id;
                    Faculty faculty = new Faculty("Testing adm", "ADM");
                    Integer openSeats;
                    Integer totalSeats;
                    Description description;
                    /**
                     * ID	Course	Section	Title	Instructor	Days	Times	Room	Capacity / Enrollment
                     */
                    for(int i=0; i<cells.size(); i++){
                        if(i == 0){
                            Log.i(TAG, cells.get(i).ownText());
                        }

                        if(i == 1){
                            Log.i(TAG, cells.get(i).ownText());
                        }

                        if(i == 2){
                            Log.i(TAG, cells.get(i).ownText());
                        }

                        if(i == 3){
                            Log.i(TAG, cells.get(i).ownText());
                        }

                        if(i == 4){
                            Log.i(TAG, cells.get(i).ownText());
                        }

                        if(i == 5){
                            Log.i(TAG, cells.get(i).ownText());
                        }

                        if(i == 6){
                            Log.i(TAG, cells.get(i).ownText());
                        }
                        if(i == 7){
                            Log.i(TAG, cells.get(i).ownText());
                        }

                        if(i == 8){
                            Log.i(TAG, cells.get(i).ownText());
                        }


                    }
                }
            }
        } catch(MalformedURLException e) {
            return courses;
        }

        return courses;
    }
}
