package ca.unb.cs.cs2063g8.birdcall.ugrad;

import java.util.List;

/**
 * @author nmagee
 * date: 2018-01-20
 * unb course object
 */

public class Course {
    private String id;
    private Faculty faculty;
    private String section;
    private String name;
    private String professor;

    //these 2 may need to be adjusted
    private List<String> days;
    private String timeslots;

    private String room;
    private Integer seatsAvailable;
    private Integer totalSeats;
    private String description;

    public Course(String id, Faculty faculty, String section, String name, String professor,
                  List<String> days, String timeslots, String room, Integer seatsAvailable,
                  Integer totalSeats, String description) {
        this.id = id;
        this.faculty = faculty;
        this.section = section;
        this.name = name;
        this.professor = professor;
        this.days = days;
        this.timeslots = timeslots;
        this.room = room;
        this.seatsAvailable = seatsAvailable;
        this.totalSeats = totalSeats;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public String getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(String timeslots) {
        this.timeslots = timeslots;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", faculty=" + faculty +
                ", section='" + section + '\'' +
                ", name='" + name + '\'' +
                ", professor='" + professor + '\'' +
                ", days=" + days +
                ", timeslots='" + timeslots + '\'' +
                ", room='" + room + '\'' +
                ", seatsAvailable=" + seatsAvailable +
                ", totalSeats=" + totalSeats +
                ", description='" + description + '\'' +
                '}';
    }


}
