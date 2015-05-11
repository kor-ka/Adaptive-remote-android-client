package ru.korinc.sockettest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import net.dinglisch.android.tasker.FireReceiver;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ButtonFnManager {

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
    final static int getProcessIcon = 11;
    final static int centerClick = 12;
    final static int wheel = 13;
    private FireReceiver fr;

    Context ctx;
	ST st;
	public final static int FN_LAUNCHFROM_TASKBAR=-6;
	public final static int FN_VOICE_INPUT=-5;
	public final static int FN_VOICE_FN=-4;
	public final static int FN_FIRE_FN=-3;
	public final static int FN_COMMAND_LINE=-2;
	public final static int FN_CUSTOM=-1;
	public final static int NO_FUNCTION=-7;
	public final static int FN_SCAN=1;	

	public final static int FN_CLICK=7; 
	public final static int FN_R_CLICK=8; 
	public final static int FN_LAUNCH_APP=9;
	public final static int FN_ARROWS=10;

	public final static int FN_CONTEXT_MENU=23;

	public final static int FN_CTRL_ALT_DEL=28;

	public final static int FN_ALT_TAB=33;

    public final static int FN_CONTEXT_BUTTONS=43;
    public final static int FN_CENTER_CLICK =44;
    public final static int FN_WHELL_DOWN =45;
    public final static int FN_WHELL_UP =46;
    public final static int FN_SETTINGS =47;
    public final static int FN_HELP =48;
	public static HashMap<Integer, String> fnMap;
	
	public ButtonFnManager(ST st) {
		this.ctx=st.getBaseContext();
		this.st = st;
		initiateMap();
	}
	
	public ButtonFnManager(Context ctx, FireReceiver fr) {
        this.ctx=ctx;
        this.fr = fr;
		initiateMap();
	}
	
	 public void initiateMap(){
		 fnMap = new LinkedHashMap<Integer, String>();
			fnMap.put(NO_FUNCTION, ctx.getString(R.string.no_functon));
            fnMap.put(FN_SETTINGS, ctx.getString(R.string.title_activity_settings));
            fnMap.put(FN_HELP, ctx.getString(R.string.first_launch  ));
			fnMap.put(FN_VOICE_FN, ctx.getString(R.string.btn_voice_fn));
			fnMap.put(FN_VOICE_INPUT, ctx.getString(R.string.btn_voice_input));
			fnMap.put(FN_FIRE_FN, ctx.getString(R.string.btn_fire_fn));
			fnMap.put(FN_SCAN, ctx.getString(R.string.btn_connect_to_server));
			fnMap.put(FN_LAUNCH_APP, ctx.getString(R.string.btn_launch_app));
			fnMap.put(FN_LAUNCHFROM_TASKBAR, ctx.getString(R.string.btn_launch_app_from_taskbar));

			fnMap.put(FN_CLICK, ctx.getString(R.string.btn_left_click));
			fnMap.put(FN_R_CLICK, ctx.getString(R.string.btn_right_click));
            fnMap.put(FN_CENTER_CLICK, ctx.getString(R.string.btn_center_click));
            fnMap.put(FN_WHELL_UP, ctx.getString(R.string.btn_wheel_up));
            fnMap.put(FN_WHELL_DOWN, ctx.getString(R.string.btn_wheel_down));
			fnMap.put(FN_CTRL_ALT_DEL, "Ctrl+Alt+Del");
			fnMap.put(FN_ALT_TAB, "Alt+Tab");

	 }
	
 public void press(int function, String args, String voiceInputArgs){
     
     SharedPreferences shp = ctx.getSharedPreferences("default", Context.MODE_MULTI_PROCESS);
     String ip = shp.getString("ip", "192.168.0.1");
     int port = Integer.parseInt(shp.getString("port", "1026"));

	 if(st!=null){
     
		 switch (function) {

         case FN_SETTINGS:
             st.startActivityForResult(new Intent(st, MappingList.class), ST.REQUEST_CODE_SETTINGS);
             break;

         case FN_HELP:
                st.showFirstLaunchDialog();
             break;
		 
		 case FN_VOICE_INPUT:
			 if(!voiceInputArgs.isEmpty()){
				new Thread(new SocketThread(st, ip, port,
							keyboard, voiceInputArgs)).start();
					if (st.shp.getBoolean("enterOnVoiceInput", true)) {
						new Thread(new SocketThread(st, ip, port,
								keyboard, "\n")).start();
					}
		 		}else{
		 			st.startVoiceRecognitionActivity(st.REQUEST_CODE_VOICE_INPUT, args);
		 		}
			 break;
			 
		 case FN_VOICE_FN:			
			 
			 		st.startVoiceRecognitionActivity(st.REQUEST_CODE_VOICE_FN, args); 	
		 		
		 		break;
		 
		 case FN_LAUNCHFROM_TASKBAR:			
			 
			 Intent intent1 = new Intent(st, LaunchFromTaskBar.class);
			 intent1.putExtra("ip", ip);
			 intent1.putExtra("port", port);
	 		 st.startActivityForResult(intent1, ST.REQUEST_CODE_LAUNCHAPP_FROM_TASKBAR);
			 
	 		break;
		 
		 case FN_COMMAND_LINE:
			 	if(args.contains("<input>")){
			 		if(!voiceInputArgs.isEmpty()){
			 			new Thread(new SocketThread(st, ip, port, commandLine, args.replace("<input>", voiceInputArgs))).start();
			 		}else{
			 			st.startVoiceRecognitionActivity(st.REQUEST_CODE_COMMAND_LINE_VOICE_INPUT, args);
			 		}
			 		
			 	}else{
			 		new Thread(new SocketThread(st, ip, port, commandLine, args)).start();
			 	}
		 		
		 		break;
		 
		 case FN_FIRE_FN:
			 	Intent intent = new Intent(st, FnBind.class);
			 	intent.putExtra("requestCode", st.REQUEST_CODE_FIRE_FN);
				st.startActivityForResult(intent, st.REQUEST_CODE_FIRE_FN);		 		
		 		break;
		 		
		 	case FN_CUSTOM:
		 		new Thread(new SocketThread(st, ip, port, shortcut, args)).start();
		 		break;
		 
			case NO_FUNCTION:
				 Toast.makeText(ctx, "no function", Toast.LENGTH_SHORT).show();
				break;

			case FN_CLICK:
											
				new Thread(new SocketThread( ip, port, click, 0, 0, st)).start();
				break;
			
			case FN_R_CLICK:
											
				new Thread(new SocketThread( ip, port, rclick, 0, 0, st)).start();
				break;

            case FN_CENTER_CLICK:

                 new Thread(new SocketThread( ip, port, centerClick, 0, 0, st)).start();
                 break;

            case FN_WHELL_UP:
                 new Thread(new SocketThread(st, ip, port, wheel, "up")).start();
                 break;

            case FN_WHELL_DOWN:
                 new Thread(new SocketThread(st, ip, port, wheel, "down")).start();
                 break;

            case FN_SCAN:
				 st.scan();
				break;
				
				
			case FN_LAUNCH_APP:
				if(!voiceInputArgs.isEmpty()){
				
					new Thread(new SocketThread(st, ip, port, launch, voiceInputArgs)).start();
					
				}else{
					st.startVoiceRecognitionActivity(st.REQUEST_CODE_LAUNCH_APP, null);	
				}
				 
				break;
				
				




             case FN_CTRL_ALT_DEL:
                 new Thread(new SocketThread(st, ip, port, keyboard, "Ctrl+Alt+Del")).start();
                 break;



             case FN_ALT_TAB:
                 new Thread(new SocketThread(st, ip, port, keyboard, "Alt+Tab")).start();
                 break;
				
				
				
			case FN_CONTEXT_MENU:
				new Thread(new SocketThread(st, ip, port, keyboard, "contextMenu")).start();
				break;
				

					
			}
	 }else{
         
         switch (function){

             case FN_CLICK:

                 new Thread(new SocketThread( ip, port, click, 0, 0, st)).start();
                 break;

             case FN_R_CLICK:

                 new Thread(new SocketThread( ip, port, rclick, 0, 0, st)).start();
                 break;

             case FN_CENTER_CLICK:

                 new Thread(new SocketThread( ip, port, centerClick, 0, 0, st)).start();
                 break;

             case FN_WHELL_UP:
                 new Thread(new SocketThread(st, ip, port, wheel, "up")).start();
                 break;

             case FN_WHELL_DOWN:
                 new Thread(new SocketThread(st, ip, port, wheel, "down")).start();
                 break;


             case FN_COMMAND_LINE:
                 if(args.contains("<input>")){
                     /*
                     if(!voiceInputArgs.isEmpty()){
                         new Thread(new SocketThread(st, ip, port2, commandLine, args.replace("<input>", voiceInputArgs))).start();
                     }else{
                         st.startVoiceRecognitionActivity(st.REQUEST_CODE_COMMAND_LINE_VOICE_INPUT, args);
                     }
                        */
                     Toast.makeText(ctx, "Sorry, cant use <input> for now>", Toast.LENGTH_SHORT).show();
                 }else{
                     new Thread(new SocketThread(ip, port, commandLine, args, fr)).start();
                 }

                 break;



             case FN_CUSTOM:
                 new Thread(new SocketThread(ip, port, shortcut, args,fr)).start();
                 break;
         }
     }

 }

    public static String xorDecrypt(String input, String key) {
        byte[] inputBytes = Base64.decode(input, Base64.DEFAULT);
        int inputSize = inputBytes.length;

        byte[] keyBytes = key.getBytes();
        int keySize = keyBytes.length - 1;

        byte[] outBytes = new byte[inputSize];
        for (int i=0; i<inputSize; i++) {
            outBytes[i] = (byte) (inputBytes[i] ^ keyBytes[i % keySize]);
        }

        return new String(outBytes);
    }


}
