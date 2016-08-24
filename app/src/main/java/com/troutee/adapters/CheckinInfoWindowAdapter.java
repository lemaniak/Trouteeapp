package com.troutee.adapters;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.troutee.R;
import com.troutee.activities.RegisterClientLocation;
import com.troutee.activities.TrouteeActivity;

/**
 * Created by vicente on 08/07/16.
 */
public class CheckinInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private TrouteeActivity activity;
    private TextView clientname;

    public CheckinInfoWindowAdapter(TrouteeActivity activity) {
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    // Defines the contents of the InfoWindow
    @Override
    public View getInfoContents(Marker marker) {
        View v = activity.getLayoutInflater().inflate(R.layout.troute_checkin_info, null);
        clientname = (TextView) v.findViewById(R.id.troutee_cki_client_name);
        if(activity.getxClient()!=null){
            if(marker.getId().compareTo(activity.getCurrentLocationMarker().getId())==0){
                return null;
            }else{
                clientname.setText(activity.getxClient().getName());
            }
        }else{
            return null;
        }
        return v;

    }

}
