package com.example.user.rinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class OutActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonScan;
    private EditText editText;

    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        editText = (EditText) findViewById(R.id.editText2);

        buttonScan.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            } else {
                // jika qrcode berisi data
                editText.setText("");
                String contents = data.getStringExtra("SCAN_RESULT");
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                editText.setText(contents);
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();

    }
}
