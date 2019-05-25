package com.github.susan.demo;

import android.view.View;
import android.widget.Toast;
import com.github.susan.clickdebounce.java.ClickDebounceExtra;

public class Test {
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Test clicked", Toast.LENGTH_LONG).show();
    }

    @ClickDebounceExtra
    public void onXXClick(View view) {
        Toast.makeText(view.getContext(), "Test ClickDebounceExtra", Toast.LENGTH_LONG).show();
    }
}
