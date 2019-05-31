package com.github.susan.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ButterKnifeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button, R.id.button1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                Log.e("什么鬼","-------button-----------");
//                DialogUtil.show2Button(this, "确定退出当前账号吗?", "确定", "取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("Debounce", v.getId() + "id----show2Button");
//                    }
//                }, null);
                break;
            case R.id.button1:
                Log.e("什么鬼","-------button1-----------");
                break;
        }
    }
}
