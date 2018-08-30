package com.example.amit.myassignment.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dell .
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "Artists.db";
    public static final String ARTISTS_TABLE_NAME = "Artists";
    public static final String  ARTISTS_COLUMN_ID = "id";
    public static final String  ARTISTS__ID = "artist_id";
    public static final String  ARTISTS_COLUMN_NAME = "artist_name";
    public static final String  ARTISTS_COLUMN_Image = "artist_image";
    public static final String  ARTISTS_COLUMN_Summary = "artist_summary";
    public static final String  ARTISTS_COLUMN_BioLink = "artist_bio_link";
    public static final String  ARTISTS_COLUMN_Image_path = "artist_image_path";
    public static final String  ARTISTS_COLUMN_ISBOOKMARKED = "artist_bookMarked";
    public static final String  ARTISTS_COLUMN_CACHEDIMAGE = "artist_cachedImg";
    public static final String  ARTISTS_COLUMN_Listener = "artist_listener";

    public static final String  ARTISTS_COLUMN_PBDATE = "artist_infodate";
    public static final String  ARTISTS_COLUMN_LMURL = "artist_lmurl";
    public static final String  ARTISTS_COLUMN_Streamble = "streamable";

    private static final String CREATE_TABLE_ARTISTS = "CREATE TABLE " + ARTISTS_TABLE_NAME
            + "(" + ARTISTS_COLUMN_ID + " INTEGER PRIMARY KEY,"
            + ARTISTS__ID + " TEXT,"
            + ARTISTS_COLUMN_NAME + " TEXT,"
            + ARTISTS_COLUMN_Image + " TEXT," +
            ""+ ARTISTS_COLUMN_Summary + " TEXT,"
            + ARTISTS_COLUMN_BioLink + " TEXT,"
            + ARTISTS_COLUMN_Image_path + " TEXT,"
            + ARTISTS_COLUMN_CACHEDIMAGE + " BLOB,"
            + ARTISTS_COLUMN_Listener + " TEXT,"
            + ARTISTS_COLUMN_PBDATE + " TEXT,"
            + ARTISTS_COLUMN_LMURL + " TEXT,"
            + ARTISTS_COLUMN_Streamble + " TEXT,"

            + ARTISTS_COLUMN_ISBOOKMARKED + " TEXT"
            + ")";


    private HashMap hp;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO Auto-generated method stub
        sqLiteDatabase.execSQL(CREATE_TABLE_ARTISTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ARTISTS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String id,String name, String iamgeUrl, String summary, String biolink, String imgepath,String artist_listener,String isbookMarked,String streamble) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("artist_id", id);
        contentValues.put("artist_name", name);
        contentValues.put("artist_image", iamgeUrl);
        contentValues.put("artist_summary", summary);
        contentValues.put("artist_bio_link", biolink);
        contentValues.put("artist_image_path", imgepath);
        contentValues.put("artist_image_path", imgepath);
        contentValues.put("artist_listener", artist_listener);
        contentValues.put("artist_bookMarked", isbookMarked);
        contentValues.put("streamable", streamble);
        db.insert("Artists", null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from Artists", null);
        return res;
    }
    public Cursor getSortedData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Artists order by id desc limit 30", null);
        return res;
    }

    public Cursor getArtistDataDB(String mbid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Artists where id ="+mbid, null);
        return res;
    }
    public Cursor getBookMarked() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select  *"+" from Artists where artist_bookMarked ="+"1", null);
        return res;
    }

    public Cursor getNoOfBookMarked() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select  count(*)"+" from Artists where artist_bookMarked ="+"1", null);
        return res;
    }


    public boolean updateCacheImage(Integer id, Bitmap bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        byte[] img = Util.getBytes(bitmap);
        contentValues.put("artist_cachedImg", img);

        db.update("Artists", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }





    public boolean updateArtistDetailsDB(String id,String summary,String pbdate,String lmurl,String imUrl,String bioUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("artist_bio_link", bioUrl);
        contentValues.put("artist_summary", summary);
        contentValues.put("artist_infodate", pbdate);
        contentValues.put("artist_lmurl", lmurl);
      //  db.update("Artists", contentValues, "artist_id ="+id, null);
        db.update("Artists", contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public boolean bookMarkDB(String id,String bookMarked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTISTS_COLUMN_ISBOOKMARKED, bookMarked);
        //  db.update("Artists", contentValues, "artist_id ="+id, null);
        db.update("Artists", contentValues, "id = ? ", new String[]{id});
        return true;
    }
    public Cursor getBookMarkedData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Artists where artist_bookMarked="+"true"+" order by id desc limit 30", null);
        return res;
    }




    public Bitmap getImage(int i){
        ArrayList<Bitmap> img = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qu = "select artist_cachedImg  from artists where id=" +i ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
            cur.close();

            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null ;
    }

    public byte[] getImageBYTE(int i){
        SQLiteDatabase db = this.getReadableDatabase();
        String qu = "select artist_cachedImg  from artists where id=" +i ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
            cur.close();

            return imgByte;
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null ;
    }


}
