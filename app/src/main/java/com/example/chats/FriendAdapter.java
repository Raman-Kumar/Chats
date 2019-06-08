package com.example.chats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private String[] mDataset;
    Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textView;
        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("tag", "Element " + getAdapterPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.textView2);
            imageView = (ImageView) v.findViewById(R.id.imageView2);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
    }

    public FriendAdapter(String[] myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LinearLayout friend = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.friend, viewGroup, false);

        return new ViewHolder(friend);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int iPosition) {
        viewHolder.textView.setText(mDataset[iPosition]);
        Glide.with(getApplicationContext()).load("https://www.jiocinema.com/assets/img/facebook-Hover.png").into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
