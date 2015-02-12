package ru.korinc.sockettest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
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
        init(context, ocl, olcl, db);
    }

    public void init(long id, Context context, OnClickListener ocl, OnLongClickListener olcl, ButtonFnManager fnManager){
        this.fnManager = fnManager;
        DbTool db = new DbTool();
        this.id = id;
        init(context, ocl, olcl, db);
    }

    private void init(Context context, OnClickListener ocl, OnLongClickListener olcl, DbTool db) {
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
        this.setText(name);
        this.setOnLongClickListener(olcl);

        setText(ButtonFnManager.fnMap.get(this.type));



        //Если шорткат или команда - выяставляем их аргумент

        if(this.type==ButtonFnManager.FN_CUSTOM||this.type==ButtonFnManager.FN_COMMAND_LINE)setText(this.args);


        //Если есть иконка плагина - ставим её
        if(this.plugin!=null && !this.plugin.isEmpty()){
            File ico  = new File(new File(FnListFragment.PLUGINS_FOLDER_PATH), this.plugin+".png");
            if(ico.exists())
                setCompoundDrawablesWithIntrinsicBounds(Drawable.createFromPath(ico.getPath()), null, null, null);
            else
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }else{
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        //Cтавим фон
        setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));

        //Если есть цвет, ставим его
        if(this.color!=0){
            this.getBackground().setColorFilter(this.color, PorterDuff.Mode.DARKEN);
        }else{
            this.getBackground().setColorFilter(context.getResources().getColor(android.R.color.holo_blue_light), PorterDuff.Mode.DARKEN);
        }

        //Если пустая - меняем фон
        if(this.type== ButtonFnManager.NO_FUNCTION){
            setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }

        //Если есть имя - выставляем его

        if(!this.name.equals("") && !this.name.isEmpty()){
            setText(name);
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
