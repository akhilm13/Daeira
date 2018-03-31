package app.jugaad.daeira;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ReadArticleActivity extends AppCompatActivity {

    String articleTitle;
    int articleID;
    TextView titleTextView;
    TextView articleTextView;
    Context context;
    String articleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_article);

        //Gets the article ID and title that was chosen from the previous activity
        Intent intent = getIntent();
        articleID = Integer.parseInt(intent.getStringArrayExtra(Constants.ARTICLE_ID_INTENT_KEY)[0]);
        articleTitle = intent.getStringArrayExtra(Constants.ARTICLE_ID_INTENT_KEY)[1];
        context = this.getApplicationContext();

        titleTextView = (TextView) findViewById(R.id.title__text);
        articleTextView = (TextView) findViewById(R.id.article_text);
        articleTextView.setMovementMethod(new ScrollingMovementMethod());
        new getArticle().execute();


    }

    /*
    Gets the article from the database and displays it in the UI
     */
    private class getArticle extends AsyncTask <Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            SQLiteHelper database = new SQLiteHelper(context);

            articleText  = database.getArticleByID(articleID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    titleTextView.setText(articleTitle);
                    articleTextView.setText(articleText);

                }
            });
        }
    }

    /*
    Function to mark the article as read so that it can be deleted later.
    Not following best practises as the database access is performed in the main thread
    as it is guaranteed to be super fast. Stack Overflow thinks it's okay too.
     */
    public void markAsRead(View view){

        SQLiteHelper database = new SQLiteHelper(context);
        database.markArticleDelivered(articleID);
        Toast.makeText(context, "Marked as read", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, SelectArtcleToReadActivity.class));
    }

}
