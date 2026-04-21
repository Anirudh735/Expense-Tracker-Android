package com.example.financecompanionmobileapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "FinanceDB";
    private static final int DB_VERSION = 2; // Incremented version to add date column
    private static final String TABLE_NAME = "transactions";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_TYPE = "type";
    private static final String COL_DATE = "date";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_AMOUNT + " REAL, " +
                COL_TYPE + " TEXT, " +
                COL_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_DATE + " TEXT");
        }
    }

    public long addTransaction(String title, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_AMOUNT, amount);
        cv.put(COL_TYPE, type);
        cv.put(COL_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        return db.insert(TABLE_NAME, null, cv);
    }

    public int updateTransaction(int id, String title, double amount, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, title);
        cv.put(COL_AMOUNT, amount);
        cv.put(COL_TYPE, type);
        // We keep the original date for now on update, or we could update it. 
        // For simplicity, let's keep it.
        return db.update(TABLE_NAME, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_DATE + " ASC", null);

        if (cursor.moveToFirst()) {
            int idIdx = cursor.getColumnIndex(COL_ID);
            int titleIdx = cursor.getColumnIndex(COL_TITLE);
            int amountIdx = cursor.getColumnIndex(COL_AMOUNT);
            int typeIdx = cursor.getColumnIndex(COL_TYPE);
            int dateIdx = cursor.getColumnIndex(COL_DATE);

            do {
                list.add(new Transaction(
                        cursor.getInt(idIdx),
                        cursor.getString(titleIdx),
                        cursor.getDouble(amountIdx),
                        cursor.getString(typeIdx),
                        cursor.getString(dateIdx)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    
    public double getTotal(String type) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COL_AMOUNT + ") FROM " + TABLE_NAME + " WHERE " + COL_TYPE + "=?", new String[]{type});
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }
}
