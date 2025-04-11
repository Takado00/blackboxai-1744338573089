package com.example.recicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MaterialManager {
    private DatabaseHelper dbHelper;

    public MaterialManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addMaterial(String name, String description, long userId, String location) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("userId", userId);
        values.put("location", location);
        values.put("rating", 0.0); // Default rating
        
        long materialId = db.insert("materials", null, values);
        db.close();
        return materialId;
    }

    public Cursor getAllMaterials() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query("materials", null, null, null, null, null, "rating DESC");
    }

    public Cursor searchMaterials(String query) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "name LIKE ? OR description LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};
        return db.query("materials", null, selection, selectionArgs, null, null, "rating DESC");
    }

    public void rateMaterial(long materialId, long userId, int rating, String comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Add rating to ratings table
        ContentValues ratingValues = new ContentValues();
        ratingValues.put("materialId", materialId);
        ratingValues.put("userId", userId);
        ratingValues.put("rating", rating);
        ratingValues.put("comment", comment);
        db.insert("ratings", null, ratingValues);

        // Update average rating in materials table
        Cursor ratingsCursor = db.rawQuery(
            "SELECT AVG(rating) FROM ratings WHERE materialId = ?",
            new String[]{String.valueOf(materialId)}
        );
        
        if (ratingsCursor.moveToFirst()) {
            float avgRating = ratingsCursor.getFloat(0);
            ContentValues materialValues = new ContentValues();
            materialValues.put("rating", avgRating);
            db.update("materials", materialValues, "id = ?", 
                     new String[]{String.valueOf(materialId)});
        }
        
        ratingsCursor.close();
        db.close();
    }

    public Cursor getMaterialsByUser(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "userId = ?";
        String[] selectionArgs = new String[]{String.valueOf(userId)};
        return db.query("materials", null, selection, selectionArgs, null, null, "rating DESC");
    }

    public Cursor getMaterialsByLocation(String location) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "location LIKE ?";
        String[] selectionArgs = new String[]{"%" + location + "%"};
        return db.query("materials", null, selection, selectionArgs, null, null, "rating DESC");
    }
}
