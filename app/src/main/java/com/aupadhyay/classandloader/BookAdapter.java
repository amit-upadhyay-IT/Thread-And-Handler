package com.aupadhyay.classandloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aupadhyay on 7/26/16.
 */

public class BookAdapter extends ArrayAdapter<BookBean> {

    Context context;
    int resource;
    ArrayList<BookBean> bookList;

    public BookAdapter(Context context, int resource, ArrayList<BookBean> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        bookList = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem;

        listItem = LayoutInflater.from(context).inflate(resource, parent, false);
        TextView textName = (TextView) listItem.findViewById(R.id.textViewName);
        TextView textAuthor = (TextView) listItem.findViewById(R.id.textViewAuthor);
        TextView textPrice = (TextView) listItem.findViewById(R.id.textViewPrice);

        BookBean bb = bookList.get(position);
        textName.setText(bb.getName());
        textAuthor.setText(bb.getAuthor());
        textPrice.setText(bb.getPrice());

        return listItem;

    }
}
