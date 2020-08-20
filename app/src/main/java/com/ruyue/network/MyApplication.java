package com.ruyue.network;

import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private static LocalDataSource localDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        localDataSource = Room.databaseBuilder(getApplicationContext(),
                LocalDataSource.class,
                "person").build();
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public LocalDataSource getLocalDataSource() {
        return localDataSource;
    }
}
