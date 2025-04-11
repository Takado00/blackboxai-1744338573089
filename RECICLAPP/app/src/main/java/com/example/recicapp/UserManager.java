package com.example.recicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserManager {
    private DatabaseHelper dbHelper;
    private Context context;

    public UserManager(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public long registerUser(String username, String email, String password, String userType) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);  // En una app real, la contraseña debería estar hasheada
        values.put("userType", userType);

        return db.insert("users", null, values);
    }

    public boolean userExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", 
            new String[]{"id"}, 
            "email = ?", 
            new String[]{email}, 
            null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public long getUserId(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", 
            new String[]{"id"}, 
            "email = ?", 
            new String[]{email}, 
            null, null, null);

        long userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return userId;
    }

    public String getUserType(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", 
            new String[]{"userType"}, 
            "id = ?", 
            new String[]{String.valueOf(userId)}, 
            null, null, null);

        String userType = "";
        if (cursor.moveToFirst()) {
            userType = cursor.getString(cursor.getColumnIndexOrThrow("userType"));
        }
        cursor.close();
        return userType;
    }

    public boolean validateCredentials(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", 
            new String[]{"id"}, 
            "email = ? AND password = ?", 
            new String[]{email, password}, 
            null, null, null);

        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public boolean updateUser(long userId, String username, String email, String userType) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("userType", userType);

        return db.update("users", values, "id = ?", new String[]{String.valueOf(userId)}) > 0;
    }

    public boolean updatePassword(long userId, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);  // En una app real, la contraseña debería estar hasheada

        return db.update("users", values, "id = ?", new String[]{String.valueOf(userId)}) > 0;
    }

    public boolean deleteUser(long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("users", "id = ?", new String[]{String.valueOf(userId)}) > 0;
    }
}
