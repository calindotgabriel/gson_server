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

import places.model.Farmacie;
import places.util.Log;


import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class Server {

    private static Server instance;
    public static Gson gson;
    public static Transport t;


    public static void main(String[] args){
        System.setProperty("javax.net.ssl.keyStore","/home/motan/tools/gson_server/mySrvKeystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        Server.getInstance();
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

    /*
     * Method called from Android
     */
    @RMI
    public List<Farmacie> test(String text, double lat, double lng){

        System.out.println(text + "*" + lat + "/" + lng);

        List<Farmacie> fs = PlacesProvider.getPlaces(lat, lng);

//        Db.savePharmacylist(fs);
//        Db.showRecords();

        return Db.processQueriedPharmacies(fs);
    }


    @RMI
    public Farmacie setCompensatField(String googleId, boolean state) {

        System.out.println(" GOOGLE ID = " + googleId);

        Farmacie f = Db.getPharmacyByGoogleId(googleId);

        if (state) {
            Db.updateCompensatField(f, true, f.getCompensatDa() + 1);
            System.out.println("COMPENSAT DA \n" + f.getCompensatDa() + "\n COMPENSAT DA");
        }
        else {
            Db.updateCompensatField(f, false, f.getCompensatNu() + 1);
            System.out.println("COMPENSAT NU \n" + f.getCompensatDa() + "\n COMPENSAT NU");
        }

        Log.debug("accesed by compensat", f);

        return f;
    }






}


