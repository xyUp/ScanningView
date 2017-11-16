package com.example.xy.scanningview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ScanActivity extends AppCompatActivity {

    private ScanningView scanView;
    private Button btnGo,btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scanView = (ScanningView) findViewById(R.id.scanView);
        btnGo = (Button) findViewById(R.id.btn_go);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanView.setRun(true);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanView.setRun(false);
            }
        });

    }
}
