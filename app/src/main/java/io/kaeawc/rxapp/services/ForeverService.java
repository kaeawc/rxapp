package io.kaeawc.rxapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ForeverService extends Service {

    public static boolean RUNNING = false;
    private final IBinder mBinder = new LocalBinder();
    private static ForeverService sInstance = null;

    public static boolean isInstanceCreated() {
        return sInstance != null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUp();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!RUNNING) {
            setUp();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        sInstance = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setUp() {
        sInstance = this;
        RUNNING = true;
    }

    public class LocalBinder extends Binder {

        @SuppressWarnings("unused")
        public ForeverService getService() {
            return ForeverService.this;
        }
    }

}
