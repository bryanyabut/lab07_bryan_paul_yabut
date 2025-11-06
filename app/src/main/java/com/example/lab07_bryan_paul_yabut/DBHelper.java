package com.example.lab07_bryan_paul_yabut;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "library.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_BOOKS = "books";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_GENRE = "genre";
    public static final String COL_YEAR = "year";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_AUTHOR + " TEXT, " +
                COL_GENRE + " TEXT, " +
                COL_YEAR + " INTEGER)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    public long addBook(Book b) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, b.getTitle());
        cv.put(COL_AUTHOR, b.getAuthor());
        cv.put(COL_GENRE, b.getGenre());
        cv.put(COL_YEAR, b.getYear());
        long id = db.insert(TABLE_BOOKS, null, cv);
        db.close();
        return id;
    }

    public int updateBook(Book b) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, b.getTitle());
        cv.put(COL_AUTHOR, b.getAuthor());
        cv.put(COL_GENRE, b.getGenre());
        cv.put(COL_YEAR, b.getYear());
        int rows = db.update(TABLE_BOOKS, cv, COL_ID + "=?", new String[]{String.valueOf(b.getId())});
        db.close();
        return rows;
    }

    public int deleteBook(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_BOOKS, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public Book getBook(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_BOOKS, null, COL_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        Book b = null;
        if (c != null) {
            if (c.moveToFirst()) {
                b = new Book(
                        c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(COL_AUTHOR)),
                        c.getString(c.getColumnIndexOrThrow(COL_GENRE)),
                        c.getInt(c.getColumnIndexOrThrow(COL_YEAR))
                );
            }
            c.close();
        }
        db.close();
        return b;
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_BOOKS, null, null, null, null, null, COL_TITLE + " ASC");
        if (c != null) {
            while (c.moveToNext()) {
                Book b = new Book(
                        c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                        c.getString(c.getColumnIndexOrThrow(COL_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(COL_AUTHOR)),
                        c.getString(c.getColumnIndexOrThrow(COL_GENRE)),
                        c.getInt(c.getColumnIndexOrThrow(COL_YEAR))
                );
                list.add(b);
            }
            c.close();
        }
        db.close();
        return list;
    }
}