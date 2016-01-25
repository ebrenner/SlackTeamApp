package com.example.ericbrenner.slackteamapp.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ericbrenner on 1/22/16.
 */
public class Profile {
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
}
