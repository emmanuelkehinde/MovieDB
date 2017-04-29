package com.kehinde.moviedb.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KEHINDE on 25-Apr-17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE="Movie.db";
    public static final String TABLE_NAME= "movies";
    public static final String COL_1="ID";
    public static final String COL_2="M_ID";
    public static final String COL_3="TITLE";
    public static final String COL_4="POSTER_URL";
    public static final String COL_5="SYNOPSIS";
    public static final String COL_6="RATING";
    public static final String COL_7="RELEASE_DATE";



    public DatabaseHelper(Context context) {
        super(context,DATABASE,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,M_ID TEXT,TITLE TEXT,POSTER_URL TEXT,SYNOPSIS TEXT,RATING TEXT,RELEASE_DATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String m_id, String title, String poster_url, String synopsis, String rating, String release_date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_2,m_id);
        cv.put(COL_3,title);
        cv.put(COL_4,poster_url);
        cv.put(COL_5, synopsis);
        cv.put(COL_6, rating);
        cv.put(COL_7, release_date);
        long result=db.insert(TABLE_NAME,null,cv);
        if (result==-1)
            return false;
        else
            return true;
    }

    public Cursor viewAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from " + TABLE_NAME +" ORDER BY " + COL_1 +" DESC", null);
        return res;
    }

    public boolean deleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{id});
        return true;
    }

    public boolean isLocal(String m_id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ TABLE_NAME +" WHERE M_ID="+m_id,null);
        return res.getCount() > 0;
    }

    public String getMovieLocalID(String movieId) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select ID from "+ TABLE_NAME +" WHERE M_ID="+movieId,null);
        while (cursor.moveToNext()) {
            return String.valueOf(cursor.getInt(0));
        }
        return null;
    }
}
