package ru.korinc.sockettest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;


public class TutorialActivity extends FragmentActivity {

    private static final int NUM_PAGES = 4;
    String[] pageNames;
    String[] hints1;
    String[] hints2;
    int[] imageRes;

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

        hints1 = new String[]{
            "Пустое место - мышь",
            "Все кнопки настравиваются перетаскиванием кнопок (так себе звучит) из контекстного меню (в нём хранятся все кнопки)",
            "Можно выбрать готовые кнопки, настроить свои, а можно скачать плагин (в плагинах есть много интересного + они сами настраивают контекстные кнопки для приложений)",
            "Если провести пальцем от любой кнопки к центру экрана, контекстные кнопки (те, про которые написано на первой картинке) отобразятся на экране PC (псс... не забудь их сначала настроить, или скачать плагин, например)",
            "Чтобы подключиться к серверу (если он запущен, конечно) жмахни сюда"
        };

        hints2 = new String[]{
            "Эта штука - отображает текущее приложение на PC, при нажатии показывает контекстные кнопки для этого приложения",
            "Здесь можно добавить добавить новую кнопку (любые шорткаты, или коммандные строчки, например)",
            "",
            "Псс, если провести пальцем от центральной кнопки, сможешь переключать приложения",

        };

        imageRes = new int[]{
                R.drawable.t1,
                R.drawable.t2,
                R.drawable.t3,
                R.drawable.t3,
                R.drawable.t5,
        };

        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(0);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return ImageFragment.newInstance(hints1[position], hints2[position], pageNames[position], imageRes[position]);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}
