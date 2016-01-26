package com.example.ericbrenner.slackteamapp.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericbrenner.slackteamapp.Constants;
import com.example.ericbrenner.slackteamapp.R;
import com.example.ericbrenner.slackteamapp.Utilities;
import com.example.ericbrenner.slackteamapp.pojos.Member;
import com.squareup.picasso.Picasso;

/**
 * Created by ericbrenner on 1/25/16.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpActionBar();
        Member member = grabMemberArg();
        loadCorrectProfileImage(member);
        setTextContent(member);
    }

    private void setUpActionBar() {
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private Member grabMemberArg() {
        Bundle bundle = getIntent().getExtras();
        Member member = bundle.getParcelable(Constants.KEY_MEMBER);
        return member;
    }

    private void loadImage(Uri uri, ImageView imageView) {
        Picasso.with(this)
                .load(uri)
                .placeholder(R.drawable.background_profile_placeholder)
                .error(R.drawable.background_profile_placeholder)
                .into(imageView);
    }

    private void loadCorrectProfileImage(Member member) {
        ImageView imageView = (ImageView)findViewById(R.id.profile_image);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch(metrics.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                loadImage(Uri.parse(member.profile.image32), imageView);
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                loadImage(Uri.parse(member.profile.image48), imageView);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                loadImage(Uri.parse(member.profile.image72), imageView);
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                loadImage(Uri.parse(member.profile.image192), imageView);
                break;
            default:
                loadImage(Uri.parse(member.profile.imageOriginal), imageView);
                break;
        }
    }

    private void setTextContent(Member member) {
        TextView nameText = (TextView)findViewById(R.id.profile_name);
        nameText.setText(member.name);
        nameText.setTextColor(Color.parseColor(Utilities.formatColorString(member.color)));

        TextView realNameText = (TextView)findViewById(R.id.profile_real_name);
        realNameText.setText(member.profile.realName);

        TextView emailText = (TextView)findViewById(R.id.profile_email);
        emailText.setText(member.profile.email);

        TextView phoneText = (TextView)findViewById(R.id.profile_phone);
        phoneText.setText(member.profile.phone);
    }
}
