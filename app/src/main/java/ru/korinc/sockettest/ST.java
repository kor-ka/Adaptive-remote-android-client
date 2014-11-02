package ru.korinc.sockettest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyCharacterMap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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

public class ST extends FragmentActivity implements OnClickListener {
    Thread listener;
    EditText ipEt;
    EditText portEt;
    EditText clientPortEt;
    EditText aEt;
    EditText bEt;
    EditText keyboardEt;
    Button scan;
    Button send;
    Button b4;
    Button b5;
    Button b6;
    ImageButton up;
    ImageButton down;
    ImageButton left;
    ImageButton right;
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
    KeyCharacterMap mKeyCharacterMap = KeyCharacterMap
            .load(KeyCharacterMap.VIRTUAL_KEYBOARD);
    SocketThread st;
    ServerSocket ss;
    LinearLayout ll;
    TextView tv;
    ArrayAdapter<String> adapter;
    ArrayList<String> results;
    final static int ab = 0;
    final static int register = 1;
    final static int wat = 2;
    final static int click = 3;
    final static int dndDown = 4;
    final static int dndUp = 5;
    final static int rclick = 6;
    final static int keyboard = 7;
    final static int launch = 8;
    final static int shortcut = 9;
    final static int commandLine = 10;
    public static final int REQUEST_CODE_LAUNCH_APP = 1234;
    public static final int REQUEST_CODE_VOICE_INPUT = 12345;
    public static final int REQUEST_CODE_FIRE_FN = 12352;
    public static final int REQUEST_CODE_COMMAND_LINE_VOICE_INPUT = 12353;
    public static final int REQUEST_CODE_VOICE_FN = 12354;
    public String currentProcess = "";
    FnButton fnb;
    private String dialogInputText = "";
    private static final int NUM_PAGES = 3;
    ScreenSlidePagerAdapter topPagerAdapter;
    private ViewPager topPager;
    ScreenSlidePagerAdapter botPagerAdapter;
    private ViewPager botPager;
    Set<String> keyoVoiceInputFix;

    public boolean isPortFixRunning = false;
    public long lastFixed = System.currentTimeMillis();

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

        ll = (LinearLayout) findViewById(R.id.ll);

        tv = (TextView) findViewById(R.id.tv);

        status = (TextView) findViewById(R.id.textStatus);

