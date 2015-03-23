/*
 * Copyright 2013 two forty four a.m. LLC <http://www.twofortyfouram.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package net.dinglisch.android.tasker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import ru.korinc.sockettest.ButtonFnManager;
import ru.korinc.sockettest.DbTool;
import ru.korinc.sockettest.FnBind;
import ru.korinc.sockettest.FnButton;
import ru.korinc.sockettest.FnBynId;
import ru.korinc.sockettest.InAppLog;
import ru.korinc.sockettest.R;
import ru.korinc.sockettest.ST;


public final class FireReceiver extends BroadcastReceiver
{
Intent intent;
Context ctx;



    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        /*
         * Always be strict on input parameters! A malicious third-party app could send a malformed Intent.
         */

        this.intent = intent;
        this.ctx = context.getApplicationContext();

        if (!com.twofortyfouram.locale.Intent.ACTION_FIRE_SETTING.equals(intent.getAction()))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format(Locale.US, "Received unexpected Intent action %s", intent.getAction())); //$NON-NLS-1$
            }
            return;
        }

        BundleScrubber.scrub(intent);

        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(bundle);

        ButtonFnManager btn = new ButtonFnManager(context, this);
        if (bundle.containsKey(FnBynId.BTN_ID))
        {
            FnButton fnbtn= new FnButton(ctx);
            DbTool db = new DbTool();

            Cursor c = db.getButtonCursor(Integer.parseInt(bundle.getString(FnBynId.BTN_ID, "0").trim()), ctx);
            if(c!=null){
                btn.press( c.getInt(c.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)), c.getString(c.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)), "");
            }

        }else if(bundle.containsKey(FnBind.BTN_TYPE) && bundle.containsKey(FnBind.BTN_CMD)){

            btn.press( bundle.getInt(FnBind.BTN_TYPE, ButtonFnManager.NO_FUNCTION), bundle.getString(FnBind.BTN_CMD, ""), "");
        }

        if ( isOrderedBroadcast() )
            setResultCode( TaskerPlugin.Setting.RESULT_CODE_PENDING );

    }

    public void onBtnPressResult(String pr){
        /*
        if ( isOrderedBroadcast() )  {

            setResultCode( TaskerPlugin.Setting.RESULT_CODE_OK );

            if ( TaskerPlugin.Setting.hostSupportsVariableReturn( intent.getExtras() ) ) {
                Bundle vars = new Bundle();
                vars.putString( "%context", pr );

                TaskerPlugin.addVariableBundle( getResultExtras( true ), vars );
            }
        }
        */
        pr = pr.substring(pr.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", "");

        DbTool db = new DbTool();



        String[] btnIds = new String[]{
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN1) + pr, ctx)+"",
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN2) + pr, ctx)+"",
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN3) + pr, ctx)+"",
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN4) + pr, ctx)+"",
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN5) + pr, ctx)+"",
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN6) + pr, ctx)+"",
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN7) + pr, ctx)+"",
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN8) + pr, ctx)+"",
                db.getButtonIdByPlace(ST.getReqCodeById(R.id.workSpaceBTN9) + pr, ctx)+"",
        };

        String[] btnNames = new String[9];
        for (int i = 0; i < 9; i++) {
            Cursor c = db.getButtonCursor(Integer.parseInt(btnIds[i]), ctx);
            if(c!=null){
                btnNames[i] = c.getString(c.getColumnIndex(DbTool.BUTTONS_TABLE_NAME));
            }
        }


        Bundle vars = new Bundle();

        vars.putString( "%context", pr );
        for (int i = 1; i < 10; i++) {
            vars.putString( "%btnids"+i, btnIds[i-1] );
        }

        for (int i = 1; i < 10; i++) {
            vars.putString( "%btnnames"+i, btnNames[i-1] );
        }

        vars.putStringArrayList("%btnids", new ArrayList<String>(Arrays.asList(btnIds)));
        vars.putStringArrayList("%btnnames", new ArrayList<String>(Arrays.asList(btnNames)));
        TaskerPlugin.Setting.signalFinish( ctx, intent, TaskerPlugin.Setting.RESULT_CODE_OK, vars );
        InAppLog.writeLog(null, "", vars.toString(), false);
    }
}