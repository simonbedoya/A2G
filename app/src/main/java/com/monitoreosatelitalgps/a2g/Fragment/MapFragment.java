package com.monitoreosatelitalgps.a2g.Fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.monitoreosatelitalgps.a2g.Api.RetrofitSingleton;
import com.monitoreosatelitalgps.a2g.Fragment.Interface.ErrorInterface;
import com.monitoreosatelitalgps.a2g.Fragment.Interface.MapInterface;
import com.monitoreosatelitalgps.a2g.Models.Query;
import com.monitoreosatelitalgps.a2g.Models.VehiculoMap;
import com.monitoreosatelitalgps.a2g.R;
import com.monitoreosatelitalgps.a2g.Utils.CustomInfoWindow;
import com.monitoreosatelitalgps.a2g.Utils.Error;
import com.monitoreosatelitalgps.a2g.Utils.Interface.ValidateTokenInterface;
import com.monitoreosatelitalgps.a2g.Utils.PermissionUtils;
import com.monitoreosatelitalgps.a2g.Utils.ValidateToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import static com.monitoreosatelitalgps.a2g.Utils.Constants.tabs.*;
import static com.monitoreosatelitalgps.a2g.Utils.Constants.tags.*;
import static com.monitoreosatelitalgps.a2g.Utils.Utils.logout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,
                                                     ActivityCompat.OnRequestPermissionsResultCallback,
                                                     GoogleApiClient.ConnectionCallbacks,
                                                     GoogleApiClient.OnConnectionFailedListener,
                                                     GoogleMap.OnInfoWindowClickListener,
                                                     GoogleMap.OnMarkerClickListener,
                                                     GoogleMap.OnMyLocationButtonClickListener,
                                                     ErrorInterface, ValidateTokenInterface{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private MapInterface mapInterface;
    private Timer timer;
    private List<Marker> markerList;
    private ProgressDialog loadCars;
    private ValidateToken validateToken;

    public MapFragment() {
        // Required empty public constructor


    }

    public void setMapInterface(MapInterface mapInterface){
        this.mapInterface = mapInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        configProgressLoad();
        validateToken = new ValidateToken();
        validateToken.setValidateTokenInterface(this);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        return v;
    }

    private void configProgressLoad(){
        loadCars = new ProgressDialog(getContext());
        loadCars.setTitle("Vehiculos");
        loadCars.setMessage("Cargando...");
        loadCars.setCancelable(false);
    }


    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        GoogleMapOptions options = new GoogleMapOptions();

        options.compassEnabled(true).tiltGesturesEnabled(true).ambientEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        updateMyLocation();
        reloadMap();
        CustomInfoWindow  customInfoWindow = new CustomInfoWindow(this.getActivity());
        googleMap.setInfoWindowAdapter(customInfoWindow);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMyLocationButtonClickListener(this);
    }

    private boolean checkReady() {
        if (googleMap == null) {
            Toast.makeText(getActivity(), R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateMyLocation() {
        if (!checkReady()) {
            return;
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    private void setMakerMap(List<VehiculoMap> vehiculoMaps){
        mapInterface.setListVehiculos(vehiculoMaps);
        googleMap.clear();
        markerList = new ArrayList<>();
        Marker marker;
        for(VehiculoMap vehiculoMap: vehiculoMaps){
            int icon = selectIconMarker(vehiculoMap);
            marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(vehiculoMap.getLat(), vehiculoMap.getLng()))
                            .title(vehiculoMap.getPlk())
                            .rotation(vehiculoMap.getTrk())
                            .icon(BitmapDescriptorFactory.fromResource(icon)));

            markerList.add(marker);
        }

    }

    public int selectIconMarker(VehiculoMap vehiculoMap){
        int icon;
        if(vehiculoMap.getCodeCarStatus().endsWith("PAN")){
            icon = R.drawable.ic_veh_problem;
        }else if(vehiculoMap.getCodeDeviceStatus().equals("NOR") || vehiculoMap.getCodeDeviceStatus().equals("ERR")){
            icon = R.drawable.ic_veh_wifi;
        }else if(vehiculoMap.getCodeDeviceStatus().equals("ACT") && vehiculoMap.getCodeCarStatus().equals("ING") && vehiculoMap.getTrk() == 0.0){
            icon = R.drawable.ic_vehr_rem_circle;
        }else if(vehiculoMap.getCodeDeviceStatus().equals("ACT") && vehiculoMap.getCodeCarStatus().equals("OFF")){
            icon = R.drawable.ic_veh_gps_off;
        }else{
            icon = R.drawable.ic_veh;
        }
        return  icon;
    }

    public void reloadMap() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                validateToken.validateExpireToken(getActivity(),0);
                //loadMarkerMap();
            }
        }, 0, 60000);
    }

    public void stopReloadMap(){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, results,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
        } else {
            Boolean mShowPermissionDeniedDialog = true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
       // mMapView.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
      //  mMapView.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLasLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLasLocation != null){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLasLocation.getLatitude(), mLasLocation.getLongitude()),10));
        }else{
            Toast.makeText(getActivity(), "Ubicación no encontrada", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        mapInterface.setTab(TAB_DETAIL);
        mapInterface.setPlaca(marker.getTitle());
        mapInterface.viewDetails(true);
        marker.hideInfoWindow();
        //mapInterface.setListVehiculos(vehiculoMapList);
    }

    public void searchVehicle(String placa){
        for(Marker marker : markerList){
            if(marker.getTitle().equals(placa.toUpperCase())){
                marker.showInfoWindow();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),Float.valueOf("16.0")));
                mapInterface.closeViewSearch(true);
                return;
            }
        }
        Snackbar.make(getView(),"No se encuentra el vehiculo con placa " + placa.toUpperCase(),Snackbar.LENGTH_SHORT).show();
        mapInterface.closeViewSearch(true);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        zoomMarker(marker);
        return false;
    }

    private void zoomMarker(Marker marker){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),Float.valueOf("16.0")));
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Log.e("token","click en my ubicacion");
        validateToken.validateExpireToken(getActivity(),0);
        //loadMarkerMap();();
        return false;
    }

    @Override
    public void getTokenMap() {
        Log.e("getToken","getToken");
        //Token.getToken(getActivity(),getView(),loadCars);
        validateToken.validateExpireToken(getActivity(),0);
    }

    @Override
    public void loadMarkersOnMap() {
        validateToken.validateExpireToken(getActivity(),0);
        //loadMarkerMap();
    }

    @Override
    public void errorWithDataUser() {
        Snackbar.make(getView(), "Error obteniendo datos de sesión",Snackbar.LENGTH_INDEFINITE).setAction("Recargar",v -> logout(getActivity()));
    }

    @Override
    public void successValidateToken(int cod, String username, String token) {
        Query query = new Query();
        query.setUsername(username);
        Observable<List<VehiculoMap>> vehiculosMap = RetrofitSingleton.getApi(this.getActivity()).getVehiculoMap("Bearer "+token,query);
        //Observable<List<VehiculoMap>> vehiculosMap = RetrofitSingleton.getApi(this.getActivity()).getVehiculoMap(query);

        vehiculosMap.subscribeOn(Schedulers.io())
            .doOnSubscribe(()->{})
            .doOnCompleted(()->{})
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::setMakerMap,
                throwable -> Error.errControlMakers(throwable,TAG_MAP,getView(),loadCars), () -> Log.i(TAG_MAP, "succes"));
    }

}