        fnb = new FnButton(this);

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
                                port, keyboard, pressed)).start();
                    }

                    keyboardEt.setText("<>");
                    keyboardEt.setSelection(keyboardEt.getText().length());
                } else if (keyboardEt.getText().toString().equals("<")) {

                    int port = Integer.parseInt(portEt.getText().toString());
                    new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                            port, keyboard, "bksps")).start();
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
                            port, dndDown, 0, 0, ST.this)).start();
                    isDouble = true;
                    timeLongDown = System.currentTimeMillis();
                    if (timeLongDown - timeLongDownOld < 1000) {
                        new Thread(new SocketThread(ipEt.getText().toString(),
                                port, rclick, 0, 0, ST.this)).start();
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
                                        port, keyboard, "up")).start();
                                break;

                            case R.id.buttonDown:

                                new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                        port, keyboard, "down")).start();
                                break;

                            case R.id.buttonLeft:

                                new Thread( new SocketThread(ST.this, ipEt.getText().toString(),
                                        port, keyboard, "left")).start();
                                break;

                            case R.id.buttonRight:

                                new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                        port, keyboard, "right")).start();
                                break;

                        }
                }

                if (System.currentTimeMillis() - timeDown > 500 && i == 1) {

                    i = 2;

                    switch (v.getId()) {
                        case R.id.buttonUp:

                            new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                    port, keyboard, "up")).start();
                            break;

                        case R.id.buttonDown:

                            new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                    port, keyboard, "down")).start();
                            break;

                        case R.id.buttonLeft:

                            new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                    port, keyboard, "left")).start();
                            break;

                        case R.id.buttonRight:

                            new Thread(new SocketThread(ST.this, ipEt.getText().toString(),
                                    port, keyboard, "right")).start();
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
                                port, ab, a, b, ST.this)).start();
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
                                    port, click, 0, 0, ST.this)).start();

                        }

                        // Long click relise
                        if ((timeUp - timeDown) < 100
                                && (fullmovex < 20 & fullmovey < 20) && isDouble) {
                            // send dnd up
                            port = Integer.parseInt(portEt.getText().toString());
                            new Thread(new SocketThread(ipEt.getText().toString(),
                                    port, dndUp, 0, 0, ST.this)).start();

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

            keys.add("Поиск");
            ed.putInt("VoiceFn:" + "Поиск", FnButton.FN_COMMAND_LINE);

            keys.add("хром");
            ed.putString("VoiceFnArg:" + "хром", "start chrome");
            ed.putInt("VoiceFn:" + "хром", FnButton.FN_COMMAND_LINE);

            keys.add("Запустить");
            ed.putString("VoiceFnArg:" + "Запустить", "Launch app");
            ed.putInt("VoiceFn:" + "Запустить", FnButton.FN_LAUNCH_APP);

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

        if(!isPortFixRunning)fixPort();

    }

    public void fixPort() {
        isPortFixRunning = true;
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... integers) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(status!=null){
                            status.setText("Connecting...");     
                        }
                       
                    }
                });

                long time = System.currentTimeMillis();

                new Thread(new SocketThread(ipEt.getText().toString(),
                        Integer.parseInt(portEt.getText().toString()), ab, 0, 0, ST.this) {
                    @Override
                    public void run() {
                        boolean isFirstCycle = true;
                        boolean connected = false;
                        while (!connected) {
                            try {


                                InetAddress ipAddress = InetAddress.getByName(ipEt.getText().toString());
                                socket = new Socket();
                                socket.connect(new InetSocketAddress(ipAddress, port), 500);


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        portEt.setText(port + "");
                                    }
                                });

                                ed.putString("port", port + "");
                                ed.commit();
/*
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(status!=null){
                                            status.setText("Connected!");

                                        }
                                    }
                                });
                                */
                                connected = true;
                                socket.close();
                                break;

                            } catch (IOException e) {

                                if (isFirstCycle) {
                                    isFirstCycle = false;
                                    port = 1026;
                                } else if(port>=1031) {
                                    port = 1026;

                                } else {
                                    port++;
                                }

                                try {
                                    socket.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();

                            }
                        }
                        

                    }
                }).start();


                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                isPortFixRunning = false;
                lastFixed = System.currentTimeMillis();

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1);
    }

    public void setCurrentProcess(String process){
        currentProcess = process;
        status.setText(currentProcess);
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
                            new Thread(new SocketThread(ipToDesc/*broadcastAdress*/, j==1032?1025:j, ab, 12, 12, ST.this) {

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
            Menu settings = menu.getItem(2).getSubMenu();
            settings.getItem(0).setChecked(true);
        }

        if (shp.getBoolean("showFnButtons", true)) {
            Menu settings = menu.getItem(2).getSubMenu();
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
                fnb.press(FnButton.FN_FIRE_FN, "", "");
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

            case R.id.launchApp:
                startVoiceRecognitionActivity(REQUEST_CODE_LAUNCH_APP, null);

                break;

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
                            ab, a, b, ST.this)).start();

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
                input.setInputType(InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input.setKeyListener(DigitsKeyListener
                        .getInstance("0123456789."));
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
                            new Thread(new SocketThread(ipPort[0],Integer.parseInt(ipPort[1]), ab, 0,0,ST.this )).start();
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
        boolean needReinvokeVoiceFn = false;
        //Fixing voice input
        if (resultCode == RESULT_OK) {
            ArrayList<String> matchesToFix = intent
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
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

        if (requestCode == REQUEST_CODE_LAUNCH_APP) {

            switch (resultCode) {
                case RESULT_OK:
                    String m_Text = "";
                    ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    m_Text = matches.get(0);

                    //			m_Text = shp.getString(m_Text, m_Text);

                    int port = Integer.parseInt(portEt.getText().toString());
                    new Thread(new SocketThread(ST.this, ipEt.getText().toString(), port, launch, m_Text)).start();

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
                                    new Thread(new SocketThread(ST.this, ipEt.getText().toString(), port, launch, m_Text)).start();
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

                    Set<String> keys = shp.getStringSet("VoiceFnMap", new HashSet<String>());

                    if (keys != null) {
                        for (String key : keys) {
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

                    fnb.press(FnButton.FN_COMMAND_LINE, currentCommandLineaArgs.replace("<input>", matches.get(0)), "");

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
                                    fnb.press(FnButton.FN_COMMAND_LINE, currentCommandLineaArgs.replace("<input>", m_Text), "");
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
                    keyboard, m_Text)).start();
            if (shp.getBoolean("enterOnVoiceInput", true)) {
                new Thread(new SocketThread(ST.this, ipEt.getText().toString(), port,
                        keyboard, "\n")).start();
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

}
