package com.troutee.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.troutee.R;
import com.troutee.activities.RegisterClientLocation;

import org.w3c.dom.Text;

/**
 * Created by vicente on 08/07/16.
 */
public class TrouteeInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private RegisterClientLocation activity;
    private TextView clientname;

    public TrouteeInfoWindowAdapter(RegisterClientLocation activity) {
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    // Defines the contents of the InfoWindow
    @Override
    public View getInfoContents(Marker marker) {
        View v = activity.getLayoutInflater().inflate(R.layout.troute_custom_info, null);
        clientname = (TextView) v.findViewById(R.id.register_client_location_cki_client_name);
        clientname.setText(activity.getxClient().getName());
        return v;

    }

}
