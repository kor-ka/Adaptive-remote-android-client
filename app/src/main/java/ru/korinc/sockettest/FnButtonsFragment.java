package ru.korinc.sockettest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FnButtonsFragment extends Fragment {
	public static final String PAGE_ID_ARG="pageId";
	String pageId ="";
	Button b1;
	Button b2;
	Button b3;
	private static final String FN_SAVE_B1 = "fnB1";
	private static final String FN_SAVE_B2 = "fnB2";
	private static final String FN_SAVE_B3 = "fnB3";
	
	public static final int REQUEST_CODE_B1 = 12346;
	public static final int REQUEST_CODE_B2 = 12347;
	public static final int REQUEST_CODE_B3 = 12348;
	
	private static final String FN_SAVE_ARGS_B1 = "fnB1args";
	private static final String FN_SAVE_ARGS_B2 = "fnB2args";
	private static final String FN_SAVE_ARGS_B3 = "fnB3args";
	
	private static final String FN_SAVE_NAME_B1 = "fnB1name";
	private static final String FN_SAVE_NAME_B2 = "fnB2name";
	private static final String FN_SAVE_NAME_B3 = "fnB3name";
	
	 View view;
	 FnButtonsFragment thisFragment;
	public FnButtonsFragment() {
		
	}
	

	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {		
		 view = inflater.inflate(R.layout.fn_buttons_fragment, null);
	    return view;
	  }
	 
	 @Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.pageId = getArguments().getString(PAGE_ID_ARG, "top0");
			this.thisFragment = this;
			b1 = (Button) view.findViewById(R.id.buttonB1);
			b2 = (Button)  view.findViewById(R.id.buttonB2);
			b3 = (Button)  view.findViewById(R.id.buttonB3);
			final ST st = (ST) getActivity();
			OnClickListener ocl = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				switch (v.getId()) {
				case R.id.buttonB1:
					int bindedFunction1 = st.shp.getInt(FN_SAVE_B1+""+pageId, st.fnb.NO_FUNCTION);
					if(bindedFunction1 == st.fnb.NO_FUNCTION){
						Intent intentB1 = new Intent(st, FnBind.class);
						startActivityForResult(intentB1, REQUEST_CODE_B1);
					}else{
						st.fnb.press(bindedFunction1, st.shp.getString(FN_SAVE_ARGS_B1+""+pageId, "Nope"),"");
					}
					break;
				
				case R.id.buttonB2:
					int bindedFunction2 = st.shp.getInt(FN_SAVE_B2+""+pageId, st.fnb.NO_FUNCTION);
					if(bindedFunction2 == st.fnb.NO_FUNCTION){
						Intent intentB2 = new Intent(st, FnBind.class);
						startActivityForResult(intentB2, REQUEST_CODE_B2);
					}else{
						st.fnb.press(bindedFunction2, st.shp.getString(FN_SAVE_ARGS_B2+""+pageId, "Nope"), "");
					}
					break;
				
				case R.id.buttonB3:
					int bindedFunction3 = st.shp.getInt(FN_SAVE_B3+""+pageId, st.fnb.NO_FUNCTION);
					if(bindedFunction3 == st.fnb.NO_FUNCTION){
						Intent intentB3 = new Intent(st, FnBind.class);
						startActivityForResult(intentB3, REQUEST_CODE_B3);
					}else{
						st.fnb.press(bindedFunction3, st.shp.getString(FN_SAVE_ARGS_B3+""+pageId, "Nope"), "");
					}
					break;
				}
				}
			};
			
			OnLongClickListener olclFn = new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					int reqToSend=0;
					switch (v.getId()) {
					case R.id.buttonB1:
						reqToSend = REQUEST_CODE_B1;
						break;					
					
					case R.id.buttonB2:
						reqToSend = REQUEST_CODE_B2;
						break;					
					
					case R.id.buttonB3:
						reqToSend = REQUEST_CODE_B3;
						break;					
										
				}
					Intent intent = new Intent(st.getBaseContext(), FnBind.class);
					startActivityForResult(intent, reqToSend);
					return false;
				}
			};
			
			b1.setOnClickListener(ocl);
			b1.setText(st.fnb.fnMap.get(st.shp.getInt(FN_SAVE_B1+""+pageId, st.fnb.NO_FUNCTION)));
			
			b1.setOnLongClickListener(olclFn);
			b2.setOnClickListener(ocl);
			b2.setText(st.fnb.fnMap.get(st.shp.getInt(FN_SAVE_B2+""+pageId, st.fnb.NO_FUNCTION)));
			b2.setOnLongClickListener(olclFn);
			b3.setOnClickListener(ocl);
			b3.setText(st.fnb.fnMap.get(st.shp.getInt(FN_SAVE_B3+""+pageId, st.fnb.NO_FUNCTION)));
			b3.setOnLongClickListener(olclFn);
			
			if(st.shp.getInt(FN_SAVE_B1+""+pageId, st.fnb.NO_FUNCTION)==FnButton.NO_FUNCTION){
				b1.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
			}
			if(st.shp.getInt(FN_SAVE_B2+""+pageId, st.fnb.NO_FUNCTION)==FnButton.NO_FUNCTION){
				b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
			}
			if(st.shp.getInt(FN_SAVE_B3+""+pageId, st.fnb.NO_FUNCTION)==FnButton.NO_FUNCTION){
				b3.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
			}
						
			if(st.shp.getInt(FN_SAVE_B1+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_CUSTOM||st.shp.getInt(FN_SAVE_B1+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_COMMAND_LINE){
				b1.setText(st.shp.getString(FN_SAVE_ARGS_B1+""+pageId, ""));
				if(st.shp.getString(FN_SAVE_ARGS_B1+""+pageId, "").contains("chrome")){
					b1.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
				}else{
					b1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			}else{
				b1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			if(st.shp.getInt(FN_SAVE_B2+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_CUSTOM||st.shp.getInt(FN_SAVE_B2+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_COMMAND_LINE){
				b2.setText(st.shp.getString(FN_SAVE_ARGS_B2+""+pageId, ""));
				if(st.shp.getString(FN_SAVE_ARGS_B2+""+pageId, "").contains("chrome")){
					b2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
				}else{
					b2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			}else{
				b2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			if(st.shp.getInt(FN_SAVE_B3+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_CUSTOM||st.shp.getInt(FN_SAVE_B3+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_COMMAND_LINE){
				b3.setText(st.shp.getString(FN_SAVE_ARGS_B3+""+pageId, ""));
				if(st.shp.getString(FN_SAVE_ARGS_B3+""+pageId, "").contains("chrome")){
					b3.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
				}else{
					b3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			}else{
				b3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			
			if(!st.shp.getString(FN_SAVE_NAME_B1+""+pageId, "").equals("")){
				b1.setText(st.shp.getString(FN_SAVE_NAME_B1+""+pageId, ""));
			}
			if(!st.shp.getString(FN_SAVE_NAME_B2+""+pageId, "").equals("")){
				b2.setText(st.shp.getString(FN_SAVE_NAME_B2+""+pageId, ""));
			}
			if(!st.shp.getString(FN_SAVE_NAME_B3+""+pageId, "").equals("")){
				b3.setText(st.shp.getString(FN_SAVE_NAME_B3+""+pageId, ""));
			}
	 }
	 
	 public void saveFnBindResults (Intent i, int reqestCode){
		 final ST st = (ST) getActivity();
		 switch (reqestCode) {
		case REQUEST_CODE_B1:
			 	st.ed.putInt(FN_SAVE_B1+""+pageId, i.getIntExtra("FnResult", st.fnb.NO_FUNCTION));
				st.ed.putString(FN_SAVE_ARGS_B1+""+pageId, i.getStringExtra("FnResultArgs"));
				st.ed.putString(FN_SAVE_NAME_B1+""+pageId, i.getStringExtra("Name"));
				st.ed.commit();				
				b1.setText(st.fnb.fnMap.get(i.getIntExtra("FnResult", st.fnb.NO_FUNCTION)));
				if(!i.getStringExtra("FnResultArgs").equals("")){
					b1.setText(i.getStringExtra("FnResultArgs"));
				}
				if(!i.getStringExtra("Name").equals("")){
					b1.setText(i.getStringExtra("Name"));
				}
				
				if(st.shp.getInt(FN_SAVE_B1+""+pageId, st.fnb.NO_FUNCTION)==FnButton.NO_FUNCTION){
					b1.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
				}else{
					b1.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
				}
				if(st.shp.getInt(FN_SAVE_B1+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_CUSTOM||st.shp.getInt(FN_SAVE_B1+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_COMMAND_LINE){
					
					if(st.shp.getString(FN_SAVE_ARGS_B1+""+pageId, "").contains("chrome")){
						b1.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
					}else{
						b1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
					}
				}else{
					b1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			break;
			
		case REQUEST_CODE_B2:
		 	st.ed.putInt(FN_SAVE_B2+""+pageId, i.getIntExtra("FnResult", st.fnb.NO_FUNCTION));
			st.ed.putString(FN_SAVE_ARGS_B2+""+pageId, i.getStringExtra("FnResultArgs"));
			st.ed.putString(FN_SAVE_NAME_B2+""+pageId, i.getStringExtra("Name"));
			st.ed.commit();
			b2.setText(st.fnb.fnMap.get(i.getIntExtra("FnResult", st.fnb.NO_FUNCTION)));
			if(!i.getStringExtra("FnResultArgs").equals("")){
				b2.setText(i.getStringExtra("FnResultArgs"));
			}
			if(!i.getStringExtra("Name").equals("")){
				b2.setText(i.getStringExtra("Name"));
			}
			
			if(st.shp.getInt(FN_SAVE_B2+""+pageId, st.fnb.NO_FUNCTION)==FnButton.NO_FUNCTION){
				b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
			}else{
				b2.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
			}
			if(st.shp.getInt(FN_SAVE_B2+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_CUSTOM||st.shp.getInt(FN_SAVE_B2+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_COMMAND_LINE){
				
				if(st.shp.getString(FN_SAVE_ARGS_B3+""+pageId, "").contains("chrome")){
					b2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
				}else{
					b2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			}else{
				b2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			
		break;
		
		case REQUEST_CODE_B3:
		 	st.ed.putInt(FN_SAVE_B3+""+pageId, i.getIntExtra("FnResult", st.fnb.NO_FUNCTION));
			st.ed.putString(FN_SAVE_ARGS_B3+""+pageId, i.getStringExtra("FnResultArgs"));
			st.ed.putString(FN_SAVE_NAME_B3+""+pageId, i.getStringExtra("Name"));
			st.ed.commit();
			b3.setText(st.fnb.fnMap.get(i.getIntExtra("FnResult", st.fnb.NO_FUNCTION)));
			if(!i.getStringExtra("FnResultArgs").equals("")){
				b3.setText(i.getStringExtra("FnResultArgs"));
			}
			if(!i.getStringExtra("Name").equals("")){
				b3.setText(i.getStringExtra("Name"));
			}
			
			if(st.shp.getInt(FN_SAVE_B3+""+pageId, st.fnb.NO_FUNCTION)==FnButton.NO_FUNCTION){
				b3.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_fn_btn_seelctor));
			}else{
				b3.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_seelctor));
			}
			if(st.shp.getInt(FN_SAVE_B3+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_CUSTOM||st.shp.getInt(FN_SAVE_B3+""+pageId, st.fnb.NO_FUNCTION)==st.fnb.FN_COMMAND_LINE){
				
				if(st.shp.getString(FN_SAVE_ARGS_B3+""+pageId, "").contains("chrome")){
					b3.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_chrome), null, null, null);
				}else{
					b3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				}
			}else{
				b3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			
		break;		
		
		}
		
	 }
	 
	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode==ST.RESULT_OK){
			 saveFnBindResults(data, requestCode);
		 }	
		super.onActivityResult(requestCode, resultCode, data);
	}
}
