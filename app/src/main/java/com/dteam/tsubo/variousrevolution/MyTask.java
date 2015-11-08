package com.dteam.tsubo.variousrevolution;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.os.AsyncTask;

/**
 * Created by takahiroiwatani on 15/11/08.
 */



public class MyTask extends AsyncTask<URL, Void, String> {

    public static final int ASCESS_IROIRO_SERVER = 1;
    public static final int ACCESS_IMAGE_SERVER = 2;

    private int serverType = 0;
    private String word = "";

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public void setWord(String word) {
        this.word = word;
    }

    MyObserver observer = null;
    public void setObserver(MyObserver observer) {
        this.observer = observer;
    }

    @Override
    protected String doInBackground(URL... url) {
        Log.i("MyTask", "### doInBackground() ###");

        Log.i("MyTask", "word="+word);

        String result = "";
        HttpURLConnection conn = null;
        try {
            Log.i("MyTask", "AAA");
            conn = (HttpURLConnection) url[0].openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
            Log.i("MyTask", "BBB");
            conn.connect();
            Log.i("MyTask", "CCC");
            int resp = conn.getResponseCode();
            // respを使っていろいろ
            Log.i("MyTask", "resp="+resp);

            result = readIt(conn.getInputStream());

            Log.i("MyTask", "result="+result);

        } catch(Exception e) {
Log.i("MyTask", "Exception");
            e.printStackTrace();
        } finally {
            Log.i("MyTask", "finally");
            if(conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        Log.i("MyTask", "### readIt() ###");
        StringBuffer sb = new StringBuffer();
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        try {
            stream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String json){
        Log.i("MyTask", "### onPostExecute() ###");
        Log.i("MyTask", "json="+json);
        Log.i("MyTask", "this.serverType="+this.serverType);

        if(this.serverType == ASCESS_IROIRO_SERVER) {
            Log.i("MyTask", "serverType = ASCESS_IROIRO_SERVER");
            try {

/*
        {
        "returnCode":0,
        "scheme":{"n":"210",
            "scheme_id":"2627",
            "elements":[
                {"g":"253","b":"253","r":"253","p":"0.462651","rgb":"#fdfdfd"},
                {"g":"236","b":"235","r":"238","p":"0.179292","rgb":"#eeeceb"},
                {"g":"202","b":"185","r":"219","p":"0.0696542","rgb":"#dbcab9"},
                {"g":"64","b":"64","r":"59","p":"0.0656794","rgb":"#3b4040"},
                {"g":"121","b":"99","r":"104","p":"0.0627184","rgb":"#687963"},
                {"g":"8","b":"6","r":"13","p":"0.0589198","rgb":"#0d0806"},
                {"g":"195","b":"10","r":"244","p":"0.0513386","rgb":"#f4c30a"},
                {"g":"15","b":"14","r":"169","p":"0.0497468","rgb":"#a90f0e"}
              ]
          }
        }
*/
                    Log.i("MyTask", "aaa");
                    Log.i("MyTask", "json="+json);
                    JSONObject jo = new JSONObject(json);
                    Log.i("MyTask", "bbb");
                    JSONArray elements = jo.getJSONObject("scheme").getJSONArray("elements");
                    Log.i("MyTask", "ccc");
                    Log.i("MyTask", "elements.length()="+elements.length());

                    Bundle map = new Bundle();

                    for (int i = 0; i < elements.length(); i++) {
                        JSONObject element = elements.getJSONObject(i);
                        String rgb = element.getString("rgb");
                        Log.i("MyTask", "rgb="+rgb);
                        map.putString("param"+i, rgb);
                    }

                    this.observer.udpate(map, this.serverType);

                } catch (JSONException e) {
                  Log.i("MyTask", "JSONException");
                    e.printStackTrace();
                }

        } else if(this.serverType == ACCESS_IMAGE_SERVER) {
            Log.i("MyTask", "serverType = ACCESS_IMAGE_SERVER");
        }
    }

}