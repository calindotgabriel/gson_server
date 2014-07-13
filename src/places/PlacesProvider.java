package places;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import places.model.Farmacie;
import places.util.Log;
import places.util.TimeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author motan
 * @date 5/27/14
 */
public class PlacesProvider {

    private static String UNAVAILABLE = "Unavailable";

    public static List<Farmacie> getPlaces (double lat, double lng) {


        Places.Response<List<Place>> mResponse = null;
        Places.Response<Place> mPlaceResponse = null;

        String[] noOpenHours = new String[3];
        noOpenHours[0] = noOpenHours[1] = noOpenHours[2] = UNAVAILABLE;

        try {

/*            mResponse = Places.nearbySearch(new Places.Params()
                    .location(51.500702, -0.124576).radius(1000).types("food")
                    .keyword("fish & chips").openNow(), Places.Field.NAME, Places.Field.VICINITY);   */

            mResponse = Places.nearbySearch(new Places.Params()
                            .location(lat, lng).radius(5000).types("pharmacy"),
                    Places.Field.NAME,
                    Places.Field.VICINITY,
                    Places.Field.GEOMETRY,
                    Places.Field.OPEN_NOW
            );


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
                    mPlaceResponse = Places.details(new Places.Params().reference(place.getReference()),
                            Places.Field.FORMATTED_PHONE_NUMBER,
                            Places.Field.OPEN_NOW,
                            Places.Field.OPENING_HOURS,
                            Places.Field.URL);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Place detailPlace = mPlaceResponse.getResult();

                String mName = place.getName();
                Log.fName = mName; // use this just for logging
                String mVicinity = place.getVicinity();
                double mLat = place.getLatitude();
                double mLng = place.getLongitude();


                Farmacie f = new Farmacie();
                f.setName(mName);
                f.setVicinity(mVicinity);
                f.setCompensat(2); //TODO
                f.setLat(mLat);
                f.setLng(mLng);


                if (null != detailPlace.getOpenNow())
                    if (detailPlace.getOpenNow())
                        f.setOpenNow(1);
                    else f.setOpenNow(0);
                else {
                    f.setOpenNow(2);
                    Log.nullField("@ open now");
                }


                if (null != detailPlace.getFormattedPhoneNumber())
                    f.setPhNumber(detailPlace.getFormattedPhoneNumber());
                else {
                    f.setPhNumber(UNAVAILABLE);
                    Log.nullField("@ ph number");
                }

                if (null != detailPlace.getUrl())
                    f.setUrl(detailPlace.getUrl());
                else {
                    f.setUrl(UNAVAILABLE);
                    Log.nullField("@ url ");
                }

                if (null != detailPlace.getOpeningHours())
                    f.setOpenHours(TimeUtils.convertOpeningHoursToString(detailPlace.getOpeningHours()));
                else {
                    f.setOpenHours(noOpenHours);
                    Log.nullField("@ open hours ");
                }

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
