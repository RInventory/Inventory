package com.example.user.rinventory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBarang extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String JSON_STRING;
    private ListView listView;
    ImageView icon;
    SwipeRefreshLayout swLayout;
    String gambar;
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID_KATEGORI = "id_kategori";
    public static final String TAG_ID_BARANG = "id_barang";
    public static final String TAG_NAMA_BARANG = "nama_barang";
    public static final String TAG_STOK_BARANG = "stok_barang";
    public static final String TAG_GAMBAR_BARANG = "gambar_barang";
    public static final String URL_GET_ALL = "http://rinventory.online/Android/select2.php";
    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang);
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
                        getJSON();

                    }
                }, 1000);
            }
        });
        setTitle("List Barang");

        icon = (ImageView) findViewById(R.id.icon);
        listView = (ListView) findViewById(R.id.simpleListView);
        listView.setOnItemClickListener(this);
        getJSON();
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
            Intent iOut = new Intent(getApplicationContext(),OutActivity.class);
            finish();
            startActivity(iOut);
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

    private void showEmployee() {
        final String id_kategori = getIntent().getExtras().getString(TAG_ID_KATEGORI);
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
//                        String id_kategori = jo.getString(TAG_ID_KATEGORI);
                        String stok_barang = "Total stok : "+jo.getString(TAG_STOK_BARANG);
                        gambar = Server.URL + jo.getString(TAG_GAMBAR_BARANG);

                        HashMap<String, String> employees = new HashMap<>();
                        employees.put(TAG_NAMA_BARANG, nama_barang);
                        employees.put(TAG_ID_BARANG, id_barang);
//                        employees.put(TAG_ID_KATEGORI, id_kategori);
                        employees.put(TAG_STOK_BARANG, stok_barang);
                        employees.put(TAG_GAMBAR_BARANG, gambar);
                        list.add(employees);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ListAdapter adapter = new MyAdapter(
                        ListBarang.this, list, R.layout.list_row2,
                        new String[]{TAG_NAMA_BARANG, TAG_STOK_BARANG, TAG_GAMBAR_BARANG},
                        new int[]{R.id.textView, R.id.textView2, R.id.icon});


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
                params.put(TAG_ID_KATEGORI,id_kategori);
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
                loading = ProgressDialog.show(ListBarang.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
        Intent intent = new Intent(this, detailbarang.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String id_barang = map.get(TAG_ID_BARANG).toString();
        intent.putExtra(TAG_ID_BARANG,id_barang);
        startActivity(intent);
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

}
