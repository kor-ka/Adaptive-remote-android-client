package ru.korinc.sockettest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import java.io.File;

/**
 * Created by korka on 27.01.15.
 */
public class FnButton extends Button{

    public String name;
    public long id;
    public String args;
    public int type;
    public int color;
    public String plugin;
    private OnClickListener ocl;
    private OnLongClickListener olcl;
    private String place;
    private ButtonFnManager fnManager;

    public FnButton(Context context){
        super(context);
    }

    public FnButton(Context context, AttributeSet attrs){
        super(context, attrs);
    }



    public void init(String place, Activity context, OnClickListener ocl, OnLongClickListener olcl, ButtonFnManager fnManager){
        this.fnManager = fnManager;
        DbTool db = new DbTool();
        this.place = place;
        this.id = db.getButtonIdByPlace(place, context);
        init(context, ocl, olcl, db, false);
    }

    public void init(long id, Context context, OnClickListener ocl, OnLongClickListener olcl, ButtonFnManager fnManager, DbTool dbTool, boolean colorInited){
        this.fnManager = fnManager;
       if (dbTool == null) dbTool = new DbTool();
        this.id = id;
        init(context, ocl, olcl, dbTool, colorInited);
    }

    private void init(final Context context, OnClickListener ocl, OnLongClickListener olcl, DbTool db, boolean colorInited) {

        final int colorHoloBlue = context.getResources().getColor(android.R.color.holo_blue_light);

        if(!colorInited){
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                    FnButton.this.getBackground().setColorFilter(colorHoloBlue, PorterDuff.Mode.MULTIPLY);
                }
            });
        }



        Cursor c = db.getButtonCursor(id, context);
        if(c!=null){
            this.name = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_NAME));
            this.args = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_CMD));
            this.type = c.getInt(c.getColumnIndex(db.BUTTONS_TABLE_TYPE));
            this.plugin = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_PLUGIN));
            this.color = c.getInt(c.getColumnIndex(db.BUTTONS_TABLE_COLOR));
            c.close();
            db.dbHelper.close();
        }else{
            this.name = "";
            this.args = "";
            this.type = ButtonFnManager.NO_FUNCTION;
            this.plugin = "";
        }


        this.ocl = ocl;
        this.olcl = olcl;

        this.setOnClickListener(ocl);

        this.setOnLongClickListener(olcl);





        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Если шорткат или команда - выяставляем их аргумент
                setText(ButtonFnManager.fnMap.get(FnButton.this.type));
                if(FnButton.this.type==ButtonFnManager.FN_CUSTOM||FnButton.this.type==ButtonFnManager.FN_COMMAND_LINE)setText(FnButton.this.args);

                //Если есть имя - выставляем его
                if(!FnButton.this.name.equals("") && !FnButton.this.name.isEmpty()){
                    setText(name);
                }
            }
        });






        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Cтавим фон

                //Если есть цвет, ставим его
                if(FnButton.this.color!=0){
                    FnButton.this.getBackground().setColorFilter(FnButton.this.color, PorterDuff.Mode.MULTIPLY);
                }

                //Если пустая - меняем фон
                if(FnButton.this.type== ButtonFnManager.NO_FUNCTION){
                    setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }


            }
        });

        //Если есть иконка плагина - ставим её
        if(this.plugin!=null && !this.plugin.isEmpty()){
            File ico  = new File(new File(FnListFragment.PLUGINS_FOLDER_PATH), this.plugin+".png");
            if(ico.exists()){
                int size = getResources().getDimensionPixelSize(R.dimen.plugin_icon);
                Drawable dr = Drawable.createFromPath(ico.getPath());
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                final Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, size, size, true));
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                    }
                });

            }else
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                });

        }else{
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            });

        }


    }





    public void press(){
        fnManager.press(type, args, "");
    }

    public long getBtnId(){
        return id;
    }

    public String getPlace() {
        return place;
    }
}
