package com.example.doggyzone.map_functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadURL {
    public String downloadURL(String string) throws IOException {
        String urlData = "";
        HttpURLConnection conn = null;
        InputStream inputStream = null;

        try {
            URL getURL = new URL(string);
            conn = (HttpURLConnection) getURL.openConnection();
            conn.connect();
            inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringbuffer = new StringBuffer();
            String line = "";

            while((line = bufferedReader.readLine()) != null) {
                stringbuffer.append(line);
            }

            urlData = stringbuffer.toString();
            bufferedReader.close();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            inputStream.close();
            conn.disconnect();
        }
        return urlData;
    }
}
