package ca.unb.cs.cs2063g8.birdcall.ugrad;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nmagee
 * date: 2018-01-20
 */

public class Location {
    private String name;
    private String id;
    private double lat;
    private double lon;

    public Location(String name, String id, double lat, double lon) {
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

    public void setLat(double lat){
        this.lat = lat;
    }

    public void setLon(double lon){
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

    public double compareLocation(double lat, double lon){
        double theta = lon - this.lon;
        double dist = Math.sin(lat * (Math.PI/180.0)) * Math.sin(this.lat * (Math.PI/180.0))
                * Math.cos(lat * (Math.PI/180.0)) * Math.cos(this.lat * (Math.PI/180.0))
                * Math.cos(theta * (Math.PI/180.0));
        dist = Math.acos(dist);
        dist = dist * (180/Math.PI);
        dist = dist * 60 * 1.1515;
        return dist;
    }

    public static List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Fredericton", "FR", 45.9612631, -66.63934379999999));
        locations.add(new Location("Saint John", "SJ", 45.2878008, -66.0478147));
        locations.add(new Location("Moncton" , "MO", 46.0845414, -64.77711339999999));
        return locations;
    }

    public static Location getLocationByName(String name) {
        if(name.equalsIgnoreCase("Fredericton")){
            return new Location("Fredericton", "FR", 45.9612631, -66.63934379999999);
        }
        else if(name.equalsIgnoreCase("Saint John")){
            return new Location("Saint John", "SJ", 45.2878008, -66.0478147);
        }
        else if(name.equalsIgnoreCase("Moncton")){
            return new Location("Moncton" , "MO", 46.0845414, -64.77711339999999);
        }
        else{
            throw new IllegalArgumentException("No location for name: " + name);
        }
    }

    public static Location getLocationById(String id) {
        if(id.equalsIgnoreCase("FR")){
            return new Location("Fredericton", "FR", 45.9612631, -66.63934379999999);
        }
        else if(id.equalsIgnoreCase("SJ")){
            return new Location("Saint John", "SJ", 45.2878008, -66.0478147);
        }
        else if(id.equalsIgnoreCase("MO")){
            return new Location("Moncton" , "MO", 46.0845414, -64.77711339999999);
        }
        else{
            throw new IllegalArgumentException("No location for id: " + id);
        }
    }

    public static Spinner determineLocation(Context context, Spinner spinner){
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            android.location.Location loc =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            List<Location> locations = Location.getAllLocations();
            double distance = locations.get(0).compareLocation(loc.getLatitude(), loc.getLongitude());
            int index = 0;
            for(Location l : locations) {
                if(distance > l.compareLocation(loc.getLatitude(), loc.getLongitude())){
                    distance = l.compareLocation(loc.getLatitude(), loc.getLongitude());
                    index = locations.indexOf(l);
                }
            }

            spinner.setSelection(index);
        }
        else{
            spinner.setSelection(0);
        }
        return spinner;
    }
}
