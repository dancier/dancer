package net.dancier.dancer.location;

import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    public Double getDistanceOfZipCodes(ZipCode zipCodeA, ZipCode zipCodeB) {
        Double lon1 = zipCodeA.getLongitude();
        Double lon2 = zipCodeB.getLongitude();

        Double lat1 = zipCodeA.getLatitude();
        Double lat2 = zipCodeB.getLatitude();

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }
}
