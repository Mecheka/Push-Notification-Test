package com.example.suriya.firebasepushnotification.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suriya.firebasepushnotification.R;
import com.example.suriya.firebasepushnotification.activity.SendActivity;
import com.example.suriya.firebasepushnotification.utill.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Suriya on 10/1/2561.
 */

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    private List<User> userList;
    private Context mContext;

    public UsersRecyclerViewAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @Override
    public UsersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.tvName.setText(userList.get(position).getName());
        CircleImageView imgProfile = holder.imgProfile;
        Glide.with(mContext).load(userList.get(position).getImageUrl()).into(imgProfile);

        final String userID = userList.get(position).userID;
        final String userName = userList.get(position).getName();

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent(mContext, SendActivity.class);
                sendIntent.putExtra("userID", userID);
                sendIntent.putExtra("userName", userName);
                mContext.startActivity(sendIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView imgProfile;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            imgProfile = (CircleImageView)mView.findViewById(R.id.imgProfile);
            tvName = (TextView)mView.findViewById(R.id.tvName);
        }
    }
}
