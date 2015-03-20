package ru.korinc.sockettest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.dinglisch.android.tasker.BundleScrubber;
import net.dinglisch.android.tasker.PluginBundleManager;
import net.dinglisch.android.tasker.TaskerPlugin;


public class FnBynId extends Activity implements View.OnClickListener{

    public static String BTN_ID  = "FnBynIdbtnID";

    EditText et;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fn_byn_id);

        et = (EditText) findViewById(R.id.btnId);
        btn = (Button) findViewById(R.id.buttonOk);
        btn.setOnClickListener(this);
        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);



        if (null == savedInstanceState && localeBundle!=null && !localeBundle.isEmpty())
        {

            //if (PluginBundleManager.isBundleValid(localeBundle)){
            et.setText( localeBundle.getString(BTN_ID, ""));

            //}
        }
    }



    @Override
    public void onClick(View view) {
        super.onStop();
        Intent intent = new Intent();
        final Bundle resultBundle =
                PluginBundleManager.generateBundle(getApplicationContext(), et.getText().toString());
        intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

        if ( TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement( this) )
            TaskerPlugin.Setting.setVariableReplaceKeys( resultBundle, new String [] { BTN_ID } );

        if (  TaskerPlugin.Setting.hostSupportsSynchronousExecution( getIntent().getExtras() ) )
            TaskerPlugin.Setting.requestTimeoutMS( intent, 3000 );

        final String blurb = "Btn id: " + et.getText().toString();
        intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);
        if ( TaskerPlugin.hostSupportsRelevantVariables( getIntent().getExtras() ) )
            TaskerPlugin.addRelevantVariableList( intent, new String [] {
                    "%context\nContext\nCurrent PC process in focus",
                    "%btnids()\nContext Button id\nContext Button id",
            } );

        setResult(RESULT_OK, intent);
        finish();
    }
}
