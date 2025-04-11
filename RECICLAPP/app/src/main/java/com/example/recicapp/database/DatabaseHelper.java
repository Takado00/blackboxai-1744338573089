package com.example.recicapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recicapp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "username TEXT NOT NULL, " +
            "email TEXT NOT NULL UNIQUE, " +
            "password TEXT NOT NULL, " +
            "userType TEXT NOT NULL)";

        // Create materials table with new fields
        String CREATE_MATERIALS_TABLE = "CREATE TABLE materials (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "description TEXT, " +
            "userId INTEGER, " +
            "rating FLOAT DEFAULT 0, " +
            "location TEXT, " +
            "materialType TEXT NOT NULL, " +
            "quantity INTEGER NOT NULL, " +
            "dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (userId) REFERENCES users(id))";

        // Create transactions table
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE transactions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "materialId INTEGER, " +
            "sellerId INTEGER, " +
            "buyerId INTEGER, " +
            "quantity INTEGER NOT NULL, " +
            "date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "status TEXT, " +
            "FOREIGN KEY (materialId) REFERENCES materials(id), " +
            "FOREIGN KEY (sellerId) REFERENCES users(id), " +
            "FOREIGN KEY (buyerId) REFERENCES users(id))";

        // Create ratings table
        String CREATE_RATINGS_TABLE = "CREATE TABLE ratings (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "materialId INTEGER, " +
            "userId INTEGER, " +
            "rating INTEGER CHECK(rating >= 1 AND rating <= 5), " +
            "comment TEXT, " +
            "date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (materialId) REFERENCES materials(id), " +
            "FOREIGN KEY (userId) REFERENCES users(id))";

        // Create recycling tips table
        String CREATE_TIPS_TABLE = "CREATE TABLE recycling_tips (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "materialType TEXT NOT NULL, " +
            "title TEXT NOT NULL, " +
            "description TEXT NOT NULL, " +
            "imageUrl TEXT, " +
            "userId INTEGER, " +
            "dateAdded DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (userId) REFERENCES users(id))";

        // Execute the SQL statements
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_MATERIALS_TABLE);
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
        db.execSQL(CREATE_RATINGS_TABLE);
        db.execSQL(CREATE_TIPS_TABLE);

        // Insert some default recycling tips
        insertDefaultTips(db);
    }

    private void insertDefaultTips(SQLiteDatabase db) {
        String[] defaultTips = {
            "INSERT INTO recycling_tips (materialType, title, description) VALUES " +
            "('Plástico', 'Separación por Tipo', 'Identifica el tipo de plástico por el número en el símbolo de reciclaje. Cada tipo debe ser reciclado de manera diferente.')",
            
            "INSERT INTO recycling_tips (materialType, title, description) VALUES " +
            "('Papel', 'Papel Limpio y Seco', 'Mantén el papel libre de contaminantes como comida o líquidos. El papel mojado o sucio no puede ser reciclado.')",
            
            "INSERT INTO recycling_tips (materialType, title, description) VALUES " +
            "('Vidrio', 'Separación por Color', 'Separa el vidrio por colores: transparente, verde y ámbar. Esto facilita el proceso de reciclaje.')",
            
            "INSERT INTO recycling_tips (materialType, title, description) VALUES " +
            "('Metal', 'Compactación de Latas', 'Aplasta las latas de aluminio para ahorrar espacio. Asegúrate de que estén limpias y secas.')"
        };

        for (String tip : defaultTips) {
            db.execSQL(tip);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS recycling_tips");
        db.execSQL("DROP TABLE IF EXISTS ratings");
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS materials");
        db.execSQL("DROP TABLE IF EXISTS users");
        // Create tables again
        onCreate(db);
    }
}
