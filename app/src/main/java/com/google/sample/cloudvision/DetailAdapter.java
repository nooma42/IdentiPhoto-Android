package com.google.sample.cloudvision;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.util.List;

import static java.lang.Math.ceil;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private List<EntityAnnotation> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    DetailAdapter(Context context, List<EntityAnnotation> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.detail_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String description = mData.get(position).getDescription();
        Float weight = mData.get(position).getScore();
        holder.descriptionText.setText(description);
        holder.weightValue.setText(String.format("%.02f", weight));


        holder.removeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                double pos = position;
                mData.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                Log.e("finna","IM FINNA DELETE AT " + (holder.getAdapterPosition()));
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView descriptionText;
        EditText weightValue;
        ImageView removeButton;

        ViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);

            descriptionText = itemView.findViewById(R.id.Detail_description);
            weightValue = itemView.findViewById(R.id.Detail_weight);
            removeButton = itemView.findViewById(R.id.Detail_remove);

            weightValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    mData.get(getAdapterPosition()).setConfidence(Float.valueOf(weightValue.getText().toString()));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        @Override
        public void onClick(View view) {

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
       //return mData.get(id);
        return "0";
}

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}