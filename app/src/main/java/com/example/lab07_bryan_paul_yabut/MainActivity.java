package com.example.lab07_bryan_paul_yabut;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT = 100;
    private DBHelper db;
    private ListView listView;
    private BooksAdapter adapter;
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DBHelper(this);
        listView = findViewById(R.id.listViewBooks);
        Button btnAdd = findViewById(R.id.btnAdd);

        loadBooks();

        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, EditBookActivity.class);
            startActivityForResult(i, REQUEST_EDIT);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Book b = books.get(position);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete")
                    .setMessage("Delete \"" + b.getTitle() + "\"?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        db.deleteBook(b.getId());
                        loadBooks();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        });
    }

    private void loadBooks(){
        books = db.getAllBooks();
        if (adapter == null) {
            adapter = new BooksAdapter(this, R.layout.list_item_book, books);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(books);
            adapter.notifyDataSetChanged();
        }
    }
}