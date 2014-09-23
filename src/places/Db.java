package places;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import places.model.Farmacie;
import places.util.HibernateUtil;
import places.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author motan
 * @date 7/7/14
 */
public class Db {



    public static void savePharmacylist(List<Farmacie> fs) {

        Session s = HibernateUtil.getSessionFactory().openSession();
        s.beginTransaction();

        for (Farmacie f : fs) {

            if (getPharmacyByGoogleId(f.getPlacesId()) == null) // daca nu am deja in baza de date
                s.save(f);
        }

        s.getTransaction().commit();

        s.close();
    }

    public static List<Farmacie> processQueriedPharmacies(List<Farmacie> farmaciiNoi) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        s.beginTransaction();

        List farmaciiInDb = s.createQuery("FROM Farmacie").list();


        boolean isInDb;
        for (Farmacie f : farmaciiNoi) {
            isInDb = false;
            for (Iterator i = farmaciiInDb.iterator(); i.hasNext() && !isInDb; ) {
                Farmacie dbFa = (Farmacie) i.next();

                if (f.getPlacesId().equals(dbFa.getPlacesId())) {
                    isInDb = true;
                    f.setId(dbFa.getId());
                    f.setCompensatDa(dbFa.getCompensatDa());
                    f.setCompensatNu(dbFa.getCompensatNu());
                    System.out.println(" Saw that " + f.getName() + " is in db.");
                }
            }
            if (!isInDb) {
                s.save(f);
                System.out.println(" Added new pharmacy " + f.getName() + " in db.");
            }
        }

        s.getTransaction().commit();
        s.close();

        return farmaciiNoi;
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

    public static Farmacie getPharmacyById(int id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();

        Farmacie f = (Farmacie) s.get(Farmacie.class, id);

        s.getTransaction().commit();

        t.rollback();
        s.close();

        return f;
    }

    public static Farmacie getPharmacyByGoogleId(String googlePlacesId) {

        Session s = HibernateUtil.getSessionFactory().openSession();

        s.beginTransaction();

        List<Farmacie> fs = (List<Farmacie>) s.createCriteria(Farmacie.class)
                .add(Restrictions.eq("placesId", googlePlacesId))
                .list();

        s.getTransaction().commit();
        s.close();

        if (fs.size() > 0)
            return fs.get(0);
        return null;
    }

    public static void updateCompensatField(Farmacie f, boolean state, int value) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        if (state)
            f.setCompensatDa(value);
        else
            f.setCompensatNu(value);

        s.update(f);

        s.flush();
        s.close();
    }




}
