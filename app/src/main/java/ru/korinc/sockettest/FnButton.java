package ru.korinc.sockettest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by korka on 27.01.15.
 */
public class FnButton extends Button{

    public String name;
    public long id;
    public String args;
    public int type;
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

    public FnButton(String name, long id, String args, int type, OnClickListener ocl, OnLongClickListener olcl, Context context) {
        super(context);
        init(name, id, args, type, ocl, olcl);
    }

    public void init(String place, Activity context, OnClickListener ocl, OnLongClickListener olcl, ButtonFnManager fnManager){
        this.fnManager = fnManager;
        DbTool db = new DbTool();
        this.place = place;
        this.id = db.getButtonIdByPlace(place, context);
        Cursor c = db.getButtonCursor(id, context);
        if(c!=null){
            this.name = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_NAME));
            this.args = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_CMD));
            this.type = c.getInt(c.getColumnIndex(db.BUTTONS_TABLE_TYPE));
            c.close();
            db.dbHelper.close();
        }else{
            this.name = "";
            this.args = "";
            this.type = ButtonFnManager.NO_FUNCTION;
        }



        this.ocl = ocl;
        this.olcl = olcl;

        this.setOnClickListener(ocl);
        this.setText(name);
        this.setOnLongClickListener(olcl);

        setText(ButtonFnManager.fnMap.get(this.type));

        //Если пустая - меняем фон
        if(this.type== ButtonFnManager.NO_FUNCTION){
            setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }

        //Если шорткат или команда - выяставляем их аргумент

        if(this.type==ButtonFnManager.FN_CUSTOM||this.type==ButtonFnManager.FN_COMMAND_LINE){
            setText(this.args);
            if(this.args.contains("chrome")){
                setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        //Если есть имя - выставляем его

        if(!this.name.equals("") && !this.name.isEmpty()){
            setText(name);
        }
    }

    public void init(long id, Context context, OnClickListener ocl, OnLongClickListener olcl, ButtonFnManager fnManager){
        this.fnManager = fnManager;
        DbTool db = new DbTool();
        this.id = id;
        Cursor c = db.getButtonCursor(id, context);
        if(c!=null){
            this.name = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_NAME));
            this.args = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_CMD));
            this.type = c.getInt(c.getColumnIndex(db.BUTTONS_TABLE_TYPE));
            c.close();
            db.dbHelper.close();
        }else{
            this.name = "";
            this.args = "";
            this.type = ButtonFnManager.NO_FUNCTION;
        }



        this.ocl = ocl;
        this.olcl = olcl;

        this.setOnClickListener(ocl);
        this.setText(name);
        this.setOnLongClickListener(olcl);

        setText(ButtonFnManager.fnMap.get(this.type));

        //Если пустая - меняем фон
        if(this.type== ButtonFnManager.NO_FUNCTION){
            setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }

        //Если шорткат или команда - выяставляем их аргумент

        if(this.type==ButtonFnManager.FN_CUSTOM||this.type==ButtonFnManager.FN_COMMAND_LINE){
            setText(this.args);
            if(this.args.contains("chrome")){
                setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        //Если есть имя - выставляем его

        if(!this.name.equals("") && !this.name.isEmpty()){
            setText(name);
        }
    }

    public void init(String name, long id, String args, int type, OnClickListener ocl, OnLongClickListener olcl){
        this.name = name;
        this.id = id;
        this.args = args;
        this.type = type;
        this.ocl = ocl;
        this.olcl = olcl;

        this.setOnClickListener(ocl);
        this.setText(name);
        this.setOnLongClickListener(olcl);
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
