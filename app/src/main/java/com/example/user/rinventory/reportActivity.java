package com.example.user.rinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class reportActivity extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);



        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new ItemSelectedListener());
    }

    public void search(View view) {
        Intent iSearch = new Intent(getApplicationContext(),report2Activity.class);
        startActivity(iSearch);
    }

    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        //get strings of first item
        String firstItem = String.valueOf(spinner.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(spinner.getSelectedItem()))) {

            } else {
                Toast.makeText(parent.getContext(),
                        parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }

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
        if (item.getItemId()==R.id.home){
            startActivity(new Intent(getApplicationContext(), menuActivity.class));
        } else if (item.getItemId() == R.id.in) {
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