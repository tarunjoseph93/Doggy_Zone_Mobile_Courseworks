package com.example.doggyzone.functions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageLoadASyncTask extends AsyncTask<Void, Void, Bitmap> {

    private String displayPicUrl;
    private ImageView displayPicIV;

    public ImageLoadASyncTask(String displayPicUrl, ImageView displayPicIV) {
        this.displayPicUrl = displayPicUrl;
        this.displayPicIV = displayPicIV;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        try {
            URL urlConnection = new URL(displayPicUrl);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        displayPicIV.setImageBitmap(result);
    }
}
