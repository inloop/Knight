package eu.inloop.knight.sample.model;

import com.google.gson.annotations.SerializedName;

/**
 * Interface {@link Order}
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class Order {

    @SerializedName("name")
    private String mName;
    @SerializedName("count")
    private int mCount;

    public Order() {
    }

    public String getName() {
        return mName;
    }

    public int getCount() {
        return mCount;
    }

}
