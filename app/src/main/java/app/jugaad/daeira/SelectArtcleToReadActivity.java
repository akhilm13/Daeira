package app.jugaad.daeira;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectArtcleToReadActivity extends AppCompatActivity {

    //ProgressDialog progressDialog;
    ListView articlesToReadListView;
    List<String > articleTitles = new ArrayList<>();
    List<Integer> articleIds = new ArrayList<>();
    Context context;
    int selectedIndex;
    String sampleStringArray[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_artcle_to_read);



        context = this.getApplicationContext();
        articlesToReadListView = (ListView) findViewById(R.id.articles_to_read_list_view);

        //Item click listener to choose article title to read
        articlesToReadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = parent.getItemAtPosition(position).toString();
                Log.e("Selected Article: ", title);
                selectedIndex = articleTitles.indexOf(title);
                Log.e("Selected index: ", selectedIndex+"");

                showArticle();
            }
        });

     //progressDialog = new ProgressDialog(context);
        new AccessDatabase().execute();
    }


    /*
    Stuffs the title and article ID into an intent and starts the Activity to display the article
     */
    void showArticle(){

        Intent intent = new Intent(this, ReadArticleActivity.class);
        intent.putExtra(Constants.ARTICLE_ID_INTENT_KEY, new String[]{articleIds.get(selectedIndex).toString(),
                articleTitles.get(selectedIndex) });
        startActivity(intent);
    }

    /*
    Private subclass for the AsyncTask to access the database to get a list of all tittles available.
    Performs in the background and updates the List View once done.
     */
    private class AccessDatabase extends AsyncTask<Void, Void, Void>{

        /*
        Loading is instantaneous. Hence no progress dialogue required.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog.setMessage("Loading your articles ...");
            //progressDialog.show();
        }

        /*
        Gets all the titles that have not been delivered already.
         */
        @Override
        protected Void doInBackground(Void... voids) {

            Log.e("Started Access", "Querying db");
            SQLiteHelper databaseHelper = new SQLiteHelper(context);
            HashMap<Integer, String> articleMap = databaseHelper.getTitles();
            Log.e("Got Hashmap: Size = ", articleMap.size()+"");
            for (HashMap.Entry<Integer, String> entry : articleMap.entrySet()){
                articleIds.add(entry.getKey());
                articleTitles.add(entry.getValue());
            }
            return null;
        }

        /*
        Puts the titles from the database into a String Array and puts it into a List View using Array Adapter.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (articleTitles.size()!=0) {
                sampleStringArray = articleTitles.toArray(new String[0]);

                final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, sampleStringArray);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        articlesToReadListView.setAdapter(adapter);
                    }
                });

            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Error. No articles to read", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
