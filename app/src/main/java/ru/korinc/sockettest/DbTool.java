package ru.korinc.sockettest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;


public class DbTool{

    private static final String TAG = "kor_ka Log";

    DBHelper dbHelper;
    ContentValues cv;
    final int DB_VERSION = 1;

    public final static String BUTTONS_TABLE = "buttons";
    public final static String SLIDERS_BTNS_TABLE = "sliderButtons";
    public final static String DESKTOPS_BTNS_TABLE = "desktopButtons";

    public final static String BUTTONS_TABLE_NAME = "name";
    public final static String BUTTONS_TABLE_TYPE = "type";
    public final static String BUTTONS_TABLE_CMD = "cmd";
    public final static String BUTTONS_TABLE_ORDER = "order";

    public final static String SLIDERS_BTNS_PAGE_ID = "pageId";
    public final static String SLIDERS_BTNS_BTN_ID = "btnId";

    public final static String DESKTOPS_BTNS_NAME = "desktopName";
    public final static String DESKTOPS_BTNS_BTN_ID = "btnId";
    public final static String DESKTOPS_BTNS_ORDER = "order";

    public void addButton (String name, int type, String cmd, int order, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv = new ContentValues();

        cv.put("name",BUTTONS_TABLE_NAME);
        cv.put("type",BUTTONS_TABLE_TYPE);
        cv.put("cmd",BUTTONS_TABLE_CMD);
        cv.put("order",BUTTONS_TABLE_ORDER);


        db.insert(BUTTONS_TABLE, null, cv);
        db.close();
    }

    public void insert (String table, ContentValues cv, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.insert(table, null, cv);
        db.close();
    }


    public void delRec(String table, long id, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(table, "_id" + " = " + id, null);
        db.close();

    }




    public Cursor getCursor(String currentTable,Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(currentTable, null, null, null, null, null, null);

        return c;
    }


    public void updateById (String table, Context context, String idUpd, ContentValues cvUpd){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(table, cvUpd, "_id = ?", new String[] { idUpd });

        db.close();
    }


    class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context){
            // конструктор суперкласса
            super(context, "db", null, DB_VERSION); }

        @Override
        public void onCreate(SQLiteDatabase db){
            Log.d(TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            String TEXT = "TEXT";
            String INTEGER = "INTEGER";
            String CM = ", ";
            db.execSQL("create table "+BUTTONS_TABLE+" (" + "_id integer primary key autoincrement," + BUTTONS_TABLE_NAME + TEXT + CM +
                                                                                                     BUTTONS_TABLE_TYPE + INTEGER + CM +
                                                                                                     BUTTONS_TABLE_CMD + TEXT + CM +
                                                                                                     BUTTONS_TABLE_ORDER + INTEGER +");");

            db.execSQL("create table "+SLIDERS_BTNS_TABLE+" (" + "_id integer primary key autoincrement," + SLIDERS_BTNS_PAGE_ID + TEXT + CM +
                                                                                                            SLIDERS_BTNS_BTN_ID + INTEGER +");");

            db.execSQL("create table "+DESKTOPS_BTNS_TABLE+" (" + "_id integer primary key autoincrement," + DESKTOPS_BTNS_NAME + TEXT + CM +
                                                                                                            DESKTOPS_BTNS_BTN_ID + INTEGER + CM +
                                                                                                            DESKTOPS_BTNS_ORDER + INTEGER +");");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }
    }

    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = (Date) simpledateformat.parse(aDate, pos);
        return stringDate;

    }

}