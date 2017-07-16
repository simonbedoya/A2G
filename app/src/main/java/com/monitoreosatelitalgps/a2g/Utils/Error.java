package com.monitoreosatelitalgps.a2g.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.IDNA;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.monitoreosatelitalgps.a2g.Fragment.Interface.ErrorInterface;
import com.monitoreosatelitalgps.a2g.Fragment.Interface.MapInterface;
import com.monitoreosatelitalgps.a2g.Fragment.Interface.ProfileErrorInterface;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by sbv23 on 5/05/2017.
 */

public class Error {

    private static ErrorInterface errorInterface;
    private static ProfileErrorInterface profileErrorInterface;

    public static void setErrorInterface(ErrorInterface errorInterface){
        Error.errorInterface = errorInterface;
    }

    public static void setProfileErrorInterface(ProfileErrorInterface profileErrorInterface){
        Error.profileErrorInterface = profileErrorInterface;
    }

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

    public static void errControlMakers(@NonNull Throwable throwable, String tag, View view, ProgressDialog progressDialog) {
        progressDialog.hide();

        Log.e(tag,"ErrorControlMakers",throwable);
        if (throwable instanceof HttpException) {
            // We had non-2XX http error
            if(((HttpException) throwable).code() == 500){
                Snackbar.make(view,"Error intente nuevamente",Snackbar.LENGTH_SHORT).show();
            }else if(((HttpException) throwable).code() == 401){
                errorInterface.getTokenMap();
                errorInterface.loadMarkersOnMap();

            }

        }
        if (throwable instanceof IOException) {
            // A network or conversion error happened

        }
    }

    public static void errControlInfoPerson(@NonNull Throwable throwable, String tag, View view, ProgressDialog progressDialog) {
        progressDialog.hide();

        Log.e(tag,"ErrorControlMakers",throwable);
        if (throwable instanceof HttpException) {
            // We had non-2XX http error
            if(((HttpException) throwable).code() == 500){
                Snackbar.make(view,"Error intente nuevamente",Snackbar.LENGTH_SHORT).show();
            }else if(((HttpException) throwable).code() == 401){
                profileErrorInterface.getTokenProfile();
                profileErrorInterface.loadProfile();

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

    public static void errControlLogin(@NonNull Throwable throwable, String tag, View view, ProgressDialog progressDialog) {
        progressDialog.hide();
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
}
