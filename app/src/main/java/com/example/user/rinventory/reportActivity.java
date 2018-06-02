package com.example.user.rinventory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class reportActivity extends AppCompatActivity {

    public static final String URL_GET_ALL = "http://rinventory.online/Android/select.php";
    public static final String TAG_ID_KATEGORI = "id_kategori";
    public static final String TAG_NAMA_KATEGORI = "nama_kategori";
    public static final String TAG_JSON_ARRAY = "result";
    private Spinner spinner;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        spinner = (Spinner) findViewById(R.id.spinner);
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
        } else if (item.getItemId() == R.id.retur) {
            startActivity(new Intent(getApplicationContext(), returActivity.class));
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
//                String id_kategori = jo.getString(TAG_ID_KATEGORI);
                String nama_kategori = jo.getString(TAG_NAMA_KATEGORI);
//                gambar = jo.getString(TAG_GAMBAR_KATEGORI);

                HashMap<String, String> employees = new HashMap<>();
//                employees.put(TAG_ID_KATEGORI, id_kategori);
                employees.put(TAG_NAMA_KATEGORI, nama_kategori);
//                employees.put(TAG_GAMBAR_KATEGORI, gambar);
                list.add(employees);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                list);
        spinner.setAdapter(spinnerArrayAdapter);
//        ListAdapter adapter = new MyAdapter(
//                MainActivity.this, list, R.layout.list_row,
//                new String[]{TAG_NAMA_KATEGORI,TAG_GAMBAR_KATEGORI},
//                new int[]{R.id.textView, R.id.icon});
//
//
//        listView.setAdapter(adapter);


    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(reportActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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

}