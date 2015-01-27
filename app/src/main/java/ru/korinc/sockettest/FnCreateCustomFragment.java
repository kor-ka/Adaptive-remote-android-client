package ru.korinc.sockettest;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.PorterDuff;


public class FnCreateCustomFragment extends ListFragment {
	
	ArrayAdapter<String> adapter ;
	List<String> fns;
	ButtonFnManager fnb;
	TextView tv;
	Button btnOk;
	ImageButton del;
	EditText et;
	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		
	    return inflater.inflate(R.layout.fn_custom_select_lay, null);
	  }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fns = new ArrayList<String>() ;
		
		fns.add("Ctrl");
		fns.add("Alt");
		fns.add("Shift");
		fns.add("Tab");
		fns.add("Win");
		fns.add("Del");
		fns.add("Ins");
		fns.add("Home");
		fns.add("End");
		fns.add("Page Up");
		fns.add("Page Down");
		fns.add("Esc");
		fns.add("Enter");
		fns.add("Space");
		fns.add("Backspace");
		fns.add("Plus");
		fns.add("Minus");		
		fns.add("Up arrow");
		fns.add("Down arrow");
		fns.add("Left arrow");
		fns.add("Right arrow");
		
				
		for (int i = 1; i < 13; i++) {
			fns.add("F"+i);
		}
		
		for (int i = 0; i < 10; i++) {
			fns.add(i+"");
		}
		char letter = 'A';
		for(int i=0; i<26; i++){
			fns.add(letter+"");
			letter++;
		}
		
	  adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fns);
	  
	  setListAdapter(adapter);
	  
	  OnClickListener ocl = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.fnCustomBtnOk:
				Intent intent = new Intent();
				intent.putExtra("Name",et.getText().toString());
				intent.putExtra("FnResult",fnb.FN_CUSTOM);
				intent.putExtra("FnResultArgs", tv.getText().toString());
				getActivity().setResult(getActivity().RESULT_OK, intent);
				getActivity().finish();
				break;

			case R.id.fnCustomDelElement:
				String s = tv.getText().toString();
				
				String[] args = s.split(" \\+ ");
				
				int argsLength= args.length;
				
				if(argsLength>1){
					String afterDel=args[0];
					for(int  i=1; i<argsLength-1;i++){
						afterDel += " + "+args[i];
					}
					tv.setText(afterDel);
				}else{
					
					tv.setText("Select something");
				}
				
				break;
			}
			
			
			
		}
	  };
	  
	  btnOk = (Button) getActivity().findViewById(R.id.fnCustomBtnOk);
	  btnOk.setOnClickListener(ocl);
	  
	  del = (ImageButton) getActivity().findViewById(R.id.fnCustomDelElement);
	  del.setOnClickListener(ocl);
	  del.getBackground().setColorFilter(0x555555, PorterDuff.Mode.SRC_ATOP);
	  
	  et = (EditText) getActivity().findViewById(R.id.fnCustomEtName);
	  Intent i =getActivity().getIntent();
	  if(i.getIntExtra("requestCode", -1)!=-1&&i.getIntExtra("requestCode",-1)==ST.REQUEST_CODE_FIRE_FN){
		  et.setVisibility(View.GONE);
	  }
	  tv=(TextView) getActivity().findViewById(R.id.fnCustomSelectTV);
		 tv.setText("Select something");
	  
	}
	
	 public void onListItemClick(ListView l, View v, int position, long id) {
		  super.onListItemClick(l, v, position, id);
				
		  String fnKey="";
		  
		  fnKey =  fns.get(position);
		  	if(tv.getText().toString().equals("Select something")){
		  		tv.setText(fnKey);
		  	}else{
		  		tv.setText(tv.getText().toString()+" + "+fnKey);
		  	}
			
				
				
			
		  }


}
