package app.jugaad.daeira;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    boolean alarmServiceStatusRunning = false;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    DownloadArticleStatusReceiver reciever;
    boolean connectionStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Check if this is the first run of the app.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            Toast.makeText(this, "Please click Start Service to Download Articles", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
            Log.e("First Run", "Check");
        }



    }
    public void startReading (View view){

        startActivity(new Intent(this, SelectArtcleToReadActivity.class));
    }

    // Broadcast receiver for receiving status updates from the IntentService DownloadArticlesService
    private class DownloadArticleStatusReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private DownloadArticleStatusReceiver() {
        }
        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getExtras().getString(Constants.BROADCAST_STATUS);
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
        }


    }

    public void startService(View view){

        if (!alarmServiceStatusRunning) {

            //Downloads everytime the service is started
            startService(new Intent(this, DownloadArticlesService.class));
            IntentFilter statusIntentFilter = new IntentFilter(
                    Constants.BROADCAST_ACTION);
            reciever = new DownloadArticleStatusReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(reciever, statusIntentFilter);

            Intent alarmIntent = new Intent(this, AlarmReciever.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0,alarmIntent,0);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            long interval =  5184000; //24 hours

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            alarmServiceStatusRunning = true;
            ((Button) view).setText("Stop Service");
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Intent alarmIntent = new Intent (this, AlarmReciever.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0,alarmIntent,0);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(reciever);
            alarmServiceStatusRunning = false;
            ((Button) view).setText("Start Service");

        }
    }



}
