package com.example.ericbrenner.slackteamapp.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ericbrenner on 1/22/16.
 */
public class MembersResponse {
    public boolean ok;
    public ArrayList<Member> members;
    public String error;
    @SerializedName("cache_ts")
    public long cacheTs;

    public MembersResponse() {
        members = new ArrayList<Member>();
    }
}
