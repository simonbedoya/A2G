package com.monitoreosatelitalgps.a2g.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.monitoreosatelitalgps.a2g.Activity.RootActivity;

/**
 * Created by sbv23 on 15/07/2017.
 */

public class Utils {

    public static void logout(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("dataUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(activity,RootActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
