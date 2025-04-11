package com.example.recicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TransactionManager {
    private DatabaseHelper dbHelper;
    private static final String STATUS_PENDING = "PENDIENTE";
    private static final String STATUS_COMPLETED = "COMPLETADO";
    private static final String STATUS_CANCELLED = "CANCELADO";

    public TransactionManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long createTransaction(long materialId, long sellerId, long buyerId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("materialId", materialId);
        values.put("sellerId", sellerId);
        values.put("buyerId", buyerId);
        values.put("status", STATUS_PENDING);
        
        long transactionId = db.insert("transactions", null, values);
        db.close();
        return transactionId;
    }

    public void updateTransactionStatus(long transactionId, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        
        db.update("transactions", values, "id = ?", 
                 new String[]{String.valueOf(transactionId)});
        db.close();
    }

    public Cursor getTransactionHistory(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "sellerId = ? OR buyerId = ?";
        String[] selectionArgs = new String[]{
            String.valueOf(userId),
            String.valueOf(userId)
        };
        return db.query("transactions", null, selection, selectionArgs, 
                       null, null, "date DESC");
    }

    public Cursor getPendingTransactions(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "(sellerId = ? OR buyerId = ?) AND status = ?";
        String[] selectionArgs = new String[]{
            String.valueOf(userId),
            String.valueOf(userId),
            STATUS_PENDING
        };
        return db.query("transactions", null, selection, selectionArgs, 
                       null, null, "date DESC");
    }

    public Cursor getTransactionDetails(long transactionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT t.*, " +
                      "m.name as material_name, " +
                      "m.description as material_description, " +
                      "s.username as seller_name, " +
                      "b.username as buyer_name " +
                      "FROM transactions t " +
                      "JOIN materials m ON t.materialId = m.id " +
                      "JOIN users s ON t.sellerId = s.id " +
                      "JOIN users b ON t.buyerId = b.id " +
                      "WHERE t.id = ?";
        
        return db.rawQuery(query, new String[]{String.valueOf(transactionId)});
    }

    public void completeTransaction(long transactionId) {
        updateTransactionStatus(transactionId, STATUS_COMPLETED);
    }

    public void cancelTransaction(long transactionId) {
        updateTransactionStatus(transactionId, STATUS_CANCELLED);
    }
}
