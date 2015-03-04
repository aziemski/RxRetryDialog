package com.example;

import android.app.Application;

import com.example.timber.DebugTreeWithThreadInfo;

import timber.log.Timber;

public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();
    Timber.plant(new DebugTreeWithThreadInfo());
  }
}
