package com.example.mapwithmarker;
import android.location.Location;

public class TravelManager {
    public static float DEFAULT_SPEED = 55.0f; //mph
    public static int METERS_PER_MILE = 1600;

    private static Location destination;
    private static Location current;

    public static Location getCurrent(){
        return current;
    }

    public static void setCurrent(Location location){
        current = location;
    }

    public void setDestination(Location newDestination){
        destination = newDestination;
    }

    public static float distanceToDestination(Location current){
        if(current != null && destination !=null)
            return current.distanceTo(destination);
        else
            return -1.0f;
    }

    public static String[] milesToDestination(Location dest){
        destination = dest;
        float metersToGo = distanceToDestination(dest);
        float timeToGo = metersToGo / (DEFAULT_SPEED * METERS_PER_MILE);
        String[] result = new String[2];
        int hours = (int) timeToGo;
        if(hours == 1)
            result[0] += "1 hour";
        else if( hours > 1)
            result[0] += hours + " hours";
        int minutes = (int)((timeToGo - hours) * 60);
        if(minutes == 1)
            result[0] += "1 minute ";
        else if(minutes > 1)
            result[0] += minutes + " minutes";
        if(hours <= 0 && minutes <= 0)
            result[0] = "less than one minute left";
        result[1] = Float.toString(distanceToDestination(dest));
        return result;

    }
}