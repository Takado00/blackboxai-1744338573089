package com.example.recicapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclingTipsAdapter extends RecyclerView.Adapter<RecyclingTipsAdapter.TipViewHolder> {
    private Context context;
    private Cursor cursor;

    public class TipViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView descriptionText;
        public TextView materialTypeText;
        public ImageView tipImage;

        public TipViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.tipTitle);
            descriptionText = view.findViewById(R.id.tipDescription);
            materialTypeText = view.findViewById(R.id.tipMaterialType);
            tipImage = view.findViewById(R.id.tipImage);
        }
    }

    public RecyclingTipsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycling_tip_item, parent, false);
        return new TipViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TipViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String materialType = cursor.getString(cursor.getColumnIndexOrThrow("materialType"));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));

        holder.titleText.setText(title);
        holder.descriptionText.setText(description);
        holder.materialTypeText.setText(materialType);

        // Set material type icon based on the type
        int iconResource;
        switch (materialType.toLowerCase()) {
            case "plástico":
                iconResource = android.R.drawable.ic_menu_save;
                break;
            case "papel":
                iconResource = android.R.drawable.ic_menu_edit;
                break;
            case "vidrio":
                iconResource = android.R.drawable.ic_menu_view;
                break;
            case "metal":
                iconResource = android.R.drawable.ic_menu_compass;
                break;
            default:
                iconResource = android.R.drawable.ic_menu_help;
        }
        holder.tipImage.setImageResource(iconResource);
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
