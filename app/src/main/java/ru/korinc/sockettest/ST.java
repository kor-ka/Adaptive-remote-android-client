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
import android.widget.CheckBox;
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

public class ST extends FragmentActivity implements OnClickListener {


    boolean debug = false;

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
    Editor ed;
    float fullmovex;
    float fullmovey;
    boolean isDouble = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setContentView(R.layout.activity_st);

        if(shp.getBoolean("firstLaunch", true)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this,
                    AlertDialog.THEME_HOLO_LIGHT);
            builder.setTitle("Ого, да у нас тут новичок!");



            TextView tv = new TextView(this);
            tv.setText("Привет!" +
                    "\nДля начала тебе понадобится сервер для твоего компа.  ");
            tv.setPadding(10, 10, 10, 0);
            tv.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault);
            /*
            TextView tvLink = new TextView(this);
            tvLink.setText( Html.fromHtml("<a href=\"http://sites.google.com/site/adaptiveremote/\">Adptv server</a>"));
            tvLink.setMovementMethod(LinkMovementMethod.getInstance());
            tvLink.setPadding(10, 10, 10, 10);
            tvLink.setTextAppearance(this, android.R.style.TextAppearance_DeviceDefault_Large);
            tvLink.setGravity(Gravity.CENTER_HORIZONTAL);
            */
            final CheckBox chb = new CheckBox(this);
            chb.setText("Больше не показывать");
            chb.setChecked(true);


            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);



            ll.addView(tv);
            //ll.addView(tvLink);
            ll.addView(chb);

            builder.setView(ll);

            builder.setPositiveButton("Ага",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ed.putBoolean("firstLaunch", !chb.isChecked());
                            ed.commit();
                           dialog.dismiss();

                        }
                    });
            builder.setNegativeButton("Отправь мне ссылку",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://sites.google.com/site/adaptiveremote/");
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_link_via)));

                            ed.putBoolean("firstLaunch", !chb.isChecked());
                            ed.commit();
                            dialog.dismiss();
                        }
                    });

            builder.create().show();


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

        //init voice tables

        Set<String> keys = shp.getStringSet("VoiceFnMap", new HashSet<String>());
        if (keys.isEmpty()) {
/*
            keys.add("поиск");
            ed.putString("VoiceFnArg:" + "поиск", "start chrome \"\"");
            ed.putInt("VoiceFn:" + "поиск", FnButton.FN_COMMAND_LINE);
*/
            keys.add("хром");
            ed.putString("VoiceFnArg:" + "хром", "start chrome");
            ed.putInt("VoiceFn:" + "хром", ButtonFnManager.FN_COMMAND_LINE);

            keys.add("запустить");
            ed.putString("VoiceFnArg:" + "запустить", "Launch app");
            ed.putInt("VoiceFn:" + "запустить", ButtonFnManager.FN_LAUNCH_APP);

            ed.putStringSet("VoiceFnMap", keys);
            ed.commit();

            int[] buttonsToInit = new int[]{
                    ButtonFnManager.FN_VOICE_FN,
                    ButtonFnManager.FN_LAUNCHFROM_TASKBAR,
                    ButtonFnManager.FN_FIRE_FN,
                    ButtonFnManager.FN_ARROWS,
                    ButtonFnManager.FN_R_CLICK,
                    ButtonFnManager.FN_ENTER
            };

            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[0], "", 0, this, "", 0), "fnB1" + "top1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[1], "", 0, this, "", 0), "fnB2" + "top1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[2], "", 0, this, "", 0), "fnB3" + "top1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[3], "", 0, this, "", 0), "fnB1" + "bot1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[5], "", 0, this, "", 0), "fnB3" + "bot1",this);
            db.bindButtonToPlace(db.addButton(-1, "", buttonsToInit[4], "", 0, this, "", 0), "fnB2" + "bot1",this);

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
                            b.init(i.getLongExtra("id", 0), ST.this, ocl, olclFn, fnb);

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
                        switch (view.getId()){
                            case R.id.editButton:
                                ClipData.Item item2 = dragEvent.getClipData().getItemAt(0);
                                Intent i2 = item2.getIntent();
                                long id2=i2.getLongExtra("id", 0);

                                Intent editIntent = new Intent(ST.this, FnBind.class);
                                editIntent.putExtra(FnBind.BTN_ID, id2);

                                startActivityForResult(editIntent, REQUEST_CODE_EDIT_BTN);
                                break;

                            case R.id.dellButton:
                                DbTool db = new DbTool();
                                ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                                Intent i = item.getIntent();
                                long id=i.getLongExtra("id", 0);
                                db.delRec(DbTool.BUTTONS_TABLE, id, ST.this);


                                updateAllBTNS();

                                break;
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        dragMenuLL.setVisibility(View.INVISIBLE);
                        editBtn.setBackgroundColor(ST.this.getResources().getColor(R.color.transparent_gray));
                        dellBtn.setBackgroundColor(ST.this.getResources().getColor(R.color.transparent_gray));
                        editBtn.invalidate();
                        dellBtn.invalidate();
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

        ipEt.setText(shp.getString("ip", ""));
        portEt.setText(shp.getString("port", "1234"));

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

                    int port = Integer.parseInt(portEt.getText().toString());
                    if (s.length() == pressed.length()
                            | (s.length() == 3 && pressed.length() == 1)) {

                        new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                port, ButtonFnManager.keyboard, pressed)).start();
                    }

                    keyboardEt.setText("");
                    keyboardEt.setSelection(keyboardEt.getText().length());
                } /*else if (keyboardEt.getText().toString().equals("<")) {

                    int port = Integer.parseInt(portEt.getText().toString());
                    new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
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

        keyboardEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_DEL){
                    int port = Integer.parseInt(portEt.getText().toString());
                    new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                            port, ButtonFnManager.keyboard, "bksps")).start();
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

                if (fullmovey < 5 & fullmovex < 5) {
                    // Toast.makeText(getBaseContext(), "long",
                    // Toast.LENGTH_SHORT).show();
                    int port = Integer.parseInt(portEt.getText().toString());

                    new Thread(new SocketThread(ipEt.getText().toString(),
                            port, ButtonFnManager.dndDown, 0, 0, ST.this)).start();
                    isDouble = true;
                    timeLongDown = System.currentTimeMillis();
                    if (timeLongDown - timeLongDownOld < 1000) {
                        new Thread(new SocketThread(ipEt.getText().toString(),
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
                int port = Integer.parseInt(portEt.getText().toString());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        timeDown = System.currentTimeMillis();
                        switch (v.getId()) {
                            case R.id.buttonUp:

                                new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                        port, ButtonFnManager.keyboard, "up")).start();
                                break;

                            case R.id.buttonDown:

                                new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                        port, ButtonFnManager.keyboard, "down")).start();
                                break;

                            case R.id.buttonLeft:

                                new Thread( new SocketThread(ST.this, ipEt.getText().toString(),
                                        port, ButtonFnManager.keyboard, "left")).start();
                                break;

                            case R.id.buttonRight:

                                new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                        port, ButtonFnManager.keyboard, "right")).start();
                                break;

                        }
                }

                if (System.currentTimeMillis() - timeDown > 500 && i == 1) {

                    i = 2;

                    switch (v.getId()) {
                        case R.id.buttonUp:

                            new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                    port, ButtonFnManager.keyboard, "up")).start();
                            break;

                        case R.id.buttonDown:

                            new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                    port, ButtonFnManager.keyboard, "down")).start();
                            break;

                        case R.id.buttonLeft:

                            new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                    port, ButtonFnManager.keyboard, "left")).start();
                            break;

                        case R.id.buttonRight:

                            new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
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

            float downx;
            float downy;
            float x;
            float y;
            String sDown;
            String sMove;
            String sUp;

            long timeDown = System.currentTimeMillis();
            long timeUp = System.currentTimeMillis();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getX();
                y = event.getY();

                int port;
                int a;
                int b;

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

                        port = Integer.parseInt(portEt.getText().toString());

                        new Thread(new SocketThread(ipEt.getText().toString(),
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
                            port = Integer.parseInt(portEt.getText().toString());
                            new Thread(new SocketThread(ipEt.getText().toString(),
                                    port, ButtonFnManager.click, 0, 0, ST.this)).start();

                        }

                        // Long click relise
                        if ((timeUp - timeDown) < 100
                                && (fullmovex < 20 & fullmovey < 20) && isDouble) {
                            // send dnd up
                            port = Integer.parseInt(portEt.getText().toString());
                            new Thread(new SocketThread(ipEt.getText().toString(),
                                    port, ButtonFnManager.dndUp, 0, 0, ST.this)).start();

                            isDouble = false;
                        }

                        break;
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

        OnTouchListener scrollOtl = new ScrollOtl();
        View leftScroll = findViewById(R.id.leftScroll);
        View rightScroll = findViewById(R.id.rightScroll);
        leftScroll.setOnTouchListener(scrollOtl);
        rightScroll.setOnTouchListener(scrollOtl);



        InAppLog.writeLog(ST.this, "", "ST on create", debug);

        //exportDatabse("db");
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
                        this.v.vibrate(100);
                        fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                        fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                        btnCondidate.performClick();
                    }

                break;

                case MotionEvent.ACTION_CANCEL:
                    InAppLog.writeLog(ST.this, "", "Overlay release (ACTION_CANCEL)", debug);
                    overlayActivated = false;
                    fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                    fnb.press(ButtonFnManager.FN_COMMAND_LINE, "overlay::0::", "");
                    break;

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
                this.v.vibrate(50);
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

                    //hInAppLog.writeLog(ST.this, "", "Move " + x + " | " + y, debug);

                    if (!overlayActivated && (moveY > 200 || moveY < -200) && !inViewBounds(topPager, x, y) && !inViewBounds(botPager, x, y)) {

                        overlayActivated = true;
                        this.v.vibrate(50);
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
                                this.v.vibrate(50);
                            }

                        } else {
                            startX = x;
                            startY = y;
                        }


                        if (inViewBounds(v, x, y)) {
                            fnb.press(ButtonFnManager.FN_ESC, "", "");
                            overlayActivated = false;
                            InAppLog.writeLog(ST.this, "", "ALT TAB Overlay release by return", debug);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (overlayActivated) {
                        InAppLog.writeLog(ST.this, "", "ALT TAB Overlay release", debug);
                        overlayActivated = false;
                        fnb.press(ButtonFnManager.FN_ENTER, "", "");

                        Executors.newSingleThreadScheduledExecutor().schedule(new SocketThread(ipEt.getText().toString(), Integer.parseInt(portEt.getText().toString()), ButtonFnManager.ab, 0, 0, ST.this), 400, TimeUnit.MILLISECONDS);

                        this.v.vibrate(100);

                    }

                    break;

                case MotionEvent.ACTION_CANCEL:
                    InAppLog.writeLog(ST.this, "", "ALT TAB Overlay release (ACTION_CANCEL)", debug);
                    overlayActivated = false;
                    fnb.press(ButtonFnManager.FN_ESC, "", "");
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





                    if (moveY > 40 || moveY < -40) {

                        startX = x;
                        startY = y;
                        fnb.press(moveY>0?ButtonFnManager.FN_WHELL_UP:ButtonFnManager.FN_WHELL_UP, "", "");
                        this.v.vibrate(50);
                    }




                    break;





            }


            return false;
        }

    }


    public void setCurrentProcess(String process){

            currentProcess = process;
            status.setText(currentProcess);
        new Thread(new SocketThread(ipEt.getText().toString(),
                Integer.parseInt(portEt.getText().toString()), ButtonFnManager.getProcessIcon, 0, 0, ST.this)).start();
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

        int port = Integer.parseInt(portEt.getText().toString());

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
        int port = Integer.parseInt(portEt.getText().toString());
        switch (v.getId()) {
            case R.id.bSend:
                try {

                    int a = Integer.parseInt(aEt.getText().toString());
                    int b = Integer.parseInt(bEt.getText().toString());
                    bEt.setText(Integer.parseInt(bEt.getText().toString()) + 1 + "");

                    new Thread(new SocketThread(ipEt.getText().toString(), port,
                            ButtonFnManager.ab, a, b, ST.this)).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.bScan:

                final boolean installed = appInstalledOrNot("com.google.zxing.client.android");


                AlertDialog.Builder builder = new AlertDialog.Builder(this,
                        AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle("Connect to server");

                // Set up the input
                final EditText input = new EditText(this);

                input.setHint("IP");
                input.setText(shp.getString("ip", ""));
               input.setInputType(InputType.TYPE_CLASS_PHONE);
               // input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
                input.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LayoutParams.WRAP_CONTENT, 1f));

                final EditText inputPort = new EditText(this);

                inputPort.setHint("Port");
                inputPort.setText(shp.getString("port", ""));
                inputPort.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputPort.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LayoutParams.WRAP_CONTENT, 1f));

                TextView tv = new TextView(this);
                tv.setText("Ручной ввод:");
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
                builder.setPositiveButton("Connect!",
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

                                    ed.putString("ip", IP);
                                    ed.putString("port", port);
                                    ed.commit();

                                    dialog.dismiss();
                                } else if (!input.getText().toString()
                                        .equals("")) {

                                    Toast.makeText(getBaseContext(),
                                            "Вы не указали Port :'(",
                                            Toast.LENGTH_SHORT).show();
                                } else if (!inputPort.getText().toString()
                                        .equals("")) {

                                    Toast.makeText(getBaseContext(),
                                            "Вы не указали IP :'(",
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(getBaseContext(),
                                            "Вы не указали IP и Port :'(",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                builder.setNegativeButton((installed) ? "Barcode Scanner" : "Get Barcode Scanner",
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

                            ed.putString("ip", ipPort[0]);
                            ed.putString("port", ipPort[1]);
                            ed.commit();
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
                Intent intent = new Intent(this, FnBind.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_BUTTON);
                break;

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //bind contextButtonsArrayList<Integer>
        ArrayList<Integer> arr = new ArrayList<Integer>();
       for(FnButton b:fnButtons){
           arr.add(getReqCodeById(b.getId()));
       }
        if(resultCode==RESULT_OK && arr.contains(requestCode)){saveFnBindResults(intent, requestCode, currentProcess.substring(currentProcess.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", ""));}

        boolean needReinvokeVoiceFn = false;
        //Fixing voice input
        if (resultCode == RESULT_OK) {
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
                    new Thread(new SocketThread(ipEt.getText().toString(),
                            Integer.parseInt(portEt.getText().toString()), ButtonFnManager.ab, 0, 0, ST.this)).start();
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

                    int port = Integer.parseInt(portEt.getText().toString());
                    new Thread(new SocketThread(ST.this, ipEt.getText().toString(), port, ButtonFnManager.launch, m_Text)).start();

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

                                    int port = Integer.parseInt(portEt.getText().toString());
                                    String m_Text = input.getText().toString();
                                    //Check for Reinvoke
                                    if (m_Text.endsWith(" потом")) {
                                        m_Text = m_Text.substring(0, m_Text.length() - 6);
                                        needReinvokeVoiceFn = true;
                                    }
                                    new Thread(new SocketThread(ST.this, ipEt.getText().toString(), port, ButtonFnManager.launch, m_Text)).start();
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

            int port = Integer.parseInt(portEt.getText().toString());
            new Thread(new SocketThread(ST.this, ipEt.getText().toString(), port,
                    ButtonFnManager.keyboard, m_Text)).start();
            if (shp.getBoolean("enterOnVoiceInput", true)) {
                new Thread(new SocketThread(ST.this, ipEt.getText().toString(), port,
                        ButtonFnManager.keyboard, "\n")).start();
            }

        }

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                String[] adressParts = contents.split(":");
                String IP = adressParts[0];
                String port = adressParts[1];

                ipEt.setText(IP);
                portEt.setText(port);

                ed.putString("ip", IP);
                ed.putString("port", port);
                ed.commit();
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

        }

        //Reinvoke voiceInput if needed
        if (needReinvokeVoiceFn) {
            fnb.press(ButtonFnManager.FN_VOICE_FN, "", "");
            needReinvokeVoiceFn = false;
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
        if (up.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            up.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        ed.putString("ip", ipEt.getText().toString());
        ed.putString("port", portEt.getText().toString());
        ed.commit();
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
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What app to launch?");
                break;

            case REQUEST_CODE_VOICE_INPUT:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak...");
                break;

            case REQUEST_CODE_COMMAND_LINE_VOICE_INPUT:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Input...");
                currentCommandLineaArgs = args;
                break;

            case REQUEST_CODE_VOICE_FN:
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What Fn to fire? Input?");
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

    private int getReqCodeById(int id){
        id = id<0?id*-1:id;
        return id & 0xFFFF;
    }

}
