package ca.unb.cs.cs2063g8.birdcall.ugrad;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author nmagee
 * date: 2018-03-04
 * representation of a unb semester. traditionally semesters run for 4 month periods
 */

public class Semester {
    private String name;
    private String tag;

    public Semester(String name, String tag){
        this.name = name;
        this.tag = tag;
    }

    public Semester(String name){
        this.name = name;
        String tag = name.split("\\s")[1];
        if(tag.contains("Summer")){
            tag = tag + "/SM";
        }
        else if(tag.contains("Winter")){
            tag = tag + "/WI";
        }
        else{
            tag = tag + "/FA";
        }
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Semester{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Semester semester = (Semester) o;

        if (!name.equals(semester.name)) return false;
        return tag.equals(semester.tag);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + tag.hashCode();
        return result;
    }

    /**
     * @return the semester that the user is currently in
     */
    public static Semester getCurrentSemester() {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year  = Calendar.getInstance().get(Calendar.YEAR);

        if(0 <= month && month <= 3){
            return new Semester("Winter " + year, year + "/WI");
        }
        else if(4 <= month && month <= 7){
            return new Semester("Summer " + year, year + "/SM");
        }
        else{
            return new Semester("Fall " + year, year + "/FA");
        }
    }

    /**
     * returns the next semester from a given semester
     * @param semester
     * @return the semester that falls after the given semester
     */
    public static Semester getNextSemester(Semester semester){
        int year  = Calendar.getInstance().get(Calendar.YEAR);

        if(semester.equals(new Semester("Winter " + year, year + "/WI"))){
            return new Semester("Summer " + year, year + "/SM");
        }
        else if(semester.equals(new Semester("Summer " + year, year + "/SM"))){
            return new Semester("Fall " + year, year + "/FA");
        }
        else{
            return new Semester("Winter " + (year+1), (year+1) + "/WI");
        }
    }

    /**
     * returns the current semester and the next semester as a list
     * @return a list of Semester objects
     */
    public static List<Semester> getSemesters(){
        List<Semester> semesters = new ArrayList<>();
        Log.i("SEM 1", getCurrentSemester().tag);
        semesters.add(getCurrentSemester());
        Log.i("SEM 2", getNextSemester(getCurrentSemester()).tag);
        semesters.add(getNextSemester(getCurrentSemester()));
        return semesters;
    }

}
