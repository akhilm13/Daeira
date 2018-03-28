package app.jugaad.daeira;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

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
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
