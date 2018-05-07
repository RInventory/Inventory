package com.example.user.rinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
                Intent iOut = new Intent(getApplicationContext(),OutActivity.class);
                startActivity(iOut);
            }
        });

        ImageButton btnReport = (ImageButton) findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iReport = new Intent(getApplicationContext(),reportActivity.class);
                startActivity(iReport);
            }
        });
        ImageButton btnRetur = (ImageButton) findViewById(R.id.btnRetur);
        btnRetur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iRetur = new Intent(getApplicationContext(),returActivity.class);
                startActivity(iRetur);
            }
        });
        ImageButton btnExit = (ImageButton) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iExit = new Intent(getApplicationContext(),loginActivity.class);
                startActivity(iExit);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.in){
            startActivity(new Intent(getApplicationContext(), INActivity.class));
        } else if (item.getItemId() == R.id.out) {
            startActivity(new Intent(getApplicationContext(), OutActivity.class));
        } else if (item.getItemId() == R.id.inven) {
            startActivity(new Intent(getApplicationContext(), Inventory.class));
        } else if (item.getItemId() == R.id.retur) {
            startActivity(new Intent(getApplicationContext(), returActivity.class));
        } else if (item.getItemId() == R.id.report) {
            startActivity(new Intent(getApplicationContext(), reportActivity.class));
        }

        return true;
    }

}
