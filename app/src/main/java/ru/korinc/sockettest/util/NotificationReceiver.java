package ru.korinc.sockettest.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import net.dinglisch.android.tasker.FireReceiver;

import ru.korinc.sockettest.ButtonFnManager;
import ru.korinc.sockettest.DbTool;
import ru.korinc.sockettest.FnButton;
import ru.korinc.sockettest.FnBynId;

/**
 * Created by kor_ka on 05.05.2015.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long btnId = intent!=null?intent.getLongExtra("id",0):0;
        if(btnId!=0){

            DbTool db = new DbTool();
            ButtonFnManager btn = new ButtonFnManager(context, null);
            Cursor c = db.getButtonCursor(btnId, context);
            if(c!=null){
                btn.press( c.getInt(c.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)), c.getString(c.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)), "");
            }
        }

    }
}
