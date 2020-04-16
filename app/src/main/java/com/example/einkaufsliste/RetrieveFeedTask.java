package com.example.einkaufsliste;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class RetrieveFeedTask extends AsyncTask<URL, String, Void> {

    @Override
    protected Void doInBackground(URL... urls) {
        String output = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
            try {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                InputStreamReader ipr = new InputStreamReader(in);
                BufferedReader rd = new BufferedReader(ipr);

                String line;
                while ((line = rd.readLine()) != null) {
                    output+=(line);
                }
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            InputStream ip = connection.getInputStream();



        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute() {
        // TODO: check this.exception
        // TODO: do something with the feed
    }

}
