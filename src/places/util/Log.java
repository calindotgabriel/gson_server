package places.util;

import places.PlacesProvider;
import places.model.Farmacie;

import java.util.Date;


/**
 * @author motan
 * @date 7/9/14
 */
public class Log {

    public static String fName;
    public static String clsName = PlacesProvider.class.getName();

    private static void err(String errMsg) {
        System.err.println(new Date() + clsName + errMsg);
    }

    private static void info(String infoMsg) {
        System.out.println(new Date() + clsName + infoMsg);
    }


    public static void debug(Farmacie f) {

        info
                ("\n**********************\n" +
                        f.getId() + "\n" +
                        f.getName() + "\n" +
                        f.getVicinity() + "\n" +
                        f.getLat() + "/" +
                        f.getLng() + "\n" +
                        f.getPhNumber() + "\n" +
                        f.getOpenHoursString() + "\n" +
                        f.getUrl() + "\n" +
                        "open now:" + f.getOpenNow() +
                        "\n**********************");


    }

    public static void nullField(String field) {
                err("Null field by " + fName + " at " + field);
    }
}
