package com.lm.myui_demo.material;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.lm.myui_demo.R;
import java.util.Random;

public class MyTabLayoutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tab_layout);

        ViewPager viewPager = findViewById(R.id.viewpager);
        String[] labels = new String[]{"Music", "Place", "News", "Favorite"};
        int[] icons = new int[]{R.drawable.icon_music, R.drawable.icon_place, R.drawable.icon_news, R.drawable.icon_favorite};
        viewPager.setAdapter(new Adapter(labels));
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        for (int i = 0; i < icons.length; i++) {
            tabs.getTabAt(i).setIcon(icons[i]);
        }
    }

    static class Adapter extends PagerAdapter {
        private String[] labels;

        Adapter(String[] labels) {
            this.labels = labels;
        }

        @Override
        public int getCount() {
            return labels.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TextView textView = new TextView(container.getContext());
            textView.setBackgroundColor(getColor());
            textView.setText(labels[position]);
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return labels[position];
        }

        private int getColor() {
            Random random = new Random();
            return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

    }
}
