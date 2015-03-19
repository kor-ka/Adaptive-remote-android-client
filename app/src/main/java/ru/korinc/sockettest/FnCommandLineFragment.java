package ru.korinc.sockettest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import net.dinglisch.android.tasker.PluginBundleManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FnCommandLineFragment extends ListFragment {
	
	ArrayAdapter<Spannable> adapter ;
	List<Spannable> fns;
	ButtonFnManager fnb;
	EditText et;
	EditText etName;
	Button btnOk;
	ImageButton addInput;
	SharedPreferences shp;
	Editor ed;
	Set<String> commands;
    String btnName = "";
    long btnId = -1;
    String btnCmd = "";
    int btnType = 0;
	private static final String FN_COMMANDS_KEY="FnCommands";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            btnName = bundle.getString(FnBind.BTN_NAME, "");
            btnCmd = bundle.getString(FnBind.BTN_CMD, "");
            btnId = bundle.getLong(FnBind.BTN_ID, -1);
            btnType = bundle.getInt(FnBind.BTN_TYPE, 0);
        }
    }

	 @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		
	    return inflater.inflate(R.layout.fn_custom_command_lay, null);
	  }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fns = new ArrayList<Spannable>() ;
		shp = getActivity().getSharedPreferences(FN_COMMANDS_KEY,0);
		ed = shp.edit();
		
		commands  = shp.getStringSet(FN_COMMANDS_KEY, new LinkedHashSet<String>());
		if (commands.isEmpty()){
			commands.add("start chrome \"<input>\"");
			commands.add("start chrome \"google.ru/search?q=<input>\"");
			commands.add("sndvol");
			commands.add("explorer");
			commands.add("explorer shell:downloads");
			commands.add("start notepad");
			commands.add("shutdown -s");		
			commands.add("shutdown -r");	
			commands.add("shutdown -l");
			ed.putStringSet(FN_COMMANDS_KEY, commands);
			ed.commit();
		}
		
		
		if(commands != null){
			for (String key:commands) {
				fns.add(getSmiledText(getActivity(), key));
			}
		}
				
		Collections.sort(fns, new Comparator<Spannable>() {
	        @Override
	        public int compare(Spannable  s1, Spannable  s2)
	        {

	            return  s1.toString().compareTo(s2.toString());
	        }
	    });
		
	  adapter = new ArrayAdapter<Spannable>(getActivity(), android.R.layout.simple_list_item_1, fns);
	  
	  setListAdapter(adapter);
	  
	  OnClickListener ocl = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.fnCommandLineBtnOk:
					commands.add(et.getText().toString());
					ed.clear();
					ed.putStringSet(FN_COMMANDS_KEY, commands);
					ed.commit();
					Intent intent = new Intent();
					intent.putExtra("Name", etName.getText().toString());
					intent.putExtra("FnResult",fnb.FN_COMMAND_LINE);
					intent.putExtra("FnResultArgs", et.getText().toString());
                    intent.putExtra(FnBind.BTN_ID, btnId);

                    final Bundle resultBundle =
                            PluginBundleManager.generateBundle(getActivity().getApplicationContext(), etName.getText().toString(), et.getText().toString(), fnb.FN_COMMAND_LINE);
                    intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                    final String blurb =  etName.getText().toString() + " | " +  et.getText().toString() + " | " + fnb.FN_COMMAND_LINE;
                    intent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);

					getActivity().setResult(getActivity().RESULT_OK, intent);
					getActivity().finish();
					break;
					
				case R.id.add_mic:
					if(!et.getText().toString().contains("<input>")){
						String before = et.getText().toString();
						int selectionStart = et.getSelectionStart();
						String toInsert = before.substring(0, selectionStart)+"<input>"+before.substring(selectionStart);
						et.setText(getSmiledText(getActivity(), toInsert));
						et.setSelection(selectionStart+7);
					}					
					break;
			
			}
			
			
			
		}
	  };
	  
	  btnOk = (Button) getActivity().findViewById(R.id.fnCommandLineBtnOk);
      btnOk.getBackground().setColorFilter(getActivity().getResources().getColor(android.R.color.holo_blue_light), PorterDuff.Mode.MULTIPLY);
	  btnOk.setOnClickListener(ocl);
	  
	  addInput = (ImageButton) getActivity().findViewById(R.id.add_mic);
	  addInput.setOnClickListener(ocl);
	  
	  et=(EditText) getActivity().findViewById(R.id.fn_custom_command_et);

	  et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String s1 = et.getText().toString();
                if(s1.contains("<input") && !s1.contains("<input>")){
                    et.setText(s1.replace("<input", ""));
                    et.setSelection(start-6);
                }
			
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				
				
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
	  });
      if(btnType == ButtonFnManager.FN_COMMAND_LINE )et.setText(getSmiledText(getActivity(), btnCmd));
	  etName=(EditText) getActivity().findViewById(R.id.fnCommandLineEtName);
      etName.setText(btnName);
	  Intent i =getActivity().getIntent();
	  if(i.getIntExtra("requestCode", -1)!=-1&&i.getIntExtra("requestCode",-1)==ST.REQUEST_CODE_FIRE_FN){
		  etName.setVisibility(View.GONE);		 
	  }
	  getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
         
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View v,	int position, long id) {
			commands.remove(fns.get(position).toString());
			ed.clear();
			ed.putStringSet(FN_COMMANDS_KEY, commands);
			ed.commit();
			fns.remove(position);			
			adapter.notifyDataSetChanged();
			return false;
		}
      });
	  
	}
	
	 public void onListItemClick(ListView l, View v, int position, long id) {
		  super.onListItemClick(l, v, position, id);
				
		  Spannable command=getSmiledText(getActivity(), "");
		  
		  command =  fns.get(position);
		  	
		  		et.setText(command);
		  	
			
				
				
			
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