package com.example.myoga.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Observable;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (checkInternet(context)) {
            DatabaseConnection.loadData(context);
        } else {
            Toast.makeText(context, "No Network connection found", Toast.LENGTH_SHORT).show();
        }
        getObservable().connectionChanged();
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    public static class NetworkObservable extends Observable {
        private static NetworkObservable instance = null;

        private NetworkObservable() {
            // Exist to defeat instantiation.
        }

        public void connectionChanged() {
            setChanged();
            notifyObservers();
        }

        public static NetworkObservable getInstance() {
            if (instance == null) {
                instance = new NetworkObservable();
            }
            return instance;
        }
    }

    public static NetworkObservable getObservable() {
        return NetworkObservable.getInstance();
    }
}
