package com.monitoreosatelitalgps.a2g.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.monitoreosatelitalgps.a2g.Activity.LoginActivity;
import com.monitoreosatelitalgps.a2g.Activity.MainActivity;

public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("dataUser", Context.MODE_PRIVATE);

        Boolean session = prefs.getBoolean("REMEMBER",false);

        Intent intent;
        if(session){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

}
