package app.jugaad.daeira;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import app.jugaad.daeira.Models.ArticleModel;

/**
 * Created by Akhil on 30-03-2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "article_db";
    static final int DATABASE_VERSION    = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create table
        db.execSQL(ArticleModel.QUERY_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + ArticleModel.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    
}
