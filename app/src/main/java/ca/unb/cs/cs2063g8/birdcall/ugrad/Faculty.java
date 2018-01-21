package ca.unb.cs.cs2063g8.birdcall.ugrad;

import java.util.List;

/**
 * @author nmagee
 * date: 2018-01-20
 */

public class Faculty {
    private String name;
    private String prefix;
    private List<Course> courses;
    private Location location;

    public Faculty(String name, String prefix, List<Course> courses, Location location) {
        this.name = name;
        this.prefix = prefix;
        this.courses = courses;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Location getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
