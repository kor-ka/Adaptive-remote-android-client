package ru.korinc.sockettest;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import util.IabHelper;
import util.IabResult;
import util.Inventory;
import util.Purchase;

public class ST extends FragmentActivity implements OnClickListener {


    public static final String VOL_UP = "volUp";
    public static final String VOL_DOWN = "volDown";
    public static final String VOL_UP_DEFAULT_TYPE = "volUpDefaultType";
    public static final String VOL_UP_DEFAULT_ARGS = "volUpDefaultArgs";
    public static final String VOL_DOWN_DEFAULT_TYPE = "VOL_DOWN_DEFAULT_TYPE";
    public static final String VOL_DOWN_DEFAULT_ARGS = "VOL_DOWN_DEFAULT_ARGS";
    public static final String CURRENT_PROCESS = "currentProcess";
    public static final int REQUEST_CODE_TUTORIAL = 1260;
    static boolean debug = false;

    EditText ipEt;
    EditText portEt;
    EditText clientPortEt;
    EditText aEt;
    EditText bEt;
    ZanyEditText keyboardEt;
    Button scan;
    Button send;

    Button dellBtn;
    Button editBtn;

    Button volUpBtn;
    Button volDownBtn;

    LinearLayout dragMenuLL;

    ImageButton up;
    ImageButton down;
    ImageButton left;
    ImageButton right;

    FnButton wsBtn1;
    FnButton wsBtn2;
    FnButton wsBtn3;
    FnButton wsBtn4;
    FnButton wsBtn5;
    FnButton wsBtn6;
    FnButton wsBtn7;
    FnButton wsBtn8;
    FnButton wsBtn9;

    public  FnButton[] fnButtons;

    View.OnLongClickListener olclFn;
    OnClickListener ocl;


    Button addButton;

    TableRow tr1;
    TableRow tr2;
    TableRow tr3;

    public ImageButton contextBtn;

    TextView status;
    AlertDialog dialog;
    ArrayList<String> ipPortsArray = new ArrayList<String>();
    boolean breakDiscovering = false;


    String currentCommandLineaArgs;

    SharedPreferences shp;
    SharedPreferences shpCross;

    Editor ed;
    Editor edCross;

    float fullmovex;
    float fullmovey;
    boolean isDouble = false;
    static boolean  isFull = false;
    LinearLayout ll;
    TextView tv;
    ArrayAdapter<String> adapter;
    ArrayList<String> results;
    public static final int REQUEST_CODE_LAUNCH_APP = 1234;
    public static final int REQUEST_CODE_VOICE_INPUT = 12345;
    public static final int REQUEST_CODE_FIRE_FN = 12352;
    public static final int REQUEST_CODE_COMMAND_LINE_VOICE_INPUT = 12353;
    public static final int REQUEST_CODE_VOICE_FN = 12354;
    public static final int REQUEST_CODE_LAUNCHAPP_FROM_TASKBAR = 12358;
    private static final int REQUEST_CODE_EDIT_BTN = 1258;
    public static final int REQUEST_CODE_SETTINGS = 1259;
    public String currentProcess = "";
    ButtonFnManager fnb;
    private String dialogInputText = "";
    private static final int NUM_PAGES = 3;
    ScreenSlidePagerAdapter topPagerAdapter;
    private ViewPager topPager;
    ScreenSlidePagerAdapter botPagerAdapter;
    private ViewPager botPager;
    Set<String> keyoVoiceInputFix;

    OverlayOTL overlayOTL;
    OverlayAltTAbOTL overlayAltTAbOTL;

    LinearLayout topPagerContainerLL;
    LinearLayout botPagerContainerLL;

    public DrawerLayout mDrawerLayout;
    public GridView mDrawerGrid;

    public static final int REQUEST_CODE_ADD_BUTTON = 12359;
    private Button leftScroll;
    private Button rightScroll;
    private View leftScrollIndicator;
    private View rightScrollIndicator;

    long doubleTouchUpTime;
    IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String key = ButtonFnManager.xorDecrypt(ConnectionChangeReceiver.getKey() +
                DbTool.getKey() +
                DragEventListener.getKey() +
                DrawerGridAdapter.getKey() +
                FnBind.getKey() +
                FnButton.getKey() +
                FnButtonsFragment.getKey(), getString(R.string.hello_blank_fragment));

