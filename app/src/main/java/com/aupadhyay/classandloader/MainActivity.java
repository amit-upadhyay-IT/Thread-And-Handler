package com.aupadhyay.classandloader;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<BookBean> bookList;

    BookAdapter adapter;

    String response;

    ProgressDialog progressDialog;

    public void initViews()
    {
        listView = (ListView) findViewById(R.id.listView);
        bookList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkNetworkConnectivity())
        {
            //progressDialog.show();
            new FetchBookThread().start();
        }
        else
        {
            Toast.makeText(this, "Please Connect to the Internet", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkNetworkConnectivity()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, bookList.get(i).toString(), Toast.LENGTH_LONG).show();
    }


    // now the biggest concern over here is to manage the synctonization between the main UI thread and the FetchBookThread
    // for this we are going to write down the Handler (from android.os package)

    class FetchBookThread extends Thread
    {
        @Override
        public void run() {

            try
            {
                URL url = new URL("http://www.json-generator.com/api/json/get/chQLxhBjaW?indent=2");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();// send the request to server

                // after sending the request we will be waiting for the response
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader buffer = new BufferedReader(reader); // now we can read this response line by line.

                StringBuilder builder = new StringBuilder();
                String line = buffer.readLine();

                while (line != null)
                {
                    builder.append(line);
                    line = buffer.readLine();
                }
                // so finally the builder will contain the complete JSON data.

                response = builder.toString(); // now response contains JSON documents as a String.


                // once my threa has finished its task. then I;ll send a message to the handler by calling a method
                 handler.sendEmptyMessage(101);// after this the method handleMessage() inside our Handler anonymous class shall be invoked.
                 // 101 is an identity

            }catch (Exception e)
            {
                e.printStackTrace();
                // we can't do a Toast here because this is child thread and thus we can't update the ListView, we can't do any UI
                // updations in the child thread.
                // So writing Toast will give us the Runtime Exception.
                Log.e("MainActivity","Exception is "+e);
            }
        }
    }

    // this will be an anonymous class
    // Handler is the part of main UI thread so this can update the UI
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 101)
            {
                parseJSONResponse();
            }

        }
    };

    private void parseJSONResponse()
    {
        try {

            JSONObject jsonObject = new JSONObject(response);// now the JSON document containd into the response will copied into jsonObject
            // since "response" contains one Json object which internally contains one JSON array by the name BookStore
            // so I'll get the JSON array.
            JSONArray jsonArray = jsonObject.getJSONArray("bookstore");
            // So now I got the JSON array from the JSON object
            // now as I got the JsonArray thus I will iterate over this array.

            String na = "", au = "", pr = ""; // local String variables corrosponding to name, author and price
            for (int i = 0; i < jsonArray.length(); i++)
            {
                // we have got 3 json objects in JSON array
                JSONObject  object = jsonArray.getJSONObject(i);
                na = object.getString("name");
                au = object.getString("author");
                pr = object.getString("price");

                bookList.add(new BookBean(pr,au,na));
            }

            adapter = new BookAdapter(this, R.layout.list_item, bookList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(this);
/*
            if (progressDialog.isShowing())
                progressDialog.dismiss();*/

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Some Parsing issue "+response, Toast.LENGTH_LONG).show();
        }
    }

}
