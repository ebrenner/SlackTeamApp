package com.example.ericbrenner.slackteamapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ericbrenner.slackteamapp.Constants;
import com.example.ericbrenner.slackteamapp.R;
import com.example.ericbrenner.slackteamapp.Utilities;
import com.example.ericbrenner.slackteamapp.adapters.MembersAdapter;
import com.example.ericbrenner.slackteamapp.interfaces.SlackApiEndpointInterface;
import com.example.ericbrenner.slackteamapp.listeners.RecyclerItemClickListener;
import com.example.ericbrenner.slackteamapp.pojos.Member;
import com.example.ericbrenner.slackteamapp.pojos.MembersResponse;
import com.google.gson.Gson;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Call<MembersResponse> mCall;

    private final Gson gson = new Gson();

    private static final String REQUEST_URL =
            "https://slack.com/api/users.list?token=xoxp-5048173296-5048487710-19045732087-b5427e3b46";
    private static final String LATEST_RESPONSE_FILE = "LATEST_RESPONSE_FILE";

    public static final String REQUEST_TAG = "REQUEST_TAG";
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();

        //makeMembersRequest();
        makeRequest();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mCall.cancel();
        mQueue.cancelAll(REQUEST_TAG);
    }

    private void setUpActionBar() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private OkHttpClient createCacheEnabledClient() {
        final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                if (Utilities.isNetworkAvailable(MainActivity.this)) {
                    int maxAge = 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);

        File httpCacheDirectory = new File(this.getCacheDir(), Constants.CACHE_NAME);
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        client.setCache(cache);
        return client;
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

    private boolean fileExistance(String fname){
        File file = getFileStreamPath(fname);
        return file.exists();
    }

    private void handleGoodResponse(String response) {
        saveResponse(response);
        MembersResponse membersResponse = gson.fromJson(response, MembersResponse.class);
        populateRecyclerView(membersResponse.members);
    }

    private void handleBadOrMissingResponse() {
        if (fileExistance(LATEST_RESPONSE_FILE)) {
            String savedResponse = getLastSavedResponse();
            MembersResponse membersResponse = gson.fromJson(savedResponse, MembersResponse.class);
            populateRecyclerView(membersResponse.members);
        } else {
            findViewById(R.id.no_content_message).setVisibility(View.VISIBLE);
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
                            handleGoodResponse(response);
                        } else {
                            handleBadOrMissingResponse();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleBadOrMissingResponse();
            }
        });
        stringRequest.setTag(REQUEST_TAG);
        mQueue.add(stringRequest);
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
}
