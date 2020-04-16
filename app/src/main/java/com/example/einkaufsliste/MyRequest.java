package com.example.einkaufsliste;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.ConnectionPendingException;

public class MyRequest {
    private static final int REQUEST_PENDING = 0;
    private static final int REQUEST_COMPLETE = 1;
    private static final int REQUEST_ERROR = 2;
    private String charset = "UTF-8";
    private String url = "http://stahl-carport.com/Hannes/testMySQL/testSQL.php";
    public static String PASSWORD = "";
    private String withJson = "true";
    private String json = "";
    private String[] items;
    private int state = REQUEST_PENDING;



    public MyRequest(String sql) throws InternetProblemException {
        String query = "";
        try {
            query = String.format("pwd=%s&sql=%s&json=%s",
                    URLEncoder.encode(PASSWORD, charset),
                    URLEncoder.encode(sql, charset),
                    URLEncoder.encode(withJson, charset));
        } catch (UnsupportedEncodingException e) {
            Log.e("url", "UnsupportedEncodingException: " + e.getMessage());
        }
        Log.d("ja", url+"?"+query);

        final String finalQuery = query;
        final MyRequest myRequest = this;
        Thread internetThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url+"?"+ finalQuery).openStream()));

                    String line;
                    while ((line = in.readLine()) != null) {
                        json += line;
                    }
                    in.close();
                    myRequest.state = MyRequest.REQUEST_COMPLETE;
                } catch (MalformedURLException e) {
                    Log.e("url", "Malformed URL: " + e.getMessage());
                    myRequest.state = MyRequest.REQUEST_ERROR;
                } catch (IOException e) {
                    Log.e("url","I/O Error: " + e.getMessage());
                    myRequest.state = MyRequest.REQUEST_ERROR;
                }
            }
        });
        Log.d("thread", "start");
        internetThread.start();
        Log.d("thread", "started");

        try {
            internetThread.join();
            if (state == REQUEST_ERROR)
                throw new InternetProblemException();
            Log.d("thread", "joined");
        } catch (InterruptedException e){
            Log.e("url","InterruptedException: " + e.getMessage());
            throw new InternetProblemException();
        }

        if (json!=""){
            json = json.substring(1, json.length() - 1); //remove []
            Log.e("json",json);

            items = json.split("\\},\\{");
        }

    }

    public void returnJSON(String json){

    }


    public String getContentFromRow(int row, String attr){

        String rowStr = items[row];
        int i = rowStr.indexOf("\""+attr+"\"")+attr.length()+4; //index of start of content
        String value = rowStr.substring(i, rowStr.indexOf("\"",i));
        Log.d("table",value);
        if (value.equals("ull,")||value.equals("ull}"))
            return null;
        return value;
    }

    public String[] getAllContents(String attr){
        String[] out = new String[items.length];
        for (int row = 0; row < items.length; row++) {
            out[row]= getContentFromRow(row, attr);
        }
        return out;
    }

    public int getEntries(){
        return items.length;
    }


}
