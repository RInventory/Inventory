package com.example.user.rinventory;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class splashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(){
            public void run (){
                try {
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(splashScreen.this , menuActivity.class));
                    finish();
                }
            }
        };
        thread.start();
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
