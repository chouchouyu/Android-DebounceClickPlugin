package com.github.susan.demo;

import android.view.View;
import android.widget.Toast;
import com.github.susan.clickdebounce.java.ClickDebounceHandler;
import com.github.susan.clickdebounce.java.ClickDebounceMark;

public class Test {
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Test clicked", Toast.LENGTH_LONG).show();
    }
}
