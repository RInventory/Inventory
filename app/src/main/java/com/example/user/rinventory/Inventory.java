package com.example.user.rinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class Inventory extends AppCompatActivity {

    RelativeLayout kat1, kat2, kat3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        setTitle("Inventori");

        kat1 = findViewById(R.id.kat1);
        kat2 = findViewById(R.id.kat2);
        kat3 = findViewById(R.id.kat3);

        kat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent test= new Intent(getApplicationContext(), ListBarang.class);
                startActivity(test);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId()==R.id.in){
//            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
//        } else if (item.getItemId() == R.id.out) {
//            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
//        } else if (item.getItemId() == R.id.inven) {
//            startActivity(new Intent(getApplicationContext(), Inventory.class));
//        } else if (item.getItemId() == R.id.retur) {
//            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
//        } else if (item.getItemId() == R.id.report) {
//            startActivity(new Intent(getApplicationContext(), HelpActivity.class));
//        }
//
//        return true;
//    }

}
