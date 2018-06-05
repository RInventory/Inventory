package com.example.user.rinventory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.user.rinventory.adapter.Adapter;
import com.example.user.rinventory.app.AppController;
import com.example.user.rinventory.data.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class reportActivity extends AppCompatActivity implements View.OnClickListener{

    TextView txt_hasil;
    private Button go;
    String a,b,c;
    String a1,a2;
    LinearLayout llayout;
    SwipeRefreshLayout swLayout;
    Spinner spinner_pendidikan,spNamen,tipe;
    ProgressDialog pDialog;
    Adapter adapter;
    List<Data> listPendidikan = new ArrayList<Data>();

    public static final String url = "http://rinventory.online/Android/kategori.php";
    public static final String TAG_ID_KATEGORI = "id_kategori";

    private static final String TAG = reportActivity.class.getSimpleName();

    public static final String TAG_ID = "id_kategori";
    public static final String TAG_BULAN = "tgl_masuk";
    public static final String TAG_TIPE = "tipe";
    public static final String TAG_PENDIDIKAN = "nama_kategori";
    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);

        llayout = (LinearLayout) findViewById(R.id.ll_swiperefresh);

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
                }, 5000);
            }
        });

        spinner_pendidikan = (Spinner) findViewById(R.id.spinner_pendidikan);
        spNamen = (Spinner) findViewById(R.id.spinner);
        tipe = (Spinner) findViewById(R.id.spinner2);
        go = (Button) findViewById(R.id.go);

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

        adapter = new Adapter(reportActivity.this, listPendidikan);
        spinner_pendidikan.setAdapter(adapter);
        go.setOnClickListener(this);

        callData();

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
        }
        return true;
    }
    private void callData() {
        listPendidikan.clear();

        pDialog = new ProgressDialog(reportActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        showDialog();

        // Creating volley request obj
        JsonArrayRequest jArr = new JsonArrayRequest(url,
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
                Toast.makeText(reportActivity.this, "Maaf muat ulang kembali", Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        a = spNamen.getSelectedItem().toString();
        if (a.equals("Januari")){
            a1="01";
        }else if (a.equals("February")){
            a1="02";
        }else if (a.equals("Maret")){
            a1="03";
        }else if (a.equals("April")){
            a1="04";
        }else if (a.equals("Mei")){
            a1="05";
        }else if (a.equals("Juni")){
            a1="06";
        }else if (a.equals("Juli")){
            a1="07";
        }else if (a.equals("Agustus")){
            a1="08";
        }else if (a.equals("September")){
            a1="09";
        }else if (a.equals("Oktober")){
            a1="10";
        }else if (a.equals("November")){
            a1="11";
        }else if (a.equals("Desember")){
            a1="12";
        }
        c = tipe.getSelectedItem().toString();
        Intent intent = new Intent(this, report2Activity.class);
        intent.putExtra(TAG_BULAN,a1);
        intent.putExtra(TAG_ID,b);
        intent.putExtra(TAG_TIPE,c);
        startActivity(intent);
    }
}