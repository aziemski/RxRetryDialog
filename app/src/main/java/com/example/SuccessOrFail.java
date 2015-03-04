package com.example;

public final class SuccessOrFail {

  private boolean shouldFail;

  public synchronized boolean shouldFail() {
    return shouldFail;
  }

  public synchronized void toggle() {
    shouldFail = !shouldFail;
  }

}
