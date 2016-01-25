package com.example.ericbrenner.slackteamapp.interfaces;

import com.example.ericbrenner.slackteamapp.pojos.MembersResponse;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by ericbrenner on 1/22/16.
 */
public interface SlackApiEndpointInterface {

    @GET("/api/users.list?token=xoxp-5048173296-5048487710-19045732087-b5427e3b46")
    Call<MembersResponse> getMembersResponse();
}
