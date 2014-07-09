package places.util;

import places.model.Farmacie;

/**
 * @author motan
 * @date 7/9/14
 */
public class Log {

    public static void debug(Farmacie f) {

        System.out.println
                ("\n**********************\n" +
                        f.getId() + "\n" +
                        f.getName() +  "\n" +
                        f.getVicinity()+ "\n" +
                        f.getLat() + "/" +
                        f.getLng() + "\n" +
                        f.getPhNumber() + "\n" +
                        f.getOpenHoursString() +"\n" +
                        f.getUrl() + "\n" +
                        "open now:" + f.getOpenNow() +
                "\n**********************");
    }
}
