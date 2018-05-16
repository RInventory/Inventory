package com.example.user.rinventory;

import android.content.Intent;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.example.user.rinventory.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class INActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonScan,btnTambah;
    private EditText editText,namaText,lokText,katText;

    String url = "http://rinventory.online/tampil.php";

    private static final String TAG = INActivity.class.getSimpleName();
    public final static String TAG_NAMA = "nama_barang";
    public final static String TAG_LOKASI = "tmp_simpanbarang";
    public final static String TAG_KATEGORI = "nama_kategori";

    String tag_json_obj = "json_obj_req";

    public static final String KEY_ID = "id_barang";

    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        btnTambah = (Button) findViewById(R.id.btnEnter);

        editText = (EditText) findViewById(R.id.editText2);
        namaText = (EditText) findViewById(R.id.namaText);
        lokText = (EditText) findViewById(R.id.lokText);
        katText = (EditText) findViewById(R.id.katText);

        buttonScan.setOnClickListener(this);
        btnTambah.setOnClickListener(this);
    }

    //---------------------Barcode Scan -----------------------------
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
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //-----------------Onclick Button------------------------
    @Override
    public void onClick(View view) {
        if(view == buttonScan){
            intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.initiateScan();
        }

        if(view == btnTambah){
            callVolley();
        }

    }

    //----------------------------Menu Inflater----------------------------------
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

    //--------------------------Request data ke Database------------------
    //-------------------------------Menampilkan Data---------------------
    private void callVolley() {
        final String id_barang = editText.getText().toString().trim();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

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
}
