// java
package com.example.lab07_bryan_paul_yabut;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DBHelper db;
    private ListView listView;
    private BooksAdapter adapter;
    private ArrayList<Book> books;
    private ActivityResultLauncher<Intent> editLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(this);

        listView = findViewById(R.id.listViewBooks);
        Button btnAdd = findViewById(R.id.btnAdd);

        // Register launcher for activity results
        editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            loadBooks();
                        }
                    }
                }
        );

        loadBooks();

        btnAdd.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, EditBookActivity.class);
            editLauncher.launch(i);
        });

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Book b = books.get(position);
            Intent i = new Intent(MainActivity.this, EditBookActivity.class);
            i.putExtra("book_id", b.getId());
            editLauncher.launch(i);
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

    private void loadBooks() {
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
