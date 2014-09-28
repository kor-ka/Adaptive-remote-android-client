package ru.korinc.sockettest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoiceFnMappingList extends Activity{
	ListView lv;
	SharedPreferences shp;
	Editor ed;
	
	EditText keyEt;
	Button b1;
	Set<String> keys;
	List<Spannable> map;
	ArrayAdapter<Spannable> adapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_fn_mapping_list);		
		
		shp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed = shp.edit();
		
		keyEt = (EditText) findViewById(R.id.editKey);
		b1 = (Button) findViewById(R.id.voice_fn_button);
		
		
		keys  = shp.getStringSet("VoiceFnMap", new HashSet<String>());
		if (keys.isEmpty()){
			keys.add("хром");
			ed.putString("VoiceFnArg:"+"хром", "start chrome");
			ed.putInt("VoiceFn:"+"хром", FnButton.FN_COMMAND_LINE);
			ed.putStringSet("VoiceFnMap", keys);
			ed.commit();

            keys.add("Поиск");
            ed.putString("VoiceFnArg:"+"хром", "start chrome \"google.ru/search?q=<input>\"");
            ed.putInt("VoiceFn:"+"хром", FnButton.FN_COMMAND_LINE);
            ed.putStringSet("VoiceFnMap", keys);
            ed.commit();
		}
		
		map = new ArrayList<Spannable>() ;
		if(keys != null){
			for (String key:keys) {
				String descrOfCammand = shp.getString("VoiceFnArg:"+key, "null");
				if(descrOfCammand.isEmpty()){
					descrOfCammand=FnButton.fnMap.get(shp.getInt("VoiceFn:"+key, 0));
				}
				map.add(getSmiledText(getBaseContext(),  key+" = "+descrOfCammand));
			}
		}
		
		Collections.sort(map, new Comparator<Spannable>() {
	        @Override
	        public int compare(Spannable  s1, Spannable  s2)
	        {

	            return  s1.toString().compareTo(s2.toString());
	        }
	    });
		
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				Intent intent = new Intent(getBaseContext(), FnBind.class);
					startActivityForResult(intent, 0);
							
			}
		};
		
		
		
		b1.setOnClickListener(ocl);				
	
		
		lv= (ListView) findViewById(R.id.listViewMap);
		// ������� �������
	  adapter = new ArrayAdapter<Spannable>(this,
	        android.R.layout.simple_list_item_1, map);
	  
	    // ����������� ������� ������
	    lv.setAdapter(adapter);
	    
	  //����������� ������
		  lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					String[] keyAndValue = map.get(position).toString().split(" = ");
					String keyToRemove = keyAndValue[0];
					if(map.get(position).toString().contains(" = ")){
						ed.putString("VoiceFnArg:"+keyToRemove, null);
						ed.putInt("VoiceFn:"+keyToRemove, 0);
						keys.remove(keyToRemove);
						ed.putStringSet("VoiceFnMap", keys);
						ed.commit();
						map.remove(position);
						adapter.notifyDataSetChanged();
						
						keyEt.setText(keyAndValue[0]);
						b1.setText("No function");
					}
					
				}
			});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==0&&resultCode==RESULT_OK){
			ed.putString("VoiceFnArg:"+keyEt.getText().toString().toLowerCase(),  data.getStringExtra("FnResultArgs"));
			ed.putInt("VoiceFn:"+keyEt.getText().toString().toLowerCase(), data.getIntExtra("FnResult", FnButton.NO_FUNCTION));
			keys  = shp.getStringSet("VoiceFnMap", new HashSet<String>());
			keys.add(keyEt.getText().toString().toLowerCase());
			ed.putStringSet("VoiceFnMap", keys);
			ed.commit();
			
			String descrOfCammand = data.getStringExtra("FnResultArgs");
			if(descrOfCammand.isEmpty()){
				descrOfCammand=FnButton.fnMap.get(data.getIntExtra("FnResult", FnButton.NO_FUNCTION));
			}
			map.add(getSmiledText(getBaseContext(),  keyEt.getText().toString()+" = "+descrOfCammand));	
			
			adapter.notifyDataSetChanged();
			
			keyEt.setText("");
			b1.setText("No function");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//ICON
	 
		 private static final Factory spannableFactory = Spannable.Factory
			        .getInstance();

			private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

			static {
			    addPattern(emoticons, "<input>", R.drawable.ic_ic_open_mic_plus);
			}

			private static void addPattern(Map<Pattern, Integer> map, String smile,
			        int resource) {
			    map.put(Pattern.compile(Pattern.quote(smile)), resource);
			}

			public static boolean addSmiles(Context context, Spannable spannable) {
			    boolean hasChanges = false;
			    for (java.util.Map.Entry<Pattern, Integer> entry : emoticons.entrySet()) {
			        Matcher matcher = entry.getKey().matcher(spannable);
			        while (matcher.find()) {
			            boolean set = true;
			            for (ImageSpan span : spannable.getSpans(matcher.start(),
			                    matcher.end(), ImageSpan.class))
			                if (spannable.getSpanStart(span) >= matcher.start()
			                        && spannable.getSpanEnd(span) <= matcher.end())
			                    spannable.removeSpan(span);
			                else {
			                    set = false;
			                    break;
			                }
			            if (set) {
			                hasChanges = true;
			                spannable.setSpan(new ImageSpan(context, entry.getValue()),
			                        matcher.start(), matcher.end(),
			                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			            }
			        }
			    }
			    return hasChanges;
			}

			public static Spannable getSmiledText(Context context, CharSequence text) {
			    Spannable spannable = spannableFactory.newSpannable(text);
			    addSmiles(context, spannable);
			    return spannable;
			}
	

}
