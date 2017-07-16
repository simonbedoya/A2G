package com.monitoreosatelitalgps.a2g.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.monitoreosatelitalgps.a2g.Api.RetrofitSingleton;
import com.monitoreosatelitalgps.a2g.Fragment.Interface.ErrorInterface;
import com.monitoreosatelitalgps.a2g.Fragment.Interface.ProfileErrorInterface;
import com.monitoreosatelitalgps.a2g.Models.InfoPerson;
import com.monitoreosatelitalgps.a2g.R;
import com.monitoreosatelitalgps.a2g.Utils.Error;
import com.monitoreosatelitalgps.a2g.Utils.Token;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


import static com.monitoreosatelitalgps.a2g.Utils.Constants.tags.TAG_MAP;
import static com.monitoreosatelitalgps.a2g.Utils.Constants.url.URL_GET_INFO;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ProfileErrorInterface {

    @Bind(R.id.txt_name)    EditText txt_name;
    @Bind(R.id.txt_lastname) EditText txt_lastname;
    @Bind(R.id.txt_username) EditText txt_username;
    @Bind(R.id.txt_email) EditText txt_email;
    @Bind(R.id.txt_phone) EditText txt_phone;
    @Bind(R.id.save_person) Button btn_save;
    @Bind(R.id.cancel_person) Button btn_cancel;

    private ProgressDialog loadBasicData;
    private InfoPerson infoPerson;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);
        Error.setProfileErrorInterface(this);

        return v;
    }

    public void loadInfoPerson(){

        SharedPreferences prefs = getActivity().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        String token = prefs.getString("TOKEN","");
        String username = prefs.getString("USERNAME","");
        if(token.equals("") && username.equals("")){
            return;
        }

        Observable<InfoPerson> infoPerson = RetrofitSingleton.getApi(this.getActivity()).getInfoPerson(URL_GET_INFO + username,"Bearer "+token);


        infoPerson.subscribeOn(Schedulers.io())
            .doOnSubscribe(()-> {})
            .doOnCompleted(()-> {})
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::setInfoPerson,
                throwable -> Error.errControlInfoPerson(throwable,TAG_MAP,getView(),loadBasicData), () -> {});
    }

    private void setInfoPerson(InfoPerson infoPerson){
        Log.e("infoPerson",infoPerson.toString());
        this.infoPerson = infoPerson;
        txt_name.setText(infoPerson.getFirstName());
        txt_lastname.setText(infoPerson.getLastName());
        txt_username.setText(infoPerson.getUsername());
        txt_email.setText(infoPerson.getEmail());
        txt_phone.setText(infoPerson.getPhoneNumber());
    }

    public void activeFields(){
        if(!txt_name.isEnabled()){
            txt_name.setEnabled(true);
            txt_lastname.setEnabled(true);
            txt_email.setEnabled(true);
            txt_phone.setEnabled(true);
            btn_save.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
        }else{
            setInfoPerson(this.infoPerson);
            txt_name.setEnabled(false);
            txt_lastname.setEnabled(false);
            txt_email.setEnabled(false);
            txt_phone.setEnabled(false);
            btn_save.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.save_person)
    public void savePerson(){
        InfoPerson infoP = new InfoPerson();
        infoP.setFirstName(txt_name.getText().toString().trim());
        infoP.setLastName(txt_lastname.getText().toString().trim());
        infoP.setEmail(txt_email.getText().toString().trim());
        infoP.setPhoneNumber(txt_phone.getText().toString().trim());
        infoP.setUsername(this.infoPerson.getUsername());
        infoP.setEnabled(this.infoPerson.isEnabled());
        infoP.setId(this.infoPerson.getId());

        SharedPreferences prefs = getActivity().getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        String token = prefs.getString("TOKEN","");
        String username = prefs.getString("USERNAME","");
        if(token.equals("") && username.equals("")){
            return;
        }

        Observable<InfoPerson> updateInfo = RetrofitSingleton.getApi(this.getActivity()).updateInfoPerson("Bearer "+token, infoP);

        updateInfo.subscribeOn(Schedulers.io())
            .doOnSubscribe(()-> {})
            .doOnCompleted(()-> {})
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::info,
            throwable -> Error.errControlInfoPerson(throwable,TAG_MAP,getView(),loadBasicData), () -> {});



    }

    @OnClick(R.id.cancel_person)
    public void cancelPerson(){
        activeFields();
    }

    private void info(InfoPerson infoPerson){
        this.infoPerson = infoPerson;
        txt_name.setText(infoPerson.getFirstName());
        txt_lastname.setText(infoPerson.getLastName());
        txt_username.setText(infoPerson.getUsername());
        txt_email.setText(infoPerson.getEmail());
        txt_phone.setText(infoPerson.getPhoneNumber());
        disable_fields();

    }

    public void disable_fields(){
        txt_name.setEnabled(false);
        txt_lastname.setEnabled(false);
        txt_email.setEnabled(false);
        txt_phone.setEnabled(false);
        btn_save.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
    }

    @Override
    public void getTokenProfile() {
        Token.getToken(getActivity(),getView(),loadBasicData);
    }

    @Override
    public void loadProfile() {
        loadInfoPerson();
    }

    @Override
    public void updateInfo() {

    }
}
