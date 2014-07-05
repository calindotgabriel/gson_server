package places;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import places.model.Farmacie;
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
        noOpenHours[0] =noOpenHours[1] = noOpenHours[2] = UNAVAILABLE;

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
//                System.out.println(place.getName() + " - " + place.getVicinity() + " - " + place.getLatitude() + " / " + place.getLongitude());

/*                try {
                mPlaceResponse = Places.details(new Places.Params()
                    .reference(""), Places.Field.OPENING_HOURS);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }*/

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


                Farmacie pharmacy = new Farmacie();
                pharmacy.setName(place.getName());
                pharmacy.setVicinity(place.getVicinity());
                pharmacy.setCompensat(1); //TODO
                pharmacy.setLat(place.getLatitude());
                pharmacy.setLng(place.getLongitude());

                if (detailPlace.getOpenNow() != null)
                    pharmacy.setOpenNow(detailPlace.getOpenNow()); //TODO
                else
                    pharmacy.setOpenNow(false);

                if (detailPlace.getFormattedPhoneNumber() != null)
                    pharmacy.setPhNumber(detailPlace.getFormattedPhoneNumber());
                else
                    pharmacy.setPhNumber(UNAVAILABLE);

                if (detailPlace.getUrl() != null)
                    pharmacy.setUrl(detailPlace.getUrl());
                else
                    pharmacy.setUrl(UNAVAILABLE);

                if (detailPlace.getOpeningHours() != null)
                    pharmacy.setOpenHours
                            (TimeUtils.convertOpeningHoursToString(detailPlace.getOpeningHours()));
                else {

                    pharmacy.setOpenHours(noOpenHours);
                }

                pharmacies.add(pharmacy);

                System.out.println
                        ("\n________________________" +
                         pharmacy.getName() +  "\n" +
                         pharmacy.getVicinity()+ "\n" +
                         pharmacy.getLat() + "/" +
                         pharmacy.getLng() + "\n" +
                         "open now:" + pharmacy.getOpenNow() +
                         "\n________________________");



            }
        } else if (status == Places.Response.Status.ZERO_RESULTS) {
            System.out.println("no results");
        } else {
            System.out.println("error: " + status);
        }

        return pharmacies;

    }

}
