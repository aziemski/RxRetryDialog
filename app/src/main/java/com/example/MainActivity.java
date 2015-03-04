package com.example;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.widget.Button;

import com.example.api.Api;
import com.example.api.ApiResponse;
import com.example.rxretrydialog.R;

import rx.Observable;
import rx.Observer;
import rx.android.AndroidSubscriptions;
import rx.android.app.RxActivity;
import rx.android.lifecycle.LifecycleObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.view.ViewObservable;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class MainActivity extends RxActivity {

  private Api api;
  private SuccessOrFail successOrFail;
  private Button shouldFailBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    successOrFail = new SuccessOrFail();
    api = new Api(successOrFail);

    shouldFailBtn = (Button) findViewById(R.id.shouldFailBtn);
    shouldFailBtn.setOnClickListener(v -> {
      successOrFail.toggle();
      updateBtnText();
    });
    updateBtnText();

    Observable<ApiResponse> apiObservable = ViewObservable.clicks(findViewById(R.id.getResponseBtn))
        .flatMap(click -> api.getResponse()
                     .subscribeOn(Schedulers.io())
                     .retryWhen(notification -> notification.flatMap(
                         error -> dialog(MainActivity.this, R.string.dialog_title,
                                         R.string.dialog_msg)
                             .subscribeOn(AndroidSchedulers.mainThread())
                             .filter(answer -> answer)))
        );

    bindLifecycle(apiObservable)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<ApiResponse>() {
          @Override public void onCompleted() {
            Timber.d("onCompleted");
          }

          @Override public void onError(Throwable e) {
            Timber.d(e, "onError");
          }

          @Override public void onNext(ApiResponse apiResponse) {
            Timber.d("onNext: " + apiResponse);
          }
        });
  }

  private Observable<Boolean> dialog(final Context context, @StringRes final int title,
                                     @StringRes final int message) {
    return Observable.create(subscriber -> {
      Timber.d("Displaying dialog...");
      final AlertDialog ad = new AlertDialog.Builder(context)
          .setTitle(title)
          .setMessage(message)
          .setPositiveButton(android.R.string.ok, (dialog, which) -> {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(true);
              subscriber.onCompleted();
            }
          })
          .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(false);
              subscriber.onCompleted();
            }
          }).create();

      subscriber.add(AndroidSubscriptions.unsubscribeInUiThread(ad::dismiss));
      ad.show();
    });
  }

  private void updateBtnText() {
    shouldFailBtn.setText(getString(R.string.should_fail) + getString(
        successOrFail.shouldFail() ? R.string.yes : R.string.no));
  }

  public <T> Observable<T> bindLifecycle(Observable<T> source) {
    return LifecycleObservable.bindActivityLifecycle(lifecycle(), source);
  }

}
