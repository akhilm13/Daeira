package app.jugaad.daeira;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Akhil on 31-03-2018.
 */

/*
Class to receive intent from alarm clock manager to download articles
and clean  the database everyday
 */
public class AlarmReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Service Started", Toast.LENGTH_SHORT).show();

        //Cleaning up of articles that have been marked read
        SQLiteHelper database = new SQLiteHelper(context);
        database.deleteArticlesAlreadyDelivered();

        //Download new Articles
        context.startService(new Intent(context, DownloadArticlesService.class));
    }
}
