package com.example.ericbrenner.slackteamapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.ericbrenner.slackteamapp.adapters.MembersAdapter;
import com.example.ericbrenner.slackteamapp.interfaces.SlackApiEndpointInterface;
import com.example.ericbrenner.slackteamapp.pojos.MembersResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String REQUEST_TAG = "REQUEST_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        makeMembersRequest();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void makeMembersRequest() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SLACK_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        SlackApiEndpointInterface apiService =
                retrofit.create(SlackApiEndpointInterface.class);
        Call<MembersResponse> call = apiService.getMembersResponse();
        call.enqueue(new Callback<MembersResponse>() {
            @Override
            public void onResponse(Response<MembersResponse> response, Retrofit retrofit) {
                int statusCode = response.code();
                MembersResponse membersResponse = response.body();
                if (membersResponse.ok) {
                    MembersAdapter adapter = new MembersAdapter(membersResponse.members);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.members_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("SlackTeamApp", "Reason: " + t.getLocalizedMessage());
            }
        });

    }
}
