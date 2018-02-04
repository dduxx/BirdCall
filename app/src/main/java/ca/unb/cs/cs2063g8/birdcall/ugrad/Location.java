package ca.unb.cs.cs2063g8.birdcall.ugrad;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nmagee
 * date: 2018-01-20
 */

public class Location {
    private String name;
    private String id;

    public Location(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Fredericton", "FR"));
        locations.add(new Location("Saint John", "SJ"));
        locations.add(new Location("Moncton" , "MO"));
        return locations;
    }

    public static Location getLocationByName(String name) {
        if(name.equalsIgnoreCase("Fredericton")){
            return new Location("Fredericton", "FR");
        }
        else if(name.equalsIgnoreCase("Saint John")){
            return new Location("Saint John", "SJ");
        }
        else if(name.equalsIgnoreCase("Moncton")){
            return new Location("Moncton", "MO");
        }
        else{
            throw new IllegalArgumentException("No location for name: " + name);
        }
    }

    public static Location getLocationById(String id) {
        if(id.equalsIgnoreCase("FR")){
            return new Location("Fredericton", "FR");
        }
        else if(id.equalsIgnoreCase("SJ")){
            return new Location("Saint John", "SJ");
        }
        else if(id.equalsIgnoreCase("MO")){
            return new Location("Moncton", "MO");
        }
        else{
            throw new IllegalArgumentException("No location for id: " + id);
        }
    }
}
