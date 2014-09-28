package ru.korinc.sockettest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MappingList extends Activity implements OnClickListener{
	ListView lv;
	SharedPreferences shp;
	Editor ed;
	Button addToMap;
	EditText keyEt;
	EditText valueEt;
	Set<String> keys;
	List<String> map;
	ArrayAdapter<String> adapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapping_list);		
		
		shp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed = shp.edit();
		
		keyEt = (EditText) findViewById(R.id.editKey);
		valueEt = (EditText) findViewById(R.id.editTextValue);
		addToMap = (Button) findViewById(R.id.buttonAddToMap);
		addToMap.setOnClickListener(this);
		
		keys  = shp.getStringSet("map", new HashSet<String>());
		if (keys.isEmpty()){
			keys.add("хром");
			ed.putString("хром", "chrome");
			ed.putStringSet("map", keys);
			ed.commit();
		}
		
		map = new ArrayList<String>() ;
		if(keys != null){
			for (String key:keys) {
				map.add(key+" = "+shp.getString(key, "null"));
			}
		}
		
		Collections.sort(map, new Comparator<String>() {
	        @Override
	        public int compare(String  s1, String  s2)
	        {

	            return  s1.compareTo(s2);
	        }
	    });
		
		lv= (ListView) findViewById(R.id.listViewMap);
		// ������� �������
	  adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, map);
	  
	    // ����������� ������� ������
	    lv.setAdapter(adapter);
	    
	  //����������� ������
		  lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					String[] keyAndValue = map.get(position).split(" = ");
					String keyToRemove = keyAndValue[0];
					if(map.get(position).contains(" = ")){
						ed.putString(keyToRemove, null);
						keys.remove(keyToRemove);
						ed.putStringSet("map", keys);
						ed.commit();
						map.remove(position);
						adapter.notifyDataSetChanged();
						
						keyEt.setText(keyAndValue[0]);
						valueEt.setText(keyAndValue[1]);
					}
					
				}
			});
	}

	
	@Override
	public void onClick(View v) {
		if(keyEt.getText().toString().length()>0 && valueEt.getText().toString().length()>0){
			ed.putString(keyEt.getText().toString(), valueEt.getText().toString());
			keys  = shp.getStringSet("map", new HashSet<String>());
			keys.add(keyEt.getText().toString());
			ed.putStringSet("map", keys);
			ed.commit();
			map.add(keyEt.getText().toString()+" = "+valueEt.getText().toString());		
			adapter.notifyDataSetChanged();
			
			keyEt.setText("");
			valueEt.setText("");
		}
		
		 
	}


	

}
