package app.jugaad.daeira;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Akhil on 28-03-2018.
 */

public class DownloadArticlesService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadArticlesService(String name) {
        super(name);
    }

    public DownloadArticlesService() {
        super("null");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)  {


        try {
            //URL To connect and make a GET request to
            URL url = new URL("http://192.168.0.101:8000/api/");

            //Open an HTTP URL Connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(600000);
            urlConnection.getReadTimeout();
            //If response code is OK then read contents to a string buffer
            StringBuffer response = new StringBuffer();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == urlConnection.HTTP_OK) {
                Log.e("Connection established", "Yay!");
                BufferedReader inurl = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String inputLine;

                //The string is read into response
                while ((inputLine = inurl.readLine()) != null) {
                    response.append(inputLine);
                }
                inurl.close();
            }
            //Check if the response code is not OK (ie: not 200)
            else{
                Log.e("Failure", "Server returned "+responseCode);
                return;
            }

            //Parse the response received and create JSON Objects
            String finalResponse = response.toString();
            JSONArray jsonArray = new JSONArray(finalResponse);

            String title;
            String article;

            //Attempting to populate the database
            //sqlite helper for all the database operations
            SQLiteHelper databaseHelper = new SQLiteHelper(this.getApplicationContext());

            //Extract title and article from the JSON Objects
            //and add them to the database
            for (int i =0; i<jsonArray.length(); i++){

                JSONObject jObject = jsonArray.getJSONObject(i);
                title = jObject.getString("title");
                article = jObject.getString("article");

                Log.e("TITLE "+i, title);
                Log.e("ARTICLE"+i, article);

                databaseHelper.insert(title, article);
            }

            //Report the status back to the main activity by broadcasting intent
            String status = "The download is complete";
            Intent localIntent =
                    new Intent(Constants.BROADCAST_ACTION)
                            // Puts the status into the Intent
                            .putExtra(Constants.BROADCAST_STATUS, status);
            // Broadcasts the Intent to receivers in this app.
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);



        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception message", e.getMessage());
        }

    }
}
