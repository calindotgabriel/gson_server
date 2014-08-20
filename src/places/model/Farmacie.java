package places.model;

import org.hibernate.annotations.*;
import places.util.DbStringConvert;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Pharmacy class model, used to store info about one entity.
 */

@Entity
@DynamicUpdate
@Table (name = "FARMACII", indexes = {
        @Index(columnList = "PLACES_ID", name = "GOOGLE_ID") })


public class Farmacie implements Serializable, ItemInterface {


    @Id
    @GeneratedValue (strategy = IDENTITY)
    private int id;

    @Column (name = "PLACES_ID", length = 30, unique = true, nullable = false)
    private String placesId;

    @Column (name = "NAME", length = 50, nullable = false)
    private String name;

    @Column (name="OPEN_HOURS", length = 40)
    private String[] openHours;

    @Column (name="VICINITY", length = 100)
    private String vicinity;

    @Column (name="PH_NUMBER", length = 20)
    private String phNumber;

    @Column (name="URL", length = 150)
    private String url;

    @Column (name="LAT", precision = 12, scale = 10)
    private double lat;

    @Column (name="LNG", precision = 12, scale = 10)
    private double lng;

    @Column (name="COMPENSAT_DA")
    private int compensat_da;

    @Column (name="COMPENSAT_NU")
    private int compensat_nu;






    public Farmacie() {
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPlacesId() {
        return placesId;
    }
    public void setPlacesId(String placesId) {
        this.placesId = placesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     0 - closed    (provided
     1 - open        by google)
     2 - do not know (null from google)
     */


    public String getVicinity() {
        return vicinity;
    }
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }


    public String getOpenHoursString() {
        return DbStringConvert.convertArrayToString(openHours);
    }
    public String[] getOpenHours() {
        return openHours;
    }
    public void setOpenHours(String[] openHours) {
        this.openHours = openHours;
    }
    public void setOpenHoursStringAsArray(String openHours) {
        this.openHours = DbStringConvert.convertStringToArray(openHours);
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getCompensatDa() {
        return compensat_da;
    }
    public void setCompensatDa(int compensat_da) {
        this.compensat_da = compensat_da;
    }

    public int getCompensatNu() {
        return compensat_nu;
    }
    public void setCompensatNu(int compensat_nu) {
        this.compensat_nu = compensat_nu;
    }

    public String getPhNumber() {
        return phNumber;
    }
    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getItemDescription() {
        return name;
    }
}
