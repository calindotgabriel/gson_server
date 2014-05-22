package places;

import com.google.code.gsonrmi.Parameter;
import com.google.code.gsonrmi.annotations.RMI;
import com.google.code.gsonrmi.serializer.ExceptionSerializer;
import com.google.code.gsonrmi.serializer.ParameterSerializer;
import com.google.code.gsonrmi.transport.Route;
import com.google.code.gsonrmi.transport.Transport;
import com.google.code.gsonrmi.transport.rmi.Call;
import com.google.code.gsonrmi.transport.rmi.RmiService;
import com.google.code.gsonrmi.transport.tcp.SecureTcpProxy;
import com.google.code.gsonrmi.transport.tcp.TcpProxy;
import com.google.code.gsonrmi.transport.tcp.TcpProxyFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.sprockets.google.Place;
import net.sf.sprockets.google.Places;
import places.model.Farmacie;
import places.util.TimeUtils;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {


    private static Server instance;

    public static Gson gson;

    public static Transport t;



    public List<Farmacie> getPlaces (double lat, double lng) {

        Places.Response<List<Place>> mResponse = null;
        Places.Response<Place> mPlaceResponse = null;

        String[] noOpenHours = new String[3];
        noOpenHours[0] =noOpenHours[1] = noOpenHours[2] = "Unavailable";

        try {

/*            mResponse = Places.nearbySearch(new Places.Params()
                    .location(51.500702, -0.124576).radius(1000).types("food")
                    .keyword("fish & chips").openNow(), Places.Field.NAME, Places.Field.VICINITY);   */

            mResponse = Places.nearbySearch(new Places.Params()
                    .location(lat, lng).radius(5000).types("pharmacy"),
                     Places.Field.NAME, Places.Field.VICINITY, Places.Field.GEOMETRY,
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
                            Places.Field.FORMATTED_PHONE_NUMBER, Places.Field.OPEN_NOW, Places.Field.OPENING_HOURS, Places.Field.URL);
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
                    pharmacy.setOpenNow(detailPlace.getOpenNow());
                else
                    pharmacy.setOpenNow(false);

                if (detailPlace.getFormattedPhoneNumber() != null)
                    pharmacy.setPhNumber(detailPlace.getFormattedPhoneNumber());
                else
                    pharmacy.setPhNumber("Currently not available");

                if (detailPlace.getUrl() != null)
                     pharmacy.setUrl(detailPlace.getUrl());
                else
                    pharmacy.setUrl("Currently not available");

                if (detailPlace.getOpeningHours() != null)
                    pharmacy.setOpenHours(TimeUtils.convertOpeningHoursToString(detailPlace.getOpeningHours()));
                else {

                    pharmacy.setOpenHours(noOpenHours);
                }

                pharmacies.add(pharmacy);

                System.out.println(pharmacy.getName() + " - " + pharmacy.getVicinity() + " - " +
                                   pharmacy.getLat() + " / " + pharmacy.getLng() + "\n" + "open now:" + pharmacy.getOpenNow());



            }
        } else if (status == Places.Response.Status.ZERO_RESULTS) {
            System.out.println("no results");
        } else {
            System.out.println("error: " + status);
        }

        return pharmacies;

    }



    public static void main(String[] args){
        System.setProperty("javax.net.ssl.keyStore","/home/motan/tools/gson_server/mySrvKeystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        Server.getInstance();

        // test -> getPlaces() is accesed by the Android device.
    }



    public static synchronized Server getInstance(){

        try{

            if(instance == null){

                instance = new Server();
                instance.gson = new GsonBuilder()
                        .registerTypeAdapter(Exception.class, new ExceptionSerializer())
                        .registerTypeAdapter(Parameter.class, new ParameterSerializer()).create();
                instance.t = new Transport();

                InetSocketAddress inetSocketAddress = new InetSocketAddress(30310);
                List<InetSocketAddress> listeningAddresses = Arrays.asList(inetSocketAddress);


                TcpProxy tcpProxy = TcpProxyFactory.reflectTcpProxy(SecureTcpProxy.class, listeningAddresses, instance.t, instance.gson, null);
                tcpProxy.start(); // start the listening on specified port

                RmiService rmiService = new RmiService(instance.t, instance.gson);
                rmiService.start();


                URI uri = new URI("rmi:service");
                Route target = new Route(uri);

                Call call = new Call(target, "register", "server", instance);
                call.send(instance.t);

            }
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return instance;
        }


    @RMI
    public List<Farmacie> test(String text, double lat, double lng){

        // String received from Android
        System.out.println(text + "*" + lat + "/" + lng);

        //1. primeste lat si lng
        // 2. face query bazat pe ele
        // 3. returneaza rezultatul

        // Response to it
        return getPlaces(lat, lng);
    }




}


