package com.troutee.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.troutee.R;
import com.troutee.providers.TrouteeContract;

/**
 * Created by vicente on 28/06/16.
 */
public class ClientCursorAdapter extends CursorAdapter {

    public ClientCursorAdapter(Context context, Cursor cursor,int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.client_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor!=null){
            TextView clientCode= (TextView) view.findViewById(R.id.code);
            TextView clientName = (TextView) view.findViewById(R.id.name);
            TextView clientPhone = (TextView) view.findViewById(R.id.phone);
            String cursorCode= cursor.getString(cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_CODE));
            String cursorName= cursor.getString(cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_NAME));
            String cursorphone= cursor.getString(cursor.getColumnIndexOrThrow(TrouteeContract.Client.COLUMN_PHONE));

            clientCode.setText(cursorCode);
            clientName.setText(cursorName);
            clientPhone.setText(cursorphone);
        }
    }
}
