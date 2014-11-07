package ru.korinc.sockettest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
;


public class FnListFragment extends ListFragment {
	
	ArrayAdapter<String> adapter ;
	List<String> fns;
	FnButton fnb;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fnb = new FnButton();
		
		fns = new ArrayList<String>() ;
		
		for (Map.Entry<Integer, String> entry : fnb.fnMap.entrySet()) { 
			fns.add(entry.getValue());
		}
		
		
	  adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fns);
	  
	  setListAdapter(adapter);
	  
	   
	  
	}
	
	 public void onListItemClick(ListView l, View v, int position, long id) {
		  super.onListItemClick(l, v, position, id);
				Intent intent = new Intent();
				int fnKey=1;
				int i= 0;
				for (Map.Entry<Integer, String> entry : fnb.fnMap.entrySet()) { 
					
					if (i==position) {
						fnKey=entry.getKey();
					}
					i++;
				}
				intent.putExtra("Name","");
				intent.putExtra("FnResult",fnKey);
				intent.putExtra("FnResultArgs","");
				getActivity().setResult(getActivity().RESULT_OK, intent);
				
				getActivity().finish();
			
		  }


}