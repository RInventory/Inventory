package com.example.user.rinventory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.rinventory.app.AppController;
import com.squareup.picasso.Picasso;

import com.example.user.rinventory.reportActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class report2Activity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private String JSON_STRING;
    private ListView listView;
    ImageView icon;
    String gambar;
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID_KATEGORI = "id_kategori";
    public static final String TAG_ID_BARANG = "id_barang";
    public static final String TAG_NAMA_BARANG = "nama_barang";
    public static final String TAG_STOK_BARANG = "stok_barang";
    public static final String TAG_STOK_MASUK = "stok_masuk";
    public static final String TAG_STOK_KELUAR = "stok_keluar";
    public static final String TAG_STOK_RETUR = "stok_retur";
    public static final String TAG_GAMBAR_BARANG = "gambar_barang";
    public static final String URL_GET_ALL = "http://rinventory.online/Android/report.php";
    public static final String URL_GET_ALL2 = "http://rinventory.online/Android/report2.php";
    public static final String URL_GET_ALL3 = "http://rinventory.online/Android/report3.php";
    public static final String URL = "http://rinventory.online/";
    public static final String TAG_TIPE = "tipe";
    public static final String TAG_ID = "id_kategori";
    public static final String TAG_NAMA_KATEGORI = "nama_kategori";
    public static final String TAG_BULAN = "tgl_masuk";
    public static final String TAG_BULAN2 = "tgl_keluar";
    public static final String TAG_BULAN3 = "tgl_retur";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);

        icon = (ImageView) findViewById(R.id.icon);
        listView = (ListView) findViewById(R.id.simpleListView);
        String masuk = getIntent().getExtras().getString(TAG_TIPE);

        if (masuk.equals("masuk")){
            getJSON();
        }else if(masuk.equals("keluar")){
            getJSON2();
        }else {
            getJSON3();
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

    //------------------------------------------barang masuk -----------------------------------------------
    private void showEmployee() {
        final String id_kategori = getIntent().getExtras().getString(TAG_ID);
        final String tgl_masuk = getIntent().getExtras().getString(TAG_BULAN);
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GET_ALL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray(TAG_JSON_ARRAY);


                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String nama_barang = jo.getString(TAG_NAMA_BARANG);
                        String id_barang = jo.getString(TAG_ID_BARANG);
                        String tgl_masuk = jo.getString(TAG_BULAN);
//                        String id_kategori = jo.getString(TAG_ID_KATEGORI);
                        String stok_barang = "Total stok : "+jo.getString(TAG_STOK_BARANG);
                        String stok_masuk = "Stok masuk : "+jo.getString(TAG_STOK_MASUK);
                        gambar = "http://rinventory.online/" + jo.getString(TAG_GAMBAR_BARANG);

                        HashMap<String, String> employees = new HashMap<>();
                        employees.put(TAG_NAMA_BARANG, nama_barang);
                        employees.put(TAG_ID_BARANG, id_barang);
                        employees.put(TAG_BULAN, tgl_masuk);
//                        employees.put(TAG_ID_KATEGORI, id_kategori);
                        employees.put(TAG_STOK_BARANG, stok_barang);
                        employees.put(TAG_STOK_MASUK, stok_masuk);
                        employees.put(TAG_GAMBAR_BARANG, gambar);
                        list.add(employees);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ListAdapter adapter = new MyAdapter(
                        report2Activity.this, list, R.layout.list_row3,
                        new String[]{TAG_NAMA_BARANG, TAG_BULAN, TAG_STOK_MASUK, TAG_GAMBAR_BARANG},
                        new int[]{R.id.textView, R.id.textView2, R.id.textView3, R.id.icon});


                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(report2Activity.this, "Maaf muat ulang kembali", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(TAG_ID,id_kategori);
                params.put(TAG_BULAN,tgl_masuk);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(report2Activity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class MyAdapter extends SimpleAdapter {

        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){
            super(context, data, resource, from, to);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // here you let SimpleAdapter built the view normally.
            View v = super.getView(position, convertView, parent);

            // Then we get reference for Picasso
            ImageView img = (ImageView) v.getTag();
            if(img == null){
                img = (ImageView) v.findViewById(R.id.icon);
                v.setTag(img); // <<< THIS LINE !!!!
            }
            // get the url from the data you passed to the `Map`
            Object url = ((Map)getItem(position)).get(TAG_GAMBAR_BARANG);
            // do Picasso
            Picasso.with(v.getContext()).load((String) url).into(img);

            // return the view
            return v;
        }
    }

    //------------------------------------------barang keluar -----------------------------------------------
    private void showEmployee2() {
        final String id_kategori = getIntent().getExtras().getString(TAG_ID);
        final String tgl_keluar = getIntent().getExtras().getString(TAG_BULAN);
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GET_ALL2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray(TAG_JSON_ARRAY);


                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String nama_barang = jo.getString(TAG_NAMA_BARANG);
                        String id_barang = jo.getString(TAG_ID_BARANG);
                        String tgl_keluar = jo.getString(TAG_BULAN2);
//                        String id_kategori = jo.getString(TAG_ID_KATEGORI);
                        String stok_barang = "Total stok : "+jo.getString(TAG_STOK_BARANG);
                        String stok_keluar = "Stok keluar : "+jo.getString(TAG_STOK_KELUAR);
                        gambar = "http://rinventory.online/" + jo.getString(TAG_GAMBAR_BARANG);

                        HashMap<String, String> employees = new HashMap<>();
                        employees.put(TAG_NAMA_BARANG, nama_barang);
                        employees.put(TAG_ID_BARANG, id_barang);
                        employees.put(TAG_BULAN2, tgl_keluar);
//                        employees.put(TAG_ID_KATEGORI, id_kategori);
                        employees.put(TAG_STOK_BARANG, stok_barang);
                        employees.put(TAG_STOK_KELUAR, stok_keluar);
                        employees.put(TAG_GAMBAR_BARANG, gambar);
                        list.add(employees);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ListAdapter adapter = new MyAdapter(
                        report2Activity.this, list, R.layout.list_row3,
                        new String[]{TAG_NAMA_BARANG, TAG_BULAN2, TAG_STOK_KELUAR, TAG_GAMBAR_BARANG},
                        new int[]{R.id.textView, R.id.textView2, R.id.textView3, R.id.icon});


                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(TAG_ID,id_kategori);
                params.put(TAG_BULAN2,tgl_keluar);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void getJSON2() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(report2Activity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee2();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_ALL2);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    //------------------------------------------barang retur -----------------------------------------------
    private void showEmployee3() {
        final String id_kategori = getIntent().getExtras().getString(TAG_ID);
        final String tgl_retur = getIntent().getExtras().getString(TAG_BULAN);
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_GET_ALL3, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray result = jObj.getJSONArray(TAG_JSON_ARRAY);


                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String nama_barang = jo.getString(TAG_NAMA_BARANG);
                        String id_barang = jo.getString(TAG_ID_BARANG);
                        String tgl_retur = jo.getString(TAG_BULAN3);
//                        String id_kategori = jo.getString(TAG_ID_KATEGORI);
                        String stok_barang = "Total stok : "+jo.getString(TAG_STOK_BARANG);
                        String stok_retur = "Stok retur : "+jo.getString(TAG_STOK_RETUR);
                        gambar = "http://rinventory.online/" + jo.getString(TAG_GAMBAR_BARANG);

                        HashMap<String, String> employees = new HashMap<>();
                        employees.put(TAG_NAMA_BARANG, nama_barang);
                        employees.put(TAG_ID_BARANG, id_barang);
                        employees.put(TAG_BULAN3, tgl_retur);
//                        employees.put(TAG_ID_KATEGORI, id_kategori);
                        employees.put(TAG_STOK_BARANG, stok_barang);
                        employees.put(TAG_STOK_RETUR, stok_retur);
                        employees.put(TAG_GAMBAR_BARANG, gambar);
                        list.add(employees);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ListAdapter adapter = new MyAdapter(
                        report2Activity.this, list, R.layout.list_row3,
                        new String[]{TAG_NAMA_BARANG, TAG_BULAN3, TAG_STOK_RETUR, TAG_GAMBAR_BARANG},
                        new int[]{R.id.textView, R.id.textView2, R.id.textView3, R.id.icon});


                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(TAG_ID,id_kategori);
                params.put(TAG_BULAN3,tgl_retur);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }

    private void getJSON3() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(report2Activity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee3();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(URL_GET_ALL3);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}