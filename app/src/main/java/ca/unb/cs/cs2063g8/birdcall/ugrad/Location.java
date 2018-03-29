package ca.unb.cs.cs2063g8.birdcall.ugrad;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author nmagee
 * date: 2018-01-20
 */

public class Location {
    private static final String TAG = "Location";
    private String name;
    private String id;
    private float lat;
    private float lon;

    public Location(String name, String id, float lat, float lon) {
        this.name = name;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat(){
        return this.lat;
    }

    public double getLon(){
        return this.lon;
    }

    public void setLat(float lat){
        this.lat = lat;
    }

    public void setLon(float lon){
        this.lon = lon;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Float.compare(location.lat, lat) == 0 &&
                Float.compare(location.lon, lon) == 0 &&
                Objects.equals(name, location.name) &&
                Objects.equals(id, location.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, id, lat, lon);
    }

    public static List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();

        locations.add(new Location("Saint John", "SJ", 45.2878008F, -66.0478147F));
        locations.add(new Location("Moncton" , "MO", 46.0845414F, -64.77711339999999F));
        locations.add(new Location("Fredericton", "FR", 45.9612631F, -66.63934379999999F));
        return locations;
    }

    public static Location getLocationByName(String name) {
        if(name.equalsIgnoreCase("Fredericton")){
            return new Location("Fredericton", "FR", 45.9612631F, -66.63934379999999F);
        }
        else if(name.equalsIgnoreCase("Saint John")){
            return new Location("Saint John", "SJ", 45.2878008F, -66.0478147F);
        }
        else if(name.equalsIgnoreCase("Moncton")){
            return new Location("Moncton" , "MO", 46.0845414F, -64.77711339999999F);
        }
        else{
            throw new IllegalArgumentException("No location for name: " + name);
        }
    }

    public static Location getLocationById(String id) {
        if(id.equalsIgnoreCase("FR")){
            return new Location("Fredericton", "FR", 45.9612631F, -66.63934379999999F);
        }
        else if(id.equalsIgnoreCase("SJ")){
            return new Location("Saint John", "SJ", 45.2878008F, -66.0478147F);
        }
        else if(id.equalsIgnoreCase("MO")){
            return new Location("Moncton" , "MO", 46.0845414F, -64.77711339999999F);
        }
        else{
            throw new IllegalArgumentException("No location for id: " + id);
        }
    }

    /**
     * finds the distance between the current location and the given location
     * @param context application context
     * @param location the unb location
     * @return the determined distance
     */
    public static float findDistance(Context context, Location location){
        ArrayList<String> permissions = new ArrayList<>();

        /*LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            android.location.Location loc =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i(TAG, "current - (lat, lon): " + loc.getLatitude() + ", " + loc.getLongitude());
            Log.i(TAG, "given -(lat, lon): " + location.getLat() + ", " + location.getLon());

            android.location.Location campus = new android.location.Location("");
            campus.setLatitude(location.getLat());
            campus.setLongitude(location.getLon());
            return loc.distanceTo(campus);
        } catch (SecurityException e) {
            Log.i(TAG, "could not get location");
            return 0;
        }*/
    }
}
