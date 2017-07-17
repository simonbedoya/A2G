package com.monitoreosatelitalgps.a2g.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.google.gson.Gson;
import com.monitoreosatelitalgps.a2g.Api.RetrofitSingleton;
import com.monitoreosatelitalgps.a2g.Models.Authenticate;
import com.monitoreosatelitalgps.a2g.Models.DataToken;
import com.monitoreosatelitalgps.a2g.R;
import com.monitoreosatelitalgps.a2g.Utils.Error;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.monitoreosatelitalgps.a2g.Utils.Constants.tags.TAG_MAP;


public class LoginActivity extends AppCompatActivity implements EditText.OnEditorActionListener, AppCompatCheckBox.OnCheckedChangeListener{

    @Bind(R.id.login) Button login;
    @Bind(R.id.edtEmail) EditText email;
    @Bind(R.id.edtPassword) EditText password;
    @Bind(R.id.img) ImageView imgLogo;
    @Bind(R.id.linearInput) LinearLayout linearInput;
    @Bind(R.id.remember) AppCompatCheckBox remember;
    private ProgressDialog progressDialog;
    private Boolean rememberBol = false;
    private String s_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StartAnimations();
        configProgressDialog();
        password.setOnEditorActionListener(this);
        setConfigStateColoRemember();

    }

    private void setConfigStateColoRemember(){
        int[][] states = new int[][] {
            new int[] { android.R.attr.state_enabled}, // enabled
            new int[] {-android.R.attr.state_enabled}, // disabled
            new int[] {android.R.attr.state_checked}, //checked
            new int[] {-android.R.attr.state_checked}, // unchecked
            new int[] { android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[] {
            ContextCompat.getColor(this,R.color.colorPrimary),
            ContextCompat.getColor(this,R.color.colorPrimary),
            ContextCompat.getColor(this,R.color.colorPrimaryDark),
            ContextCompat.getColor(this,R.color.colorPrimary),
            ContextCompat.getColor(this,R.color.colorPrimaryLight)
        };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        remember.setSupportButtonTintList(colorStateList);
        remember.setOnCheckedChangeListener(this);
    }

    private void configProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Iniciando sesión");
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
    }

    @OnClick(R.id.login)
    public void onClickLogin(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(),0);
        getToken();
    }


    private void getToken() {
        progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        s_password = password.getText().toString().trim();
        param.put("username", email.getText().toString().trim());
        param.put("password", s_password);
        param.put("client_id", "mobile_app");
        param.put("grant_type", "password");

        Observable<Authenticate> auth = RetrofitSingleton.getApi(this).getAuth(param);

        auth.subscribeOn(Schedulers.io())
            .doOnSubscribe(() -> {})
            .doOnCompleted(() -> {})
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::responseLogin,
                throwable -> Error.errControlLogin(throwable, TAG_MAP, getCurrentFocus(),progressDialog), () -> Log.i(TAG_MAP, "succes"));
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
        saveData(dataToken,authenticate);

    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale2);
        anim.reset();
        imgLogo.clearAnimation();
        imgLogo.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim2.setStartOffset(1000);
        anim2.reset();
        linearInput.clearAnimation();
        linearInput.startAnimation(anim2);
        anim2.reset();
    }

    private void saveData(DataToken dataToken, Authenticate authenticate){
        SharedPreferences prefs = getSharedPreferences("dataUser",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("TOKEN",authenticate.getAccess_token());
        editor.putString("USERNAME", dataToken.getPreferred_username());
        editor.putString("PASS", s_password);
        editor.putString("NAME", dataToken.getName());
        editor.putString("EMAIL", dataToken.getEmail());
        editor.putString("LAST_NAME",dataToken.getFamily_name());
        editor.putBoolean("REMEMBER",rememberBol);
        editor.putInt("EXPIRE_TOKEN",authenticate.getExpires_in());
        editor.putLong("DATE_TOKEN", Calendar.getInstance().getTimeInMillis());
        editor.apply();

        progressDialog.hide();
        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);
        finish();
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(textView == password){
            if(i == EditorInfo.IME_ACTION_DONE){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(password.getWindowToken(),0);
                getToken();

            }
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        rememberBol = b;
    }
}
