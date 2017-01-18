package com.almayandex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.almayandex.domain.MyPoint;

import java.util.LinkedList;
import java.util.List;

import ru.yandex.n;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by User on 017 17.01.17.
 */

public class DbUtils extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String LOG_TAG = "DbHelper";

    //названия таблиц
    public static final String DATABASE_NAME = "PhotoDB";
    public static final String CREATE_PHOTO_TABLE = "CREATE TABLE `photos` (\n" +
            "\t`GEO_POINT_LON`\tREAL,\n" +
            "\t`PHOTO`\tBLOB,\n" +
            "\t`DESCRIPTION`\tTEXT,\n" +
            "\t`GEO_POINT_LAT`\tREAL\n" +
            ");";
    public static final String TABLE_NAME ="photos";
    public static final String PHOTO = "PHOTO";
    public static final String GEO_POINT_LON = "GEO_POINT_LON";
    public static final String GEO_POINT_LAT = "GEO_POINT_LAT";
    public static final String DESCRIPTION = "DESCRIPTION";

    public DbUtils(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DbUtils(Context context, String name,  int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PHOTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    public List<MyPoint> getAllPoints(SQLiteDatabase database) {
        List<MyPoint> res = new LinkedList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        String description;
        double lat;
        double lon;
        Bitmap bitmap;
        MyPoint myPoint;
        if (cursor != null && cursor.moveToFirst()) {
            int latIdx = cursor.getColumnIndex(GEO_POINT_LAT);
            int lonIdx = cursor.getColumnIndex(DbUtils.GEO_POINT_LON);
            int photoIdx = cursor.getColumnIndex(DbUtils.PHOTO);
            int descIdx = cursor.getColumnIndex(DbUtils.DESCRIPTION);
            List<Bitmap> photos = new LinkedList<>();
            do {
                lon = cursor.getDouble(lonIdx);
                lat = cursor.getDouble(latIdx);
                description = cursor.getString(descIdx);
                bitmap = BitmapUtil.getImage(cursor.getBlob(photoIdx));
                photos.add(bitmap);
                res.add(new MyPoint(description, photos, new GeoPoint(lat, lon)));
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public void insertPoint(MyPoint myPoint,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DESCRIPTION,myPoint.getDescription());
        contentValues.put(GEO_POINT_LAT,myPoint.getGeoPoint().getLat());
        contentValues.put(GEO_POINT_LON,myPoint.getGeoPoint().getLon());
        contentValues.put(PHOTO,BitmapUtil.getBytes(myPoint.getPhotos().get(0)));
        database.insert(TABLE_NAME,null,contentValues);
    }

}
