package ca.unb.cs.cs2063g8.birdcall.database;

import java.util.Objects;

public class BlacklistItem {
    public static final String TYPE_PROF = "professor";
    public static final String TYPE_FAC = "faculty";
    public static final String TYPE_COURSE = "course";

    private String type;
    private String name;

    public BlacklistItem(String type, String name){
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistItem that = (BlacklistItem) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, name);
    }

    @Override
    public String toString() {
        return "BlacklistItem{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
