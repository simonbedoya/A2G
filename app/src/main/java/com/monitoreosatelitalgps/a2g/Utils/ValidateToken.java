package com.monitoreosatelitalgps.a2g.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.monitoreosatelitalgps.a2g.Api.RetrofitSingleton;
import com.monitoreosatelitalgps.a2g.Models.Authenticate;
import com.monitoreosatelitalgps.a2g.Utils.Interface.ValidateTokenInterface;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.monitoreosatelitalgps.a2g.Utils.Constants.tags.TAG_MAP;

/**
 * Created by sbv23 on 15/07/2017.
 */

public class ValidateToken {

    private ValidateTokenInterface validateTokenInterface;
    private ProgressDialog progressDialog;
    private Activity activity;
    private String username, token;
    private int code;

    public void setValidateTokenInterface(ValidateTokenInterface validateTokenInterface){
        this.validateTokenInterface = validateTokenInterface;
    }

    public void validateExpireToken(Activity activity, int code){
        this.activity = activity;
        this.code = code;
        SharedPreferences prefs = activity.getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        token = prefs.getString("TOKEN","");
        username = prefs.getString("USERNAME","");
        String password = prefs.getString("PASS","");
        int expire_token = prefs.getInt("EXPIRE_TOKEN",0);
        Long date_token = prefs.getLong("DATE_TOKEN",0);
        if(token.equals("") && username.equals("")){
            validateTokenInterface.errorWithDataUser();
        }

        Calendar date_exp = Calendar.getInstance();
        date_exp.setTimeInMillis(date_token);
        date_exp.add(Calendar.MILLISECOND,(expire_token*1000)-30000);

        if(Calendar.getInstance().before(date_exp)){
            validateTokenInterface.successValidateToken(code,username,token);
        }else{
           getToken(activity,username,password);
        }

    }

    private void getToken(Activity activity, String username, String password){
        HashMap<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);
        param.put("client_id", "mobile_app");
        param.put("grant_type", "password");

        Observable<Authenticate> auth = RetrofitSingleton.getApi(activity).getAuth(param);

        auth.subscribeOn(Schedulers.io())
            .doOnSubscribe(() -> {})
            .doOnCompleted(() -> {})
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::responseLogin,
                throwable -> Error.errControlLogin(throwable, TAG_MAP, activity.getCurrentFocus(),progressDialog), () -> Log.i(TAG_MAP, "succes"));
    }

    private void responseLogin(Authenticate authenticate) {
        saveData(authenticate, this.activity);
    }

    private void saveData(Authenticate authenticate, Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("dataUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("TOKEN",authenticate.getAccess_token());
        editor.putLong("DATE_TOKEN",Calendar.getInstance().getTimeInMillis());
        editor.apply();
        editor.commit();

        validateTokenInterface.successValidateToken(code,username,authenticate.getAccess_token());
    }
}
