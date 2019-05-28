package com.github.susan.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
public class SecondActivity extends BaseActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
    Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onClick(View v) {
    super.onClick(v);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    /*no-op*/
  }
}
