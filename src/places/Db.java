package places;

import org.hibernate.Session;
import org.hibernate.Transaction;
import places.model.Farmacie;
import places.util.HibernateUtil;
import places.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.List;

/**
 * @author motan
 * @date 7/7/14
 */
public class Db {


    public static void savePharmacy(Farmacie f) {

        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();

        s.save(f);
        s.getTransaction().commit();

        s.close();
    }

    public static void savePharmacylist(List<Farmacie> fs) {

        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();

        for (Farmacie f : fs) {
            s.save(f);
        }

        s.getTransaction().commit();

        s.close();
    }

    public static void showRecords() {

        Session s = HibernateUtil.getSessionFactory().openSession();
        List farmacii = s.createQuery("FROM Farmacie").list();


        Transaction t = s.beginTransaction();

        for (Iterator i = farmacii.iterator(); i.hasNext();) {
            Farmacie f = (Farmacie) i.next();

            System.out.println("--------------- DB LOG ---------------------");
            Log.debug(f);
        }

        t.commit();

        s.close();
    }


}
