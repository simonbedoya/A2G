package com.monitoreosatelitalgps.a2g.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.monitoreosatelitalgps.a2g.Activity.MainActivity;
import com.monitoreosatelitalgps.a2g.Adapter.RecyclerAdapter;
import com.monitoreosatelitalgps.a2g.Api.RetrofitSingleton;
import com.monitoreosatelitalgps.a2g.Fragment.Interface.MapInterface;
import com.monitoreosatelitalgps.a2g.Models.DailyReportVehicle;
import com.monitoreosatelitalgps.a2g.Models.DetailsVehicle;
import com.monitoreosatelitalgps.a2g.Models.Query;
import com.monitoreosatelitalgps.a2g.Models.QueryVehicleDetail;
import com.monitoreosatelitalgps.a2g.Models.VehiculoMap;
import com.monitoreosatelitalgps.a2g.R;
import com.monitoreosatelitalgps.a2g.Utils.Error;
import com.monitoreosatelitalgps.a2g.Utils.Interface.ValidateTokenInterface;
import com.monitoreosatelitalgps.a2g.Utils.ValidateToken;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.monitoreosatelitalgps.a2g.Utils.Constants.tags.TAG_MAP;
import static com.monitoreosatelitalgps.a2g.Utils.Utils.logout;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements RecyclerAdapter.OnItemClickListener, ValidateTokenInterface {

    @Bind(R.id.listVehicle) RecyclerView listVehicle;
    @Bind(R.id.panelDetailsVehicle) LinearLayout panelDetails;
    @Bind(R.id.scrollView)  ScrollView scrollView;
    @Bind(R.id.txtMEnc) TextView txtMEnc;
    @Bind(R.id.txtUApag) TextView txtUApag;
    @Bind(R.id.txtMaxVel) TextView txtMaxVel;
    @Bind(R.id.txtEVel) TextView txtEVel;
    @Bind(R.id.txtPRec) TextView txtPRec;
    @Bind(R.id.txtOdom) TextView txtOdom;
    @Bind(R.id.txtKMURec) TextView txtKMURec;
    @Bind(R.id.txtVelProm) TextView txtVelProm;
    @Bind(R.id.txtKMRec) TextView txtKMRec;
    @Bind(R.id.tatStateVehicle) TextView txtStateVehicle;
    @Bind(R.id.txtStateGPS) TextView txtStateGPS;
    @Bind(R.id.txtPosition) TextView txtPosition;
    @Bind(R.id.txtVel) TextView txtVel;
    @Bind(R.id.txtLongitude) TextView txtLongitude;
    @Bind(R.id.txtLatitude) TextView txtLatitude;
    @Bind(R.id.txtPlate) TextView txtPlate;
    @Bind(R.id.loadBasicData) RelativeLayout loadBasicData;
    @Bind(R.id.loadDailyReport) RelativeLayout loadDailyReport;
    @Bind(R.id.imgBtnDatail) ImageButton imgBtnDatail;
    @Bind(R.id.imgBtnDailyReport) ImageButton imgBtnDailyReport;
    //@Bind(R.id.linearSearchDetail) LinearLayout searchDetail;

    private MapInterface mapInterface;
    private RecyclerAdapter recyclerAdapter;
    private List<VehiculoMap> listVehicles = new ArrayList<>();
    private String placa;
    private QueryVehicleDetail queryVehicleDetail;
    private ValidateToken validateToken;

    public DetailFragment() {}

    public void setMapInterface(MapInterface mapInterface){
        this.mapInterface = mapInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        validateToken = new ValidateToken();
        validateToken.setValidateTokenInterface(this);
        recyclerAdapter = new RecyclerAdapter(getContext(),listVehicles);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        RecyclerView.Adapter adapter = recyclerAdapter;
        listVehicle.setLayoutManager(linearLayoutManager);
        listVehicle.setItemAnimator(new DefaultItemAnimator());
        listVehicle.setAdapter(adapter);
        recyclerAdapter.setOnItemClickListener(this);
        return view;
    }

    public int getState(){
        return scrollView.getVisibility();
    }

    public void setListVehiculos(List<VehiculoMap> listVehicles){
        this.listVehicles = listVehicles;
        //Toast.makeText(this.getActivity(),"Llego info vehiculos",Toast.LENGTH_LONG).show();
    }

    public void reloadList(){
        recyclerAdapter.setListVehicles(listVehicles);
        recyclerAdapter.notifyDataSetChanged();
    }

    public void changeViews(boolean state){
        if(state){
            listVehicle.setVisibility(View.INVISIBLE);
            panelDetails.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
            loadDataVehicle();

        }else{
            listVehicle.setVisibility(View.VISIBLE);
            panelDetails.setVisibility(View.INVISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        }
    }

    public void verifyChangeList(){
        if(scrollView.getVisibility() == View.VISIBLE && panelDetails.getVisibility() == View.VISIBLE){
            changeViews(false);
        }
    }

    protected void StartAnimations(LinearLayout linearLayout) {
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
        anim.reset();
        //LinearLayout ly = (LinearLayout) findViewById(R.id.ly_Ageo);
        linearLayout.clearAnimation();
        linearLayout.startAnimation(anim);
    }

    public void searchVehicle(String placa){
        List<VehiculoMap> listSearch = new ArrayList<>();
        for (VehiculoMap vehiculoMap : listVehicles){
            if(vehiculoMap.getPlk().contains(placa.toUpperCase())){
                listSearch.add(vehiculoMap);
            }
        }
        recyclerAdapter.setListVehicles(listSearch);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        this.placa = listVehicles.get(position).getPlk();
        loadDataVehicle();
        changeViews(true);
    }

    public void setPlaca(String placa){
        this.placa = placa.toUpperCase();
    }

    public void loadDataVehicle(){
        queryVehicleDetail = new QueryVehicleDetail();
        queryVehicleDetail.setPlate(this.placa);
        clearFileds();
        loadDataDetail();
        loadDataDaily();

    }

    private void loadDataDetail(){
        validateToken.validateExpireToken(getActivity(),0);
    }

    private void loadDataDaily(){
        validateToken.validateExpireToken(getActivity(),1);
    }

    private void loadPanel(RelativeLayout relativeLayout, Boolean active){
        if(active){
            relativeLayout.setVisibility(View.VISIBLE);
        }else{
            relativeLayout.setVisibility(View.INVISIBLE);
        }
        if(relativeLayout == loadBasicData){
            if(active) {
                imgBtnDatail.setClickable(false);
            }else{
                imgBtnDatail.setClickable(true);
            }
        }else{
            if(active) {
                imgBtnDailyReport.setClickable(false);
            }else{
                imgBtnDailyReport.setClickable(true);
            }
        }
    }


    private void setDetailsVehicle(DetailsVehicle detailsVehicle){
        if(detailsVehicle != null) {
            //Toast.makeText(getContext(), "LLego info detalles " + detailsVehicle.getCarStatus(), Toast.LENGTH_SHORT).show();
            txtPlate.setText(this.placa);
            txtLatitude.setText(String.valueOf(detailsVehicle.getLat()));
            txtLongitude.setText(String.valueOf(detailsVehicle.getLng()));
            txtVel.setText(String.valueOf(detailsVehicle.getSpd()));
            txtPosition.setText(String.valueOf(detailsVehicle.getTrk()));
            txtStateGPS.setText(detailsVehicle.getStatus());
            txtStateVehicle.setText(detailsVehicle.getCarStatus());
        }else{
            Snackbar.make(getView(),"No exiten datos basicos del vehiculo",Snackbar.LENGTH_LONG).show();
        }
    }

    private void setDailyReportVehicle(DailyReportVehicle dailyReportVehicle){
        if(dailyReportVehicle != null) {
            //Toast.makeText(getContext(), "LLego info reporte diario", Toast.LENGTH_SHORT).show();
            txtKMRec.setText(String.valueOf(dailyReportVehicle.getDailyTrack()));
            txtVelProm.setText(String.valueOf(dailyReportVehicle.getAverageSpeed()));
            txtKMURec.setText(String.valueOf(dailyReportVehicle.getLastTrip()));
            txtMEnc.setText(String.valueOf(dailyReportVehicle.getEngineOnTime()));
            txtUApag.setText(String.valueOf(dailyReportVehicle.getLastTurnOffFormated()));
            txtMaxVel.setText(String.valueOf(dailyReportVehicle.getMaxSpeed()));
            txtEVel.setText(String.valueOf(dailyReportVehicle.getNumberSpeeding()));
            txtPRec.setText(String.valueOf(dailyReportVehicle.getStartTimeFormated()));
            txtOdom.setText(String.valueOf(dailyReportVehicle.getOdometer()));
        }else{
            Snackbar.make(getView(),"No exiten datos en el reporte diario del vehiculo",Snackbar.LENGTH_LONG).show();
        }
    }

    private void clearFileds(){
        txtPlate.setText(this.placa);
        txtLatitude.setText("");
        txtLongitude.setText("");
        txtVel.setText("");
        txtPosition.setText("");
        txtStateGPS.setText("");
        txtStateVehicle.setText("");
        txtKMRec.setText("");
        txtVelProm.setText("");
        txtKMURec.setText("");
        txtMEnc.setText("");
        txtUApag.setText("");
        txtMaxVel.setText("");
        txtEVel.setText("");
        txtPRec.setText("");
        txtOdom.setText("");
    }

    @OnClick(R.id.imgBtnDatail)
    public void onClickImgBtnDetail(){
        //Toast.makeText(getContext(),"Se presiono reload 1",Toast.LENGTH_SHORT).show();
        QueryVehicleDetail query = new QueryVehicleDetail();
        query.setPlate(this.placa);
        loadDataDetail();
    }

    @OnClick(R.id.imgBtnDailyReport)
    public void onClickImgBtnDaily(){
        //Toast.makeText(getContext(),"Se presiono reload 2",Toast.LENGTH_SHORT).show();
        QueryVehicleDetail query = new QueryVehicleDetail();
        query.setPlate(this.placa);
        loadDataDaily();
    }

    @Override
    public void errorWithDataUser() {
        Snackbar.make(getView(), "Error obteniendo datos de sesión",Snackbar.LENGTH_INDEFINITE).setAction("Recargar",v -> logout(getActivity()));
    }

    @Override
    public void successValidateToken(int cod, String username, String token) {
        switch (cod){
            case 0:
                Observable<DetailsVehicle> detailsVehicle = RetrofitSingleton.getApi(this.getActivity()).getDatailsVehicle("Bearer "+token,queryVehicleDetail);
                //Observable<DetailsVehicle> detailsVehicle = RetrofitSingleton.getApi(this.getActivity()).getDatailsVehicle(query);

                detailsVehicle.subscribeOn(Schedulers.io())
                    .doOnSubscribe(()-> loadPanel(loadBasicData,true))
                    .doOnCompleted(()-> {})
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setDetailsVehicle,
                        throwable -> Error.errControl2(throwable,TAG_MAP,getView(),loadBasicData,imgBtnDatail,"detail"), () -> loadPanel(loadBasicData,false));
                break;
            case 1:
                Observable<DailyReportVehicle> dailyReportVehicle = RetrofitSingleton.getApi(this.getActivity()).getDailyReportVehicle("Bearer "+token,queryVehicleDetail);
                //Observable<DailyReportVehicle> dailyReportVehicle = RetrofitSingleton.getApi(this.getActivity()).getDailyReportVehicle(query);

                dailyReportVehicle.subscribeOn(Schedulers.io())
                    .doOnSubscribe(()->loadPanel(loadDailyReport,true))
                    .doOnCompleted(()->{})
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setDailyReportVehicle,
                        throwable -> Error.errControl2(throwable,TAG_MAP,getView(),loadDailyReport,imgBtnDailyReport,"daily"), () -> loadPanel(loadDailyReport,false));
                break;
        }
    }
}
