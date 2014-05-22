package places.util;

import net.sf.sprockets.google.Place;

import java.util.List;


public class TimeUtils {



    public static String[] convertOpeningHoursToString(List<Place.OpeningHours> list) {
        String[] openHours = new String[3];

        Place.OpeningHours workingDay = list.get(0); // looking at monday
        String workingOpenHour = workingDay.getOpenHour() + ":"
                + doubleZeroAtMinute(workingDay.getOpenMinute());
        String workingCloseHour = workingDay.getCloseHour() + ":"
                + doubleZeroAtMinute(workingDay.getCloseMinute());

        openHours[0] = workingOpenHour + " - " + workingCloseHour;

        if (list.size()  >= 6) {
            // avem program pentru weekend
            // exista si 6
            Place.OpeningHours saturday = list.get(4);
            String saturdayOpenHour = saturday.getOpenHour() + ":" + doubleZeroAtMinute(saturday.getOpenMinute());
            String saturdayCloseHour = saturday.getCloseHour() + ":" + doubleZeroAtMinute(saturday.getCloseMinute());

            openHours[1] = saturdayOpenHour + " - " + saturdayCloseHour;

            Place.OpeningHours sunday = list.get(5);
            String sundayOpenHour = sunday.getOpenHour() + ":" + doubleZeroAtMinute(sunday.getOpenMinute());
            String sundayCloseHour = sunday.getCloseHour() + ":" + doubleZeroAtMinute(sunday.getCloseMinute());

            openHours[2] = sundayOpenHour + " - " + sundayCloseHour;

        } else {
            openHours[1]=openHours[2]="closed";
        }

        return openHours;
    }

    public static String doubleZeroAtMinute(int minute) {
        if (minute == 0)
            return "00";
        else
            return minute + "";
    }
}
