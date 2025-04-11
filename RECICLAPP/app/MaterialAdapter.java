package com.example.recicapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {
    private Context context;
    private Cursor cursor;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onContactClick(long materialId, String materialName);
        void onDetailsClick(long materialId);
    }

    public MaterialAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    public class MaterialViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView descriptionText;
        public TextView locationText;
        public RatingBar ratingBar;
        public Button contactButton;
        public Button detailsButton;

        public MaterialViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.materialName);
            descriptionText = view.findViewById(R.id.materialDescription);
            locationText = view.findViewById(R.id.materialLocation);
            ratingBar = view.findViewById(R.id.materialRating);
            contactButton = view.findViewById(R.id.contactButton);
            detailsButton = view.findViewById(R.id.detailsButton);
        }
    }

    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.material_list_item, parent, false);
        return new MaterialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MaterialViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        long materialId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
        float rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating"));

        holder.nameText.setText(name);
        holder.descriptionText.setText(description);
        holder.locationText.setText(location);
        holder.ratingBar.setRating(rating);

        holder.contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactClick(materialId, name);
            }
        });

        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDetailsClick(materialId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public Cursor getCursor() {
        return cursor;
    }
}
