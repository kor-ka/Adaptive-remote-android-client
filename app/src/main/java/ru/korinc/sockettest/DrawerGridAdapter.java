package ru.korinc.sockettest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;

/**
 * Created by kor_ka on 02.02.2015.
 */
public class DrawerGridAdapter extends CursorAdapter{
private Cursor c;
private Context ctx;
private ButtonFnManager fnManager;
    public DrawerGridAdapter(Context context, Cursor c, ButtonFnManager fnManager) {
        super(context, c);
        this.fnManager = fnManager;
        this.c = c;
        this.ctx = context;
    }

    static class ViewHolder{
        public FnButton b;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        c.moveToPosition(position);

        ViewHolder holder;
        View rowView = convertView;
        LayoutInflater vi = (LayoutInflater)  ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        rowView = vi.inflate(R.layout.row, parent, false);
        holder = new ViewHolder();

        holder.b = (FnButton) rowView.findViewById(R.id.gridBtn);
        holder.b.init(c.getInt(c.getColumnIndex("_id")),ctx, null, null, fnManager);
        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FnButton b = (FnButton) view;
                b.press();
            }
        });
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
