package com.github.susan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

public abstract class BaseActivity extends Activity implements View.OnClickListener {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onClick(View v) {
    /*no-op*/
  }

  public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
