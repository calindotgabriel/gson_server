package places.model;

import javax.persistence.Column;

/**
 * Created by motan on 2/23/14.
 */
public class Item implements ItemInterface {

    public String name;


    public Item() {
    }

    public Item(String name) {

        this.name = name;
    }


    public String getDescription() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}