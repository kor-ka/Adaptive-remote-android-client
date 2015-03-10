package ru.korinc.sockettest;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by kor_ka on 02.02.2015.
 */
public class DrawerGridAdapter extends CursorAdapter{
private Cursor c;
private Context ctx;
private ButtonFnManager fnManager;
private DbTool db;
    public DrawerGridAdapter(Context context, Cursor c, ButtonFnManager fnManager) {
        super(context, c);
        this.fnManager = fnManager;
        this.c = c;
        this.ctx = context;
        this.db = new DbTool();
    }

    static class ViewHolder{
        public FnButton b;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        c.moveToPosition(position);

        final ViewHolder holder;
        View rowView = convertView;
        LayoutInflater vi = (LayoutInflater)  ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowView = vi.inflate(R.layout.row, parent, false);
        holder = new ViewHolder();
        holder.b = (FnButton) rowView.findViewById(R.id.gridBtn);


        //Ставим фон
        final int colorHoloBlue = ctx.getResources().getColor(android.R.color.holo_blue_light);

        holder.b.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.btn_seelctor));
        holder.b.getBackground().setColorFilter(colorHoloBlue, PorterDuff.Mode.MULTIPLY);

        //Ставим цвет
        int color = c.getInt(c.getColumnIndex(db.BUTTONS_TABLE_COLOR));
        if(color!=0){
            holder.b.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }

        //Ставим текст
        String name = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_NAME));
        String args = c.getString(c.getColumnIndex(db.BUTTONS_TABLE_CMD));
        int type = c.getInt(c.getColumnIndex(db.BUTTONS_TABLE_TYPE));
        //Если шорткат или команда - выяставляем их аргумент
        holder.b.setText(ButtonFnManager.fnMap.get(type));
        if(type==ButtonFnManager.FN_CUSTOM||type==ButtonFnManager.FN_COMMAND_LINE)holder.b.setText(holder.b.args);

        //Если есть имя - выставляем его
        if(!name.equals("") && !name.isEmpty()){
            holder.b.setText(name);
        }
        long id = c.getLong(c.getColumnIndex("_id"));
        new AsyncTask<Object, Integer, String>(){

            @Override
            protected String doInBackground(Object... params) {
                ((ViewHolder)params[0]).b.init((Long)params[1],ctx, null, null, fnManager, ((DbTool)params[2]), true);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                holder.b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FnButton b = (FnButton) view;
                        b.press();
                    }
                });
            }
        }.execute(holder, id, db);


        return rowView;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
