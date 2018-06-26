package com.auto.archcomponentsanalyst.WorkManager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.auto.archcomponentsanalyst.AACApplicaiton;

import java.util.Set;

/**
 * Created by haohuidong on 18-6-25.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "AAC_DB";
    private static final int DB_VERSION = 1;

    /////////////////////////////////////////--> 18-6-25 下午7:13 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> WorkManager request <-- ↓↓↓/////////////////////////////////////
    public static final String TABLE_WM_REQUEST = DB_NAME+"_WM_REQUEST";
    private final String create_header = "CREATE TABLE IF NOT EXISTS ";
    private String create_table_wm_request = create_header + TABLE_WM_REQUEST + " (Id integer primary key, request_id text UNIQUE not null," +
            "type integer not null, enqueue_date integer not null)";

    public final static String COLUMN_WM_REQUEST_ID = "request_id";
    public final static String COLUMN_WM_REQUEST_ENQUEUE_DATE = "enqueue_date";
    public final static String COLUMN_WM_REQUEST_TYPE = "type";

    public final static String QUERY_WM_REQUEST = "SELECT * FROM " + TABLE_WM_REQUEST;
    public final static String DELETE_WM_REQUEST_BY_ID= DatabaseHelper.COLUMN_WM_REQUEST_ID + " =? ";
    /////////////////////////////////////↑↑↑ --> WorkManager request <-- ↑↑↑/////////////////////////////////////

    /////////////////////////////////////////--> 18-6-25 下午7:19 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> singtone <-- ↓↓↓/////////////////////////////////////
    private static DatabaseHelper helper = null;
    public static DatabaseHelper getInstance() {
        if (helper == null)
            synchronized (DatabaseHelper.class) {
                if (helper == null){
                    helper = new DatabaseHelper(AACApplicaiton.getInstance().getApplicationContext());
                }
            }
        return helper;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    /////////////////////////////////////↑↑↑ --> singtone <-- ↑↑↑/////////////////////////////////////

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_table_wm_request);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /////////////////////////////////////////--> 18-6-25 下午7:33 <--/////////////////////////////////////
    /////////////////////////////////////↓↓↓ --> fun <-- ↓↓↓/////////////////////////////////////
    public void insertTableByName(String tableName, ContentValues values){
        SQLiteDatabase db = null;
        try{
            db = helper.getWritableDatabase();
            db.insert(tableName, null, values);
        }catch (Exception e){

        }finally {
            db.close();
        }
    }

    public void insertOrReplaceTableByName(final String tableName, final ContentValues values){
        if (values == null)
            return;
        Set<String> keySet = values.keySet();
        StringBuilder keySql = new StringBuilder();
        StringBuilder valueSql = new StringBuilder();
        for (String key : keySet) {
            keySql.append("," + key + " ");

            Object value = values.get(key) ;
            if (value instanceof  String){
                String tmp;
                if (value == null)
                    tmp = "";
                else
                    tmp = value.toString();

                if (!TextUtils.isEmpty(tmp) && tmp.contains("'"))
                    tmp = tmp.replace("'", "''");
                value = tmp;
            }
            valueSql.append(",'" + value + "' ");
        }
        String sql = "INSERT OR REPLACE INTO " + tableName + " " + keySql.replace(0, 1, "(").append(")").toString() + " VALUES " + valueSql.replace(0, 1, "(").append(")").toString();
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.execSQL(sql);
        } catch (Exception e) {
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void selectTableBySQL(String sql, CursorAction action, String... args){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            db = helper.getWritableDatabase();
            cursor = db.rawQuery(sql, args);
            if (cursor != null){
                action.action(cursor);
            }else{
                Log.e("--> DataBaseHelper <--", "cursor is null.");
            }
        } catch (Exception e){
            Log.e("--> DataBaseHelper <--", e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
    }

    public int delete(String table, String whereClause, String... whereArgs) {
        int raw = 0;
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            raw = db.delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return raw;
    }
    /////////////////////////////////////↑↑↑ --> fun <-- ↑↑↑/////////////////////////////////////
}
