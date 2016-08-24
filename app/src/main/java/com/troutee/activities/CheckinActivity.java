package com.troutee.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.troutee.R;
import com.troutee.adapters.ClientCursorAdapter;
import com.troutee.mappers.ClientMapper;
import com.troutee.providers.TrouteeDBHelper;

import java.util.List;

public class CheckinActivity extends AppCompatActivity {

    private AutoCompleteTextView actv;
    private ListView clients;
    private ClientCursorAdapter clientCursorAdapter;
    private TrouteeDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.lbl__checkin));
        dbHelper=new TrouteeDBHelper(this);
        processListView(dbHelper);
        processAutoComplete(dbHelper);
    }

    protected void onDestroy(){
        super.onDestroy();
        Cursor oldCursor = clientCursorAdapter.swapCursor(null);

        if(oldCursor != null)
            oldCursor.close();
    }

    private void processListView(final TrouteeDBHelper dbHelper){
        clients= (ListView) findViewById(R.id.checkin_activity_client_list);
        clients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position+" ITEM ID:"+id, Toast.LENGTH_LONG)
                        .show();
            }
        });



        Cursor clientsCursor= dbHelper.getAllClients();
        clientCursorAdapter = new ClientCursorAdapter(this,clientsCursor,0);
        clients.setAdapter(clientCursorAdapter);


    }

    private void processAutoComplete(final TrouteeDBHelper dbHelper){
        actv= (AutoCompleteTextView) findViewById(R.id.checkin_activity_filter_etxt);
        Cursor suggestionsCursor= dbHelper.getAllClientNamesAndCodes();
         List<String>suggestions=ClientMapper.getClientSuggestions(suggestionsCursor);
        if(actv!=null && suggestions!=null && !suggestions.isEmpty()){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,suggestions);
            actv.setAdapter(adapter);
           actv.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {
                   Cursor newCursor = null;
                    if(s == null){
                        newCursor= dbHelper.getAllClients();
                    }else{
                        newCursor=dbHelper.getAllClientsFiltered(s.toString());
                    }
                   swapCursor(newCursor);
               }

               @Override
               public void afterTextChanged(Editable s) {

               }
           });
        }
    }


    private void swapCursor(Cursor newCursor){
        Cursor oldCursor = clientCursorAdapter.swapCursor(newCursor);
        if(oldCursor != null) {
            oldCursor.close();
        }
        clientCursorAdapter.notifyDataSetChanged();
    }
}
