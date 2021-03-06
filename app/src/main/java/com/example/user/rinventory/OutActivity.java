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
import android.widget.ImageView;
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

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OutActivity extends AppCompatActivity implements View.OnClickListener {
    //----------------------Inisialisasi Data-------------------------------
    private Button buttonScan,btnKeluar;
    ImageView gambar;
    ProgressDialog pDialog;
    private EditText editText,namaText,lokText,katText,stokText;

    int success;
    ConnectivityManager conMgr;
    SharedPreferences sharedpreferences;
    public final static String TAG_LOKASI = "tmp_simpanbarang";
    public final static String TAG_KATEGORI = "nama_kategori";
    public final static String TAG_GAMBAR = "gambar_barang";
    String url = "http://rinventory.online/Android/keluardata.php";
    String url2 = "http://rinventory.online/Android/tampil.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "pesan";

    private static final String TAG = OutActivity.class.getSimpleName();
    public final static String TAG_NAMA = "nama_barang";

    String tag_json_obj = "json_obj_req";

    public static final String KEY_ID = "id_barang";
    public static final String KEY_STOK = "stok_keluar";

    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

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
        gambar = (ImageView) findViewById(R.id.gambar);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        editText = (EditText) findViewById(R.id.editText2);
        namaText = (EditText) findViewById(R.id.editText8);
        lokText = (EditText) findViewById(R.id.editText9);
        katText = (EditText) findViewById(R.id.editText10);
        stokText = (EditText) findViewById(R.id.editText11);
        btnKeluar = (Button) findViewById(R.id.button3);

        buttonScan.setOnClickListener(this);
        btnKeluar.setOnClickListener(this);
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
        if(view == btnKeluar){
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                confirmOutData();
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
            Intent iHome = new Intent(getApplicationContext(),INActivity.class);
            finish();
            startActivity(iHome);
        } else if (item.getItemId() == R.id.inven) {
            Intent iHome = new Intent(getApplicationContext(),Inventory.class);
            finish();
            startActivity(iHome);
        } else if (item.getItemId() == R.id.retur) {
            Intent iHome = new Intent(getApplicationContext(),returActivity.class);
            finish();
            startActivity(iHome);
        } else if (item.getItemId() == R.id.report) {
            Intent iHome = new Intent(getApplicationContext(),reportActivity.class);
            finish();
            startActivity(iHome);
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
                    new OutActivity.DownLoadImageTask(gambar).execute(Server.URL+jObj.getString(TAG_GAMBAR));

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OutActivity.this, "Maaf muat ulang kembali", Toast.LENGTH_LONG).show();
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
    //----------------------Barang keluar Database----------------------------
    private void outData() {

        final String id_barang = editText.getText().toString().trim();
        final String stok_keluar = stokText.getText().toString().trim();
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
                Toast.makeText(OutActivity.this, "Maaf muat ulang kembali", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_ID,id_barang);
                params.put("email",email);
                params.put(KEY_STOK,stok_keluar);
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }
    //------------------Alert Out data-------------------------
    private void confirmOutData(){
        final String stok_keluar = stokText.getText().toString().trim();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda yakin ingin mengeluarkan jumlah stok = " + stok_keluar +"?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        callVolley();
                        outData();
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
        alertDialogBuilder.setTitle("Data stok barang berhasil dikeluarkan");

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


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
