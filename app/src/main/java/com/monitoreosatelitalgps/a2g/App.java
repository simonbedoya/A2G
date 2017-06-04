package com.monitoreosatelitalgps.a2g;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by home on 5/2/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
                .setNotificationOpenedHandler(new NotificationRecived())
                .autoPromptLocation(true)
                .init();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Subscribe
    public void onEvent(String json){

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private class NotificationRecived implements OneSignal.NotificationOpenedHandler, OneSignal.NotificationReceivedHandler {

        @Override
        public void notificationReceived(OSNotification notification) {
            Log.e("APP", "recived");
            /*try {
                if (!EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().register(this);
                }
                JSONObject data = notification.payload.additionalData;
                String vehiculoJson = (String) data.get("data");
                Log.e("APPMAIN",vehiculoJson);
                EventBus.getDefault().post(vehiculoJson);
            } catch (Exception e) {

            }*/
        }

        @Subscribe
        public void onEvent(String json){

        }

        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            Log.e("APP", "opened");
        }
    }
}
