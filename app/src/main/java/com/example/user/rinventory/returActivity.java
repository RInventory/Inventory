package com.example.user.rinventory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.rinventory.app.AppController;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class returActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonScan,btnRetur;
    ProgressDialog pDialog;
    private EditText editText,namaText,lokText,katText,stokText,keteranganText;

    int success;
    ConnectivityManager conMgr;
    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    public final static String TAG_LOKASI = "tmp_simpanbarang";
    public final static String TAG_KATEGORI = "nama_kategori";
    public final static String TAG_USERNAME = "username";
    String url = "http://rinventory.online/Android/returData.php";
    String url2 = "http://rinventory.online/Android/tampil.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "pesan";

    private static final String TAG = OutActivity.class.getSimpleName();
    public final static String TAG_NAMA = "nama_barang";

    String tag_json_obj = "json_obj_req";

    public static final String KEY_ID = "id_barang";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_STOK = "stok_retur";
    public final static String KEY_KETERANGAN = "keterangan_retur";

    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retur);

        //-----------------------Cek Koneksi Internet-----------------------------
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


        //---------------------get username dari shared preference-------------------------
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);

        //-------------------memangil id dari layout----------------------
        buttonScan = (Button) findViewById(R.id.buttonScan);
        editText = (EditText) findViewById(R.id.editText2);
        namaText = (EditText) findViewById(R.id.editText8);
        keteranganText =(EditText) findViewById(R.id.kerusakanText);
        lokText = (EditText) findViewById(R.id.editText9);
        katText = (EditText) findViewById(R.id.editText10);
        stokText = (EditText) findViewById(R.id.editText11);
        btnRetur = (Button) findViewById(R.id.button3);

        buttonScan.setOnClickListener(this);
        btnRetur.setOnClickListener(this);
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
            Intent iHome = new Intent(getApplicationContext(),menuActivity.class);
            finish();
            startActivity(iHome);
        } else if (item.getItemId() == R.id.in) {
            startActivity(new Intent(getApplicationContext(), INActivity.class));
        } else if (item.getItemId() == R.id.out) {
            startActivity(new Intent(getApplicationContext(), OutActivity.class));
        } else if (item.getItemId() == R.id.inven) {
            startActivity(new Intent(getApplicationContext(), Inventory.class));
        } else if (item.getItemId() == R.id.report) {
            startActivity(new Intent(getApplicationContext(), reportActivity.class));
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == buttonScan){
            intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.initiateScan();
        }
        if(view == btnRetur){
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                confirmReturData();
            } else {
                Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }

    }
    //-----------------------Barcode Scan-------------------------------
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
                if (conMgr.getActiveNetworkInfo() != null
                        && conMgr.getActiveNetworkInfo().isAvailable()
                        && conMgr.getActiveNetworkInfo().isConnected()) {
                    callVolley();
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //--------------------------Request data ke Database------------------
    //-------------------------------Menampilkan Data---------------------
    private void callVolley() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        showDialog();
        final String id_barang = editText.getText().toString().trim();

        StringRequest strReq = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    namaText.setText(jObj.getString(TAG_NAMA));
                    lokText.setText(jObj.getString(TAG_LOKASI));
                    katText.setText(jObj.getString(TAG_KATEGORI));

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_ID,id_barang);
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }
    //----------------------Barang Retur Database----------------------------
    private void returData() {

        final String id_barang = editText.getText().toString().trim();
        final String stok_retur = stokText.getText().toString().trim();
        final String keterangan_retur = keteranganText.getText().toString().trim();
        final String username = sharedpreferences.getString(TAG_USERNAME, null); //get nilai shared preference

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success==1){
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        confirmSukses();
                    }else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_ID,id_barang);
                params.put(KEY_USERNAME,username);
                params.put(KEY_STOK,stok_retur);
                params.put(KEY_KETERANGAN,keterangan_retur);
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }
    //------------------Alert Retur data-------------------------
    private void confirmReturData(){
        final String stok_retur = stokText.getText().toString().trim();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin ingin meretur jumlah stok = " + stok_retur +"?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        callVolley();
                        returData();
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //-------------------------konfirmSukses-------------------------------
    private void confirmSukses(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Data stok barang berhasil diretur");

        // set pesan dari dialog
        alertDialogBuilder
                .setIcon(R.mipmap.sukses)
                .setCancelable(false)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
