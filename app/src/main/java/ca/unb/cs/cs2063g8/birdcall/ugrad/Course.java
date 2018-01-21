package ca.unb.cs.cs2063g8.birdcall.ugrad;

import java.util.List;

/**
 * @author nmagee
 * date: 2018-01-20
 * unb course object
 */

public class Course {
    private String id;
    private String name;
    private String description;
    private String creditHours;
    private Boolean isWritingCredit;
    private List<String> prereqIds;
    private Faculty faculty;

    public Course(String id, String name, String description, String creditHours,
                  Boolean isWritingCredit, List<String> prereqIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creditHours = creditHours;
        this.isWritingCredit = isWritingCredit;
        this.prereqIds = prereqIds;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreditHours() {
        return creditHours;
    }

    public Boolean getWritingCredit() {
        return isWritingCredit;
    }

    public List<String> getPrereqIds() {
        return prereqIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }

    public void setWritingCredit(Boolean writingCredit) {
        isWritingCredit = writingCredit;
    }

    public void setPrereqIds(List<String> prereqIds) {
        this.prereqIds = prereqIds;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Boolean hasPreregs(){
        if(prereqIds.size() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != null ? !id.equals(course.id) : course.id != null) return false;
        if (name != null ? !name.equals(course.name) : course.name != null) return false;
        if (description != null ? !description.equals(course.description) :
                course.description != null)
            return false;
        if (creditHours != null ? !creditHours.equals(course.creditHours) :
                course.creditHours != null)
            return false;
        if (isWritingCredit != null ? !isWritingCredit.equals(course.isWritingCredit) :
                course.isWritingCredit != null)
            return false;
        if (prereqIds != null ? !prereqIds.equals(course.prereqIds) : course.prereqIds != null)
            return false;
        return faculty != null ? faculty.equals(course.faculty) : course.faculty == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (creditHours != null ? creditHours.hashCode() : 0);
        result = 31 * result + (isWritingCredit != null ? isWritingCredit.hashCode() : 0);
        result = 31 * result + (prereqIds != null ? prereqIds.hashCode() : 0);
        result = 31 * result + (faculty != null ? faculty.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Course: {" +
                "id: '" + id + '\'' +
                ", name: '" + name + '\'' +
                ", description: '" + description + '\'' +
                ", creditHours: '" + creditHours + '\'' +
                ", isWritingCredit: " + isWritingCredit +
                ", prereqIds: " + prereqIds +
                ", faculty: " + faculty +
                '}';
    }
}
