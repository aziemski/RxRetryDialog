package com.example.timber;

import timber.log.Timber;

public class DebugTreeWithThreadInfo extends Timber.DebugTree {

  @Override public void v(String message, Object... args) {
    super.v(addThreadInfo(message), args);
  }

  @Override public void v(Throwable t, String message, Object... args) {
    super.v(t, addThreadInfo(message), args);
  }

  @Override public void d(String message, Object... args) {
    super.d(addThreadInfo(message), args);
  }

  @Override public void d(Throwable t, String message, Object... args) {
    super.d(t, addThreadInfo(message), args);
  }

  @Override public void i(String message, Object... args) {
    super.i(addThreadInfo(message), args);
  }

  @Override public void w(String message, Object... args) {
    super.w(addThreadInfo(message), args);
  }

  @Override public void w(Throwable t, String message, Object... args) {
    super.w(t, addThreadInfo(message), args);
  }

  @Override public void e(String message, Object... args) {
    super.e(addThreadInfo(message), args);
  }

  @Override public void e(Throwable t, String message, Object... args) {
    super.e(t, addThreadInfo(message), args);
  }

  @Override public void i(Throwable t, String message, Object... args) {
    super.i(t, addThreadInfo(message), args);
  }

  private String addThreadInfo(String message) {
    return "[" + Thread.currentThread().getName() + "] " + message;
  }
}
