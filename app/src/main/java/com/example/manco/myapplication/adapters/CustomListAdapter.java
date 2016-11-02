package com.example.manco.myapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.manco.myapplication.R;

/**
 * Created by Marjan Sherdenkovski
 *
 * This adapter is works simular like a SimpleCursorAdapter just
 * also can put the right image in the image view component
 */
public class CustomListAdapter extends SimpleCursorAdapter {

    Context context;
    int layout;

    public CustomListAdapter(Context ctx, int layout, Cursor c, String[] from, int[] to) {
        super(ctx, layout, c, from, to);
        this.context = ctx;
        this.layout = layout;
    }

    @Override
    public View newView(Context ctx, Cursor cursor, ViewGroup parent) {

        Cursor mycursor = getCursor();

        final LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(layout, parent, false);

        int title = mycursor.getColumnIndex("title");
        int amount = mycursor.getColumnIndex("amount");
        int category = mycursor.getColumnIndex("category");

        String titletext = mycursor.getString(title);
        String amounttext = mycursor.getString(amount);
        String categorytext = mycursor.getString(category);

        TextView customTvTitle = (TextView) v.findViewById(R.id.customTvTitle);
        TextView customTvAmount = (TextView) v.findViewById(R.id.customTvAmount);
        if (customTvTitle != null) {
            customTvTitle.setText(titletext);
        }
        if (customTvAmount != null) {
            customTvAmount.setText(amounttext);
        }

        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        if (imageView != null) {

            if(categorytext.equals("Food")){
                imageView.setImageResource(R.drawable.food);
            }
            else if(categorytext.equals("Shopping")){
                imageView.setImageResource(R.drawable.shopping);
            }
            else if(categorytext.equals("Other")){
                imageView.setImageResource(R.drawable.other);
            }
            else if(categorytext.equals("credit")){
                imageView.setImageResource(R.drawable.credit);
            }
            else if(categorytext.equals("Salary")){
                imageView.setImageResource(R.drawable.salary);
            }
            else if(categorytext.equals("Travel")){
                imageView.setImageResource(R.drawable.travel);
            }
            else if(categorytext.equals("Party")){
                imageView.setImageResource(R.drawable.party);
            }
        }

        return v;
    }
}