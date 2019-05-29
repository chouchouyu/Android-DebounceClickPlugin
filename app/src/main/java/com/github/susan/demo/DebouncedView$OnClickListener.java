package com.github.susan.demo;

import android.view.View;

import java.util.concurrent.TimeUnit;

public abstract class DebouncedView$OnClickListener implements View.OnClickListener {

  private final long debounceIntervalInMillis;
  private long previousClickTimestamp;

  public DebouncedView$OnClickListener(long debounceIntervalInMillis) {
    this.debounceIntervalInMillis = debounceIntervalInMillis;
  }

  @Override
  public void onClick(View view) {

    final long currentClickTimestamp = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

    if (previousClickTimestamp == 0
        || currentClickTimestamp - previousClickTimestamp >= debounceIntervalInMillis) {

      //update click timestamp
      previousClickTimestamp = currentClickTimestamp;

      this.onDebouncedClick(view);
    }
  }

  public abstract void onDebouncedClick(View v);
}
