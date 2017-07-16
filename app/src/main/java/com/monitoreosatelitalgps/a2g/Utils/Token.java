package com.monitoreosatelitalgps.a2g.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.monitoreosatelitalgps.a2g.Activity.MainActivity;
import com.monitoreosatelitalgps.a2g.Api.RetrofitSingleton;
import com.monitoreosatelitalgps.a2g.Models.Authenticate;
import com.monitoreosatelitalgps.a2g.Models.DataToken;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.monitoreosatelitalgps.a2g.Utils.Constants.tags.TAG_MAP;

/**
 * Created by sbv23 on 4/07/2017.
 */

public class Token {

    private static Activity activity;

    public static void getToken(Activity activity, View view, ProgressDialog progressDialog) {

        Token.activity = activity;
        SharedPreferences prefs = activity.getSharedPreferences("dataUser", Context.MODE_PRIVATE);

        String username = prefs.getString("USERNAME","");
        String password = prefs.getString("PASS","");
        if(password.equals("") && username.equals("")){
            return;
        }

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
            .subscribe(Token::responseLogin,
                throwable -> Error.errControlMakers(throwable,TAG_MAP,view,progressDialog), () -> Log.i(TAG_MAP, "succes"));
    }

    public static void responseLogin(Authenticate authenticate) {
        saveData(authenticate, Token.activity);
    }

    private static void saveData(Authenticate authenticate, Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("dataUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("TOKEN",authenticate.getAccess_token());
        editor.apply();
    }
}
