package com.example.countreyteames;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    RequestQueue requestQueue;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ProgressDialog progressDialog;
    ArrayList<String> arrayList;

    {
        arrayList = new ArrayList<>();
    }

    String url="https://test.oye.direct/players.json";
    String keyname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView=findViewById(R.id.playerList);

        requestQueue= Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

        keyname=getIntent().getStringExtra("key");

        arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadMethod(keyname);
    }

    private void loadMethod(String keyname) {
        progressDialog.show();
        progressDialog.setMessage("Loading....");

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    // String countryplayerlist=response.getString("Afghanistan");
                    String countryplayerlist=response.getString(""+keyname);

                    JSONArray jsonArray=new JSONArray(countryplayerlist);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String name=jsonObject.getString("name");
                        Boolean captin=jsonObject.getBoolean("captain");

                        arrayList.add(""+name+"\n"+captin+"\n\n");
                        arrayAdapter.notifyDataSetChanged();
                    }



                    // Toast.makeText(MainActivity2.this, ""+countryplayerlist, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity2.this, ""+error, Toast.LENGTH_SHORT).show();


            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}


