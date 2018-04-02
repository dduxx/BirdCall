package ca.unb.cs.cs2063g8.birdcall.ugrad;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * campus location. campus coordinates determined via google and are pre defined for each campus
 *     users current coordinates are passed in for determining distance
 * @author nmagee
 * date: 2018-01-20
 */

public class Location{
    private static final String TAG = "Location";
    public static final float FR_LAT = 45.9612631F;
    public static final float FR_LON = -66.63934379999999F;
    public static final float SJ_LAT = 45.2878008F;
    public static final float SJ_LON = -66.0478147F;
    public static final float MO_LAT = 46.0845414F;
    public static final float MO_LON = -64.77711339999999F;

    private String name;
    private String id;
    private float lat;
    private float lon;
    private double currentLat;
    private double currentLon;

    public Location(String name, String id, float lat, float lon, double currentLat, double currentLon) {
        this.name = name;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.currentLat = currentLat;
        this.currentLon = currentLon;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public double getCurrentLon() {
        return currentLon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setCurrentLat(float currentLat) {
        this.currentLat = currentLat;
    }

    public void setCurrentLon(float currentLon) {
        this.currentLon = currentLon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Float.compare(location.lat, lat) == 0 &&
                Float.compare(location.lon, lon) == 0 &&
                Double.compare(location.currentLat, currentLat) == 0 &&
                Double.compare(location.currentLon, currentLon) == 0 &&
                Objects.equals(name, location.name) &&
                Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, lat, lon, currentLat, currentLon);
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", currentLat=" + currentLat +
                ", currentLon=" + currentLon +
                '}';
    }

    /**
     * calculates the distance between the user current location the campus defined in this class
     * @return float distance
     */
    public float findDistance(){
        Log.i(TAG, "finding distance between: (" + currentLat + ", " + currentLon + ") " +
                "and " + id + ": (" + lat + ", " + lon + ")");
        android.location.Location campus = new android.location.Location("");
        campus.setLongitude(this.lon);
        campus.setLatitude(this.lat);

        android.location.Location current = new android.location.Location("");
        current.setLatitude(currentLat);
        current.setLongitude(currentLon);
        float distance = current.distanceTo(campus);
        Log.i(TAG, "distance calculated: " + distance);
        return distance;
    }

    public static List<Location> getAllLocations(double currentLat, double currentLon) {
        List<Location> locations = new ArrayList<>();

        locations.add(new Location("Saint John", "SJ", SJ_LAT, SJ_LON, currentLat, currentLon));
        locations.add(new Location("Moncton" , "MO", MO_LAT, MO_LON, currentLat, currentLon));
        locations.add(new Location("Fredericton", "FR", FR_LAT, FR_LON, currentLat, currentLon));

        return locations;
    }

    public static Location getLocationByName(String name, double currentLat, double currentLon) {
        if(name.equalsIgnoreCase("Fredericton")){
            return new Location("Fredericton", "FR", FR_LAT, FR_LON, currentLat, currentLon);
        }
        else if(name.equalsIgnoreCase("Saint John")){
            return new Location("Saint John", "SJ", SJ_LAT, SJ_LON, currentLat, currentLon);
        }
        else if(name.equalsIgnoreCase("Moncton")){
            return new Location("Moncton" , "MO", MO_LAT, MO_LON, currentLat, currentLon);
        }
        else{
            throw new IllegalArgumentException("No location for name: " + name);
        }
    }

    public static Location getLocationById(String id, double currentLat, double currentLon) {
        if(id.equalsIgnoreCase("FR")){
            return new Location("Fredericton", "FR", FR_LAT, FR_LON, currentLat, currentLon);
        }
        else if(id.equalsIgnoreCase("SJ")){
            return new Location("Saint John", "SJ", SJ_LAT, SJ_LON, currentLat, currentLon);
        }
        else if(id.equalsIgnoreCase("MO")){
            return new Location("Moncton" , "MO", MO_LAT, MO_LON, currentLat, currentLon);
        }
        else{
            throw new IllegalArgumentException("No location for name: " + id);
        }
    }
}
