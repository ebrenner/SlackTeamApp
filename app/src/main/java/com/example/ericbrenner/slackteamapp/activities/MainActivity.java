package com.example.ericbrenner.slackteamapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.ericbrenner.slackteamapp.Constants;
import com.example.ericbrenner.slackteamapp.R;
import com.example.ericbrenner.slackteamapp.adapters.MembersAdapter;
import com.example.ericbrenner.slackteamapp.interfaces.SlackApiEndpointInterface;
import com.example.ericbrenner.slackteamapp.listeners.RecyclerItemClickListener;
import com.example.ericbrenner.slackteamapp.pojos.Member;
import com.example.ericbrenner.slackteamapp.pojos.MembersResponse;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Call<MembersResponse> mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();

        makeMembersRequest();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCall.cancel();
    }

    private void setUpActionBar() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void populateRecyclerView(ArrayList<Member> members) {
        final MembersAdapter adapter = new MembersAdapter(members);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.members_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Member member = adapter.getItem(position);
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra(Constants.KEY_MEMBER, member);
                        startActivity(intent);
                    }
                })
        );
        recyclerView.setAdapter(adapter);
    }

    private void makeMembersRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SLACK_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SlackApiEndpointInterface apiService = retrofit.create(SlackApiEndpointInterface.class);

        mCall = apiService.getMembersResponse();
        mCall.enqueue(new Callback<MembersResponse>() {
            @Override
            public void onResponse(Response<MembersResponse> response, Retrofit retrofit) {
                int statusCode = response.code();
                MembersResponse membersResponse = response.body();
                if (membersResponse.ok) {
                    populateRecyclerView(membersResponse.members);
                } else {
                    Log.d(Constants.APP_TAG, membersResponse.error);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(Constants.APP_TAG, t.getLocalizedMessage());
            }
        });
    }
}
