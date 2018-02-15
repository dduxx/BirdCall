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
    private String name;
    private Faculty faculty;
    private Integer openSeats;
    private Description description;

    public Course(){

    }

    public Course(
            String id,
            String name,
            Faculty faculty,
            Integer openSeats,
            Description description){
        this.id = id;
        this.name = name;
        this.faculty = faculty;
        this.openSeats = openSeats;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Integer getOpenSeats() {
        return openSeats;
    }

    public void setOpenSeats(Integer openSeats) {
        this.openSeats = openSeats;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", faculty=" + faculty +
                ", openSeats=" + openSeats +
                ", description=" + description.getDescriptionUrl().toString() +
                '}';
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
            params.put("subject", "ANTH"); //performance is WAAY better if we force faculty selection
            params.put("location", "FR");
            params.put("format", "CLASS");
            String response = UNBAccess.getResponse(
                    params, new URL(COURSE_SOURCE_URL), UNBAccess.Expected.HTML);


            Document doc = Jsoup.parse(response);
            Iterator<Element> rows = doc.getElementsByTag("tbody").first().children().iterator();

            while(rows.hasNext()){
                Elements cells = rows.next().children();
                if(cells.size() != 9){
                    continue;
                }
                else{
                    String id = cells.get(1).text().replace("*", "");
                    String name = cells.get(3).text();
                    Faculty faculty = new Faculty("Anthropology","ANTH");
                    Integer total = Integer.parseInt(cells.get(8).text().replaceAll("\\s", "").split("/")[0]);
                    Integer enrollment = Integer.parseInt(cells.get(8).text().replaceAll("\\s", "").split("/")[1]);
                    Integer openSeats = total - enrollment;
                    String url = cells.get(1).getElementsByAttribute("href").first().attr("href");
                    Description description = new Description(new URL(url));

                    courses.add(new Course(id, name, faculty, openSeats, description));

                }
            }

        } catch(MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            return courses;
        }

        return courses;
    }
}
