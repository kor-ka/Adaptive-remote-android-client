package ru.korinc.sockettest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LaunchFromTaskBar extends Activity {
	ListView lv;
	ArrayAdapter<Spannable> adapter ;
    List<String> winIds;
	List<Spannable> map;
    List<String> process;
	ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	ButtonFnManager fbn;
	final int GET_TASKBAR_APPS = 1;
	final int OPEN_TASKBAR_APP = 2;
    final int OPEN_TASKBAR_APP_WINDOW = 4;
	static final int GET_TASKBAR_ICONS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch_from_task_bar);
		new Thread(new SocketThread(getIntent().getStringExtra("ip"), getIntent().getIntExtra("port",4444), GET_TASKBAR_APPS)).start();

        process = new ArrayList<String>();
		map = new ArrayList<Spannable>();
        winIds = new ArrayList<String>();
		map.add(spannableFactory.newSpannable("..."));
		lv= (ListView) findViewById(R.id.launchFromTaskBarLv);
		// ������� �������
	  adapter = new ArrayAdapter<Spannable>(this,
	        android.R.layout.simple_list_item_1, map);
	  
	    // ����������� ������� ������
	    lv.setAdapter(adapter);
	    
	  //����������� ������
		  lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    if(process.get(position).isEmpty()){
                        new Thread(new SocketThread(getIntent().getStringExtra("ip"), getIntent().getIntExtra("port",4444), OPEN_TASKBAR_APP, map.get(position).toString().substring(3),0)).start();
                    }else{
                        new Thread(new SocketThread(getIntent().getStringExtra("ip"), getIntent().getIntExtra("port",4444), OPEN_TASKBAR_APP_WINDOW, winIds.get(position),0)).start();
                    }
                    setResult(RESULT_OK, new Intent());
					finish();
				}
				
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launch_from_task_bar, menu);
		return true;
	}
	
	class SocketThread implements Runnable {

		String ip;
		int port;
		Socket socket;
		int mode;
		String appToLaunch;
		int bitMapPlace;

		public SocketThread(String ip, int port, int mode) {
			this.ip = ip;
			this.port = port;
			this.mode = mode;
		}

		public SocketThread(String ip, int port, int mode, String appToLaunch, int bitMapPlace) {
			this.ip = ip;
			this.port = port;
			this.mode = mode;
			this.appToLaunch = appToLaunch;
			this.bitMapPlace = bitMapPlace;
		}
		

		@Override
		public void run() {
			try {

				InetAddress ipAddress = InetAddress.getByName(ip);
				socket = new Socket();
				socket.connect(new InetSocketAddress(ipAddress, port), 10000);

				send();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

		public void send() {

			while (true) {

				if (socket != null) {

					try {

						InputStream sin = socket.getInputStream();
						OutputStream sout = socket.getOutputStream();

						final DataInputStream in = new DataInputStream(sin);
						DataOutputStream out = new DataOutputStream(sout);
						
						switch (mode) {
						case GET_TASKBAR_APPS:
							out.writeUTF("launchFromTaskBarList");
							
							

							final String line = in.readUTF();

							
							runOnUiThread(new Runnable() {
								public void run() {

									map = new ArrayList<Spannable>();
									List<String> mapStrings =  Arrays.asList(line.split("<split1>"));
									for(String s:mapStrings){

                                        if(s.contains("<split2>")){
                                            map.add(spannableFactory.newSpannable(s.substring(s.lastIndexOf("<split2>")+8, s.lastIndexOf("<split3>"))));

                                            winIds.add(s.substring(s.lastIndexOf("<split3>")+8));

                                            process.add(s.substring(0, s.lastIndexOf("<split2>")));
                                        }else{
                                            map.add(spannableFactory.newSpannable(s));
                                            process.add("");
                                            winIds.add("");
                                        }


									}
									
									getIcons();
									
										//  lv.setAdapter(adapter);
									
								}
							});	
							
							break;

						case GET_TASKBAR_ICONS:

                            getTaskBarIcons(in, out);
							break;
						
							
						    case OPEN_TASKBAR_APP:
							out.writeUTF("commandLine::" + "start \"\" " +"\"%userprofile%/AppData/Roaming/Microsoft/Internet Explorer/Quick Launch/User Pinned/TaskBar/"+appToLaunch+".lnk\"");
							
							break;


                            case OPEN_TASKBAR_APP_WINDOW:
                                //nircmdc win min process chrome.exe 0 & nircmdc win normal process chrome.exe 0
                                //out.writeUTF("commandLine::" + "nircmdc win min stitle \""+appToLaunch+"\" 0 & "+ "nircmdc win normal stitle \""+appToLaunch+"\" 0 ");
                                //setForegroundWindow
                                out.writeUTF("setForegroundWindow::" + appToLaunch);
                                break;
						}

						out.flush();
						
						socket.close();
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

        private void getTaskBarIcons(DataInputStream in, DataOutputStream out) throws IOException {
            out.writeUTF("getTaskBarIcons::"+appToLaunch+".lnk");
            final Bitmap bitmap = BitmapFactory.decodeStream(in);


            runOnUiThread(new Runnable() {
                public void run() {


                    Spannable spannable =spannableFactory.newSpannable("1  "+map.get(bitMapPlace).toString());

                    spannable.setSpan(new ImageSpan(getBaseContext(), bitmap),
0, 1,
Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    map.set(bitMapPlace,spannable);


                }
            });
            if(map.size()==bitmaps.size()){
                drawIcons();
            }
        }

    }

	public void getIcons(){
		for(int i=0; i<map.size(); i++){
			bitmaps.add(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));
		}
		for(int i=0; i<map.size(); i++){
			new Thread(new SocketThread(getIntent().getStringExtra("ip"), getIntent().getIntExtra("port",4444), GET_TASKBAR_ICONS, (!process.get(i).isEmpty())?process.get(i):map.get(i).toString(),i)).start();
		}
	}
	
	public void drawIcons(){
		//TODO 
		runOnUiThread(new Runnable() {
			public void run() {
				 adapter = new ArrayAdapter<Spannable>(getBaseContext(),
					        android.R.layout.simple_list_item_1, map);
				 lv.setAdapter(adapter);
			}
		});	
		
	}
	
	 private static final Factory spannableFactory = Spannable.Factory
		        .getInstance();
	
}
