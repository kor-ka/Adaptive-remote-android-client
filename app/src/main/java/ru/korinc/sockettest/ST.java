package ru.korinc.sockettest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ST extends FragmentActivity implements OnClickListener {

    boolean debug = true;

    EditText ipEt;
    EditText portEt;
    EditText clientPortEt;
    EditText aEt;
    EditText bEt;
    EditText keyboardEt;
    Button scan;
    Button send;

    ImageButton up;
    ImageButton down;
    ImageButton left;
    ImageButton right;

    Button wsBtn1;
    Button wsBtn2;
    Button wsBtn3;
    Button wsBtn4;
    Button wsBtn5;
    Button wsBtn6;
    Button wsBtn7;
    Button wsBtn8;
    Button wsBtn9;

    TableRow tr1;
    TableRow tr2;
    TableRow tr3;

    public ImageButton context;

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


    private static final String FN_SAVE_B1 = "cwfnB1";
    private static final String FN_SAVE_B2 = "cwfnB2";
    private static final String FN_SAVE_B3 = "cwfnB3";
    private static final String FN_SAVE_B4 = "cwfnB4";
    private static final String FN_SAVE_B5 = "cwfnB5";
    private static final String FN_SAVE_B6 = "cwfnB6";
    private static final String FN_SAVE_B7 = "cwfnB7";
    private static final String FN_SAVE_B8 = "cwfnB8";
    private static final String FN_SAVE_B9 = "cwfnB9";

    public static final int REQUEST_CODE_B1 = 12346;
    public static final int REQUEST_CODE_B2 = 12347;
    public static final int REQUEST_CODE_B3 = 12348;
    public static final int REQUEST_CODE_B4 = 12349;
    public static final int REQUEST_CODE_B5 = 12350;
    public static final int REQUEST_CODE_B6 = 12351;
    public static final int REQUEST_CODE_B7 = 12355;
    public static final int REQUEST_CODE_B8 = 12356;
    public static final int REQUEST_CODE_B9 = 12357;

    private static final String FN_SAVE_ARGS_B1 = "cwfnB1args";
    private static final String FN_SAVE_ARGS_B2 = "cwfnB2args";
    private static final String FN_SAVE_ARGS_B3 = "cwfnB3args";
    private static final String FN_SAVE_ARGS_B4 = "cwfnB4args";
    private static final String FN_SAVE_ARGS_B5 = "cwfnB5args";
    private static final String FN_SAVE_ARGS_B6 = "cwfnB6args";
    private static final String FN_SAVE_ARGS_B7 = "cwfnB7args";
    private static final String FN_SAVE_ARGS_B8 = "cwfnB8args";
    private static final String FN_SAVE_ARGS_B9 = "cwfnB9args";

    private static final String FN_SAVE_NAME_B1 = "cwfnB1name";
    private static final String FN_SAVE_NAME_B2 = "cwfnB2name";
    private static final String FN_SAVE_NAME_B3 = "cwfnB3name";
    private static final String FN_SAVE_NAME_B4 = "cwfnB4name";
    private static final String FN_SAVE_NAME_B5 = "cwfnB5name";
    private static final String FN_SAVE_NAME_B6 = "cwfnB6name";
    private static final String FN_SAVE_NAME_B7 = "cwfnB7name";
    private static final String FN_SAVE_NAME_B8 = "cwfnB8name";
    private static final String FN_SAVE_NAME_B9 = "cwfnB9name";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }

        shp = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        ed = shp.edit();
        setContentView(R.layout.activity_st);

        ipEt = (EditText) findViewById(R.id.etIp);
        portEt = (EditText) findViewById(R.id.etSocket);
        clientPortEt = (EditText) findViewById(R.id.etPort);
        aEt = (EditText) findViewById(R.id.etA);
        bEt = (EditText) findViewById(R.id.etB);
        keyboardEt = (EditText) findViewById(R.id.etKeyboard);

        scan = (Button) findViewById(R.id.bScan);
        send = (Button) findViewById(R.id.bSend);

        up = (ImageButton) findViewById(R.id.buttonUp);
        down = (ImageButton) findViewById(R.id.buttonDown);
        left = (ImageButton) findViewById(R.id.buttonLeft);
        right = (ImageButton) findViewById(R.id.buttonRight);

        wsBtn1 = (Button) findViewById(R.id.workSpaceBTN1);
        wsBtn2 = (Button) findViewById(R.id.workSpaceBTN2);
        wsBtn3 = (Button) findViewById(R.id.workSpaceBTN3);
        wsBtn4 = (Button) findViewById(R.id.workSpaceBTN4);
        wsBtn5 = (Button) findViewById(R.id.workSpaceBTN5);
        wsBtn6 = (Button) findViewById(R.id.workSpaceBTN6);
        wsBtn7 = (Button) findViewById(R.id.workSpaceBTN7);
        wsBtn8 = (Button) findViewById(R.id.workSpaceBTN8);
        wsBtn9 = (Button) findViewById(R.id.workSpaceBTN9);

        tr1 = (TableRow) findViewById(R.id.tableWSRow1);
        tr2 = (TableRow) findViewById(R.id.tableWSRow2);
        tr3 = (TableRow) findViewById(R.id.tableWSRow3);
        tr1.setVisibility(View.INVISIBLE);
        tr2.setVisibility(View.INVISIBLE);
        tr3.setVisibility(View.INVISIBLE);

        context = (ImageButton) findViewById(R.id.context);

        ll = (LinearLayout) findViewById(R.id.ll);

        tv = (TextView) findViewById(R.id.tv);

        status = (TextView) findViewById(R.id.textStatus);

        fnb = new ButtonFnManager(this);

        context.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fnb.press(ButtonFnManager.FN_CONTEXT_BUTTONS, "", "");
            }
        });

        ipEt.setText(shp.getString("ip", ""));
        portEt.setText(shp.getString("port", "1234"));

        keyboardEt.setText("<>");
        keyboardEt.setSelection(keyboardEt.getText().length());
        keyboardEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0
                        && keyboardEt.getText().toString().length() > 2) {
                    String pressed = s.toString().replace("<>", "");
                    // int spaces = pressed.length() - pressed.replaceAll(" ",
                    // "").length();

                    int port = Integer.parseInt(portEt.getText().toString());
                    if (s.length() == pressed.length()
                            | (s.length() == 3 && pressed.length() == 1)) {

                        new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                port, ButtonFnManager.keyboard, pressed)).start();
                    }

                    keyboardEt.setText("<>");
                    keyboardEt.setSelection(keyboardEt.getText().length());
                } else if (keyboardEt.getText().toString().equals("<")) {

                    int port = Integer.parseInt(portEt.getText().toString());
                    new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                            port, ButtonFnManager.keyboard, "bksps")).start();
                    keyboardEt.setText("<>");
                    keyboardEt.setSelection(keyboardEt.getText().length());
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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

        overlayOTL = new OverlayOTL();
        overlayAltTAbOTL = new OverlayAltTAbOTL();

        context.setOnTouchListener(overlayOTL);
        topPager.setOnTouchListener(overlayOTL);
        botPager.setOnTouchListener(overlayOTL);


        InAppLog.writeLog(ST.this, "", "ST on create", debug);
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

                    int moveX = startX - x;
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
                            int moveCondidateX = 0;
                            int moveCondidateY = 0;

                            if (moveY > 40 || moveY < -40) {
                                moveCondidateY = moveY > 0 ? 1 : -1;
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

    public void setCurrentProcess(String process){

            currentProcess = process;
            status.setText(currentProcess);
        new Thread(new SocketThread(ipEt.getText().toString(),
                Integer.parseInt(portEt.getText().toString()), ButtonFnManager.getProcessIcon, 0, 0, ST.this)).start();
            bindContextButtons(currentProcess.substring(currentProcess.lastIndexOf("\\") + 1).replace(".exe", "").replace(".EXE", ""));


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

        if (shp.getBoolean("enterOnVoiceInput", true)) {
            Menu settings = menu.getItem(3).getSubMenu();
            settings.getItem(0).setChecked(true);
        }

        if (shp.getBoolean("showFnButtons", true)) {
            Menu settings = menu.getItem(3).getSubMenu();
            settings.getItem(1).setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int port = Integer.parseInt(portEt.getText().toString());

        switch (item.getItemId()) {
            case R.id.action_scan:
                scan.performClick();
                break;
            case R.id.enterAfterVoiceInput:
                if (shp.getBoolean("enterOnVoiceInput", true)) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                ed.putBoolean("enterOnVoiceInput", item.isChecked());
                ed.commit();
                break;

            case R.id.showFnButtons:
                if (shp.getBoolean("showFnButtons", true)) {
                    item.setChecked(false);

                    topPager.setVisibility(View.GONE);
                    botPager.setVisibility(View.GONE);

                } else {
                    item.setChecked(true);

                    topPager.setVisibility(View.VISIBLE);
                    botPager.setVisibility(View.VISIBLE);

                }
                ed.putBoolean("showFnButtons", item.isChecked());
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

            case R.id.fireFN:
                fnb.press(ButtonFnManager.FN_FIRE_FN, "", "");
                break;

            case R.id.arrows:
                switch (up.getVisibility()) {
                    case View.VISIBLE:
                        up.setVisibility(View.GONE);
                        down.setVisibility(View.GONE);
                        left.setVisibility(View.GONE);
                        right.setVisibility(View.GONE);
                        break;

                    case View.GONE:
                        up.setVisibility(View.VISIBLE);
                        down.setVisibility(View.VISIBLE);
                        left.setVisibility(View.VISIBLE);
                        right.setVisibility(View.VISIBLE);
                        break;
                }
                break;

            case R.id.keyboard:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);
                break;
/*
            case R.id.launchApp:
                startVoiceRecognitionActivity(REQUEST_CODE_LAUNCH_APP, null);

                break;
*/
            case R.id.voiceInput:
                startVoiceRecognitionActivity(REQUEST_CODE_VOICE_INPUT, null);

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

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //bind contextButtonsArrayList<Integer>
        ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(REQUEST_CODE_B1);
        arr.add(REQUEST_CODE_B2);
        arr.add(REQUEST_CODE_B3);
        arr.add(REQUEST_CODE_B4);
        arr.add(REQUEST_CODE_B5);
        arr.add(REQUEST_CODE_B6);
        arr.add(REQUEST_CODE_B7);
        arr.add(REQUEST_CODE_B8);
        arr.add(REQUEST_CODE_B9);
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
                                        fnb.press(fnb.FN_VOICE_FN, "", "");
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

                    Set<String> keys = shp.getStringSet("VoiceFnMap", new HashSet<String>());

                    if (keys != null) {
                        for (String key : keys) {
                            key = key.toLowerCase();
                            if (m_Text.startsWith(key + " ")) {
                                String args = "";

                                if (m_Text.equals(key)) {
                                    args = "";
                                } else {
                                    args = m_Text.replaceFirst(key + " ", "");
                                }

                                fnb.press(shp.getInt("VoiceFn:" + key, 0), shp.getString("VoiceFnArg:" + key, "null"), args);

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

                                    Set<String> keys = shp.getStringSet("VoiceFnMap", new HashSet<String>());
                                    //Check for Reinvoke
                                    if (m_Text.endsWith(" потом")) {
                                        m_Text = m_Text.substring(0, m_Text.length() - 6);
                                        needReinvokeVoiceFn = true;
                                    }
                                    if (keys != null) {
                                        for (String key : keys) {
                                            if (m_Text.startsWith(key)) {
                                                String args = "";

                                                if (m_Text.equals(key)) {
                                                    args = "";
                                                } else {
                                                    args = m_Text.replaceFirst(key + " ", "");
                                                }

                                                fnb.press(shp.getInt("VoiceFn:" + key, 0), shp.getString("VoiceFnArg:" + key, "null"), args);

                                            }
                                        }
                                    }
                                    //Reinvoke
                                    if (needReinvokeVoiceFn) {
                                        fnb.press(fnb.FN_VOICE_FN, "", "");
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
                                        fnb.press(fnb.FN_VOICE_FN, "", "");
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
            } else {

            }
        }

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CODE_FIRE_FN:
                    fnb.press(intent.getIntExtra("FnResult", fnb.NO_FUNCTION),
                            intent.getStringExtra("FnResultArgs"), "");
                    break;

            }
        }
        //Reinvoke voiceInput if needed
        if (needReinvokeVoiceFn) {
            fnb.press(fnb.FN_VOICE_FN, "", "");
            needReinvokeVoiceFn = false;
        }
    }


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
        boolean app_installed = false;
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        String topOrBot = "";

        public ScreenSlidePagerAdapter(
                android.support.v4.app.FragmentManager fragmentManager,
                String topOrBot) {
            super(fragmentManager);
            this.topOrBot = topOrBot;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Fragment fr = null;
            Bundle arguments = new Bundle();

            arguments.putString(FnButtonsFragment.PAGE_ID_ARG, topOrBot
                    + position);
            fr = new FnButtonsFragment();
            fr.setArguments(arguments);

            return fr;

        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "Oops";
            switch (position) {
                case 0:
                    title = "Create shortcut";
                    break;

                case 1:
                    title = "Choose existing fn";
                    break;

                case 2:
                    title = "Create command line command";
                    break;

                default:
                    title = "Oops";
                    break;
            }


            return title;


        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void bindContextButtons(final String currentProcess){
        OnClickListener ocl = new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.workSpaceBTN1:
                        int bindedFunction1 = shp.getInt(FN_SAVE_B1+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction1 == fnb.NO_FUNCTION){
                            Intent intentB1 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB1, REQUEST_CODE_B1);
                        }else{
                            fnb.press(bindedFunction1, shp.getString(FN_SAVE_ARGS_B1+""+currentProcess, "Nope"),"");
                        }
                        break;

                    case R.id.workSpaceBTN2:
                        int bindedFunction2 = shp.getInt(FN_SAVE_B2+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction2 == fnb.NO_FUNCTION){
                            Intent intentB2 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB2, REQUEST_CODE_B2);
                        }else{
                            fnb.press(bindedFunction2, shp.getString(FN_SAVE_ARGS_B2+""+currentProcess, "Nope"), "");
                        }
                        break;

                    case R.id.workSpaceBTN3:
                        int bindedFunction3 = shp.getInt(FN_SAVE_B3+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction3 ==fnb.NO_FUNCTION){
                            Intent intentB3 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB3, REQUEST_CODE_B3);
                        }else{
                            fnb.press(bindedFunction3, shp.getString(FN_SAVE_ARGS_B3+""+currentProcess, "Nope"), "");
                        }
                        break;
                    case R.id.workSpaceBTN4:
                        int bindedFunction4 = shp.getInt(FN_SAVE_B4+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction4 == fnb.NO_FUNCTION){
                            Intent intentB4 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB4, REQUEST_CODE_B4);
                        }else{
                            fnb.press(bindedFunction4, shp.getString(FN_SAVE_ARGS_B4+""+currentProcess, "Nope"),"");
                        }
                        break;

                    case R.id.workSpaceBTN5:
                        int bindedFunction5 = shp.getInt(FN_SAVE_B5+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction5 == fnb.NO_FUNCTION){
                            Intent intentB5 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB5, REQUEST_CODE_B5);
                        }else{
                            fnb.press(bindedFunction5, shp.getString(FN_SAVE_ARGS_B5+""+currentProcess, "Nope"), "");
                        }
                        break;

                    case R.id.workSpaceBTN6:
                        int bindedFunction6 = shp.getInt(FN_SAVE_B6+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction6 ==fnb.NO_FUNCTION){
                            Intent intentB6 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB6, REQUEST_CODE_B6);
                        }else{
                            fnb.press(bindedFunction6, shp.getString(FN_SAVE_ARGS_B6+""+currentProcess, "Nope"), "");
                        }
                        break;
                    case R.id.workSpaceBTN7:
                        int bindedFunction7 = shp.getInt(FN_SAVE_B7+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction7 == fnb.NO_FUNCTION){
                            Intent intentB7 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB7, REQUEST_CODE_B7);
                        }else{
                            fnb.press(bindedFunction7, shp.getString(FN_SAVE_ARGS_B7+""+currentProcess, "Nope"),"");
                        }
                        break;

                    case R.id.workSpaceBTN8:
                        int bindedFunction8 = shp.getInt(FN_SAVE_B8+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction8 == fnb.NO_FUNCTION){
                            Intent intentB8 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB8, REQUEST_CODE_B8);
                        }else{
                            fnb.press(bindedFunction8, shp.getString(FN_SAVE_ARGS_B8+""+currentProcess, "Nope"), "");
                        }
                        break;

                    case R.id.workSpaceBTN9:
                        int bindedFunction9 = shp.getInt(FN_SAVE_B9+""+currentProcess, fnb.NO_FUNCTION);
                        if(bindedFunction9 ==fnb.NO_FUNCTION){
                            Intent intentB9 = new Intent(getBaseContext(), FnBind.class);
                            startActivityForResult(intentB9, REQUEST_CODE_B9);
                        }else{
                            fnb.press(bindedFunction9, shp.getString(FN_SAVE_ARGS_B9+""+currentProcess, "Nope"), "");
                        }
                        break;
                }
            }
        };

        View.OnLongClickListener olclFn = new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int reqToSend=0;
                switch (v.getId()) {
                    case R.id.workSpaceBTN1:
                        reqToSend = REQUEST_CODE_B1;
                        break;

                    case R.id.workSpaceBTN2:
                        reqToSend = REQUEST_CODE_B2;
                        break;

                    case R.id.workSpaceBTN3:
                        reqToSend = REQUEST_CODE_B3;
                        break;
                    case R.id.workSpaceBTN4:
                        reqToSend = REQUEST_CODE_B4;
                        break;

                    case R.id.workSpaceBTN5:
                        reqToSend = REQUEST_CODE_B5;
                        break;

                    case R.id.workSpaceBTN6:
                        reqToSend = REQUEST_CODE_B6;
                        break;
                    case R.id.workSpaceBTN7:
                        reqToSend = REQUEST_CODE_B7;
                        break;

                    case R.id.workSpaceBTN8:
                        reqToSend = REQUEST_CODE_B8;
                        break;

                    case R.id.workSpaceBTN9:
                        reqToSend = REQUEST_CODE_B9;
                        break;

                }
                Intent intent = new Intent(getBaseContext(), FnBind.class);
                startActivityForResult(intent, reqToSend);
                return false;
            }
        };

        wsBtn1.setOnClickListener(ocl);
        wsBtn1.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B1 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn1.setOnLongClickListener(olclFn);

        wsBtn2.setOnClickListener(ocl);
        wsBtn2.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B2 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn2.setOnLongClickListener(olclFn);

        wsBtn3.setOnClickListener(ocl);
        wsBtn3.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B3 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn3.setOnLongClickListener(olclFn);

        wsBtn4.setOnClickListener(ocl);
        wsBtn4.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B4 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn4.setOnLongClickListener(olclFn);

        wsBtn5.setOnClickListener(ocl);
        wsBtn5.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B5 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn5.setOnLongClickListener(olclFn);

        wsBtn6.setOnClickListener(ocl);
        wsBtn6.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B6 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn6.setOnLongClickListener(olclFn);

        wsBtn7.setOnClickListener(ocl);
        wsBtn7.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B7 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn7.setOnLongClickListener(olclFn);

        wsBtn8.setOnClickListener(ocl);
        wsBtn8.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B8 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn8.setOnLongClickListener(olclFn);

        wsBtn9.setOnClickListener(ocl);
        wsBtn9.setText(fnb.fnMap.get(shp.getInt(FN_SAVE_B9 + "" + currentProcess, fnb.NO_FUNCTION)));
        wsBtn9.setOnLongClickListener(olclFn);

        if(shp.getInt(FN_SAVE_B1+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }
        if(shp.getInt(FN_SAVE_B2+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }
        if(shp.getInt(FN_SAVE_B3+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn3.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn3.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }

        if(shp.getInt(FN_SAVE_B4+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn4.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn4.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }
        if(shp.getInt(FN_SAVE_B5+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn5.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn5.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }
        if(shp.getInt(FN_SAVE_B6+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn6.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn6.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }

        if(shp.getInt(FN_SAVE_B7+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn7.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn7.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }
        if(shp.getInt(FN_SAVE_B8+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn8.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn8.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }
        if(shp.getInt(FN_SAVE_B9+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
            wsBtn9.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
        }else{
            wsBtn9.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
        }

        if(shp.getInt(FN_SAVE_B1+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B1+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn1.setText(shp.getString(FN_SAVE_ARGS_B1 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B1+""+currentProcess, "").contains("chrome")){
                wsBtn1.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if(shp.getInt(FN_SAVE_B2+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B2+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn2.setText(shp.getString(FN_SAVE_ARGS_B2 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B2+""+currentProcess, "").contains("chrome")){
                wsBtn2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if(shp.getInt(FN_SAVE_B3+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B3+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn3.setText(shp.getString(FN_SAVE_ARGS_B3 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B3+""+currentProcess, "").contains("chrome")){
                wsBtn3.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if(shp.getInt(FN_SAVE_B4+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B4+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn4.setText(shp.getString(FN_SAVE_ARGS_B4 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B4+""+currentProcess, "").contains("chrome")){
                wsBtn4.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if(shp.getInt(FN_SAVE_B5+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B5+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn5.setText(shp.getString(FN_SAVE_ARGS_B5 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B5+""+currentProcess, "").contains("chrome")){
                wsBtn5.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if(shp.getInt(FN_SAVE_B6+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B6+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn6.setText(shp.getString(FN_SAVE_ARGS_B6 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B6+""+currentProcess, "").contains("chrome")){
                wsBtn6.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if(shp.getInt(FN_SAVE_B7+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B7+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn7.setText(shp.getString(FN_SAVE_ARGS_B7 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B7+""+currentProcess, "").contains("chrome")){
                wsBtn7.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if(shp.getInt(FN_SAVE_B8+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B8+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn8.setText(shp.getString(FN_SAVE_ARGS_B8 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B8+""+currentProcess, "").contains("chrome")){
                wsBtn8.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn8.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn8.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        if(shp.getInt(FN_SAVE_B9+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B9+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){
            wsBtn9.setText(shp.getString(FN_SAVE_ARGS_B9 + "" + currentProcess, ""));
            if(shp.getString(FN_SAVE_ARGS_B9+""+currentProcess, "").contains("chrome")){
                wsBtn9.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
            }else{
                wsBtn9.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }else{
            wsBtn9.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if(!shp.getString(FN_SAVE_NAME_B1+""+currentProcess, "").equals("")){
            wsBtn1.setText(shp.getString(FN_SAVE_NAME_B1 + "" + currentProcess, ""));
        }
        if(!shp.getString(FN_SAVE_NAME_B2+""+currentProcess, "").equals("")){
            wsBtn2.setText(shp.getString(FN_SAVE_NAME_B2 + "" + currentProcess, ""));
        }
        if(!shp.getString(FN_SAVE_NAME_B3+""+currentProcess, "").equals("")){
            wsBtn3.setText(shp.getString(FN_SAVE_NAME_B3 + "" + currentProcess, ""));
        }

        if(!shp.getString(FN_SAVE_NAME_B4+""+currentProcess, "").equals("")){
            wsBtn4.setText(shp.getString(FN_SAVE_NAME_B4 + "" + currentProcess, ""));
        }
        if(!shp.getString(FN_SAVE_NAME_B5+""+currentProcess, "").equals("")){
            wsBtn5.setText(shp.getString(FN_SAVE_NAME_B5 + "" + currentProcess, ""));
        }
        if(!shp.getString(FN_SAVE_NAME_B6+""+currentProcess, "").equals("")){
            wsBtn6.setText(shp.getString(FN_SAVE_NAME_B6 + "" + currentProcess, ""));
        }

        if(!shp.getString(FN_SAVE_NAME_B7+""+currentProcess, "").equals("")){
            wsBtn7.setText(shp.getString(FN_SAVE_NAME_B7 + "" + currentProcess, ""));
        }
        if(!shp.getString(FN_SAVE_NAME_B8+""+currentProcess, "").equals("")){
            wsBtn8.setText(shp.getString(FN_SAVE_NAME_B8 + "" + currentProcess, ""));
        }
        if(!shp.getString(FN_SAVE_NAME_B9+""+currentProcess, "").equals("")){
            wsBtn9.setText(shp.getString(FN_SAVE_NAME_B9 + "" + currentProcess, ""));
        }
    }

    public void saveFnBindResults (Intent i, int reqestCode, final String currentProcess){
        
        switch (reqestCode) {
            case REQUEST_CODE_B1:
                ed.putInt(FN_SAVE_B1+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B1+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B1+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn1.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn1.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn1.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B1+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B1+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B1+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B1+""+currentProcess, "").contains("chrome")){
                        wsBtn1.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
                break;

            case REQUEST_CODE_B2:
                ed.putInt(FN_SAVE_B2+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B2+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B2+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn2.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn2.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn2.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B2+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B2+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B2+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B3+""+currentProcess, "").contains("chrome")){
                        wsBtn2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

                break;

            case REQUEST_CODE_B3:
                ed.putInt(FN_SAVE_B3+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B3+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B3+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn3.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn3.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn3.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B3+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn3.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn3.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B3+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B3+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B3+""+currentProcess, "").contains("chrome")){
                        wsBtn3.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

                break;

            case REQUEST_CODE_B4:
                ed.putInt(FN_SAVE_B4+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B4+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B4+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn4.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn4.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn4.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B4+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn4.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn4.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B4+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B4+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B4+""+currentProcess, "").contains("chrome")){
                        wsBtn4.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
                break;

            case REQUEST_CODE_B5:
                ed.putInt(FN_SAVE_B5+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B5+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B5+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn5.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn5.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn5.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B5+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn5.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn5.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B5+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B5+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B6+""+currentProcess, "").contains("chrome")){
                        wsBtn5.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

                break;

            case REQUEST_CODE_B6:
                ed.putInt(FN_SAVE_B6+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B6+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B6+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn6.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn6.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn6.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B6+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn6.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn6.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B6+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B6+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B6+""+currentProcess, "").contains("chrome")){
                        wsBtn6.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn6.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

                break;
            case REQUEST_CODE_B7:
                ed.putInt(FN_SAVE_B7+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B7+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B7+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn7.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn7.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn7.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B7+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn7.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn7.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B7+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B7+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B7+""+currentProcess, "").contains("chrome")){
                        wsBtn7.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn7.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
                break;

            case REQUEST_CODE_B8:
                ed.putInt(FN_SAVE_B8+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B8+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B8+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn8.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn8.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn8.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B8+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn8.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn8.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B8+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B8+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B9+""+currentProcess, "").contains("chrome")){
                        wsBtn8.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn8.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn8.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

                break;

            case REQUEST_CODE_B9:
                ed.putInt(FN_SAVE_B9+""+currentProcess, i.getIntExtra("FnResult", fnb.NO_FUNCTION));
                ed.putString(FN_SAVE_ARGS_B9+""+currentProcess, i.getStringExtra("FnResultArgs"));
                ed.putString(FN_SAVE_NAME_B9+""+currentProcess, i.getStringExtra("Name"));
                ed.commit();
                wsBtn9.setText(fnb.fnMap.get(i.getIntExtra("FnResult", fnb.NO_FUNCTION)));
                if(!i.getStringExtra("FnResultArgs").equals("")){
                    wsBtn9.setText(i.getStringExtra("FnResultArgs"));
                }
                if(!i.getStringExtra("Name").equals("")){
                    wsBtn9.setText(i.getStringExtra("Name"));
                }

                if(shp.getInt(FN_SAVE_B9+""+currentProcess, fnb.NO_FUNCTION)== ButtonFnManager.NO_FUNCTION){
                    wsBtn9.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
                }else{
                    wsBtn9.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
                }
                if(shp.getInt(FN_SAVE_B9+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_CUSTOM||shp.getInt(FN_SAVE_B9+""+currentProcess, fnb.NO_FUNCTION)==fnb.FN_COMMAND_LINE){

                    if(shp.getString(FN_SAVE_ARGS_B9+""+currentProcess, "").contains("chrome")){
                        wsBtn9.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
                    }else{
                        wsBtn9.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }else{
                    wsBtn9.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

                break;
        }

    }


}
