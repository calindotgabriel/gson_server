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


    public static void main(String[] args){
        System.setProperty("javax.net.ssl.keyStore","/home/motan/tools/gson_server/mySrvKeystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

//        Server.getInstance();

        Db.startDb();

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
        return PlacesProvider.getPlaces(lat, lng);
    }




}


