package com.example.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "classmate.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "classmate";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(createTable);

        // Добавляем записи
        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, "Артём");
        values.put(COLUMN_DATE, "2024-11-01");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Мария");
        values.put(COLUMN_DATE, "2024-11-10");
        db.insert(TABLE_NAME, null, values);

        values.put(COLUMN_NAME, "Дмитрий");
        values.put(COLUMN_DATE, "2024-11-15");
        db.insert(TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME); // Удаляем только содержимое таблицы
        db.close();
    }


    public void restoreInitialData() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Проверяем, пуста ли таблица
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            if (count == 0) { // Если записей нет, добавляем начальные данные
                insertInitialData(db);
            }
        }
        cursor.close();
        db.close();
    }

    public void addUser(String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DATE, date);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void updateLastUserName(String newName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Получаем ID последней записи
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            int lastId = cursor.getInt(0); // ID последней записи
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, newName);

            // Обновляем имя в последней записи
            db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(lastId)});
        }
        cursor.close();
        db.close();
    }

}
