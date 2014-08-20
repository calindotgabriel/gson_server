package places;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import places.model.Farmacie;
import places.util.Converter;
import places.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author motan
 * @date 5/27/14
 */
public class PlacesProvider {


    public static List<Farmacie> getPlaces (double lat, double lng) {

        Places.Response<List<Place>> mResponse = null;
        Places.Response<Place> mPlaceDetails = null;

        try {
            mResponse = Places.nearbySearch(new Places.Params()
                    .location(lat, lng).radius(5000).types("pharmacy"),
                    Places.Field.NAME,
                    Places.Field.VICINITY,
                    Places.Field.GEOMETRY,
                    Places.Field.OPEN_NOW);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Places.Response.Status status = null;
        List<Place> places = null;

        if (mResponse != null) {
            status = mResponse.getStatus();
            places = mResponse.getResult();
        }

        List<Farmacie> pharmacies = new ArrayList<Farmacie>();

        if (status == Places.Response.Status.OK && places != null) {
            for (Place place : places) {
                try {
                    mPlaceDetails = Places.details(new Places.Params()
                            .placeId(place.getPlaceId().getId()),
                            Places.Field.FORMATTED_PHONE_NUMBER,
                            Places.Field.OPEN_NOW,
                            Places.Field.OPENING_HOURS,
                            Places.Field.URL
                            );


                } catch (IOException e) {
                    e.printStackTrace();
                }

        Place detailPlace = mPlaceDetails != null ? mPlaceDetails.getResult() : null;

        Farmacie f = Converter.placesToPharmacy(place, detailPlace);
        pharmacies.add(f);

        Log.debug(f);

        }
        } else if (status == Places.Response.Status.ZERO_RESULTS) {
            System.out.println("no results");
        } else {
            System.out.println("error: " + status);
        }

        return pharmacies;

    }



}
