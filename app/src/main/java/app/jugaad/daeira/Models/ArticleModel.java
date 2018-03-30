package app.jugaad.daeira.Models;

/**
 * Created by Akhil on 30-03-2018.
 */

public class ArticleModel {

    //Relational metadata
    public static final String TABLE_NAME = "ARTICLES";
    public static final String COLUMN_ID = "ID";
    public static  final String COLUMN_TITLE = "TITLE";
    public static  final String COLUMN_ARTICLES = "ARTICLES";
    public static final String COLUMN_DELIVERED = "DELIVERED";

    //SQL commands
    public final static String QUERY_CREATE_TABLE = "CREATE TABLE "
            +TABLE_NAME
            + " ( "
            + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE
            + " TEXT, "
            + COLUMN_ARTICLES
            + " TEXT, "
            + COLUMN_DELIVERED
            + " INTEGER ";

    //fields for each model
    int id;
    String title;
    String article;
    Boolean delivered;

    public ArticleModel(){

    };

    public ArticleModel(int id, String title, String article)
    {
        this.id = id;
        this.title = title;
        this.article = article;
        delivered = false;
    }

    public int getInt() {

        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getArticle(){
        return article;
    }



}
