package ru.korinc.sockettest;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import net.dinglisch.android.tasker.BundleScrubber;
import net.dinglisch.android.tasker.TaskerPlugin;


public class FnBind extends FragmentActivity implements OnClickListener
{

    public static final String BTN_ID = "btnId";
    public static final String BTN_NAME = "btnName";
    public static final String BTN_CMD = "btnCmd";
    public static final String BTN_TYPE = "btnType";

    @Override
	public void onClick(View v)
	{
		finish();
	}


	ScreenSlidePagerAdapter pagerAdapter;

	private ViewPager mPager;
    Bundle bundle;

	Intent inputintent;
	private static final int NUM_PAGES = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_dialog);
        inputintent = getIntent();

        bundle = new Bundle();


        DbTool db = new DbTool();
        long id =inputintent.getLongExtra(BTN_ID, -1);
        Cursor c = db.getButtonCursor(id, this);
        if(c!=null){

            bundle.putLong(BTN_ID, id);
            bundle.putString(BTN_NAME, c.getString(c.getColumnIndex(DbTool.BUTTONS_TABLE_NAME)));
            bundle.putString(BTN_CMD, c.getString(c.getColumnIndex(DbTool.BUTTONS_TABLE_CMD)));
            bundle.putInt(BTN_TYPE, c.getInt(c.getColumnIndex(DbTool.BUTTONS_TABLE_TYPE)));
            c.close();
        }


        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);



        if (null == savedInstanceState && localeBundle!=null && !localeBundle.isEmpty())
        {

            //if (PluginBundleManager.isBundleValid(localeBundle)){

                bundle.putString(BTN_NAME, localeBundle.getString(BTN_NAME, ""));
                bundle.putString(BTN_CMD, localeBundle.getString(BTN_CMD, ""));
                bundle.putInt(BTN_TYPE, localeBundle.getInt(BTN_TYPE, ButtonFnManager.NO_FUNCTION));



            //}
        }



        //Pager...
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);


        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(1);

	}

	

	//Pager...

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    	
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fragmentManager) {
            super(fragmentManager);

        }

       

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
        	ListFragment fr = null;
        	
			switch (position) {
				case 0:
					fr = new FnCreateCustomFragment();	
					fr.setArguments(bundle);
					return fr ;

				case 1:
					
					fr = new FnListFragment();
                    fr.setArguments(bundle);
					return fr ;

				case 2:
					fr = new FnCommandLineFragment();
                    fr.setArguments(bundle);
					return fr ;

			}
			return new FnListFragment();			


        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	String title = "Oops";
			switch (position) {
				case 0:
					title = getString(R.string.fn_bind_tab_title_shortcut);
					break;

				case 1:					
					title = getString(R.string.fn_bind_tab_title_choose);
					break;

				case 2:
					title = getString(R.string.fn_bind_tab_title_cmd);
					break;
				
				default:
					title = "Oops";
					break;
			}
			return title;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
   	
}