package com.example.ericbrenner.slackteamapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ericbrenner on 1/22/16.
 */
public class Profile implements Parcelable {
    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("real_name")
    public String realName;

    @SerializedName("real_name_normalized")
    public String realNameNormalized;

    public String title;
    public String email;
    public String skype;
    public String phone;

    @SerializedName("image_24")
    public String image24;

    @SerializedName("image_32")
    public String image32;

    @SerializedName("image_48")
    public String image48;

    @SerializedName("image_72")
    public String image72;

    @SerializedName("image_192")
    public String image192;

    @SerializedName("image_original")
    public String imageOriginal;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(realName);
        dest.writeString(realNameNormalized);
        dest.writeString(title);
        dest.writeString(email);
        dest.writeString(skype);
        dest.writeString(phone);
        dest.writeString(image24);
        dest.writeString(image32);
        dest.writeString(image48);
        dest.writeString(image72);
        dest.writeString(image192);
        dest.writeString(imageOriginal);
    }

    public static final Parcelable.Creator<Profile> CREATOR
            = new Parcelable.Creator<Profile>() {
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    private Profile(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        realName = in.readString();
        realNameNormalized = in.readString();
        title = in.readString();
        email = in.readString();
        skype = in.readString();
        phone = in.readString();
        image24 = in.readString();
        image32 = in.readString();
        image48 = in.readString();
        image72 = in.readString();
        image192 = in.readString();
        imageOriginal = in.readString();
    }
}
