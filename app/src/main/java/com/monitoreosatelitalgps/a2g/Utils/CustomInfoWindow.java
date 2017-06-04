package com.monitoreosatelitalgps.a2g.Utils;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.monitoreosatelitalgps.a2g.R;

/**
 * Created by sbv23 on 10/05/2017.
 */

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;


    public CustomInfoWindow(Activity activity) {
        this.mWindow = activity.getLayoutInflater().inflate(R.layout.marker_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }

    private void render(Marker marker, View view){
        String placa = marker.getTitle();
        TextView placaUI = ((TextView) view.findViewById(R.id.txtNumberPlaca));
        placaUI.setText(placa);
    }
}
