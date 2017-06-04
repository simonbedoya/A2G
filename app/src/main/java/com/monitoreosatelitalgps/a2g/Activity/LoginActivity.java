package com.monitoreosatelitalgps.a2g.Activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.monitoreosatelitalgps.a2g.Api.RetrofitSingleton;
import com.monitoreosatelitalgps.a2g.Models.Authenticate;
import com.monitoreosatelitalgps.a2g.Models.DataToken;
import com.monitoreosatelitalgps.a2g.Models.Query;
import com.monitoreosatelitalgps.a2g.Models.VehiculoMap;
import com.monitoreosatelitalgps.a2g.R;
import com.monitoreosatelitalgps.a2g.Utils.Error;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.monitoreosatelitalgps.a2g.Utils.Constants.tags.TAG_MAP;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.login) Button login;
    @Bind(R.id.edtEmail) EditText email;
    @Bind(R.id.edtPassword) EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.login)
    public void onClickLogin(){
        getToken();
    }


    private void getToken() {
        HashMap<String, String> param = new HashMap<>();
        param.put("username", email.getText().toString().trim());
        param.put("password", password.getText().toString().trim());
        param.put("client_id", "mobile_app");
        param.put("grant_type", "password");

        Observable<Authenticate> auth = RetrofitSingleton.getApi(this).getAuth(param);

        auth.subscribeOn(Schedulers.io())
            .doOnSubscribe(() -> {
            })
            .doOnCompleted(() -> {
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::responseLogin,
                throwable -> Error.errControl(throwable, TAG_MAP, getCurrentFocus()), () -> Log.i(TAG_MAP, "succes"));
    }

    private void responseLogin(Authenticate authenticate) {
        Log.e("token auth", authenticate.toString());
        String[] data = authenticate.getAccess_token().split("\\.");
        String datab = null;
        try {
            datab = new String(Base64.decode(data[1].getBytes("UTF-8"), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        DataToken dataToken = gson.fromJson(datab, DataToken.class);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}
