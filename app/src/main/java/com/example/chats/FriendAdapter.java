package com.example.chats;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public FriendAdapter(String[] myDataset) {
        mDataset = myDataset;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ConstraintLayout friend = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.friend, viewGroup, false);

        return new ViewHolder(friend);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int iPosition) {
        viewHolder.textView.setText(mDataset[iPosition]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
