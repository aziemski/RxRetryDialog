package com.example.api;

import android.os.SystemClock;

import com.example.SuccessOrFail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Observable;
import timber.log.Timber;

public class Api {

  private final SimpleDateFormat
      sdf =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
  private final SuccessOrFail successOrFail;

  public Api(SuccessOrFail successOrFail) {
    this.successOrFail = successOrFail;
  }

  public Observable<ApiResponse> getResponse() {
    return Observable.create(subscriber -> {
      try {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(response());
          subscriber.onCompleted();
        }

      } catch (Throwable throwable) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onError(throwable);
        }
      }
    });
  }

  private ApiResponse response() {
    boolean shouldFail = successOrFail.shouldFail();
    Timber.d("Getting response. Should fail: " + shouldFail);
    SystemClock.sleep(1000);
    if (shouldFail) {
      throw new UnknownError();
    }
    return new ApiResponse(message());
  }

  private String message() {
    return sdf.format(new Date());
  }

}
