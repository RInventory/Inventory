package com.example.user.rinventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class ListBarang extends AppCompatActivity {

    RelativeLayout br1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang);

        setTitle("List Barang");

        br1 = findViewById(R.id.br1);

        br1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent test= new Intent(getApplicationContext(), detailbarang.class);
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
}
