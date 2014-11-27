package ru.korinc.sockettest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class InAppLog extends Thread 
{


	
	static TextView inAppLog;
	static TextView inAppLogSL;
	static ScrollView scroll;
	static FrameLayout inapploglay;
	public Activity activity; 
	static String time;
	static SimpleDateFormat sdf;
	static ImageButton play;
	static ImageButton pause;
	static ImageButton collapse;
	static ImageButton expand;
	static Boolean playPause;
	static OnClickListener ocl;
	static int actId=0;
	static View ial;
	static boolean afterClose;
	
	static LayoutParams params;
	private static final String TAG = "inAppLog";

	
	public static void writeLog(Activity act, String tag, String str, boolean isLoggingOn){
		if(isLoggingOn)
			writeLog(act, tag, str);
		else
			close();
		if(tag==null || tag.isEmpty())tag = TAG;
			Log.d(tag, str);
	}

	public static void close() {
		afterClose = true;
		if(ial!=null)
		if((ViewGroup)ial.getParent()!=null)((ViewGroup)ial.getParent()).removeView(ial);		
	}

	public static void writeLog(Activity act, final String tag, final String str)
	{
		if (act!=null && str!=null ) {
			if (act.hashCode() != actId || afterClose) {
				afterClose=false;
				actId = act.hashCode();
				ViewGroup v = (ViewGroup) act.getWindow().getDecorView()
						.findViewById(android.R.id.content);
				LayoutInflater inflater = (LayoutInflater) act
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (ocl == null){
					ocl = new OnClickListener() {

						@Override
						public void onClick(View p1) {
							switch (p1.getId()) {
							case R.id.playButton:

								play.setVisibility(play.GONE);
								pause.setVisibility(pause.VISIBLE);
								playPause = true;
								break;

							case R.id.pauseButton:

								pause.setVisibility(pause.GONE);
								play.setVisibility(play.VISIBLE);
								playPause = false;
								break;

							case R.id.collapse:
								//	params.height=40;
								scroll.setVisibility(scroll.GONE);
								collapse.setVisibility(collapse.GONE);
								play.setVisibility(play.GONE);
								pause.setVisibility(pause.GONE);
								expand.setVisibility(expand.VISIBLE);
								inAppLogSL.setVisibility(inAppLogSL.VISIBLE);
								break;

							case R.id.expand:
								//	params.height= 100;
								scroll.setVisibility(scroll.VISIBLE);
								collapse.setVisibility(collapse.VISIBLE);
								if (playPause) {
									pause.setVisibility(pause.VISIBLE);
								} else {
									play.setVisibility(play.VISIBLE);
								}

								expand.setVisibility(expand.GONE);
								inAppLogSL.setVisibility(inAppLogSL.GONE);
								break;
							}
						}
					};
				}
				if(ial==null){
					ial = inflater.inflate(R.layout.inapploglay, null);
					sdf = new SimpleDateFormat("HH:mm:ss");
					inAppLog = (TextView) ial.findViewById(R.id.inAppLogTV);
					inAppLogSL = (TextView) ial.findViewById(R.id.inAppLogTVSL);
					scroll = (ScrollView) ial.findViewById(R.id.scroll);
					inapploglay = (FrameLayout) ial.findViewById(R.id.inapploglay);
					play = (ImageButton) ial.findViewById(R.id.playButton);
					pause = (ImageButton) ial.findViewById(R.id.pauseButton);
					collapse = (ImageButton) ial.findViewById(R.id.collapse);
					expand = (ImageButton) ial.findViewById(R.id.expand);
					play.setOnClickListener(ocl);
					pause.setOnClickListener(ocl);
					collapse.setOnClickListener(ocl);
					expand.setOnClickListener(ocl);
					play.setVisibility(play.GONE);
					expand.setVisibility(expand.GONE);
					inAppLogSL.setVisibility(inAppLogSL.GONE);
					playPause = true;
					params = scroll.getLayoutParams();
				}
				if((ViewGroup)ial.getParent()!=null)((ViewGroup)ial.getParent()).removeView(ial);
				v.addView(ial, new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));

				
					

				
			}
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String tagToAdd = tag;
					if(tagToAdd==null || tagToAdd.isEmpty())tagToAdd = TAG;
					if (playPause) {
						time = sdf.format(new Date(System.currentTimeMillis()));

						String current = inAppLog.getText().toString();
						if(current.length()>30000){
							String toSet = current.substring(current.length() - 10000);
							
							inAppLog.setText(toSet.concat("\n").concat(time).concat(" | ").concat(tagToAdd).concat(" | ").concat(str));
						}else{
							//append рабоавет быстрее, но при огромных строчках даже он тормозит, так что выше мы переиодически чистим медленным способом 
							inAppLog.append(new String("\n").concat(time).concat(" | ").concat(tagToAdd).concat(" | ").concat(str));
						}	
						scroll.post(new Runnable() {
							@Override
							public void run() {
								scroll.fullScroll(View.FOCUS_DOWN);
							}
						});
						inAppLogSL.setText(time.concat(" | ").concat(tagToAdd).concat(" | ").concat(str));
					}

					Log.d(tagToAdd, str);
				}
			});
		}
	}
	
		
}
