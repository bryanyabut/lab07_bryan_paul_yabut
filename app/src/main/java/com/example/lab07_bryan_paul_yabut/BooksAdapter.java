package com.example.lab07_bryan_paul_yabut;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BooksAdapter extends ArrayAdapter<Book> {
    private final int resource;

    public BooksAdapter(Context ctx, int resource, List<Book> items) {
        super(ctx, resource, items);
        this.resource = resource;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        Book b = getItem(pos);
        TextView tvTitle = v.findViewById(R.id.tvTitle);
        TextView tvSubtitle = v.findViewById(R.id.tvSubtitle);

        if (b != null) {
            tvTitle.setText(b.getTitle());
            tvSubtitle.setText(b.getAuthor() + " — " + b.getGenre() + " (" + b.getYear() + ")");
        }
        return v;
    }
}