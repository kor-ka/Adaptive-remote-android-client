package ru.korinc.sockettest;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;


public class TutorialActivity extends FragmentActivity implements View.OnClickListener {

    private static final int NUM_PAGES = 4;
    String[] pageNames;
    String[][] hints;

    int[] imageRes;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        //Pager...
        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        pageNames = new String[]{
                "Главный экран",
                "Боковое меню, настройка кнопок",
                "Окно добавления новой кнопки",
                "Интегрированный интерфейс",
                "Чуть не забыл!"
        };
        hints = new String[][]{
                getResources().getStringArray(R.array.tutorial_1),
                getResources().getStringArray(R.array.tutorial_2),
                getResources().getStringArray(R.array.tutorial_3),
                getResources().getStringArray(R.array.tutorial_4),

        };
/*
        hints = new String[]{





        };

        hints2 = new String[]{




            ""

        };
*/
        imageRes = new int[]{
                R.drawable.t1,
                R.drawable.t2,

                R.drawable.t3,
                R.drawable.t4,

        };

        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(0);

        // Bind the title indicator to the adapter
        CirclePageIndicator topTitleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        topTitleIndicator.setViewPager(mPager);
        topTitleIndicator.setStrokeColor(getResources().getColor(android.R.color.holo_blue_light));
        topTitleIndicator.setFillColor(getResources().getColor(android.R.color.holo_blue_light));

        btn = (Button) findViewById(R.id.btn);
        btn.getBackground().setColorFilter(getResources().getColor(android.R.color.holo_blue_light), PorterDuff.Mode.MULTIPLY);
        btn.setText("Отправляй мне ссылку на сервер - и погнали!");
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        setResult(RESULT_OK);
        finish();
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return ImageFragment.newInstance(hints[position], pageNames[position], imageRes[position]);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}
