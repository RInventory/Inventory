package com.example.user.rinventory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.rinventory.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class detailbarang extends AppCompatActivity {
    ProgressDialog pDialog;
    SwipeRefreshLayout swLayout;
    private TextView kodeText,namaText,lokText,katText,stokText;
    ImageView gambar;
    public static final String TAG_ID_BARANG = "id_barang";
    public static final String URL_GET_ALL = "http://rinventory.online/Android/select3.php";
    public static final String KEY_ID = "id_barang";
    public final static String TAG_BARANG = "nama_barang";
    public final static String TAG_STOK = "stok_barang";
    public final static String TAG_LOKASI = "tmp_simpanbarang";
    public final static String TAG_KATEGORI = "nama_kategori";
    public final static String TAG_GAMBAR = "gambar_barang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailbarang);
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
                        callVolley();

                    }
                }, 1000);
            }
        });

        kodeText = (TextView) findViewById(R.id.kodeBarang);
        namaText = (TextView) findViewById(R.id.namaBarang);
        lokText = (TextView) findViewById(R.id.lokBarang);
        katText = (TextView) findViewById(R.id.katBarang);
        stokText = (TextView) findViewById(R.id.stokBarang);
        gambar = (ImageView) findViewById(R.id.imageView3);
        callVolley();
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
            Intent iIN = new Intent(getApplicationContext(),INActivity.class);
            finish();
            startActivity(iIN);
        } else if (item.getItemId() == R.id.out) {
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
    private void callVolley() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        showDialog();
        final String id_barang = getIntent().getExtras().getString(TAG_ID_BARANG);
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GET_ALL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    kodeText.setText(jObj.getString(KEY_ID));
                    namaText.setText(jObj.getString(TAG_BARANG));
                    lokText.setText(jObj.getString(TAG_LOKASI));
                    katText.setText(jObj.getString(TAG_KATEGORI));
                    stokText.setText(jObj.getString(TAG_STOK));
                    new detailbarang.DownLoadImageTask(gambar).execute(Server.URL+jObj.getString(TAG_GAMBAR));

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
        AppController.getInstance().addToRequestQueue(strReq, "json_obj_req");

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
