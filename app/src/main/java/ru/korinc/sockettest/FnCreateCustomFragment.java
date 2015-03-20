package ru.korinc.sockettest;

import android.content.Intent;
import android.graphics.PorterDuff;
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

import net.dinglisch.android.tasker.PluginBundleManager;
import net.dinglisch.android.tasker.TaskerPlugin;

import java.util.ArrayList;
import java.util.List;


public class FnCreateCustomFragment extends ListFragment {
	
	ArrayAdapter<String> adapter ;
	List<String> fns;
	ButtonFnManager fnb;
	TextView tv;
	Button btnOk;
	ImageButton del;
	EditText et;
    String btnName = "";
    long btnId = -1;
    int btnType = 0;
    String btnCmd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            btnName = bundle.getString(FnBind.BTN_NAME, "");
            btnId = bundle.getLong(FnBind.BTN_ID, -1);
            btnType = bundle.getInt(FnBind.BTN_TYPE, 0);
            btnCmd = bundle.getString(FnBind.BTN_CMD);
        }
    }

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
                intent.putExtra(FnBind.BTN_ID, btnId);

                final Bundle resultBundle =
                        PluginBundleManager.generateBundle(getActivity().getApplicationContext(), et.getText().toString(), tv.getText().toString(), fnb.FN_CUSTOM);
                intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                if ( TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement( getActivity() ) )
                    TaskerPlugin.Setting.setVariableReplaceKeys( resultBundle, new String [] { FnBind.BTN_CMD } );

                if (  TaskerPlugin.Setting.hostSupportsSynchronousExecution( getActivity().getIntent().getExtras() ) )
                    TaskerPlugin.Setting.requestTimeoutMS( intent, 3000 );

                final String blurb = et.getText().toString() + " | " + tv.getText().toString() + " | " +  fnb.FN_CUSTOM;
                intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);

                if ( TaskerPlugin.hostSupportsRelevantVariables( getActivity().getIntent().getExtras() ) )
                    TaskerPlugin.addRelevantVariableList( intent, new String [] {
                            "%context\nContext\nCurrent PC process in focus",
                            "%btnids()\nContext Button id\nContext Button id",
                    } );

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
      btnOk.getBackground().setColorFilter(getActivity().getResources().getColor(android.R.color.holo_blue_light), PorterDuff.Mode.MULTIPLY);
	  btnOk.setOnClickListener(ocl);
	  
	  del = (ImageButton) getActivity().findViewById(R.id.fnCustomDelElement);
	  del.setOnClickListener(ocl);
	  del.getBackground().setColorFilter(0x555555, PorterDuff.Mode.SRC_ATOP);
	  
	  et = (EditText) getActivity().findViewById(R.id.fnCustomEtName);
      et.setText(btnName);
	  Intent i =getActivity().getIntent();
	  if(i.getIntExtra("requestCode", -1)!=-1&&i.getIntExtra("requestCode",-1)==ST.REQUEST_CODE_FIRE_FN){
		  et.setVisibility(View.GONE);
	  }
	  tv=(TextView) getActivity().findViewById(R.id.fnCustomSelectTV);
	  tv.setText("Select something");
      if(btnType == ButtonFnManager.FN_CUSTOM) tv.setText(btnCmd);
	  
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
