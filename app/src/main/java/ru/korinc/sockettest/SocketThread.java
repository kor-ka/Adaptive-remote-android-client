package ru.korinc.sockettest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class SocketThread implements Runnable {

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

    @Override
    public void run() {
        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            socket = new Socket();
            socket.connect(new InetSocketAddress(ipAddress, port), 500);

            send(mode);
            st.lastFixed = System.currentTimeMillis();

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
                        case ST.ab:
                            out.writeUTF("ab:" + a + "lolParseMe" + b);

                            break;

                        case ST.click:
                            out.writeUTF("click:");

                            break;

                        case ST.rclick:
                            out.writeUTF("rclick:");

                            break;

                        case ST.dndDown:
                            out.writeUTF("dndDown:");

                            break;

                        case ST.dndUp:
                            out.writeUTF("dndUp:");

                            break;

                        case ST.register:

                            out.writeUTF("registerMe:"
                                    + st.clientPortEt.getText().toString());
                            break;

                        case ST.keyboard:
                            if (chr.equals("\n")) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            out.writeUTF("keyboard::" + chr);
                            break;

                        case ST.shortcut:

                            out.writeUTF("shortcut::" + chr);
                            break;

                        case ST.commandLine:

                            out.writeUTF("commandLine::" + chr);
                            break;

                        case ST.launch:

                            out.writeUTF("launch:" + chr);
                            break;

                        case ST.getProcessIcon:
                            out.writeUTF("getTaskBarIcons::"+st.currentProcess+".lnk");
                            final Bitmap bitmap = BitmapFactory.decodeStream(in);
                            st.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    st.context.setImageBitmap(bitmap);
                                }
                            });
                            in.close();
                            break;

                    }

                    out.flush();


                    line = in.readUTF()+"";
                    in.close();
                    closeSocket();


                    //don't need bind buttons while move mouse (it slows down)
                    if(line!=null && !line.isEmpty()){
                        st.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String[] reply = line.split("<process>");
                                if(reply.length>=2){
                                    String proccess = reply[1].trim();
                                    //
                                    if(!st.currentProcess.equals(proccess)){
                                        st.setCurrentProcess(proccess);

                                    }

                                }

                            }
                        });
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

