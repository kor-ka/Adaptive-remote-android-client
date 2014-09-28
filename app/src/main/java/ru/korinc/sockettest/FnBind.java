package ru.korinc.sockettest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;



public class FnBind extends FragmentActivity implements OnClickListener
{
	
	@Override
	public void onClick(View v)
	{
		finish();
	}


	ScreenSlidePagerAdapter pagerAdapter;

	private ViewPager mPager;

	
	Intent inputintent;
	private static final int NUM_PAGES = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fragment_dialog);
			inputintent = getIntent();

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
					
					return fr ;

				case 1:
					
					fr = new FnListFragment();	
					return fr ;

				case 2:
					fr = new FnCommandLineFragment();	
					
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
					title = "Create shortcut";
					break;

				case 1:					
					title = "Choose existing fn";
					break;

				case 2:
					title = "Create command line command";
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