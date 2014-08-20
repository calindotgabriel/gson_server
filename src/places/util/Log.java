package places.util;

import places.PlacesProvider;
import places.model.Farmacie;

import java.util.Date;


/**
 * @author motan
 * @date 7/9/14
 */
public class Log {

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
                        f.getCompensatDa() + " compensat da \n" +
                        f.getCompensatNu() + " compensat nu \n" +
                        f.getId() + "\n" +
                        f.getPlacesId() + "\n" +
                        f.getName() + "\n" +
                        f.getVicinity() + "\n" +
                        f.getLat() + "/" +
                        f.getLng() + "\n" +
                        f.getPhNumber() + "\n" +
                        f.getOpenHoursString() + "\n" +
                        f.getUrl() + "\n" +
                        "\n**********************");
    }


    public static void debug(String specialText, Farmacie f) {

        info("\n***** " + specialText + " *******\n");
        debug(f);

    }

    public static void nullField(String field) {
                err("Null field by at " + field);
    }
}
