package com.example.ericbrenner.slackteamapp.pojos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ericbrenner on 1/22/16.
 */
public class Member {
    public String id;

    @SerializedName("team_id")
    public String teamId;

    public String name;
    public boolean deleted;
    public String color;
    public Profile profile;

    @SerializedName("is_admin")
    public boolean isAdmin;

    @SerializedName("is_owner")
    public boolean isOwner;

    @SerializedName("is_primary_owner")
    public boolean isPrimaryOwner;

    @SerializedName("is_restricted")
    public boolean isRestricted;

    @SerializedName("is_ultra_restricted")
    public boolean isUltraRestricted;

    @SerializedName("is_bot")
    public boolean isBot;
}
