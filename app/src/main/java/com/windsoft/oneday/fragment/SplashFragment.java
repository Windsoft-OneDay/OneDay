package com.windsoft.oneday.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;

import com.windsoft.oneday.R;

/**
 * Created by ironFactory on 2015-08-03.
 */
public class SplashFragment extends Fragment {

    private OnSplashHandler sender;

    public SplashFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sender = (OnSplashHandler) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.fragment_splash_container);

        AlphaAnimation visibleAnimation = new AlphaAnimation(0,2f);
        visibleAnimation.setDuration(1500);
        AlphaAnimation invisibleAnimation = new AlphaAnimation(2f,0);
        invisibleAnimation.setDuration(1500);
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(visibleAnimation);
        set.addAnimation(invisibleAnimation);
        set.setDuration(3000);
        layout.setAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sender.onSplash();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return view;
    }


    public interface OnSplashHandler {
        void onSplash();
    }
}
