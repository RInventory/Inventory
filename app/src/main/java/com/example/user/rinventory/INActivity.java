package com.example.user.rinventory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.user.rinventory.adapter.Adapter;
import com.example.user.rinventory.app.AppController;
import com.example.user.rinventory.data.Data;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class INActivity extends AppCompatActivity implements View.OnClickListener {
    //----------------------Inisialisasi Data-------------------------------
    private Button buttonScan,btnTambah;
    ImageView gambar;
    ProgressDialog pDialog;
    SwipeRefreshLayout swLayout;
    private EditText editText,namaText,lokText,katText,stokText;
    Spinner spinner_pendidikan,spNamen,tipe;
    Adapter adapter;
    List<Data> listPendidikan = new ArrayList<Data>();

    int success;
    ConnectivityManager conMgr;
    SharedPreferences sharedpreferences;
    public static final String TAG_ID = "id_supplier";
    public static final String TAG_PENDIDIKAN = "nama_supplier";
    public final static String TAG_LOKASI = "tmp_simpanbarang";
    public final static String TAG_KATEGORI = "nama_kategori";
    public final static String TAG_GAMBAR = "gambar_barang";
    String url = "http://rinventory.online/Android/tambahdata.php";
    String url2 = "http://rinventory.online/Android/tampil.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "pesan";
    public static final String urlx = "http://rinventory.online/Android/supplier.php";

    private static final String TAG = INActivity.class.getSimpleName();
    public final static String TAG_NAMA = "nama_barang";

    String tag_json_obj = "json_obj_req";

    public static final String KEY_ID = "id_barang";
    public static final String KEY_STOK = "stok_masuk";
    String b;

    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);
        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);

        // Mengeset properti warna yang berputar pada SwipeRefreshLayout
        swLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swLayout.setRefreshing(false);

                        // fungsi-fungsi lain yang dijalankan saat refresh berhenti
                        callData();

                    }
                }, 1000);
            }
        });

        //-------------------spinner----------------------------
        spinner_pendidikan = (Spinner) findViewById(R.id.spinner);
        spNamen = (Spinner) findViewById(R.id.spinner);
        spinner_pendidikan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                b = listPendidikan.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        adapter = new Adapter(INActivity.this, listPendidikan);
        spinner_pendidikan.setAdapter(adapter);
        callData();

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
        sharedpreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);

        //-------------------memangil id dari layout----------------------
        buttonScan = (Button) findViewById(R.id.buttonScan);
        editText = (EditText) findViewById(R.id.editText2);
        namaText = (EditText) findViewById(R.id.namaText);
        lokText = (EditText) findViewById(R.id.lokText);
        katText = (EditText) findViewById(R.id.katText);
        stokText = (EditText) findViewById(R.id.stokText);
        btnTambah = (Button) findViewById(R.id.btnTambah);
        gambar = (ImageView) findViewById(R.id.gambarView);

        buttonScan.setOnClickListener(this);
        btnTambah.setOnClickListener(this);
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

    //------------------------------Onclick Button----------------------------
    @Override
    public void onClick(View view) {
        if(view == buttonScan){
            intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.initiateScan();
        }
        if(view == btnTambah){
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                confirmINData();
            } else {
                Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    //-----------------------Menu Inflater/Option bar------------------------
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
            Intent iIN = new Intent(getApplicationContext(),INActivity.class);
            finish();
            startActivity(iIN);
        }else if (item.getItemId() == R.id.out) {
            Intent iOUT = new Intent(getApplicationContext(),OutActivity.class);
            finish();
            startActivity(iOUT);
        } else if (item.getItemId() == R.id.inven) {
            Intent iInven = new Intent(getApplicationContext(),Inventory.class);
            finish();
            startActivity(iInven);
        } else if (item.getItemId() == R.id.retur) {
            Intent iRetur = new Intent(getApplicationContext(),returActivity.class);
            finish();
            startActivity(iRetur);
        } else if (item.getItemId() == R.id.report) {
            Intent iReport = new Intent(getApplicationContext(),reportActivity.class);
            finish();
            startActivity(iReport);
        }

        return true;
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
                    new DownLoadImageTask(gambar).execute(Server.URL+jObj.getString(TAG_GAMBAR));

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(INActivity.this, "Maaf muat ulang kembali", Toast.LENGTH_LONG).show();
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
    //----------------------Barang Tambah Database----------------------------
    private void addData() {

        final String id_barang = editText.getText().toString().trim();
        final String stok_masuk = stokText.getText().toString().trim();
        sharedpreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
        final String email = sharedpreferences.getString("email", null); //get nilai shared preference

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
                Toast.makeText(INActivity.this, "Maaf muat ulang kembali", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_ID,id_barang);
                params.put("email",email);
                params.put(KEY_STOK,stok_masuk);
                params.put(TAG_ID,b);
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }
    //------------------Alert IN data-------------------------
    private void confirmINData(){
        final String stok_masuk = stokText.getText().toString().trim();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin ingin memasukkan jumlah stok = " + stok_masuk +"?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        addData();
                        callVolley();
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
        alertDialogBuilder.setTitle("Data stok barang berhasil ditambahkan");

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


    //--------------------Load Image View dengan URL ------------------------------
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

  //--------------------------------Spinner from DB---------------------------
    private void callData() {
        listPendidikan.clear();

        pDialog = new ProgressDialog(INActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(urlx,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, response.toString());

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                Data item = new Data();

                                item.setId(obj.getString(TAG_ID));
                                item.setPendidikan(obj.getString(TAG_PENDIDIKAN));

                                listPendidikan.add(item);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();

                        hideDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(INActivity.this, "Maaf muat ulang kembali", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr, tag_json_obj);
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
