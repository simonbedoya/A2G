package com.monitoreosatelitalgps.a2g.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by sbv23 on 5/05/2017.
 */

public class Error {

    public static void errControl(@NonNull Throwable throwable, String tag, View view) {
        Snackbar.make(view,"Error intente nuevamente",Snackbar.LENGTH_SHORT).show();
        Log.e(tag,throwable.getMessage(),throwable);
        if (throwable instanceof HttpException) {
            // We had non-2XX http error
            if(((HttpException) throwable).code() == 500){

            }

        }
        if (throwable instanceof IOException) {
            // A network or conversion error happened

        }
    }

    public static void errControl2(@NonNull Throwable throwable, String tag, View view, RelativeLayout relativeLayout, ImageButton imageButton, String type) {

        relativeLayout.setVisibility(View.INVISIBLE);
        imageButton.setClickable(true);
        if(type.equals("detail")){
            Snackbar.make(view,"Error con datos basicos, intente nuevamente",Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(view,"Error con datos de reporte diario, intente nuevamente",Snackbar.LENGTH_SHORT).show();
        }
        Log.e(tag,throwable.getMessage(),throwable);
        if (throwable instanceof HttpException) {
            // We had non-2XX http error
            if(((HttpException) throwable).code() == 500){

            }

        }
        if (throwable instanceof IOException) {
            // A network or conversion error happened

        }
    }
}
