package com.example.countreyteames;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.provider.ContactsContract.*;

public class MainActivity extends AppCompatActivity {


    RequestQueue requestQueue;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ProgressDialog progressDialog;
    ArrayList<String> arrayList=new ArrayList<>();
    String url="https://test.oye.direct/players.json";
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.myList);
        requestQueue= Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, ""+arrayList.get(position), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("key",arrayList.get(position));
                startActivity(intent);
            }
        });
    }

    private <T> Iterable<T> iterate(final Iterator<T> i){
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }

    public void load(View view) {
        progressDialog.show();
        progressDialog.setMessage("Loading Countries.");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject=new JSONObject(String.valueOf(response));


                    for (String key : iterate(jsonObject.keys()))
                    {
                        // here key will be containing your OBJECT NAME YOU CAN SET IT IN Listview.
                        //Toast.makeText(MainActivity.this, ""+key, Toast.LENGTH_SHORT).show();
                        arrayList.add(""+key);
                        arrayAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();


            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}



