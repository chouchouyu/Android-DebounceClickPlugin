package com.github.susan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.susan.debounceclick.java.DebounceClickMark;

public class ButterKnifeActivity extends Activity {

    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        DialogUtil.show2Button(this, "确定退出当前账号吗?", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debounce", v.getId() + "id----show2Button");
            }
        }, null);
    }
}
