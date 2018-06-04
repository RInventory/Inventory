package com.example.user.rinventory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class menuActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    TextView user;
    ConnectivityManager conMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedpreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
        final String username = sharedpreferences.getString("email", null);
        setTitle("hi, "+username+"!");


        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        ImageButton btnInvent = (ImageButton) findViewById(R.id.btnInvent);
        btnInvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    Intent iInvent = new Intent(getApplicationContext(),Inventory.class);
                    startActivity(iInvent);
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageButton btnIn = (ImageButton) findViewById(R.id.btnIn);
        btnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    Intent iIN = new Intent(getApplicationContext(),INActivity.class);
                    startActivity(iIN);
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageButton btnOut = (ImageButton) findViewById(R.id.btnOut);
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    Intent iOut = new Intent(getApplicationContext(),OutActivity.class);
                    startActivity(iOut);
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageButton btnReport = (ImageButton) findViewById(R.id.btnReport);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    Intent iReport = new Intent(getApplicationContext(),reportActivity.class);
                    startActivity(iReport);
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        ImageButton btnRetur = (ImageButton) findViewById(R.id.btnRetur);
        btnRetur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    Intent iRetur = new Intent(getApplicationContext(),returActivity.class);
                    startActivity(iRetur);
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        ImageButton btnExit = (ImageButton) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    showDialog();
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
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

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Apakah anda yakin ingin logout?");

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.mipmap.exit)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        SharedPreferences sharedpreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent iExit = new Intent(getApplicationContext(),loginActivity.class);
                        finish();
                        startActivity(iExit);
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

}
