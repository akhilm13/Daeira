package app.jugaad.daeira;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /*Function to insert an article into the table.
    Takes title and article and inserts it into the table
    */
    public void insert (String title, String article){

        //writable database instance
        SQLiteDatabase database = getWritableDatabase();

        //Create ContentValue and add the entry to it
        ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleModel.COLUMN_TITLE, title);
        contentValues.put(ArticleModel.COLUMN_ARTICLES, article);
        contentValues.put(ArticleModel.COLUMN_DELIVERED, String.valueOf(0));
        //Insert into table in the database
        long id = database.insert(ArticleModel.TABLE_NAME, null, contentValues);

        Log.e("New Insertion #: ", id+"");
        //close connection with the database
        database.close();
    }

    /*Function to get all the titles in the table.
    Takes no arguements and returns a Map with ID as key and TITLE as value
    */
    public HashMap<Integer,String> getTitles(){

        //Get a readable instance of the database
        SQLiteDatabase database = getReadableDatabase();

        //Select titles query
        String titlesQuery = "SELECT "+ArticleModel.COLUMN_ID+" , "+ ArticleModel.COLUMN_TITLE+ " FROM "+ArticleModel.TABLE_NAME +" WHERE "
                + ArticleModel.COLUMN_DELIVERED + " = 0";

        //Get the queryset into a Cursor
        Cursor cursor = database.query(ArticleModel.TABLE_NAME, new String[]{ArticleModel.COLUMN_ID, ArticleModel.COLUMN_TITLE, ArticleModel.COLUMN_DELIVERED},
                ArticleModel.COLUMN_DELIVERED + " = ?", new String[]{String.valueOf(0)},null,null,null);



        //looping through the result
        HashMap<Integer, String > titlesList = new HashMap<Integer, String>();

        //For logging
        int tempID;
        String tempTile;

        if (cursor.moveToFirst()){
            do {
                tempID = cursor.getInt(cursor.getColumnIndex(ArticleModel.COLUMN_ID));
                tempTile = cursor.getString(cursor.getColumnIndex(ArticleModel.COLUMN_TITLE));
                titlesList.put(tempID, tempTile);

                Log.e("Got from database: "+tempID, tempTile);
                Log.e("Delivered Value = ", ""+cursor.getInt(cursor.getColumnIndex(ArticleModel.COLUMN_DELIVERED)));
            } while (cursor.moveToNext());
        }
        else{
            //todo remove
            Log.e("The cursor is Null", "the SQL");
        }

        //Free cursor and close database connection
        cursor.close();
        database.close();
        return titlesList;
    }

    /*Function to get an article by the ID.
    Takes ID as int and returns String
    */
    public String getArticleByID (int id){

        String article = "";
        //Get a readable instance of the database
        SQLiteDatabase database = getReadableDatabase();

        //SQL Query to execute
        //String query = "SELECT "+ArticleModel.COLUMN_ARTICLES+" FROM "+ArticleModel.TABLE_NAME+" WHERE "+ArticleModel.COLUMN_ID
            //    +" = "+id;

        //Get Cursor from executing the query
        Cursor cursor = database.query(ArticleModel.TABLE_NAME, new String[]{ArticleModel.COLUMN_ARTICLES},ArticleModel.COLUMN_ID+" = ?",
                new String[]{String.valueOf(id)},null,null,null);

        if (cursor != null){
            cursor.moveToFirst();

            article = cursor.getString(cursor.getColumnIndex(ArticleModel.COLUMN_ARTICLES));
        }

        //Free cursor and close connection to database
        cursor.close();
        database.close();

        return article;
    }

    /*
        Function to mark an article as read so that it can be cleared from the database
        Takes ID as argument and updates the value in the table. Returns nothing
     */
    public void markArticleDelivered(int id){

        //Get writable instance of the database
        SQLiteDatabase database = getWritableDatabase();

        //Content Value to perform the updation
        ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleModel.COLUMN_DELIVERED, 1);

        //Update value to the table
        database.update(ArticleModel.TABLE_NAME, contentValues, ArticleModel.COLUMN_ID+" = ?",
                new String[]{String.valueOf(id)});

        database.close();
    }

    /*
    Function to delete articles by ID/
    Takes ID as argument and deletes corresponding article from the database.
    Returns nothing.
     */
    public void deleteArticles(int id){

        //Get a writable instance of the database
        SQLiteDatabase database = getWritableDatabase();

        //Delete from the table
        database.delete(ArticleModel.TABLE_NAME, ArticleModel.COLUMN_ID+" = ? ",
                new String []{String.valueOf(id)});

        //Close connection
        database.close();
    }

    /*
    Function to find and delete all articles that have already been delivered.
    Takes no arguments and returns number of articles deleted.
     */
    public int deleteArticlesAlreadyDelivered(){

        int numberOfArticlesDeleted = 0;

        //Get Readable Instance of the database
        SQLiteDatabase database = getReadableDatabase();


        //Cursor to get the IDs of all the articles that are already delivered
        Cursor cursor = database.query(ArticleModel.TABLE_NAME, new String[]{ArticleModel.COLUMN_ID}, ArticleModel.COLUMN_DELIVERED+" = ?",
                new String[]{"1"},null,null,null);

        List<Integer> listOfIDs = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                listOfIDs.add(cursor.getInt(cursor.getColumnIndex(ArticleModel.COLUMN_ID)));
            }while (cursor.moveToNext());
        }

        //Free cursor and close connection
        cursor.close();
        database.close();

        //Delete each article from the table
        for (int articleId : listOfIDs){
            deleteArticles(articleId);
            numberOfArticlesDeleted++;
        }

        //return the total number of articles deleted
        return numberOfArticlesDeleted;
    }

}
