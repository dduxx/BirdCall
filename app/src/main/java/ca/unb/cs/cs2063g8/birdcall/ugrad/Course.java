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
import java.util.Random;

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
    public static final String COURSE_ID ="COURSE_ID";
    public static final String COURSE_NAME="COURSE_NAME";
    public static final String SEATS_OPEN="SEATS_OPEN";
    public static final String DESCRIPTION="DESCRIPTION";
    public static final String DAYS_OFFERED="DAYS_OFFERED";
    public static final String TIME_SLOT="TIME_SLOT";
    public static final String PROFESSOR="PROFESSOR";

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

    /**
     * gets a course list based on certain form parameters
     *     examples of possible params
     *         action=index
     *         noncredit=0
     *         term=2018/WI
     *         level=UG
     *         subject=ANTH
     *         location=FR
     *         format=CLASS
     * @param formParams
     * @return the full list of courses
     */
    public static List<Course> getCourseList(String... formParams){
        List<Course> courses = new ArrayList<>();
        try{
            String response = UNBAccess.getResponse(
                    new URL(COURSE_SOURCE_URL), UNBAccess.Expected.HTML, formParams);


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

    public static List<Course> randomizer(List<Course> fullList){
        final int MAX_LIST_SIZE = 5;
        List<Course> suggestions = new ArrayList<>();
        //Remember to go back for other faculties
        if(fullList.size() <= 5){
            return fullList;
        }
        else{
            for(int i = 0; i < MAX_LIST_SIZE; i++){
                suggestions.add(fullList.get(new Random().nextInt(fullList.size())));
            }
            return suggestions;
        }

    }
    //TODO add a checking method that takes in the suggestion and compares against the old array
}
