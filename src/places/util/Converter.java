package places.util;

import net.sf.sprockets.google.Place;
import places.model.Farmacie;

/**
 * @author motan
 * @date 7/28/14
 */
public class Converter {

    public static Farmacie placesToPharmacy(Place place, Place detailPlace) {
        assert place != null;
        assert detailPlace != null;


        String UNAVAILABLE = "Unavailable";
        String[] noOpenHours = new String[3];
        noOpenHours[0] = noOpenHours[1] = noOpenHours[2] = UNAVAILABLE;

        Farmacie f = new Farmacie();

        String placesId = place.getPlaceId().getId();
        String mName = place.getName();
        String mVicinity = place.getVicinity();
        double mLat = place.getLatitude();
        double mLng = place.getLongitude();

        f.setPlacesId(placesId);
        f.setName(mName);
        f.setVicinity(mVicinity);
        f.setCompensatDa(0);
        f.setCompensatNu(0);
        f.setLat(mLat);
        f.setLng(mLng);

        if (detailPlace == null)
            System.err.println("detailPlace is NULL !!!");

        if (null != detailPlace.getFormattedPhoneNumber())
            f.setPhNumber(detailPlace.getFormattedPhoneNumber());
        else {
            f.setPhNumber(UNAVAILABLE);
        }

        if (null != detailPlace.getUrl())
            f.setUrl(detailPlace.getUrl());
        else {
            f.setUrl(UNAVAILABLE);
        }

        if (null != detailPlace.getOpeningHours())
            f.setOpenHours(TimeUtils.convertOpeningHoursToString(detailPlace.getOpeningHours()));
        else {
            f.setOpenHours(noOpenHours);
        }

        return f;
    }
}
