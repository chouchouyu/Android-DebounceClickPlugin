package com.github.susan.demo;

import android.view.View;
import android.widget.Toast;
import com.github.susan.clickdebounce.java.ClickDebounceExtra;

public class TestJava {
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "TestJava onClick", Toast.LENGTH_LONG).show();
    }

    @ClickDebounceExtra
    public void onXXClick(View view) {
        Toast.makeText(view.getContext(), "TestJava onXXClick with ClickDebounceExtra", Toast.LENGTH_LONG).show();
    }
}
