package com.example.ericbrenner.slackteamapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ericbrenner.slackteamapp.Constants;
import com.example.ericbrenner.slackteamapp.R;
import com.example.ericbrenner.slackteamapp.adapters.MembersAdapter;
import com.example.ericbrenner.slackteamapp.listeners.RecyclerItemClickListener;
import com.example.ericbrenner.slackteamapp.pojos.Member;
import com.example.ericbrenner.slackteamapp.pojos.MembersResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final Gson gson = new Gson();
    private static final String REQUEST_URL =
            "https://slack.com/api/users.list?token=xoxp-5048173296-5048487710-19045732087-b5427e3b46";
    private static final String LATEST_RESPONSE_FILE = "LATEST_RESPONSE_FILE";
    public static final String REQUEST_TAG = "REQUEST_TAG";
    private RequestQueue mQueue;
    View mProgressBar;
    View mMessage;
    RecyclerView mRecyclerView;
    MembersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        setUpRecyclerView();
        mProgressBar = findViewById(R.id.progress_bar);
        mMessage = findViewById(R.id.no_content_message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        makeRequest();
    }

    @Override
    public void onStop() {
        super.onStop();
        mQueue.cancelAll(REQUEST_TAG);
    }

    private void setUpActionBar() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void saveResponse(String response) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(LATEST_RESPONSE_FILE, Context.MODE_PRIVATE);
            fos.write(response.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLastSavedResponse() {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        try {
            fis = openFileInput(LATEST_RESPONSE_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private boolean fileExists(String fileName){
        File file = getFileStreamPath(fileName);
        return file.exists();
    }

    private void handleBadOrMissingResponse() {
        if (fileExists(LATEST_RESPONSE_FILE)) {
            mMessage.setVisibility(View.GONE);
            String savedResponse = getLastSavedResponse();
            MembersResponse membersResponse = gson.fromJson(savedResponse, MembersResponse.class);
            populateRecyclerView(membersResponse.members);
        } else {
            mMessage.setVisibility(View.VISIBLE);
        }
    }

    private void makeRequest() {
        mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, REQUEST_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MembersResponse membersResponse = gson.fromJson(response, MembersResponse.class);
                        if (membersResponse.ok) {
                            mMessage.setVisibility(View.GONE);
                            populateRecyclerView(membersResponse.members);
                            saveResponse(response);
                        } else {
                            handleBadOrMissingResponse();
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleBadOrMissingResponse();
                mProgressBar.setVisibility(View.GONE);
            }
        });
        stringRequest.setTag(REQUEST_TAG);
        mQueue.add(stringRequest);
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.members_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Member member = mAdapter.getItem(position);
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra(Constants.KEY_MEMBER, member);
                        startActivity(intent);
                    }
                })
        );
    }

    private void populateRecyclerView(ArrayList<Member> members) {
        mAdapter = new MembersAdapter(members);
        mRecyclerView.setAdapter(mAdapter);
    }
}
