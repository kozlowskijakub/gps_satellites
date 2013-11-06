package lab.gps3.calculations;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 11/3/13
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeoCalculations {

    public static double countDistance(double lat1, double lng1, double lat2, double lng2) {
        //double earthRadius = 3958.75; in miles
        double earthRadius = 6371.009;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }
}

