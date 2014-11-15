package ru.korinc.sockettest;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class FnButton {
	
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
	public final static int FN_ENTER=2;
	public final static int FN_BKSPC=3; 
	public final static int FN_ESC=4;
	public final static int FN_CTRL_Z=5; 
	public final static int FN_CTRL_Y=6; 
	public final static int FN_CLICK=7; 
	public final static int FN_R_CLICK=8; 
	public final static int FN_LAUNCH_APP=9;
	public final static int FN_ARROWS=10;
	public final static int FN_F1=11;
	public final static int FN_F2=12;
	public final static int FN_F3=13;
	public final static int FN_F4=14;
	public final static int FN_F5=15;
	public final static int FN_F6=16;
	public final static int FN_F7=17;
	public final static int FN_F8=18;
	public final static int FN_F9=19;
	public final static int FN_F10=20;
	public final static int FN_F11=21;
	public final static int FN_F12=22;
	public final static int FN_CONTEXT_MENU=23;
	public final static int FN_CTRL_C=24; 
	public final static int FN_CTRL_V=25; 
	public final static int FN_CTRL_X=26; 
	public final static int FN_CTRL_A=27; 
	public final static int FN_CTRL_ALT_DEL=28; 
	public final static int FN_CTRL_SHIFT_Z=29; 
	public final static int FN_CTRL_S=30; 
	public final static int FN_ALT_ENTER=31; 
	public final static int FN_CAPS=32; 
	public final static int FN_ALT_TAB=33; 
	public final static int FN_WIN=34; 
	public final static int FN_NUM=35; 
	public final static int FN_DEL=36; 
	public final static int FN_INS=37; 
	public final static int FN_HOME=38; 
	public final static int FN_END=39; 
	public final static int FN_PAGE_UP=40; 
	public final static int FN_PAGE_DWN=41; 
	public final static int FN_CTRL_P=42;
    public final static int FN_CONTEXT_BUTTONS=43;
	public static HashMap<Integer, String> fnMap;
	
	public  FnButton(ST st) {
		this.ctx=st.getBaseContext();
		this.st = st;
		initiateMap();
	}
	
	public  FnButton() {
		
		initiateMap();
	}
	
	 public void initiateMap(){
		 fnMap = new LinkedHashMap<Integer, String>();
			fnMap.put(NO_FUNCTION, "No function");
			fnMap.put(FN_VOICE_FN, "Voice Fn");
			fnMap.put(FN_VOICE_INPUT, "Voice input");
			fnMap.put(FN_FIRE_FN, "Fire Fn");
			fnMap.put(FN_SCAN, "Connect to server");
			fnMap.put(FN_LAUNCH_APP, "Launch app");
			fnMap.put(FN_LAUNCHFROM_TASKBAR, "Launch app from taskbar");
			fnMap.put(FN_ARROWS, "Arrows");
            fnMap.put(FN_CONTEXT_BUTTONS, "Context buttons");
			fnMap.put(FN_CLICK, "Left click");
			fnMap.put(FN_R_CLICK, "Right click");
			fnMap.put(FN_ENTER, "Enter");
			fnMap.put(FN_BKSPC, "Backspace");
			fnMap.put(FN_ESC, "Escape");
			fnMap.put(FN_CAPS, "Caps Lock"); //
			fnMap.put(FN_NUM, "Num Lock"); //
			fnMap.put(FN_WIN, "Win"); //
			fnMap.put(FN_DEL, "Del"); //
			fnMap.put(FN_INS, "Insert"); //
			fnMap.put(FN_HOME, "Home"); //
			fnMap.put(FN_END, "End"); //
			fnMap.put(FN_PAGE_UP, "Page Up"); //
			fnMap.put(FN_PAGE_DWN, "Page Down"); //
			fnMap.put(FN_CTRL_Z, "Ctrl+Z");
			fnMap.put(FN_CTRL_SHIFT_Z, "Ctrl+Shift+Z");
			fnMap.put(FN_CTRL_Y, "Ctrl+Y");
			fnMap.put(FN_CTRL_C, "Ctrl+C");
			fnMap.put(FN_CTRL_V, "Ctrl+V");
			fnMap.put(FN_CTRL_A, "Ctrl+A");
			fnMap.put(FN_CTRL_X, "Ctrl+X");
			fnMap.put(FN_CTRL_S, "Ctrl+S");	//
			fnMap.put(FN_CTRL_P, "Ctrl+P");	
			fnMap.put(FN_CTRL_ALT_DEL, "Ctrl+Alt+Del");	//
			fnMap.put(FN_ALT_ENTER, "Alt+Enter");	//
			fnMap.put(FN_ALT_TAB, "Alt+Tab");	//
			fnMap.put(FN_CONTEXT_MENU, "Context menu");
			fnMap.put(FN_F1, "F1");
			fnMap.put(FN_F2, "F2");
			fnMap.put(FN_F3, "F3");
			fnMap.put(FN_F4, "F4");
			fnMap.put(FN_F5, "F5");
			fnMap.put(FN_F6, "F6");
			fnMap.put(FN_F7, "F7");
			fnMap.put(FN_F8, "F8");
			fnMap.put(FN_F9, "F9");
			fnMap.put(FN_F10, "F10");
			fnMap.put(FN_F11, "F11");
			fnMap.put(FN_F12, "F12");
	 }
	
 public void press(int function, String args, String voiceInputArgs){
	 
	 int port = Integer.parseInt(st.portEt.getText().toString());
	 if(st!=null){
		 switch (function) {
		 
		 case FN_VOICE_INPUT:
			 if(!voiceInputArgs.isEmpty()){
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port,
							st.keyboard, voiceInputArgs)).start();
					if (st.shp.getBoolean("enterOnVoiceInput", true)) {
						new Thread(new SocketThread(st, st.ipEt.getText().toString(), port,
								st.keyboard, "\n")).start();
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
			 intent1.putExtra("ip", st.ipEt.getText().toString());
			 intent1.putExtra("port", port);
	 		 st.startActivityForResult(intent1, ST.REQUEST_CODE_LAUNCHAPP_FROM_TASKBAR);
			 
	 		break;
		 
		 case FN_COMMAND_LINE:
			 	if(args.contains("<input>")){
			 		if(!voiceInputArgs.isEmpty()){
			 			new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.commandLine, args.replace("<input>", voiceInputArgs))).start();
			 		}else{
			 			st.startVoiceRecognitionActivity(st.REQUEST_CODE_COMMAND_LINE_VOICE_INPUT, args);
			 		}
			 		
			 	}else{
			 		new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.commandLine, args)).start();	
			 	}
		 		
		 		break;
		 
		 case FN_FIRE_FN:
			 	Intent intent = new Intent(st, FnBind.class);
			 	intent.putExtra("requestCode", st.REQUEST_CODE_FIRE_FN);
				st.startActivityForResult(intent, st.REQUEST_CODE_FIRE_FN);		 		
		 		break;
		 		
		 	case FN_CUSTOM:
		 		new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.shortcut, args)).start();
		 		break;
		 
			case NO_FUNCTION:
				 Toast.makeText(ctx, "no function", Toast.LENGTH_SHORT).show();
				break;

			case FN_CLICK:
											
				new Thread(new SocketThread( st.ipEt.getText().toString(), port, st.click, 0, 0, st)).start();
				break;
			
			case FN_R_CLICK:
											
				new Thread(new SocketThread( st.ipEt.getText().toString(), port, st.rclick, 0, 0, st)).start();
				break;	
				
			case FN_SCAN:
				 st.scan.performClick();
				break;
				
				
			case FN_LAUNCH_APP:
				if(!voiceInputArgs.isEmpty()){
				
					new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.launch, voiceInputArgs)).start();
					
				}else{
					st.startVoiceRecognitionActivity(st.REQUEST_CODE_LAUNCH_APP, null);	
				}
				 
				break;
				
				
			case FN_ARROWS:
				switch (st.up.getVisibility()) {
				case View.VISIBLE:
					st.up.setVisibility(View.GONE);
					st.down.setVisibility(View.GONE);
					st.left.setVisibility(View.GONE);
					st.right.setVisibility(View.GONE);
					break;

				case View.GONE:
					st.up.setVisibility(View.VISIBLE);
					st.down.setVisibility(View.VISIBLE);
					st.left.setVisibility(View.VISIBLE);
					st.right.setVisibility(View.VISIBLE);
					break;
				}
				break;

            case FN_CONTEXT_BUTTONS:
                 switch (st.tr1.getVisibility()) {
                     case View.VISIBLE:
                         st.tr1.setVisibility(View.GONE);
                         st.tr2.setVisibility(View.GONE);
                         st.tr3.setVisibility(View.GONE);

                         break;

                     case View.GONE:
                         st.tr1.setVisibility(View.VISIBLE);
                         st.tr2.setVisibility(View.VISIBLE);
                         st.tr3.setVisibility(View.VISIBLE);

                         break;
                 }
                 break;
				
			case FN_BKSPC:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "bksps")).start();
				break;
				
			case FN_ENTER:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "enter")).start();
				break;
			
			case FN_ESC:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "esc")).start();
				break;
				
			case FN_CTRL_Z:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "ctrlz")).start();
				break;
			
			case FN_CTRL_SHIFT_Z:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "ctrl_shift_z")).start();
				break;	
				
			case FN_CTRL_Y:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "ctrly")).start();
				break;
				
			case FN_CTRL_C:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "ctrlc")).start();
				break;
				
			case FN_CTRL_V:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "ctrlv")).start();
				break;
			
			case FN_CTRL_A:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "ctrla")).start();
				break;	
			
			case FN_CTRL_X:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "ctrlx")).start();
				break;		
				
			case FN_CAPS:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Caps Lock")).start();
				break;	
				
			case FN_NUM:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Num Lock")).start();
				break;	
				
			case FN_WIN:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Win")).start();
				break;	
				
			case FN_DEL:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Del")).start();
				break;	
				
			case FN_INS:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Insert")).start();
				break;	
				
			case FN_HOME:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Home")).start();
				break;	
				
			case FN_END:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "End")).start();
				break;	
				
			case FN_PAGE_UP:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Page Up")).start();
				break;	
				
			case FN_PAGE_DWN:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Page Down")).start();
				break;	
				
			case FN_CTRL_S:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Ctrl+S")).start();
				break;	
				
			case FN_CTRL_ALT_DEL:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Ctrl+Alt+Del")).start();
				break;	
				
			case FN_ALT_ENTER:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Alt+Enter")).start();
				break;	
				
			case FN_ALT_TAB:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "Alt+Tab")).start();
				break;	
				
			case FN_CTRL_P:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "ctrlp")).start();
				break;	
				
				
				
			case FN_CONTEXT_MENU:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "contextMenu")).start();
				break;
				
			case FN_F1:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f1")).start();
				break;
			case FN_F2:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f2")).start();
				break;
			case FN_F3:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f3")).start();
				break;
			case FN_F4:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f4")).start();
				break;
			case FN_F5:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f5")).start();
				break;
			case FN_F6:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f6")).start();
				break;
			case FN_F7:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f7")).start();
				break;
			case FN_F8:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f8")).start();
				break;
			case FN_F9:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f9")).start();
				break;
			case FN_F10:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f10")).start();
				break;
			case FN_F11:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f11")).start();
				break;
			case FN_F12:
				new Thread(new SocketThread(st, st.ipEt.getText().toString(), port, st.keyboard, "f12")).start();
				break;
					
			}
	 }
	
 }
 

}
