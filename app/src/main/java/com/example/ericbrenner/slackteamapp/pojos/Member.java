package com.example.ericbrenner.slackteamapp.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ericbrenner on 1/22/16.
 */
public class Member implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(teamId);
        dest.writeString(name);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(color);
        dest.writeParcelable(profile, flags);
        dest.writeByte((byte) (isAdmin ? 1 : 0));
        dest.writeByte((byte) (isOwner ? 1 : 0));
        dest.writeByte((byte) (isPrimaryOwner ? 1 : 0));
        dest.writeByte((byte) (isRestricted ? 1 : 0));
        dest.writeByte((byte) (isUltraRestricted ? 1 : 0));
        dest.writeByte((byte) (isBot ? 1 : 0));
    }

    public static final Parcelable.Creator<Member> CREATOR
            = new Parcelable.Creator<Member>() {
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    private Member(Parcel in) {
        id = in.readString();
        teamId = in.readString();
        name = in.readString();
        deleted = in.readByte() != 0;
        color = in.readString();
        profile = in.readParcelable(Profile.class.getClassLoader());
        isAdmin = in.readByte() != 0;
        isOwner = in.readByte() != 0;
        isPrimaryOwner = in.readByte() != 0;
        isRestricted = in.readByte() != 0;
        isUltraRestricted = in.readByte() != 0;
        isBot = in.readByte() != 0;
    }
}
