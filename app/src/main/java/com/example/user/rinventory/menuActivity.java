package com.example.user.rinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class menuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton btnInvent = (ImageButton) findViewById(R.id.btnInvent);
        btnInvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iInvent = new Intent(getApplicationContext(),Inventory.class);
                startActivity(iInvent);
            }
        });

        ImageButton btnIn = (ImageButton) findViewById(R.id.btnIn);
        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iIN = new Intent(getApplicationContext(),INActivity.class);
                startActivity(iIN);
            }
        });

        ImageButton btnOut = (ImageButton) findViewById(R.id.btnOut);
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iOut = new Intent(getApplicationContext(),INActivity.class);
                startActivity(iOut);
            }
        });

    }
}
