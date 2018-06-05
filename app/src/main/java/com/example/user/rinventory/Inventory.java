package com.example.user.rinventory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String JSON_STRING;
    private ListView listView;
    ImageView icon;
    String gambar;
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_ID_KATEGORI = "id_kategori";
    public static final String TAG_NAMA_KATEGORI = "nama_kategori";
    public static final String TAG_GAMBAR_KATEGORI = "gambar_kategori";
    public static final String URL_GET_ALL = "http://rinventory.online/Android/select.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        setTitle("Inventory");

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


    private void showEmployee() {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id_kategori = jo.getString(TAG_ID_KATEGORI);
                String nama_kategori = jo.getString(TAG_NAMA_KATEGORI);
                gambar = jo.getString(TAG_GAMBAR_KATEGORI);

                HashMap<String, String> employees = new HashMap<>();
                employees.put(TAG_ID_KATEGORI, id_kategori);
                employees.put(TAG_NAMA_KATEGORI, nama_kategori);
                employees.put(TAG_GAMBAR_KATEGORI, gambar);
                list.add(employees);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new MyAdapter(
                Inventory.this, list, R.layout.list_row,
                new String[]{TAG_NAMA_KATEGORI,TAG_GAMBAR_KATEGORI},
                new int[]{R.id.textView,R.id.icon});


        listView.setAdapter(adapter);


    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Inventory.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
        Intent intent = new Intent(this, ListBarang.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String id_kategori = map.get(TAG_ID_KATEGORI).toString();
        intent.putExtra(TAG_ID_KATEGORI,id_kategori);
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
            Object url = ((Map)getItem(position)).get(TAG_GAMBAR_KATEGORI);
            // do Picasso
            Picasso.with(v.getContext()).load((String) url).into(img);

            // return the view
            return v;
        }
    }
}
