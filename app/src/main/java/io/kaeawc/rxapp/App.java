package io.kaeawc.rxapp;

import android.app.Application;

import timber.log.Timber;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Debug and Verbose levels should be turned on unless this is a production release.
        Timber.plant(new Timber.DebugTree());
    }
}
