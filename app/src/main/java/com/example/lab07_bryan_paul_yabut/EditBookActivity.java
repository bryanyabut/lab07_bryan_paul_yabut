package com.example.lab07_bryan_paul_yabut;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etGenre, etYear;
    private DBHelper db;
    private int bookId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        db = new DBHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etGenre = findViewById(R.id.etGenre);
        etYear = findViewById(R.id.etYear);
        Button btnSave = findViewById(R.id.btnSave);

        if (getIntent().hasExtra("book_id")) {
            bookId = getIntent().getIntExtra("book_id", 0);
            if (bookId > 0) {
                Book b = db.getBook(bookId);
                if (b != null) {
                    etTitle.setText(b.getTitle());
                    etAuthor.setText(b.getAuthor());
                    etGenre.setText(b.getGenre());
                    etYear.setText(String.valueOf(b.getYear()));
                }
            }
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String author = etAuthor.getText().toString().trim();
            String genre = etGenre.getText().toString().trim();
            String yearStr = etYear.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(author)) {
                Toast.makeText(EditBookActivity.this, "Title and author required", Toast.LENGTH_SHORT).show();
                return;
            }

            int year = 0;
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException ignored) {}

            if (bookId > 0) {
                Book b = new Book(bookId, title, author, genre, year);
                db.updateBook(b);
            } else {
                Book b = new Book(title, author, genre, year);
                db.addBook(b);
            }
            setResult(RESULT_OK);
            finish();
        });
    }
}