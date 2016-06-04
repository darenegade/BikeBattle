package edu.hm.cs.bikebattle.app.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Zwen on 04.06.2016.
 */
public class URIParser extends AsyncTask<String, Void, Bitmap> {
  private ImageView view;

  public URIParser(ImageView view) {
    this.view = view;
  }

  @Override
  protected Bitmap doInBackground(String... params) {
    String uri = params[0];
    try {
      URL url = new URL(uri);
      HttpURLConnection connection = (HttpURLConnection) url
          .openConnection();
      connection.setDoInput(true);
      connection.connect();
      InputStream input = connection.getInputStream();
      Bitmap myBitmap = BitmapFactory.decodeStream(input);
      System.out.printf("Bitmap", "returned");
      myBitmap = Bitmap.createScaledBitmap(myBitmap, 100, 100, false);//This is only if u want to set the image size.
      return myBitmap;

    } catch (IOException e) {
      e.printStackTrace();
      System.out.printf("Exception", e.getMessage());
      return null;
    }
  }

  @Override
  protected void onPostExecute(Bitmap drawable) {
    view.setImageBitmap(drawable);
  }
}
