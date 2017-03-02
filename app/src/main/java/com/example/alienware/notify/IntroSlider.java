package com.example.alienware.notify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Alienware on 30-05-2016.
 */




//this activity will be the first ie launch activity
//with checker it will launch intro slides if first time launch
//notification bar transparent, view pager for the the slides and inflting all slides
//listener for click on next and click button

public class IntroSlider extends Fragment {


    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout linearLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button skip, next;
    ChangeFrag changeFrag;
    TheSessionKeeper theSessionKeeper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        changeFrag = (ChangeFrag)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.intro,container,false);
        theSessionKeeper = TheSessionKeeper.getInstance(getContext());
    //to make the notification bar transparent for the intro slides
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        linearLayout = (LinearLayout) view.findViewById(R.id.DotsLayout);
        skip = (Button) view.findViewById(R.id.SkipButton);
        next = (Button) view.findViewById(R.id.NextButton);


        //layout of intro slides in array
        layouts = new int[]{
                R.layout.intro1,
                R.layout.intro2,
                R.layout.intro3,
                R.layout.intro4
        };

        //adding bottom dots
        addDots(0);
        //turning notification bar transparent
        changeStatusbarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        //on click listener for skip and next

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change frag login
                theSessionKeeper.setFirstTime(false);
                changeFrag.bringChange(new Login());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we check if current slide is less than total no of slides if yes next slide else launchHome
                int curr = getItem(+1);
                if (curr < layouts.length) {
                    viewPager.setCurrentItem(curr); //next slide
                } else {
                    theSessionKeeper.setFirstTime(false);
                    changeFrag.bringChange(new Login());
                    //change frag login
                }
            }
        });

     return view;
    }
        //adding dots to slide according to slide
        public void addDots(int currPage){
        dots = new TextView[layouts.length];
        int[] activecolors = getResources().getIntArray(R.array.active);
        int[] inactivecolors = getResources().getIntArray(R.array.inactive);
        linearLayout.removeAllViews();
        for(int i = 0; i< dots.length; i++){
            dots[i]= new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(25);
            dots[i].setTextColor(inactivecolors[currPage]);
            linearLayout.addView(dots[i]);
        }
        if(dots.length > 0)
            dots[currPage].setTextColor(activecolors[currPage]);
         }


    private int getItem(int i){
        return viewPager.getCurrentItem() + i;
    }





    //change listener for view pager
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            //if last page next button should display text got it and skip = invisible
            if(position == layouts.length -1){
                next.setText(getString(R.string.got_it));
                skip.setVisibility(View.GONE);
            }else {
                next.setText(getString(R.string.next));
                skip.setVisibility(View.VISIBLE);
            }

        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


//making notification bar transparent for intro slides
    private void changeStatusbarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        public MyViewPagerAdapter(){
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position],container,false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            View view = (View) object;
            container.removeView(view);
        }

    }

}


