package ru.korinc.sockettest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.dinglisch.android.tasker.FireReceiver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class SocketThread implements Runnable {

    private FireReceiver fr;
    String ip;
    int port;
    int mode;
    int a;
    int b;
    String chr;
    Socket socket;
    DataInputStream in;
    String line;
    ST st;

    public SocketThread(String ip, int port, int mode, int a, int b, ST st) {
        this.ip = ip;
        this.port = port;
        this.mode = mode;
        this.a = a;
        this.b = b;
        this.st = st;

    }

    public SocketThread(ST st, String ip, int port, int mode, String chr) {
        this.ip = ip;
        this.port = port;
        this.mode = mode;
        this.chr = chr;
        this.st = st;

    }

    public SocketThread(String ip, int port, int mode, String chr, FireReceiver fr) {
        this.ip = ip;
        this.port = port;
        this.mode = mode;
        this.chr = chr;
        this.fr  = fr;

    }

    @Override
    public void run() {
        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            socket = new Socket();
            socket.connect(new InetSocketAddress(ipAddress, port), 500);

            send(mode);


        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void send(int mode) {

        while (true) {

            if (socket != null) {

                try {

                    InputStream sin = socket.getInputStream();
                    OutputStream sout = socket.getOutputStream();

                    in = new DataInputStream(sin);
                    DataOutputStream out = new DataOutputStream(sout);

                    switch (mode) {
                        case ButtonFnManager.ab:
                            out.writeUTF("ab:" + a + "lolParseMe" + b);

                            break;

                        case ButtonFnManager.click:
                            out.writeUTF("click:");

                            break;

                        case ButtonFnManager.rclick:
                            out.writeUTF("rclick:");

                            break;

                        case ButtonFnManager.centerClick:
                            out.writeUTF("centerClick:");

                            break;

                        case ButtonFnManager.wheel:
                            out.writeUTF("wheel::"+chr);

                            break;

                        case ButtonFnManager.dndDown:
                            out.writeUTF("dndDown:");

                            break;

                        case ButtonFnManager.dndUp:
                            out.writeUTF("dndUp:");

                            break;

                        case ButtonFnManager.register:
                            if(st!=null)
                            out.writeUTF("registerMe:"
                                    + st.clientPortEt.getText().toString());
                            break;

                        case ButtonFnManager.keyboard:
                            if (chr.equals("\n")) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            out.writeUTF("keyboard::" + chr);
                            break;

                        case ButtonFnManager.shortcut:

                            out.writeUTF("shortcut::" + chr);
                            break;

                        case ButtonFnManager.commandLine:

                            out.writeUTF("commandLine::" + chr);
                            break;

                        case ButtonFnManager.launch:

                            out.writeUTF("launch:" + chr);
                            break;

                        case ButtonFnManager.getProcessIcon:
                            if(st!=null){
                                out.writeUTF("getTaskBarIcons::"+st.currentProcess+".lnk");
                                final Bitmap bitmap = BitmapFactory.decodeStream(in);
                                st.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(bitmap!=null)st.context.setImageBitmap(bitmap);
                                        //else st.contextBtn.setImageResource(android.R.drawable.radiobutton_off_background);
                                    }
                                });
                                in.close();
                            }

                            break;

                    }

                    out.flush();


                    line = in.readUTF()+"";
                    in.close();
                    closeSocket();


                    //don't need bind buttons while move mouse (it slows down)

                    if(st!=null && line!=null && !line.isEmpty()){
                        st.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String[] reply = line.split("<process>");
                                if(reply.length>=2){
                                    String proccess = reply[1].trim();
                                    //
                                    if(!proccess.isEmpty() && !st.currentProcess.equals(proccess)){
                                        st.setCurrentProcess(proccess);

                                    }

                                }

                            }
                        });
                    }else if(st==null && fr!=null){
                        String[] reply = line.split("<process>");
                        if(reply.length>=2){
                            String proccess = reply[1].trim();
                            //
                            if(!proccess.isEmpty()){
                               fr.onBtnPressResult(proccess);
                            }

                        }
                    }



                    // socket = null;
                    // Toast.makeText(getBaseContext(), line + "",
                    // Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            }
        }
    }


    public void closeSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

