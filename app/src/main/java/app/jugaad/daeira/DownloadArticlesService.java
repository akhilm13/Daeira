package app.jugaad.daeira;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
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
            URL url = new URL("http://10.3.4.143:8080/api/");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            StringBuffer response = new StringBuffer();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == urlConnection.HTTP_OK) {
                Log.e("Connection established", "Yay!");
                BufferedReader inurl = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = inurl.readLine()) != null) {
                    response.append(inputLine);
                }
                inurl.close();
            }
            else{

                Log.e("Failure", "Server returned "+responseCode);
                return;
            }

            String finalResponse = response.toString();
            JSONArray jsonArray = new JSONArray(finalResponse);

            String title;
            String article;

            for (int i =0; i<jsonArray.length(); i++){

                JSONObject jObject = jsonArray.getJSONObject(i);
                title = jObject.getString("title");
                article = jObject.getString("article");

                Log.e("TITLE "+i, title);
                Log.e("ARTICLE"+i, article);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception message", e.getMessage());
        }

    }
}
