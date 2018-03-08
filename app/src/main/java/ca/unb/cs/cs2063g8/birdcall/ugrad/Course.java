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
    public static final String COURSE_LEVEL = "COURSE_LEVEL";
    public static final String DAYS_OFFERED="DAYS_OFFERED";
    public static final String TIME_SLOT="TIME_SLOT";
    public static final String PROFESSOR="PROFESSOR";

    private String id;
    private String name;
    private Integer openSeats;
    private Description description;
    private String daysOffered;
    private String professor;
    private String timeSlot;

    public Course(){

    }

    public Course(
            String id,
            String name,
            Integer openSeats,
            Description description,
            String daysOffered,
            String timeSlot,
            String professor){
        this.id = id;
        this.name = name;
        this.openSeats = openSeats;
        this.description = description;
        this.daysOffered = daysOffered;
        this.timeSlot = timeSlot;
        this.professor = professor;
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

    public void setProfessor(String professor){
        this.professor = professor;
    }

    public String getDaysOffered(){
        return this.daysOffered;
    }

    public void setDaysOffered(String daysOffered){
        this.daysOffered = daysOffered;
    }

    public String getProfessor(){
        return this.professor;
    }

    public String getTimeSlot(){
        return this.timeSlot;
    }

    public void setTimeSlot(String timeSlot){
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
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
    public static List<Course> getCourseList(Map<String, String> filters, String... formParams){
        List<Course> courses = new ArrayList<>();
        Log.i(TAG, "given filters:");

        for(String s: filters.keySet()){
            Log.i(TAG, "filter (" + s + ", " + filters.get(s) + ")");
        }

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
                    if(filters.containsKey(Course.COURSE_LEVEL)){
                        if(!filters.get(Course.COURSE_LEVEL).equals("Any Level")
                                && !filters.get(Course.COURSE_LEVEL).substring(0,1).equals(
                                id.replaceAll("[a-zA-Z]", "").charAt(0)+"")){
                            Log.i(TAG, "course id: " + id + " did not match filter level: "
                                    + filters.get(Course.COURSE_LEVEL));
                            continue;
                        }
                    }
                    String name = cells.get(3).text();

                    String professor = cells.get(4).text();

                    String days = cells.get(5).text();
                    if(filters.containsKey(Course.DAYS_OFFERED)){
                        if(!days.equals(filters.get(Course.DAYS_OFFERED))){
                            Log.i(TAG, "Course " + id + " does not match days filter");
                            continue;
                        }
                    }

                    Integer total = Integer.parseInt(cells.get(8).text().replaceAll("\\s", "").split("/")[0]);

                    String time = cells.get(6).text().replaceAll("\\s", "");
                    if(filters.containsKey(Course.TIME_SLOT)){
                        if(!time.equals(filters.get(Course.TIME_SLOT))){
                            Log.i(TAG, "Course " + id + " does not match time filter");
                            continue;
                        }
                    }

                    Integer enrollment;

                    if(cells.get(8).text().contains("(W)")){
                        enrollment = total;
                    }
                    else{
                        enrollment = Integer.parseInt(cells.get(8).text().replaceAll("\\s", "").split("/")[1]);
                    }
                    Integer openSeats = total - enrollment;

                    if(openSeats <= 0){
                        Log.i(TAG, "Course: " + id + " is full");
                        continue;
                    }

                    String url;
                    try{
                        url = cells.get(1).getElementsByAttribute("href").first().attr("href");
                        Description description = new Description(new URL(url));
                        courses.add(new Course(id, name, openSeats, description, days, time, professor));
                    } catch (NullPointerException e) {
                        Log.i(TAG, "no url for course: " + id);
                        courses.add(new Course(id, name, openSeats, null, days, time,professor));
                    }
                }
            }

        } catch(MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            return courses;
        } catch(Exception e){
            Log.e(TAG, "unable to get courses", e);
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
