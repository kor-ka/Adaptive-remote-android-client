package ru.korinc.sockettest;

import android.content.Context;
import android.database.Cursor;
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

    public FnButton(Context context){
        super(context);
    }


    public FnButton(String name, long id, String args, int type, OnClickListener ocl, OnLongClickListener olcl, Context context) {
        super(context);
        init(name, id, args, type, ocl, olcl);
    }

    public void init(String place, Context context, OnClickListener ocl, OnLongClickListener olcl){
        DbTool db = new DbTool();
        this.id = db.getButtonIdByPlace(place, context);
        Cursor c = db.getButtonCursor(id, context);
        this.name = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_NAME));
        this.args = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_CMD));
        this.type = c.getInt(c.getColumnIndex(db.BUTTONS_TABLE_TYPE));

        this.ocl = ocl;
        this.olcl = olcl;

        this.setOnClickListener(ocl);
        this.setText(name);
        this.setOnLongClickListener(olcl);
        c.close();
        db.dbHelper.close();
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



}
