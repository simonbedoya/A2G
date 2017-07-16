package com.monitoreosatelitalgps.a2g.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.monitoreosatelitalgps.a2g.Adapter.ViewPagerAdapter;
import com.monitoreosatelitalgps.a2g.Fragment.DetailFragment;
import com.monitoreosatelitalgps.a2g.Fragment.MapFragment;
import com.monitoreosatelitalgps.a2g.Fragment.Interface.MapInterface;
import com.monitoreosatelitalgps.a2g.Fragment.ProfileFragment;
import com.monitoreosatelitalgps.a2g.Models.VehiculoMap;
import com.monitoreosatelitalgps.a2g.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.onesignal.OneSignal;
import java.util.List;
import static com.monitoreosatelitalgps.a2g.Utils.Constants.tabs.*;

public class MainActivity extends AppCompatActivity implements MapInterface,
                                                                TabLayout.OnTabSelectedListener,
                                                                SearchView.OnClickListener,
                                                                SearchView.OnCloseListener,
                                                                SearchView.OnQueryTextListener{

  @Bind(R.id.pagerPcpal) ViewPager viewPager;
  @Bind(R.id.tabLayout) TabLayout tabLayout;
  @Bind(R.id.toolbar) Toolbar toolbar;
  private static String TAG = MainActivity.class.getSimpleName();
  private List<VehiculoMap> vehiculoMapList;
  private DetailFragment detailFragment;
  private MapFragment mapFragment;
  private ProfileFragment profileFragment;
  private boolean stateDetails = false;
  private Menu menu;
  private SearchView searchView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);


    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    detailFragment = new DetailFragment();
    mapFragment = new MapFragment();
    profileFragment = new ProfileFragment();
    mapFragment.setMapInterface(this);
    detailFragment.setMapInterface(this);
    viewPagerAdapter.addFragment(detailFragment, this.getString(R.string.TAB_DETAIL_NAME));
    viewPagerAdapter.addFragment(mapFragment, this.getString(R.string.TAB_MAP_NAME));
    viewPagerAdapter.addFragment(profileFragment, this.getString(R.string.TAB_PROFILE_NAME));
    viewPager.setAdapter(viewPagerAdapter);
    tabLayout.setupWithViewPager(viewPager);
    tabLayout.getTabAt(TAB_MAP).select();
    tabLayout.addOnTabSelectedListener(this);
    OneSignal.idsAvailable((userId, registrationId) -> Log.e(TAG, userId));

  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_bar, menu);

    searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    searchView.setQueryHint("Buscar");
    searchView.setOnSearchClickListener(this);
    searchView.setOnCloseListener(this);
    searchView.setOnQueryTextListener(this);
    this.toolbar.getMenu().getItem(1).setVisible(false);

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    Log.i("mainActivity", Integer.toString(item.getItemId()));
    int id = item.getItemId();
    switch (id){
      case R.id.search:
          if(tabLayout.getSelectedTabPosition() == TAB_DETAIL){

          }
        break;
      case R.id.log_out:
          logout();
        break;
        case R.id.edit:
            profileFragment.activeFields();
            break;
    }
    return false;
  }


  private void logout(){
      SharedPreferences prefs = getSharedPreferences("dataUser", Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = prefs.edit();
      editor.clear();
      editor.apply();
      Intent intent = new Intent(this,RootActivity.class);
      startActivity(intent);
      finish();
  }

  @Override
  public void setTab(int tab) {
    tabLayout.getTabAt(tab).select();
  }

  @Override
  public void setPlaca(String placa) {
      detailFragment.setPlaca(placa);
    //Toast.makeText(this,"Seleccion placa " + placa,Toast.LENGTH_LONG).show();
  }

  @Override
  public void setListVehiculos(List<VehiculoMap> vehiculoMapList) {
    this.vehiculoMapList = vehiculoMapList;
    detailFragment.setListVehiculos(vehiculoMapList);
  }

    @Override
    public void viewDetails(boolean state) {
        this.stateDetails = state;
        detailFragment.changeViews(state);
    }

    @Override
    public void closeViewSearch(boolean state) {
        if(state){
            if(!searchView.isIconified()){
                searchView.setIconified(true);
            }
            if(!searchView.isIconified()){
                searchView.setIconified(true);
            }
        }else{
            searchView.setIconified(false);
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.e("tab",Integer.toString(tab.getPosition()));
        if(tab.getPosition() == TAB_DETAIL){
            closeSearchView();
            detailFragment.reloadList();
            detailFragment.changeViews(stateDetails);
            this.toolbar.getMenu().getItem(0).setVisible(true);
            this.toolbar.getMenu().getItem(1).setVisible(false);
        }else if(tab.getPosition() == TAB_MAP){
            closeSearchView();
            this.stateDetails = false;
            this.toolbar.getMenu().getItem(0).setVisible(true);
            this.toolbar.getMenu().getItem(1).setVisible(false);
            mapFragment.reloadMap();
        }else if(tab.getPosition() == TAB_PROFILE){
            closeSearchView();
            this.toolbar.getMenu().getItem(0).setVisible(false);
            this.toolbar.getMenu().getItem(1).setVisible(true);
            this.stateDetails = false;
            profileFragment.loadInfoPerson();
            profileFragment.disable_fields();
        }
    }

    private void closeSearchView(){
        if(!searchView.isIconified()){
            searchView.setIconified(true);
        }
        if(!searchView.isIconified()){
            searchView.setIconified(true);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        if(tab.getPosition() == TAB_MAP){
            mapFragment.stopReloadMap();
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

  @Override
  public boolean onClose() {
    this.menu.getItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    this.menu.getItem(1).setVisible(true);
    return false;
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
      if(tabLayout.getSelectedTabPosition() == TAB_MAP) {
          mapFragment.searchVehicle(query);
      }else if(tabLayout.getSelectedTabPosition() == TAB_DETAIL){
          detailFragment.searchVehicle(query);
      }
      return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
      if(tabLayout.getSelectedTabPosition() == TAB_DETAIL){
          detailFragment.searchVehicle(newText);
      }
      return false;
  }

  @Override
  public void onClick(View view) {
    this.menu.getItem(1).setVisible(false);
  }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if(tabLayout.getSelectedTabPosition() == TAB_DETAIL && detailFragment.getState() == View.VISIBLE){
                detailFragment.verifyChangeList();
                return false;
            }
        }

        return super.onKeyDown(keyCode,event);
    }
}