        mHelper = new IabHelper(this, key);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Toast.makeText(ST.this, "Problem setting up In-app Billing: " + result, Toast.LENGTH_LONG).show();
                    Log.w("IAB", "Problem setting up In-app Billing: " + result);
                }else{
                    checkInventory();
                }

            }
        });



        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(false);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }


        DbTool db = new DbTool();

        shp = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        ed = shp.edit();

        shpCross = getSharedPreferences("default", Context.MODE_MULTI_PROCESS);
        edCross = shpCross.edit();

        setContentView(R.layout.activity_st);

        if(shp.getBoolean("firstLaunch", true)){
            Intent intent = new Intent(getBaseContext(), TutorialActivity.class);
            startActivityForResult(intent, REQUEST_CODE_TUTORIAL);
            ed.putBoolean("firstLaunch", false);
            ed.commit();
        }


        ipEt = (EditText) findViewById(R.id.etIp);
        portEt = (EditText) findViewById(R.id.etSocket);
        clientPortEt = (EditText) findViewById(R.id.etPort);
        aEt = (EditText) findViewById(R.id.etA);
        bEt = (EditText) findViewById(R.id.etB);
        keyboardEt = (ZanyEditText) findViewById(R.id.etKeyboard);

        EditText[] ets  = new EditText[]{ipEt, portEt, clientPortEt, aEt, bEt, keyboardEt};
        for (EditText et:ets){
            et.setOnDragListener( new View.OnDragListener() {
                @Override
                public boolean onDrag( View v, DragEvent event) {
                    return true;
                }
            });
        }



        keyoVoiceInputFix = shp.getStringSet("map", new HashSet<String>());
        if (keyoVoiceInputFix.isEmpty()) {

            keyoVoiceInputFix.add("хром");
            ed.putString("хром", "chrome");

            keyoVoiceInputFix.add("хром");
            ed.putString("дропбокс", "dropbox");

            keyoVoiceInputFix.add("ворд");
            ed.putString("ворд", "word");

            keyoVoiceInputFix.add("ексель");
            ed.putString("ексель", "excel");

            ed.putStringSet("map", keyoVoiceInputFix);
            ed.commit();


            int[] buttonsToInit = new int[]{
                    ButtonFnManager.FN_VOICE_FN,
                    ButtonFnManager.FN_LAUNCHFROM_TASKBAR,
                    ButtonFnManager.FN_FIRE_FN,
                    ButtonFnManager.FN_ARROWS,
                    ButtonFnManager.FN_R_CLICK,
                    ButtonFnManager.FN_CUSTOM,
                    ButtonFnManager.FN_SETTINGS,
                    ButtonFnManager.FN_HELP
            };

            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[0], "", 0, this, "", 0), "fnB1" + "top1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[1], "", 0, this, "", 0), "fnB2" + "top1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[2], "", 0, this, "", 0), "fnB3" + "top1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[3], "", 0, this, "", 0), "fnB1" + "bot1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[4], "", 0, this, "", 0), "fnB2" + "bot1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[5], "Enter", 0, this, "", 0), "fnB3" + "bot1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[6], "", 0, this, "", 0), "fnB1" + "bot2",this);
            db.addButton(-1, "", buttonsToInit[7], "", 0, this, "", 0);
        }


        scan = (Button) findViewById(R.id.bScan);
        send = (Button) findViewById(R.id.bSend);

        up = (ImageButton) findViewById(R.id.buttonUp);
        down = (ImageButton) findViewById(R.id.buttonDown);
        left = (ImageButton) findViewById(R.id.buttonLeft);
        right = (ImageButton) findViewById(R.id.buttonRight);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        wsBtn1 = (FnButton) findViewById(R.id.workSpaceBTN1);
        wsBtn2 = (FnButton) findViewById(R.id.workSpaceBTN2);
        wsBtn3 = (FnButton) findViewById(R.id.workSpaceBTN3);
        wsBtn4 = (FnButton) findViewById(R.id.workSpaceBTN4);
        wsBtn5 = (FnButton) findViewById(R.id.workSpaceBTN5);
        wsBtn6 = (FnButton) findViewById(R.id.workSpaceBTN6);
        wsBtn7 = (FnButton) findViewById(R.id.workSpaceBTN7);
        wsBtn8 = (FnButton) findViewById(R.id.workSpaceBTN8);
        wsBtn9 = (FnButton) findViewById(R.id.workSpaceBTN9);

        fnButtons = new FnButton[]{wsBtn1,wsBtn2,wsBtn3,wsBtn4,wsBtn5,wsBtn6,wsBtn7,wsBtn8,wsBtn9};
        int i =0;
        for(FnButton b:fnButtons){
            ed.putInt("ButtonId" + i, getReqCodeById(b.getId()));
            i++;
        }
        ed.commit();

        ocl = new OnClickListener() {

            @Override
            public void onClick(View v) {
                FnButton b = (FnButton) v;
                if(b.type == ButtonFnManager.NO_FUNCTION){
                    Intent intentB1 = new Intent(ST.this, FnBind.class);
                    startActivityForResult(intentB1, getReqCodeById(v.getId()));
                }else {
                    fnb.press(b.type, b.args, "");
                }
            }
        };

        olclFn = new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                FnButton b = (FnButton) v;
                Intent intent = new Intent(getBaseContext(), FnBind.class);
                intent.putExtra(FnBind.BTN_ID, b.getBtnId());
                startActivityForResult(intent, getReqCodeById(v.getId()));
                return false;
            }
        };

        for(final FnButton b:fnButtons){
            b.setOnClickListener(ocl);
            b.setOnLongClickListener(olclFn);
            b.setOnDragListener( new View.OnDragListener() {


                @Override
                public boolean onDrag( View v, DragEvent event) {
                    final int action = event.getAction();

                    AlphaAnimation enterAnimation=new AlphaAnimation(1,0);
                    enterAnimation.setDuration(200);
                    enterAnimation.setFillAfter(true);

                    AlphaAnimation afterDropAnimation=new AlphaAnimation(0,1);
                    afterDropAnimation.setDuration(200);
                    afterDropAnimation.setFillAfter(true);
                    // Handles each of the expected events
                    switch(action) {

                        case DragEvent.ACTION_DRAG_ENTERED:
                            v.startAnimation(enterAnimation);
                            break;

                        case DragEvent.ACTION_DRAG_EXITED:
                            v.startAnimation(afterDropAnimation);
                            break;

                        case DragEvent.ACTION_DROP:
                            ClipData.Item item = event.getClipData().getItemAt(0);
                            Intent i = item.getIntent();
                            long id=i.getLongExtra("id", 0);
                            b.init(i.getLongExtra("id", 0), ST.this, ocl, olclFn, fnb, null, false);

                            DbTool dbt = new DbTool();
                            FnButton fnb = (FnButton) v;
                            dbt.bindButtonToPlace(id, fnb.getPlace(), ST.this);
                            v.clearAnimation();
                            v.startAnimation(afterDropAnimation);
                            break;
                    }
                    return true;
                }
            });
        }

        tr1 = (TableRow) findViewById(R.id.tableWSRow1);
        tr2 = (TableRow) findViewById(R.id.tableWSRow2);
        tr3 = (TableRow) findViewById(R.id.tableWSRow3);
        tr1.setVisibility(View.INVISIBLE);
        tr2.setVisibility(View.INVISIBLE);
        tr3.setVisibility(View.INVISIBLE);

        dragMenuLL = (LinearLayout) findViewById(R.id.dragMenuLL);
        editBtn = (Button) findViewById(R.id.editButton);
        dellBtn = (Button) findViewById(R.id.dellButton);

        volDownBtn = (Button) findViewById(R.id.VolDownButton);
        volUpBtn = (Button) findViewById(R.id.VolUpButton);


        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                final int action = dragEvent.getAction();

                // Handles each of the expected events
                switch(action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        dragMenuLL.setVisibility(View.VISIBLE);
                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:

                        switch (view.getId()){
                            case R.id.editButton:
                            case R.id.VolDownButton:
                            case R.id.VolUpButton:
                                view.setBackgroundColor(ST.this.getResources().getColor(android.R.color.holo_blue_light));
                                break;

                            case R.id.dellButton:
                                view.setBackgroundColor(ST.this.getResources().getColor(android.R.color.holo_red_light));
                                break;
                        }


                        view.invalidate();

                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:

                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        view.setBackgroundColor(ST.this.getResources().getColor(R.color.transparent_gray));
                        view.invalidate();
                        return true;

                    case DragEvent.ACTION_DROP:
                        ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                        Intent i = item.getIntent();
                        final long id=i.getLongExtra("id", 0);
                        final DbTool db = new DbTool();

                        switch (view.getId()){
                            case R.id.editButton:

                                if(isFull || (db.getButtonsCount(ST.this)<=8)){
                                    Intent editIntent = new Intent(ST.this, FnBind.class);
                                    editIntent.putExtra(FnBind.BTN_ID, id);

                                    startActivityForResult(editIntent, REQUEST_CODE_EDIT_BTN);
                                }else{
                                    showBuyDialog();
                                }

                                break;

                            case R.id.dellButton:


                                db.delRec(DbTool.BUTTONS_TABLE, id, ST.this);
                                updateAllBTNS();
                                break;

                            case R.id.VolUpButton:
                                AlertDialog alertDialog = new AlertDialog.Builder(ST.this).create();
                                alertDialog.setTitle(getString(R.string.set_vol_buttons_dialog_title));
                                alertDialog.setMessage(getString(R.string.set_vol_buttons_dialog_body));
                                alertDialog.setButton(getString(R.string.set_vol_buttons_b1), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ed.putLong(currentProcess + VOL_UP, id);
                                        ed.commit();
                                    }
                                });

                                alertDialog.setButton2(getString(R.string.set_vol_buttons_b2), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                       Cursor btnCursor =db.getButtonCursor(id, ST.this);
                                       ed.putInt(VOL_UP_DEFAULT_TYPE, btnCursor.getInt(btnCursor.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)));
                                       ed.putString(VOL_UP_DEFAULT_ARGS, btnCursor.getString(btnCursor.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)));
                                       ed.commit();
                                    }
                                });

                                alertDialog.setButton3(getString(R.string.set_vol_buttons_b3), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ed.putLong(currentProcess + VOL_UP, -1);
                                        ed.commit();
                                    }
                                });

                                alertDialog.show();

                                break;

                            case R.id.VolDownButton:
                                AlertDialog alertDialog2 = new AlertDialog.Builder(ST.this).create();
                                alertDialog2.setTitle(getString(R.string.set_vol_buttons_dialog_title));
                                alertDialog2.setMessage(getString(R.string.set_vol_buttons_dialog_body));
                                alertDialog2.setButton(getString(R.string.set_vol_buttons_b1), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ed.putLong(currentProcess + VOL_DOWN, id);
                                        ed.commit();
                                    }
                                });

                                alertDialog2.setButton2(getString(R.string.set_vol_buttons_b2), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Cursor btnCursor =db.getButtonCursor(id, ST.this);
                                        ed.putInt(VOL_DOWN_DEFAULT_TYPE, btnCursor.getInt(btnCursor.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)));
                                        ed.putString(VOL_DOWN_DEFAULT_ARGS, btnCursor.getString(btnCursor.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)));
                                        ed.commit();
                                    }
                                });

                                alertDialog2.setButton3(getString(R.string.set_vol_buttons_b3), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ed.putLong(currentProcess + VOL_DOWN, -1);
                                        ed.commit();
                                    }
                                });

                                alertDialog2.show();

                                break;
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        dragMenuLL.setVisibility(View.INVISIBLE);
                        editBtn.setBackgroundColor(ST.this.getResources().getColor(R.color.transparent_gray));
                        dellBtn.setBackgroundColor(ST.this.getResources().getColor(R.color.transparent_gray));
                        editBtn.invalidate();
                        dellBtn.invalidate();

                        volDownBtn.setBackgroundColor(ST.this.getResources().getColor(R.color.transparent_gray));
                        volUpBtn.setBackgroundColor(ST.this.getResources().getColor(R.color.transparent_gray));
                        volDownBtn.invalidate();
                        volUpBtn.invalidate();
                        return true;

                    // An unknown action type was received.
                    default:

                        break;
                }

                return false;
            }
        };

        editBtn.setOnDragListener(dragListener);
        dellBtn.setOnDragListener(dragListener);
        volUpBtn.setOnDragListener(dragListener);
        volDownBtn.setOnDragListener(dragListener);
        //Костыль, но без этого ACTION_DRAG_STARTED не ловится (вообще хз)
        addButton.setOnDragListener(dragListener);

        contextBtn = (ImageButton) findViewById(R.id.context);

        ll = (LinearLayout) findViewById(R.id.ll);

        tv = (TextView) findViewById(R.id.tv);
        tv.setVisibility(View.GONE);

        status = (TextView) findViewById(R.id.textStatus);
        status.setVisibility(View.GONE);


        fnb = new ButtonFnManager(this);

        contextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fnb.press(ButtonFnManager.FN_CONTEXT_BUTTONS, "", "");
            }
        });

        ipEt.setText(shpCross.getString("ip", ""));
        portEt.setText(shpCross.getString("port", "1026"));

        keyboardEt.setText("");
        keyboardEt.setSelection(keyboardEt.getText().length());
        keyboardEt.addTextChangedListener(new TextWatcher() {



            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0
                        && keyboardEt.getText().toString().length() > 0) {
                   // String pressed = s.toString().replace("<>", "");
                    String pressed = s.toString();
                    // int spaces = pressed.length() - pressed.replaceAll(" ",
                    // "").length();

                    int port = Integer.parseInt(shpCross.getString("port", "12342"));
                    if (s.length() == pressed.length()
                            | (s.length() == 3 && pressed.length() == 1)) {

                        new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                port, ButtonFnManager.keyboard, pressed)).start();
                    }

                    keyboardEt.setText("");
                    keyboardEt.setSelection(keyboardEt.getText().length());
                } /*else if (keyboardEt.getText().toString().equals("<")) {

                    int port = Integer.parseInt(shpCross.getString("port", "12342"));
                    new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                            port, ButtonFnManager.keyboard, "bksps")).start();
                    keyboardEt.setText("<>");
                    keyboardEt.setSelection(keyboardEt.getText().length());
                }*/

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final long[] lastBackSpace = {0};
        keyboardEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_DEL && System.currentTimeMillis() - lastBackSpace[0] >5){
                    int port = Integer.parseInt(shpCross.getString("port", "12342"));
                    new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                            port, ButtonFnManager.keyboard, "bksps")).start();
                    lastBackSpace[0] = System.currentTimeMillis();
                    //keyboardEt.setText("<>");
                    //keyboardEt.setSelection(keyboardEt.getText().length());
                }
                return false;
            }
        });

        ll.setLongClickable(true);
        ll.setClickable(true);
        ll.setOnLongClickListener(new View.OnLongClickListener() {

            long timeLongDownOld = System.currentTimeMillis();
            long timeLongDown = System.currentTimeMillis();

            @Override
            public boolean onLongClick(View p1) {

                if (fullmovey < 5 & fullmovex < 5 && System.currentTimeMillis()-doubleTouchUpTime>200) {
                    // Toast.makeText(getBaseContext(), "long",
                    // Toast.LENGTH_SHORT).show();
                    int port = Integer.parseInt(shpCross.getString("port", "12342"));

                    new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"),
                            port, ButtonFnManager.dndDown, 0, 0, ST.this)).start();
                    isDouble = true;
                    timeLongDown = System.currentTimeMillis();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(25);
                    if (timeLongDown - timeLongDownOld < 1000) {
                        new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"),
                                port, ButtonFnManager.dndUp, 0, 0, ST.this)).start();
                        new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"),
                                port, ButtonFnManager.rclick, 0, 0, ST.this)).start();
                    }
                    timeLongDownOld = System.currentTimeMillis();
                    return true;
                }

                return false;
            }

        });

        scan.setOnClickListener(this);
        send.setOnClickListener(this);

        OnTouchListener otlArrows = new OnTouchListener() {
            long timeDown = System.currentTimeMillis();
            int i = 1;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int port = Integer.parseInt(shpCross.getString("port", "12342"));
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        timeDown = System.currentTimeMillis();
                        switch (v.getId()) {
                            case R.id.buttonUp:

                                new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                        port, ButtonFnManager.keyboard, "up")).start();
                                break;

                            case R.id.buttonDown:

                                new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                        port, ButtonFnManager.keyboard, "down")).start();
                                break;

                            case R.id.buttonLeft:

                                new Thread( new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                        port, ButtonFnManager.keyboard, "left")).start();
                                break;

                            case R.id.buttonRight:

                                new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                        port, ButtonFnManager.keyboard, "right")).start();
                                break;

                        }
                }

                if (System.currentTimeMillis() - timeDown > 500 && i == 1) {

                    i = 2;

                    switch (v.getId()) {
                        case R.id.buttonUp:

                            new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                    port, ButtonFnManager.keyboard, "up")).start();
                            break;

                        case R.id.buttonDown:

                            new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                    port, ButtonFnManager.keyboard, "down")).start();
                            break;

                        case R.id.buttonLeft:

                            new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                    port, ButtonFnManager.keyboard, "left")).start();
                            break;

                        case R.id.buttonRight:

                            new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"),
                                    port, ButtonFnManager.keyboard, "right")).start();
                            break;

                    }
                } else {
                    i = 1;
                }
                return false;
            }
        };

        up.setOnTouchListener(otlArrows);
        down.setOnTouchListener(otlArrows);
        left.setOnTouchListener(otlArrows);
        right.setOnTouchListener(otlArrows);

        results = new ArrayList<String>();

        OnTouchListener otl = new OnTouchListener() {
            float oldx;
            float oldy;
            float movex;
            float movey;
            int startX;
            int startY;
            float downx;
            float downy;
            int x;
            int y;
            String sDown;
            String sMove;
            String sUp;


            long timeDown = System.currentTimeMillis();
            long timeUp = System.currentTimeMillis();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = (int) event.getX();
                y = (int) event.getY();

                int port;
                int a;
                int b;
                if(event.getPointerCount()<2 && System.currentTimeMillis()-doubleTouchUpTime>200){
                    try{
                        port = Integer.parseInt(shpCross.getString("port", "1234"));
                    }catch (Exception e){
                        e.printStackTrace();
                        port = 1234;
                    }
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            timeDown = System.currentTimeMillis();
                            sDown = "Down: " + x + "," + y + "|" + timeDown;
                            sMove = "";
                            sUp = "";
                            oldx = x;
                            oldy = y;
                            downx = x;
                            downy = y;

                            break;

                        case MotionEvent.ACTION_MOVE:



                            sMove = "Move: x_" + x + "\nMove: y_" + y;
                            movex = (x - oldx);
                            movey = (y - oldy);

                            // Need for control long click
                            fullmovex = x - downx;
                            fullmovey = y - downy;
                            if (fullmovex < 0) {
                                fullmovex = fullmovex * -1;
                            }

                            if (fullmovey < 0) {
                                fullmovey = fullmovey * -1;
                            }
                            //

                            a = Math.round(movex);
                            b = Math.round(movey);



                            new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"),
                                    port, ButtonFnManager.ab, a, b, ST.this)).start();
                            oldx = x;
                            oldy = y;
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            timeUp = System.currentTimeMillis();
                            sMove = "";
                            sUp = "Up: " + x + "," + y + "|" + timeUp;
                            fullmovex = x - downx;
                            fullmovey = y - downy;

                            // Make module
                            if (fullmovex < 0) {
                                fullmovex = fullmovex * -1;
                            }

                            if (fullmovey < 0) {
                                fullmovey = fullmovey * -1;
                            }

                            // Click
                            if ((timeUp - timeDown) < 200
                                    && (fullmovex < 30 & fullmovey < 30) && !isDouble) {

                                new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"),
                                        port, ButtonFnManager.click, 0, 0, ST.this)).start();

                            }

                            // Long click relise
                            if ((timeUp - timeDown) < 100
                                    && (fullmovex < 20 & fullmovey < 20) && isDouble) {
                                // send dnd up

                                new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"),
                                        port, ButtonFnManager.dndUp, 0, 0, ST.this)).start();

                                isDouble = false;
                            }

                            break;
                    }
                }else if (event.getPointerCount()==2){
                    Vibrator v2 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            startX = (int) event.getRawX();
                            startY = (int) event.getRawY();
                            InAppLog.writeLog(ST.this, "", "On Touch Scroll" + startX + " | " + startY, debug);
                            fullmovex=6;
                            fullmovey=6;
                            break;

                        case MotionEvent.ACTION_MOVE:

                            x = (int) event.getRawX();
                            y = (int) event.getRawY();

                            int moveY = startY - y;

                            InAppLog.writeLog(ST.this, "", " Scroll Move " + x + " | " + y + "|" + moveY, debug);

                            if (moveY > 10 || moveY < -10) {

                                startX = x;
                                startY = y;
                                fnb.press(moveY>0?ButtonFnManager.FN_WHELL_UP:ButtonFnManager.FN_WHELL_DOWN, "", "");
                                v2.vibrate(5);
                            }

                            doubleTouchUpTime = System.currentTimeMillis();

                            break;


                    }
                }

                tv.setText(sDown + "\n" + sMove + "\n" + sUp);
                return false;
            }

        };

        ll.setOnTouchListener(otl);

        //Left Menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerGrid = (GridView) findViewById(R.id.right_drawer);

        // Set the adapter for the list view

        mDrawerGrid.setAdapter(new DrawerGridAdapter(this, db.getCursor(DbTool.BUTTONS_TABLE, this), fnb));
        // Set the list's click listener
        mDrawerGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                FnButton b = (FnButton) view;
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                view.setOnDragListener(new DragEventListener().setDrawerLayout(mDrawerLayout));
                view.startDrag(new ClipData("", new String[]{""}, new ClipData.Item(new Intent().putExtra("id", b.getBtnId()))), myShadow, null, 0);
                return false;
            }
        });

        mDrawerGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FnButton b = (FnButton) view;
                fnb.press(b.type, b.args, "");
            }
        });

        // Pagers...
        // Instantiate a ViewPager and a PagerAdapter.
        topPager = (ViewPager) findViewById(R.id.pagerTop);
        topPagerAdapter = new ScreenSlidePagerAdapter(
                getSupportFragmentManager(), "top");
        topPager.setAdapter(topPagerAdapter);
        topPager.setCurrentItem(1);

        botPager = (ViewPager) findViewById(R.id.pagerBot);
        botPagerAdapter = new ScreenSlidePagerAdapter(
                getSupportFragmentManager(), "bot");
        botPager.setAdapter(botPagerAdapter);
        botPager.setCurrentItem(1);

        topPagerContainerLL = (LinearLayout) findViewById(R.id.topPagerContainer);
        botPagerContainerLL = (LinearLayout) findViewById(R.id.botPagerContainer);


        // Bind the title indicator to the adapter
        CirclePageIndicator topTitleIndicator = (CirclePageIndicator) findViewById(R.id.indicatorTop);
        topTitleIndicator.setViewPager(topPager);
        topTitleIndicator.setStrokeColor(getResources().getColor(android.R.color.holo_blue_light));
        topTitleIndicator.setFillColor(getResources().getColor(android.R.color.holo_blue_light));

        CirclePageIndicator botTitleIndicator = (CirclePageIndicator) findViewById(R.id.indicatorBot);
        botTitleIndicator.setViewPager(botPager);
        botTitleIndicator.setStrokeColor(getResources().getColor(android.R.color.holo_blue_light));
        botTitleIndicator.setFillColor(getResources().getColor(android.R.color.holo_blue_light));

        if (shp.getBoolean("showFnButtons", true)) {
            topPager.setVisibility(View.VISIBLE);
            botPager.setVisibility(View.VISIBLE);

        } else {
            topPager.setVisibility(View.GONE);
            botPager.setVisibility(View.GONE);

        }



        overlayOTL = new OverlayOTL();
        overlayAltTAbOTL = new OverlayAltTAbOTL();

        contextBtn.setOnTouchListener(overlayOTL);
        topPager.setOnTouchListener(overlayOTL);
        botPager.setOnTouchListener(overlayOTL);

        ScrollOtl scrollOtl = new ScrollOtl();
        leftScroll = (Button) findViewById(R.id.leftScroll);
        rightScroll = (Button) findViewById(R.id.rightScroll);
        leftScrollIndicator = findViewById(R.id.leftScrollIndicator);
        rightScrollIndicator = findViewById(R.id.rightScrollIndicator);
        leftScroll.setOnTouchListener(scrollOtl);
        rightScroll.setOnTouchListener(scrollOtl);
        leftScroll.bringToFront();
        rightScroll.bringToFront();

        setScrollVisability();

        if(savedInstanceState!=null){
            setCurrentProcess(savedInstanceState.getString(CURRENT_PROCESS, ""));
        }

        InAppLog.writeLog(ST.this, "", "ST on create", debug);

        //exportDatabse("db");
    }

    private void checkInventory() {
        mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                if (result.isFailure()) {
                    Log.w("IAB", "Cant get inventory");
                }
                else {
                    // does the user have the premium upgrade?
                    if(inv.hasPurchase("full"))
                        isFull = inv.getPurchase("full").getPurchaseState()==0;

                    // update UI accordingly
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CURRENT_PROCESS, currentProcess);
        super.onSaveInstanceState(outState);
    }

    private void setScrollVisability() {
        leftScroll.setVisibility(shp.getBoolean("leftScroll", true)? View.VISIBLE:View.GONE);
        rightScroll.setVisibility(shp.getBoolean("rightScroll", true)?View.VISIBLE:View.GONE);
        leftScrollIndicator.setVisibility(shp.getBoolean("leftScroll", true)?View.VISIBLE:View.GONE);
        rightScrollIndicator.setVisibility(shp.getBoolean("rightScroll", true)?View.VISIBLE:View.GONE);
    }

    private void updateAllBTNS() {
        bindContextButtons(currentProcess.substring(currentProcess.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", ""), 0);
        DrawerGridAdapter adapter = (DrawerGridAdapter) mDrawerGrid.getAdapter();
        adapter.getCursor().requery();
        adapter.notifyDataSetChanged();
        FnButtonsFragment btnsFragment;

        for(int f = 0 ; f<topPagerAdapter.getCount(); f++){
            btnsFragment = (FnButtonsFragment) topPagerAdapter.getFragment(f);
            if(btnsFragment!=null){
                btnsFragment.initButtons(btnsFragment.ocl, btnsFragment.olclFn, this);
            }

            btnsFragment = (FnButtonsFragment) botPagerAdapter.getFragment(f);
            if(btnsFragment!=null){
                btnsFragment.initButtons(btnsFragment.ocl, btnsFragment.olclFn, this);
            }
        }
    }


    Rect outRect = new Rect();
    int[] location = new int[2];

    private boolean inViewBounds(View view, int x, int y){
		//TODO добавить возможность игнорировать выход за пределы по одной из осей
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }



    public class OverlayOTL implements OnTouchListener {

        boolean overlayActivated = false;
        View btnCondidate = wsBtn5;
        int startX;
        int startY;
        int x;
        int y;
        int[] btnCondidateCoord = new int[]{0, 0};
        int btnCondidateInt = 0;
        long timeActivated;
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                                        startX = (int)event.getRawX();
                    startY = (int)event.getRawY();
                    InAppLog.writeLog(ST.this, "", "On Touch " + startX + " | " + startY, debug);
                    break;

                case MotionEvent.ACTION_MOVE:

                    x = (int)event.getRawX();
                    y = (int)event.getRawY();

                    int moveX = startX - x;
                    int moveY = startY - y;

                    //hInAppLog.writeLog(ST.this, "", "Move " + x + " | " + y, debug);

                    if(!overlayActivated && (moveY > 130 || moveY < -130) && !inViewBounds(topPager, x, y) && !inViewBounds(botPager, x, y)){

                        overlayActivated = true;

                        btnCondidate = wsBtn5;
                        btnCondidateCoord[0] = 1;
                        btnCondidateCoord[1] = 1;
                        fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::5::"+wsBtn1.getText().toString()+":"+wsBtn2.getText().toString()+":"+wsBtn3.getText().toString()+":"+wsBtn4.getText().toString()+":"+wsBtn5.getText().toString()+":"+wsBtn6.getText().toString()+":"+wsBtn7.getText().toString()+":"+wsBtn8.getText().toString()+":"+wsBtn9.getText().toString()+":", "");
                        timeActivated = System.currentTimeMillis();
                        InAppLog.writeLog(ST.this, "", "Activated!", debug);
                        startY = y;
                        startX = x;

                    }
                    if(overlayActivated){
                        if(System.currentTimeMillis()-timeActivated>100){
                            int moveCondidateX=0;
                            int moveCondidateY=0;
                            if(moveX > 40 || moveX < -40){
                                moveCondidateX = moveX>0?-1:1;
                                startX = x;
                                startY = y;
                            }
                            if(moveY > 40 || moveY < -40){
                                moveCondidateY = moveY>0?1:-1;
                                startX = x;
                                startY = y;
                            }
                            moveCondidate(moveCondidateX, moveCondidateY);
                        }else{
                            startX = x;
                            startY = y;
                        }


                        if(inViewBounds(v, x,y)){
                            fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                            fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                            overlayActivated = false;
                            InAppLog.writeLog(ST.this, "", "Overlay release by return", debug);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if(overlayActivated){
                        InAppLog.writeLog(ST.this, "", "Overlay release", debug);
                        overlayActivated = false;
                        this.v.vibrate(20);
                        fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                        fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                        btnCondidate.performClick();
                    }

                break;
/*
                case MotionEvent.ACTION_CANCEL:
                    InAppLog.writeLog(ST.this, "", "Overlay release (ACTION_CANCEL)", debug);
                    overlayActivated = false;
                    fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                    fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                    break;
*/
            }



            return false;
        }

        private void moveCondidate(int x, int y){
            int btnCoordinateOld = btnCondidateInt;
            btnCondidateCoord[0] += x;
            btnCondidateCoord[1] += y;
            if(btnCondidateCoord[0]>2) btnCondidateCoord[0] =2;
            if(btnCondidateCoord[1]>2) btnCondidateCoord[1] =2;

            if(btnCondidateCoord[0]<0) btnCondidateCoord[0] =0;
            if(btnCondidateCoord[1]<0) btnCondidateCoord[1] =0;

            if (btnCondidateCoord[0] == 0 && btnCondidateCoord[1] == 0){
                btnCondidate = wsBtn7;
                btnCondidateInt = 7;
            }
            if (btnCondidateCoord[0] == 0 && btnCondidateCoord[1] == 1){
                btnCondidate = wsBtn4;
                btnCondidateInt = 4;
            }
            if (btnCondidateCoord[0] == 0 && btnCondidateCoord[1] == 2){
                btnCondidate = wsBtn1;
                btnCondidateInt = 1;
            }
            if (btnCondidateCoord[0] == 1 && btnCondidateCoord[1] == 0){
                btnCondidate = wsBtn8;
                btnCondidateInt = 8;
            }
            if (btnCondidateCoord[0] == 1 && btnCondidateCoord[1] == 1){
                btnCondidate = wsBtn5;
                btnCondidateInt = 5;
            }
            if (btnCondidateCoord[0] == 1 && btnCondidateCoord[1] == 2){
                btnCondidate = wsBtn2;
                btnCondidateInt = 2;
            }
            if (btnCondidateCoord[0] == 2 && btnCondidateCoord[1] == 0){
                btnCondidate = wsBtn9;
                btnCondidateInt = 9;
            }
            if (btnCondidateCoord[0] == 2 && btnCondidateCoord[1] == 1){
                btnCondidate = wsBtn6;
                btnCondidateInt = 6;
            }
            if (btnCondidateCoord[0] == 2 && btnCondidateCoord[1] == 2){
                btnCondidate = wsBtn3;
                btnCondidateInt = 3;
            }


            if(btnCoordinateOld!=btnCondidateInt){
                fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::"+btnCondidateInt+"::", "");
                this.v.vibrate(10);
            }
        }
    }

    public class OverlayAltTAbOTL implements OnTouchListener {

        boolean overlayActivated = false;

        int startX;
        int startY;
        int x;
        int y;
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long timeActivated;

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    InAppLog.writeLog(ST.this, "", "On Touch " + startX + " | " + startY, debug);
                    break;

                case MotionEvent.ACTION_MOVE:

                    x = (int) event.getRawX();
                    y = (int) event.getRawY();

                    int moveY = startY - y;

                    InAppLog.writeLog(ST.this, "", "Move " + x + " | " + y, debug);

                    if (!overlayActivated && (moveY > 200 || moveY < -200) && !inViewBounds(topPager, x, y) && !inViewBounds(botPager, x, y)) {

                        overlayActivated = true;
                        this.v.vibrate(10);
                        fnb.press(ButtonFnManager.FN_ALT_TAB, "", "");
                        timeActivated = System.currentTimeMillis();
                        InAppLog.writeLog(ST.this, "", "ALTTAb Activated!", debug);
                        startY = y;
                        startX = x;

                    }
                    if (overlayActivated) {
                        if (System.currentTimeMillis() - timeActivated > 100) {


                            if (moveY > 40 || moveY < -40) {

                                startX = x;
                                startY = y;
                                fnb.press(ButtonFnManager.FN_CUSTOM, moveY>0?"Left arrow":"Right arrow", "");
                                this.v.vibrate(10);
                            }

                        } else {
                            startX = x;
                            startY = y;
                        }


                        if (inViewBounds(v, x, y)) {
                            fnb.press(ButtonFnManager.FN_CUSTOM, "Esc", "");
                            overlayActivated = false;
                            InAppLog.writeLog(ST.this, "", "ALT TAB Overlay release by return", debug);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (overlayActivated) {
                        InAppLog.writeLog(ST.this, "", "ALT TAB Overlay release", debug);
                        overlayActivated = false;
                        fnb.press(ButtonFnManager.FN_CUSTOM, "Enter", "");

                        Executors.newSingleThreadScheduledExecutor().schedule(new SocketThread(shpCross.getString("ip", "198.168.0.1"), Integer.parseInt(shpCross.getString("port", "12342")), ButtonFnManager.ab, 0, 0, ST.this), 400, TimeUnit.MILLISECONDS);

                        this.v.vibrate(20);

                    }

                    break;

                case MotionEvent.ACTION_CANCEL:
                    InAppLog.writeLog(ST.this, "", "ALT TAB Overlay release (ACTION_CANCEL)", debug);
                    overlayActivated = false;
                    fnb.press(ButtonFnManager.FN_CUSTOM, "Esc", "");
                    break;

            }


            return false;
        }

    }

    public class ScrollOtl implements OnTouchListener {

        boolean overlayActivated = false;

        int startX;
        int startY;
        int x;
        int y;
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long timeActivated;

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    InAppLog.writeLog(ST.this, "", "On Touch Scroll" + startX + " | " + startY, debug);
                    break;

                case MotionEvent.ACTION_MOVE:

                    x = (int) event.getRawX();
                    y = (int) event.getRawY();

                    int moveY = startY - y;

                    InAppLog.writeLog(ST.this, "", " Scroll Move " + x + " | " + y + "|" + moveY, debug);

                    if (moveY > 10 || moveY < -10) {

                        startX = x;
                        startY = y;
                        fnb.press(moveY>0?ButtonFnManager.FN_WHELL_UP:ButtonFnManager.FN_WHELL_DOWN, "", "");
                        this.v.vibrate(5);
                    }
                    break;

            }


            return false;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DbTool db = new DbTool();
        switch (keyCode){

            case KeyEvent.KEYCODE_VOLUME_UP:
                Cursor btnCursor = db.getButtonCursor(shp.getLong(currentProcess+VOL_UP, -1), this);
                if(btnCursor!=null){
                    fnb.press(btnCursor.getInt(btnCursor.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)),
                            btnCursor.getString(btnCursor.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)),"");
                }else{
                    fnb.press(shp.getInt(VOL_UP_DEFAULT_TYPE, ButtonFnManager.FN_COMMAND_LINE), shp.getString(VOL_UP_DEFAULT_ARGS, "nircmdc changesysvolume 2000"), "");
                }
               return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Cursor btnCursor2 = db.getButtonCursor(shp.getLong(currentProcess+VOL_DOWN, -1), this);
                if(btnCursor2!=null){
                    fnb.press(btnCursor2.getInt(btnCursor2.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)),
                            btnCursor2.getString(btnCursor2.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)),"");
                }else{
                    fnb.press(shp.getInt(VOL_DOWN_DEFAULT_TYPE, ButtonFnManager.FN_COMMAND_LINE), shp.getString(VOL_DOWN_DEFAULT_ARGS, "nircmdc changesysvolume -2000"), "");
                }
                return true;

            default:
                return super.onKeyDown(keyCode, event);
        }


    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){

            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;

            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                return super.onKeyDown(keyCode, event);

            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void setCurrentProcess(String process){

            currentProcess = process;
            status.setText(currentProcess);
        new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"),
                Integer.parseInt(shpCross.getString("port", "12342")), ButtonFnManager.getProcessIcon, 0, 0, ST.this)).start();
            bindContextButtons(currentProcess.substring(currentProcess.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", ""), 0);


    }


    public  String getBroadcast(){
        String found_bcast_address=null;
        System.setProperty("java.net.preferIPv4Stack", "true");
        try
        {
            Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces();
            while (niEnum.hasMoreElements())
            {
                NetworkInterface ni = niEnum.nextElement();
                if(!ni.isLoopback()){
                    for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses())
                    {
                            if(interfaceAddress.getBroadcast()==null)continue;
                            found_bcast_address = interfaceAddress.getBroadcast().toString();
                            found_bcast_address = found_bcast_address.substring(1);

                        //found_bcast_address = interfaceAddress.getAddress().toString();//.substring(0, interfaceAddress.getAddress().toString().lastIndexOf('.'));
                        //found_bcast_address += ".255";
                        //Toast.makeText(this, found_bcast_address+ "", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        return found_bcast_address;
    }

    private void discoverServers() {
                ipPortsArray.clear();
                String broadcastAdress = getBroadcast();
                   breakDiscovering = false;
                if(broadcastAdress!=null){
                    for (int j = 1026; j <= 1032; j++) {
                        for (int i = 1; i <=255 ; i++) {
                            if(breakDiscovering)break;
                            String ipToDesc = broadcastAdress.substring(0, broadcastAdress.lastIndexOf("255"))+i;
                            new Thread(new SocketThread(ipToDesc/*broadcastAdress*/, j==1032?1025:j, ButtonFnManager.ab, 12, 12, ST.this) {

                                @Override
                                public void run() {

                                    super.run();

                                    try {
                                        //final String s = in.readUTF();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(line!=null&&line.startsWith("ok")) {
                                                    ((ArrayAdapter) dialog.getListView().getAdapter()).add(line.substring(0, line.lastIndexOf("<process>")).substring(2));
                                                    ipPortsArray.add(ip + ":" + port);
                                                }

                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }).start();
                        }

                    }


                }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.st, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int port = Integer.parseInt(shpCross.getString("port", "12342"));

        switch (item.getItemId()) {
            case R.id.action_scan:
                scan.performClick();
                break;
            /*
            case R.id.enterAfterVoiceInput:
                if (shp.getBoolean("enterOnVoiceInput", true)) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                ed.putBoolean("enterOnVoiceInput", item.isChecked());
                ed.commit();
                break;


            case R.id.map:
                Intent intent = new Intent(this, MappingList.class);
                startActivity(intent);
                break;

            case R.id.voiceFnSettings:
                Intent intent1 = new Intent(this, VoiceFnMappingList.class);
                startActivity(intent1);
                break;
            */

            case R.id.keyboard:
                keyboardEt.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
                break;

            case R.id.voiceInput:
                startVoiceRecognitionActivity(REQUEST_CODE_VOICE_INPUT, null);
                break;

            case R.id.settings:
                if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT))mDrawerLayout.closeDrawers();
                else mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int port = Integer.parseInt(shpCross.getString("port", "12342"));
        switch (v.getId()) {
            case R.id.bSend:
                try {

                    int a = Integer.parseInt(aEt.getText().toString());
                    int b = Integer.parseInt(bEt.getText().toString());
                    bEt.setText(Integer.parseInt(bEt.getText().toString()) + 1 + "");

                    new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"), port,
                            ButtonFnManager.ab, a, b, ST.this)).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.bScan:

                final boolean installed = appInstalledOrNot("com.google.zxing.client.android");


                AlertDialog.Builder builder = new AlertDialog.Builder(this,
                        AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle(getString(R.string.connect_dialog_title));

                // Set up the input
                final EditText input = new EditText(this);

                input.setHint("IP");
                input.setText(shpCross.getString("ip", ""));
               input.setInputType(InputType.TYPE_CLASS_PHONE);
               // input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                input.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LayoutParams.WRAP_CONTENT, 1f));

                final EditText inputPort = new EditText(this);

                inputPort.setHint("Port");
                inputPort.setText(shpCross.getString("port", ""));
                inputPort.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputPort.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LayoutParams.WRAP_CONTENT, 1f));

                TextView tv = new TextView(this);
                tv.setText(getString(R.string.connect_dialog_body));
                tv.setPadding(10, 10, 10, 0);
                tv.setTextSize(15);


                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);

                LinearLayout llHorizontal = new LinearLayout(this);
                llHorizontal.setOrientation(LinearLayout.HORIZONTAL);

                llHorizontal.addView(input);
                llHorizontal.addView(inputPort);

                ll.addView(tv);
                ll.addView(llHorizontal);

                builder.setView(ll);

                // Set up the buttons
                builder.setPositiveButton(getString(R.string.connect_dialog_okj),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                dialogInputText = input.getText().toString()
                                        + ":" + inputPort.getText().toString();

                                if (!input.getText().toString().equals("")
                                        && !inputPort.getText().toString()
                                        .equals("")) {
                                    String[] adressParts = dialogInputText
                                            .split(":");
                                    String IP = adressParts[0];
                                    String port = adressParts[1];

                                    ipEt.setText(IP);
                                    portEt.setText(port);

                                    saveIpPort(IP, port);

                                    dialog.dismiss();
                                } else if (!input.getText().toString()
                                        .equals("")) {

                                    Toast.makeText(getBaseContext(),
                                            getString(R.string.connect_dialog_no_port),
                                            Toast.LENGTH_SHORT).show();
                                } else if (!inputPort.getText().toString()
                                        .equals("")) {

                                    Toast.makeText(getBaseContext(),
                                            getString(R.string.connect_dialog_no_ip),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(getBaseContext(),
                                            getString(R.string.connect_dilog_no_ip_no_port),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                builder.setNegativeButton((installed) ? getString(R.string.connect_dialog_scan_qr) : getString(R.string.connect_dialog_no_barcode_scanner),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (!installed) {
                                    String url = "https://play.google.com/store/apps/details?id=com.google.zxing.client.android";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                    dialog.cancel();

                                } else {

                                    Intent intent = new Intent(
                                            "com.google.zxing.client.android.SCAN");
                                    intent.setPackage("com.google.zxing.client.android");
                                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

                                    startActivityForResult(intent, 0);
                                }

                            }
                        });

                final ArrayAdapter<String> serversArrayAdapter = new ArrayAdapter<String>(
                        ST.this,
                        android.R.layout.select_dialog_singlechoice);

                builder.setAdapter(serversArrayAdapter,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String[] ipPort = ipPortsArray.get(which).split(":");
                            ipEt.setText(ipPort[0]);
                            portEt.setText(ipPort[1]);

                            saveIpPort(ipPort[0], ipPort[1]);
                            breakDiscovering = true;
                            new Thread(new SocketThread(ipPort[0],Integer.parseInt(ipPort[1]), ButtonFnManager.ab, 0,0,ST.this )).start();
                        }
                    });

                dialog = builder.create();

                dialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        discoverServers();
                    }
                }).start();
                break;

            case R.id.addButton:
                DbTool db = new DbTool();
                if(isFull || (db.getButtonsCount(this)<=8)){
                    Intent intent = new Intent(this, FnBind.class);
                    startActivityForResult(intent, REQUEST_CODE_ADD_BUTTON);
                }else{
                    showBuyDialog();
                }

                break;

        }

    }

    private void saveIpPort(String IP, String port) {
        ed.putString("ip", IP);
        ed.putString("port", port);
        ed.commit();

        WifiManager wifiManager = (WifiManager) getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        edCross.putString("WIFIIP:" + info.getBSSID(), IP);
        edCross.putString("WIFIPORT:" + info.getBSSID(), port);

        edCross.putString("ip", IP);
        edCross.putString("port", port);
        edCross.commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                String[] adressParts = contents.split(":");
                String IP = adressParts[0];
                String port = adressParts[1];

                ipEt.setText(IP);
                portEt.setText(port);
                saveIpPort(IP, port);

            }
        }

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, intent)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, intent);

        //bind contextButtonsArrayList<Integer>
        ArrayList<Integer> arr = new ArrayList<Integer>();
       for(FnButton b:fnButtons){
           arr.add(getReqCodeById(b.getId()));
       }
        if(resultCode==RESULT_OK && arr.contains(requestCode)){saveFnBindResults(intent, requestCode, currentProcess.substring(currentProcess.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", ""));}

        boolean needReinvokeVoiceFn = false;
        //Fixing voice input
        if (resultCode == RESULT_OK && requestCode!=REQUEST_CODE_TUTORIAL) {
            ArrayList<String> matchesToFix = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matchesToFix != null && matchesToFix.size() > 0) {
                String m_Text = "";
                String m_Text_old = "";

                m_Text = matchesToFix.get(0);
                //Reinvoke?
                if (m_Text.endsWith(" потом")) {
                    m_Text = m_Text.substring(0, m_Text.length() - 6);
                    needReinvokeVoiceFn = true;
                }
                m_Text_old = m_Text;
                for (String s : keyoVoiceInputFix) {

                    if (m_Text.contains(s)) {
                        m_Text = m_Text.replace(s, shp.getString(s, s));

                    }
                }

                matchesToFix.add(0, m_Text);
                intent.putExtra(RecognizerIntent.EXTRA_RESULTS, matchesToFix);
                if (m_Text.equals(m_Text_old)) {
                    Toast.makeText(getBaseContext(), "Voice input: " + m_Text,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(),
                            "Voice input fix: " + m_Text_old + "\u2192" + m_Text,
                            Toast.LENGTH_SHORT).show();
                }


            }
        }

        if(requestCode == REQUEST_CODE_LAUNCHAPP_FROM_TASKBAR){
            switch (resultCode){
                case RESULT_OK:
                    new Thread(new SocketThread(shpCross.getString("ip", "198.168.0.1"),
                            Integer.parseInt(shpCross.getString("port", "12342")), ButtonFnManager.ab, 0, 0, ST.this)).start();
                    break;

            }
        }

        if (requestCode == REQUEST_CODE_LAUNCH_APP) {

            switch (resultCode) {
                case RESULT_OK:
                    String m_Text = "";
                    ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    m_Text = matches.get(0);

                    //			m_Text = shp.getString(m_Text, m_Text);

                    int port = Integer.parseInt(shpCross.getString("ip", "12342"));
                    new Thread(new SocketThread(ST.this, shpCross.getString("port", "198.168.0.1"), port, ButtonFnManager.launch, m_Text)).start();

                    break;

                case RESULT_CANCELED:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this,
                            AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Keyboard input");

                    // Set up the input
                    final EditText input = new EditText(this);

                    input.setHint("App to launch");

                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                boolean needReinvokeVoiceFn = false;

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int port = Integer.parseInt(shpCross.getString("port", "12342"));
                                    String m_Text = input.getText().toString();
                                    //Check for Reinvoke
                                    if (m_Text.endsWith(" потом")) {
                                        m_Text = m_Text.substring(0, m_Text.length() - 6);
                                        needReinvokeVoiceFn = true;
                                    }
                                    new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"), port, ButtonFnManager.launch, m_Text)).start();
                                    //Reinvoke
                                    if (needReinvokeVoiceFn) {
                                        fnb.press(ButtonFnManager.FN_VOICE_FN, "", "");
                                        needReinvokeVoiceFn = false;
                                    }
                                }
                            });
                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.cancel();
                                }
                            });

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    break;


            }

        }


        if (requestCode == REQUEST_CODE_VOICE_FN) {
            String m_Text = "";

            switch (resultCode) {
                case RESULT_OK:
                    ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    m_Text = matches.get(0);
                    m_Text += " ";
                    m_Text = m_Text.toLowerCase();

                    //Set<String> keys = shp.getStringSet("VoiceFnMap", new HashSet<String>());
                    DbTool db = new DbTool();
                    Cursor buttonsNames = db.getCursor(DbTool.BUTTONS_TABLE, this);

                    HashMap<String, Long> map = new HashMap<String, Long>();

                    if(buttonsNames.moveToFirst()){
                        do {
                            map.put(buttonsNames.getString(buttonsNames.getColumnIndex(DbTool.BUTTONS_TABLE_NAME)), buttonsNames.getLong(buttonsNames.getColumnIndex("_id")));
                        }while (buttonsNames.moveToNext());

                    }
                    Set<String> keys = map.keySet();
                    if (!keys.isEmpty()) {
                        for (String key : keys) {
                            key = key.toLowerCase();
                            if (m_Text.startsWith(key + " ")) {
                                String args = "";

                                if (m_Text.equals(key)) {
                                    args = "";
                                } else {
                                    args = m_Text.replaceFirst(key + " ", "");
                                }
                                Cursor button = db.getButtonCursor(map.get(key), ST.this);
                                if(button!=null){
                                    fnb.press(button.getInt(button.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)), button.getString(button.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)), args);
                                }
                            }
                        }
                    }

                    break;


                case RESULT_CANCELED:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this,
                            AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Keyboard input");

                    // Set up the input
                    final EditText input = new EditText(this);

                    input.setHint("What Fn to fire? Input?");

                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                boolean needReinvokeVoiceFn = false;

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String m_Text_old = input.getText().toString();

                                    String m_Text = input.getText().toString();

                                    //Set<String> keys = shp.getStringSet("VoiceFnMap", new HashSet<String>());a
                                    //Check for Reinvoke
                                    if (m_Text.endsWith(" потом")) {
                                        m_Text = m_Text.substring(0, m_Text.length() - 6);
                                        needReinvokeVoiceFn = true;
                                    }

                                    //Set<String> keys = shp.getStringSet("VoiceFnMap", new HashSet<String>());
                                    DbTool db = new DbTool();
                                    Cursor buttonsNames = db.getCursor(DbTool.BUTTONS_TABLE, ST.this);

                                    HashMap<String, Long> map = new HashMap<String, Long>();

                                    if(buttonsNames.moveToFirst()){
                                        do {
                                            map.put(buttonsNames.getString(buttonsNames.getColumnIndex(DbTool.BUTTONS_TABLE_NAME)), buttonsNames.getLong(buttonsNames.getColumnIndex("_id")));
                                        }while (buttonsNames.moveToNext());

                                    }
                                    Set<String> keys = map.keySet();
                                    if (keys!=null && !keys.isEmpty()) {

                                        for (String key : keys) {
                                            if (m_Text.startsWith(key)) {
                                                String args;

                                                if (m_Text.equals(key)) {
                                                    args = "";
                                                } else {
                                                    args = m_Text.replaceFirst(key + " ", "");
                                                }
                                                Cursor button = db.getButtonCursor(map.get(key), ST.this);
                                                if(button!=null){
                                                    fnb.press(button.getInt(button.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)), button.getString(button.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)), args);
                                                }


                                            }
                                        }
                                    }
                                    //Reinvoke
                                    if (needReinvokeVoiceFn) {
                                        fnb.press(ButtonFnManager.FN_VOICE_FN, "", "");
                                        needReinvokeVoiceFn = false;
                                    }

                                }
                            });
                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.cancel();
                                }
                            });

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    break;

            }

        }


        if (requestCode == REQUEST_CODE_COMMAND_LINE_VOICE_INPUT) {
            String m_Text = "";
            switch (resultCode) {
                case RESULT_OK:
                    ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    fnb.press(ButtonFnManager.FN_COMMAND_LINE, currentCommandLineaArgs.replace("<input>", matches.get(0)), "");

                    break;

                case RESULT_CANCELED:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this,
                            AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Keyboard input");

                    // Set up the input
                    final EditText input = new EditText(this);

                    input.setHint("input");

                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                boolean needReinvokeVoiceFn = false;

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Check for Reinvoke
                                    String m_Text = input.getText().toString();
                                    if (m_Text.endsWith(" потом")) {
                                        m_Text = m_Text.substring(0, m_Text.length() - 6);
                                        needReinvokeVoiceFn = true;
                                    }
                                    fnb.press(ButtonFnManager.FN_COMMAND_LINE, currentCommandLineaArgs.replace("<input>", m_Text), "");
                                    //Reinvoke
                                    if (needReinvokeVoiceFn) {
                                        fnb.press(ButtonFnManager.FN_VOICE_FN, "", "");
                                        needReinvokeVoiceFn = false;
                                    }
                                }
                            });
                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    dialog.cancel();
                                }
                            });

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    break;


            }

        }


        if (requestCode == REQUEST_CODE_VOICE_INPUT && resultCode == RESULT_OK) {
            ArrayList<String> matches = intent
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String m_Text = matches.get(0);

//			m_Text = shp.getString(m_Text, m_Text);

            int port = Integer.parseInt(shpCross.getString("port", "12342"));
            new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"), port,
                    ButtonFnManager.keyboard, m_Text)).start();
            if (shp.getBoolean("enterOnVoiceInput", true)) {
                new Thread(new SocketThread(ST.this, shpCross.getString("ip", "198.168.0.1"), port,
                        ButtonFnManager.keyboard, "\n")).start();
            }

        }





        switch (requestCode) {

            case REQUEST_CODE_FIRE_FN:
                if (resultCode == RESULT_OK) {
                    fnb.press(intent.getIntExtra("FnResult", ButtonFnManager.NO_FUNCTION),
                            intent.getStringExtra("FnResultArgs"), "");
                }
                break;

            case REQUEST_CODE_ADD_BUTTON:
                if (resultCode == RESULT_OK) {
                    DbTool db = new DbTool();
                    db.addButton(-1, intent.getStringExtra("Name"), intent.getIntExtra("FnResult", ButtonFnManager.NO_FUNCTION), intent.getStringExtra("FnResultArgs"), -1, this, intent.getStringExtra("plugin"), intent.getIntExtra("color", 0));

                }
                DrawerGridAdapter adapter = (DrawerGridAdapter) mDrawerGrid.getAdapter();
                adapter.getCursor().requery();
                adapter.notifyDataSetChanged();
                break;

            case REQUEST_CODE_EDIT_BTN:
                if (resultCode == RESULT_OK) {
                    DbTool db2 = new DbTool();
                    if(intent!=null && intent.getLongExtra(FnBind.BTN_ID, -1)!=-1){
                        db2.addButton(intent.getLongExtra(FnBind.BTN_ID, -1), intent.getStringExtra("Name"), intent.getIntExtra("FnResult", ButtonFnManager.NO_FUNCTION), intent.getStringExtra("FnResultArgs"), -1, this, intent.getStringExtra("plugin"), intent.getIntExtra("color", 0));
                        updateAllBTNS();

                    }
                }

                break;

            case REQUEST_CODE_SETTINGS:
                setScrollVisability();
                break;

            case REQUEST_CODE_TUTORIAL:
                if(resultCode==RESULT_OK){
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "http://kor-ka.github.io/Adaptive-remote-android-client/");
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_link_via)));
                }


                break;

        }

        //Reinvoke voiceInput if needed
        if (needReinvokeVoiceFn) {
            fnb.press(ButtonFnManager.FN_VOICE_FN, "", "");
            needReinvokeVoiceFn = false;
        }

        }
        else {
            Log.d("IAB", "onActivityResult handled by IABUtil.");
        }
    }

    /*Когда то я был сервером...
    public class Listener extends Thread {
        protected ServerSocket listenSocket;
        DataOutputStream out;
        Socket socket;

        public Listener(ServerSocket listenSocket) {
            this.listenSocket = listenSocket;

        }

        public void run() {

            while (true) {
                try {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "waiting",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    socket = listenSocket.accept();

                    InputStream sin = socket.getInputStream();
                    OutputStream sout = socket.getOutputStream();

                    DataInputStream in = new DataInputStream(sin);
                    DataOutputStream out = new DataOutputStream(sout);

                    final String line = in.readUTF();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (line.contains("results:")) {
                                results.clear();
                                if (line.length() > 8) {
                                    List<String> list = new ArrayList<String>(
                                            Arrays.asList(line.substring(9)
                                                    .split(":")));
                                    results = (ArrayList<String>) list;
                                }

                                Toast.makeText(getBaseContext(),
                                        "incoming: " + results.size(),
                                        Toast.LENGTH_SHORT).show();
                                adapter.clear();
                                for (String s : results) {
                                    adapter.add(s);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getBaseContext(), line,
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } catch (final IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }

            }

        }
    }
    */

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
            mDrawerLayout.closeDrawers();
        }else if (up.getVisibility() == View.VISIBLE) {
            up.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }



    }

    @Override
    protected void onDestroy() {
        ed.putString("ip", ipEt.getText().toString());
        ed.putString("port", portEt.getText().toString());
        ed.commit();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
        super.onDestroy();
    }

    // Utils
    // check is app installed
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void startVoiceRecognitionActivity(int requesrCode, String args) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        switch (requesrCode) {
            case REQUEST_CODE_LAUNCH_APP:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_input_hint_launch_app));
                break;

            case REQUEST_CODE_VOICE_INPUT:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_input_hint_voice_input));
                break;

            case REQUEST_CODE_COMMAND_LINE_VOICE_INPUT:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_input_hint_cmd_input));
                currentCommandLineaArgs = args;
                break;

            case REQUEST_CODE_VOICE_FN:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_input_hint_voice_fn));
                break;
        }

        startActivityForResult(intent, requesrCode);
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        String topOrBot = "";
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        public ScreenSlidePagerAdapter(
                android.support.v4.app.FragmentManager fragmentManager,
                String topOrBot) {
            super(fragmentManager);
            this.topOrBot = topOrBot;
            mFragmentManager = fragmentManager;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            Bundle arguments = new Bundle();

            arguments.putString(FnButtonsFragment.PAGE_ID_ARG, topOrBot
                    + position);


            return Fragment.instantiate(ST.this, FnButtonsFragment.class.getName(), arguments);

        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void bindContextButtons(final String currentProcess, int buttonToUpdate){
        if(buttonToUpdate==0){
            for (FnButton b:fnButtons ) {
                b.init(getReqCodeById(b.getId())+""+currentProcess, this, ocl, olclFn, fnb);
            }
        }else{
            for (FnButton b:fnButtons ){
                if(getReqCodeById(b.getId())==buttonToUpdate){
                    b.init(getReqCodeById(b.getId())+""+currentProcess, this, ocl, olclFn, fnb);
                    break;
                }
            }
        }

    }

    public void saveFnBindResults (Intent i, int reqestCode, final String currentProcess){
        DbTool db = new DbTool();
        if(i.getIntExtra("FnResult", ButtonFnManager.NO_FUNCTION)!= ButtonFnManager.NO_FUNCTION){
            //Пишем кнопку в базу
            //Сейчас всегда заливаем новую. Потом будем обновлять по id

            long id = db.addButton(i.getLongExtra("id", -1), i.getStringExtra("Name"), i.getIntExtra("FnResult", ButtonFnManager.NO_FUNCTION), i.getStringExtra("FnResultArgs"), -1, this, null, 0);

            db.bindButtonToPlace(id, reqestCode+""+currentProcess.substring(currentProcess.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", ""), this);
            DrawerGridAdapter adapter = (DrawerGridAdapter) mDrawerGrid.getAdapter();
            adapter.getCursor().requery();
            adapter.notifyDataSetChanged();

        }else{
            db.bindButtonToPlace(-1,reqestCode+""+currentProcess.substring(currentProcess.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", ""), this);
        }

        bindContextButtons(currentProcess.substring(currentProcess.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", ""), reqestCode);

    }

    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+getPackageName()+"//databases//"+databaseName+"";
                String backupDBPath = "backup.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    public static int getReqCodeById(int id){
        id = id<0?id*-1:id;
        return id & 0xFFFF;
    }


    public void showBuyDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(ST.this).create();
        alertDialog.setTitle(getString(R.string.full_access_dialog_title));
        alertDialog.setMessage(getString(R.string.full_access_dialog_body));
        alertDialog.setButton(getString(R.string.full_access_dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mHelper.launchPurchaseFlow(ST.this, "full", 10001,
                        new IabHelper.OnIabPurchaseFinishedListener() {
                            @Override
                            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                                if(info!=null)
                                    isFull = info.getPurchaseState()==0;
                            }
                        }, "");
                dialog.dismiss();
            }
        });

        alertDialog.setButton2(getString(R.string.full_access_dialog_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });



        alertDialog.show();
    }
}
