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
    public final static String BUTTONS_TABLE_ORDER = "morder";

    public final static String SLIDERS_BTNS_PLACE_ID = "pageId";
    public final static String SLIDERS_BTNS_BTN_ID = "btnId";

    public final static String DESKTOPS_BTNS_NAME = "desktopName";
    public final static String DESKTOPS_BTNS_BTN_ID = "btnId";
    public final static String DESKTOPS_BTNS_ORDER = "morder";

    public long addButton (long id ,String name, int type, String cmd, int order, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv = new ContentValues();
        if(id!=-1){

            cv.put(BUTTONS_TABLE_NAME, name);
            cv.put(BUTTONS_TABLE_TYPE, type);
            cv.put(BUTTONS_TABLE_CMD, cmd);
            cv.put(BUTTONS_TABLE_ORDER, order);
            db.update(BUTTONS_TABLE, cv, "_id=?", new String[]{id+""});
        }else{
            cv.put(BUTTONS_TABLE_NAME, name);
            cv.put(BUTTONS_TABLE_TYPE, type);
            cv.put(BUTTONS_TABLE_CMD, cmd);
            cv.put(BUTTONS_TABLE_ORDER, order);
            String and = " AND ";
            Cursor c = db.query(BUTTONS_TABLE, new String[]{}, BUTTONS_TABLE_NAME + " like ?" + and + BUTTONS_TABLE_TYPE + " = " + type + and + BUTTONS_TABLE_CMD + " like ?", new String[]{name, cmd
            }, null, null, null);
            if(c.moveToFirst()){
                id = c.getLong(c.getColumnIndex("_id"));
            }else{
                id = db.insert(BUTTONS_TABLE, null, cv);
            }
        }
/*
        String sql = "INSERT INTO " + BUTTONS_TABLE + " (" + BUTTONS_TABLE_NAME + ", " + BUTTONS_TABLE_TYPE + ", "  + BUTTONS_TABLE_CMD + ", " + BUTTONS_TABLE_ORDER + ")" +
                       " VALUES " + "(" + "\'" + name + "\'" + ", " + type + ", " + "\'" + cmd + "\'" + ", " + order + ");";
        db.execSQL(sql);
  */
        db.close();

        return  id;
    }

    public long getButtonIdByPlace(String place, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /*
        Cursor c2log = db.query(false, SLIDERS_BTNS_TABLE, null, null, null, null,null,null,null);
        InAppLog.writeLog(act, "", ">>>>>>");
        if(act!=null && c2log.moveToFirst()){
            do{

                InAppLog.writeLog(act, "", c2log.getString(c2log.getColumnIndex(SLIDERS_BTNS_PLACE_ID))+"|"+c2log.getInt(c2log.getColumnIndex(SLIDERS_BTNS_BTN_ID)));

            }while (c2log.moveToNext());
        }
        InAppLog.writeLog(act, "", "<<<<<<");
        */
        Cursor c = db.query(false, SLIDERS_BTNS_TABLE, new String[]{SLIDERS_BTNS_BTN_ID}, SLIDERS_BTNS_PLACE_ID + " like '" +place+"'", null, null,null,null,null);
        if(c.moveToFirst()){
            return c.getLong(c.getColumnIndex(SLIDERS_BTNS_BTN_ID));
        }else return -1;
    }


    //Пишется криво! Не оверрайдит существующие, бомбит всё новое!!
    public void bindButtonToPlace(long buttonId, String place, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(buttonId!=-1){
            cv = new ContentValues();
            cv.put(SLIDERS_BTNS_BTN_ID, buttonId);
            cv.put(SLIDERS_BTNS_PLACE_ID, place);

            db.insertWithOnConflict(SLIDERS_BTNS_TABLE, null, cv, db.CONFLICT_REPLACE);
        }else{
            db.delete(SLIDERS_BTNS_TABLE, SLIDERS_BTNS_PLACE_ID + " like '" + place + "'", null);
        }


        db.close();
    }

    public Cursor getButtonCursor(long id, Context context){
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(BUTTONS_TABLE, null, "_id=" + id, null, null, null, null);

        return c.moveToFirst()?c:null;
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
            String TEXT = " TEXT ";
            String INTEGER = " INTEGER ";
            String CM = ", ";
            db.execSQL("create table "+BUTTONS_TABLE+" (" + "_id integer primary key autoincrement," + BUTTONS_TABLE_NAME + TEXT + CM +
                                                                                                     BUTTONS_TABLE_TYPE + INTEGER + CM +
                                                                                                     BUTTONS_TABLE_CMD + TEXT + CM +
                                                                                                     BUTTONS_TABLE_ORDER + INTEGER +");");

            db.execSQL("create table "+SLIDERS_BTNS_TABLE+" ("  + SLIDERS_BTNS_PLACE_ID + TEXT + " primary key " + CM +
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