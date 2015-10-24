package eu.inloop.knight.sample.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Interface {@link Items}
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class Items<T> {

    @SerializedName("items")
    private List<T> mItems;

    public List<T> getItems() {
        return mItems;
    }

}
