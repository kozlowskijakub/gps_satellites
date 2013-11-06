package lab.gps3.logicObjects;

import android.location.Location;

/**
 * Created with IntelliJ IDEA.
 * User: jakub
 * Date: 10/28/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class POI extends Location {
    public long order_id;

    public POI(String provider) {
        super(provider);
    }

    public POI(Location l) {
        super(l);
    }
}

