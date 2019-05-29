package com.github.susan.demo;

        import android.app.Activity;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.Toast;
        import butterknife.BindView;
        import butterknife.ButterKnife;
        import butterknife.OnClick;

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
        Toast.makeText(ButterKnifeActivity.this,"butterKnife clicked",Toast.LENGTH_SHORT).show();
    }
}
