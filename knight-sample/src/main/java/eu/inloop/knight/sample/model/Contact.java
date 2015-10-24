package eu.inloop.knight.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Interface {@link Contact}
 *
 * @author f3rog
 * @version 2015-07-09
 */
public class Contact implements Parcelable, Comparable<Contact> {

    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("pictureUrl")
    private String mPictureUrl;

    public Contact() {
    }

    public Contact(String name, String phone) {
        mName = name;
        mPhone = phone;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mPhone);
        dest.writeString(mPictureUrl);
    }

    public Contact(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mPhone = in.readString();
        mPictureUrl = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {

        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int compareTo(Contact another) {
        if (getName() == null) {
            return -1;
        }
        if (another == null || another.getName() == null) {
            return 1;
        }
        return getName().compareToIgnoreCase(another.getName());
    }

}
