package com.example.ericbrenner.slackteamapp.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ericbrenner.slackteamapp.R;
import com.example.ericbrenner.slackteamapp.Utilities;
import com.example.ericbrenner.slackteamapp.pojos.Member;

import java.util.ArrayList;

/**
 * Created by ericbrenner on 1/24/16.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {

    private ArrayList<Member> mMembers;

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public MemberViewHolder(RelativeLayout layout, TextView textView) {
            super(layout);
            mTextView = textView;
        }
    }

    public MembersAdapter(ArrayList<Member> members) {
        mMembers = members;
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        TextView nameTextView = (TextView)relativeLayout.findViewById(R.id.item_member_text);
        MemberViewHolder vh = new MemberViewHolder(relativeLayout, nameTextView);
        return vh;
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        Member member = mMembers.get(position);
        int color = Color.parseColor(Utilities.formatColorString(member.color));
        holder.mTextView.setText(member.name);
        holder.mTextView.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    public Member getItem(int position) {
        return mMembers.get(position);
    }
}
