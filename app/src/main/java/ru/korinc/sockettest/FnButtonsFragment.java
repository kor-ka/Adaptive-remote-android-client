package ru.korinc.sockettest;


import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class FnButtonsFragment extends Fragment {
	public static final String PAGE_ID_ARG="pageId";
	String pageId ="";
	FnButton b1;
    FnButton b2;
    FnButton b3;

    private  FnButton[] fnButtons;

	private static final String FN_SAVE_B1 = "fnB1";
	private static final String FN_SAVE_B2 = "fnB2";
	private static final String FN_SAVE_B3 = "fnB3";

    private static  final String[] FN_SAVE_B = new String[]{FN_SAVE_B1, FN_SAVE_B2, FN_SAVE_B3};
	
	public static final int REQUEST_CODE_B1 = 12346;
	public static final int REQUEST_CODE_B2 = 12347;
	public static final int REQUEST_CODE_B3 = 12348;
	
	private static final String FN_SAVE_ARGS_B1 = "fnB1args";
	private static final String FN_SAVE_ARGS_B2 = "fnB2args";
	private static final String FN_SAVE_ARGS_B3 = "fnB3args";

    private static final String[] FN_SAVE_ARGS_B = new String[]{FN_SAVE_ARGS_B1, FN_SAVE_ARGS_B2, FN_SAVE_ARGS_B3};
	
	private static final String FN_SAVE_NAME_B1 = "fnB1name";
	private static final String FN_SAVE_NAME_B2 = "fnB2name";
	private static final String FN_SAVE_NAME_B3 = "fnB3name";

    private static final String[] FN_SAVE_NAME_B = new String[]{FN_SAVE_NAME_B1, FN_SAVE_NAME_B2, FN_SAVE_NAME_B3};

    OnLongClickListener olclFn;
    OnClickListener ocl;
	
	 View view;
	 FnButtonsFragment thisFragment;
    DbTool db = new DbTool();
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
			b1 = (FnButton) view.findViewById(R.id.buttonB1);
			b2 = (FnButton)  view.findViewById(R.id.buttonB2);
			b3 = (FnButton)  view.findViewById(R.id.buttonB3);


            fnButtons = new FnButton[]{b1,b2,b3};
            final ST st = (ST) getActivity();
             for (final FnButton btn:fnButtons){
                 btn.setOnDragListener( new View.OnDragListener() {


                     @Override
                     public boolean onDrag( View v, DragEvent event) {
                         final int action = event.getAction();
                         int animationConvert = pageId.startsWith("top")?-1:1;
                         TranslateAnimation enterAnimation = new TranslateAnimation(
                                 Animation.ABSOLUTE, 0, Animation.ABSOLUTE,0,
                                 Animation.ABSOLUTE, 0, Animation.ABSOLUTE,b1.getHeight()*2*animationConvert);
                         enterAnimation.setDuration(300);
                         enterAnimation.setFillAfter(true);

                         TranslateAnimation exitAnimation = new TranslateAnimation(
                                 Animation.ABSOLUTE, 0, Animation.ABSOLUTE,0,
                                 Animation.ABSOLUTE, b1.getHeight()*2*animationConvert, Animation.ABSOLUTE, 0);
                         exitAnimation.setDuration(300);
                         exitAnimation.setFillAfter(true);

                         AlphaAnimation afterDropAnimation=new AlphaAnimation(0,1);
                         afterDropAnimation.setDuration(200);
                         afterDropAnimation.setFillAfter(true);

                         // Handles each of the expected events
                         switch(action) {

                             case DragEvent.ACTION_DRAG_ENTERED:
                                 v.startAnimation(enterAnimation);
                                 break;

                             case DragEvent.ACTION_DRAG_EXITED:
                                 v.startAnimation(exitAnimation);
                                 break;

                             case DragEvent.ACTION_DROP:
                                 ClipData.Item item = event.getClipData().getItemAt(0);
                                 Intent i = item.getIntent();
                                 long id=i.getLongExtra("id", 0);
                                 btn.init(id, getActivity(), ocl, olclFn, st.fnb);

                                 DbTool db = new DbTool();
                                 FnButton fnb = (FnButton) v;
                                 db.bindButtonToPlace(id, fnb.getPlace(), getActivity());
                                 v.clearAnimation();
                                 v.startAnimation(afterDropAnimation);
                                 break;
                         }
                         return true;
                     }
                 });
             }


			ocl = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    Intent editIntent = new Intent(st, FnBind.class);

				switch (v.getId()) {
				case R.id.buttonB1:

					if(b1.type == st.fnb.NO_FUNCTION){
						startActivityForResult(editIntent, REQUEST_CODE_B1);
					}else{
						st.fnb.press(b1.type, b1.args,"");
					}
					break;
				
				case R.id.buttonB2:
					if(b2.type == st.fnb.NO_FUNCTION){
						startActivityForResult(editIntent, REQUEST_CODE_B2);
					}else{
						st.fnb.press(b2.type, b2.args, "");
					}
					break;
				
				case R.id.buttonB3:

					if(b3.type == st.fnb.NO_FUNCTION){
						startActivityForResult(editIntent, REQUEST_CODE_B3);
					}else{
						st.fnb.press(b3.type, b3.args, "");
					}
					break;
				}
				}
			};
			
			olclFn = new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					int reqToSend=0;
                    long btnId =-1;
					switch (v.getId()) {
					case R.id.buttonB1:
						reqToSend = REQUEST_CODE_B1;
                        btnId = b1.getBtnId();
						break;					
					
					case R.id.buttonB2:
						reqToSend = REQUEST_CODE_B2;
                        btnId = b2.getBtnId();
						break;					
					
					case R.id.buttonB3:
						reqToSend = REQUEST_CODE_B3;
                        btnId = b3.getBtnId();
						break;					
										
				}
                    db = new DbTool();

					Intent intent = new Intent(st.getBaseContext(), FnBind.class);
                    intent.putExtra(FnBind.BTN_ID, btnId);
					startActivityForResult(intent, reqToSend);
					return false;
				}
			};

            //Инитим кнопки
         initButtons(ocl, olclFn, st);

         //Ухо на тач - экслюзивно для слйдера
            b1.setOnTouchListener(st.overlayOTL);
            b2.setOnTouchListener(st.overlayAltTAbOTL);
            b3.setOnTouchListener(st.overlayOTL);

	 }

    public void initButtons(OnClickListener ocl, OnLongClickListener olclFn, ST st) {
        if(fnButtons!=null){
            for (int i = 0; i < fnButtons.length; i++) {
                fnButtons[i].init(FN_SAVE_B[i]+""+pageId, getActivity(), ocl, olclFn, st.fnb);
            }
        }
    }

    public void saveFnBindResults (Intent i, int reqestCode){
        final ST st = (ST) getActivity();
        String fn_save = "";

        switch (reqestCode){
            case REQUEST_CODE_B1:
                fn_save = FN_SAVE_B1;
                break;
            case REQUEST_CODE_B2:
                fn_save = FN_SAVE_B2;
                break;
            case REQUEST_CODE_B3:
                fn_save = FN_SAVE_B3;
                break;
        }
        if(i.getIntExtra("FnResult", st.fnb.NO_FUNCTION)!=st.fnb.NO_FUNCTION){
            //Пишем кнопку в базу
            //Сейчас всегда заливаем новую. Потом будем обновлять по id
            long id = db.addButton(i.getLongExtra("id", -1), i.getStringExtra("Name"), i.getIntExtra("FnResult", st.fnb.NO_FUNCTION), i.getStringExtra("FnResultArgs"), -1, getActivity(), i.getStringExtra("plugin"), i.getIntExtra("color", 0));

            db.bindButtonToPlace(id, fn_save+""+pageId, getActivity());
            DrawerGridAdapter adapter = (DrawerGridAdapter) st.mDrawerGrid.getAdapter();
            adapter.getCursor().requery();
            adapter.notifyDataSetChanged();

        }else{
            db.bindButtonToPlace(-1,fn_save+""+pageId, st);
        }

        initButtons(ocl, olclFn, st);


	 }
	 
	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode==ST.RESULT_OK){
			 saveFnBindResults(data, requestCode);


		 }	
		super.onActivityResult(requestCode, resultCode, data);
	}
}
