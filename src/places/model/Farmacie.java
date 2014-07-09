package places.model;

import places.util.DbStringConvert;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Pharmacy class model, used to store info about one entity.
 */

@Entity
@Table (name = "FARMACII")
public class Farmacie extends Item implements Serializable {


    @Id
    @GeneratedValue (strategy = IDENTITY)
    @Column (name = "ID", unique = true, nullable = false)
    private int id;

    @Column (name="OPEN_HOURS", length = 20)
    private String[] openHours;

    @Column (name="VICINITY", length = 50)
    private String vicinity;

    @Column (name="PH_NUMBER", length = 14)
    private String phNumber;

    @Column (name="URL", length = 25)
    private String url;

    @Column (name="LAT", precision = 12, scale = 10)
    private double lat;

    @Column (name="LNG", precision = 12, scale = 10)
    private double lng;

    @Column (name="COMPENSAT")
    private int compensat;

    @Column (name = "OPEN_NOW" )
    private int openNow;

    @Column (name = "NAME", length = 20)
    private String name = super.getName();



    public Farmacie() {
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getOpenNow() {
        return openNow;
    }
    public void setOpenNow(int openNow) {
        this.openNow = openNow;
    }


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


    public int getCompensat() {
        return compensat;
    }
    public void setCompensat(int compensat) {
        this.compensat = compensat;
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


}
