package com.example.asami.json;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    String weather;

    public class Download extends AsyncTask<String , Void , String>
       {



           @Override
           protected String doInBackground(String... urls) {
               String code = null;
               URL url ;
               HttpURLConnection connect;

               try {
                   url = new URL(urls[0]);

                   connect = (HttpURLConnection) url.openConnection();

                   InputStream stream = connect.getInputStream();

                   InputStreamReader reader = new InputStreamReader(stream);

                   int data = reader.read();

                   while (data!=-1)
                      {

                          char current = (char) data;

                          code+=current;

                          data = reader.read();


                      }
                   return code;
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               } catch (Exception e)
               {
                    e.printStackTrace();
               }

                  return "Failed";

           }

           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);

               //convert the string we got into json data from the result string which contain the result of an api weather call

               try {
                   JSONObject j;
                   j = new JSONObject(s);

                   //now you can extract particular stuff that you want from the jason object
                   //this is a structure that the waether api created for us it not likely to change tthat much always to get the werathe part you extract weather from the json object

                    weather = j.getString("weather");

                     //now to extract stuff from the string , you can convert it to json array sisce the string looks like an array
                   //json array contains json objects
                   JSONArray arr = new JSONArray(weather);
                    //now using loops convert each part as an jason object tto get what you want easily

                   for (int i =0 ;i<arr.length(); i++)
                   {
                       JSONObject part = arr.getJSONObject(i); // the ith object

                       Log.i("main",part.getString("main"));
                       Log.i("Description",part.getString("description"));


                   }

               } catch (JSONException e) {
                   e.printStackTrace();
               }


           }
       }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Download task = new Download();



        try {
            task.execute("http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a11").get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }
}
