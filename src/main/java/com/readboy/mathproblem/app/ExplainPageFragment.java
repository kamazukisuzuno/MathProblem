package com.readboy.mathproblem.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.libs.CirclePageIndicator;

/**
 * A fragment representing a single Subject detail screen.
 * This fragment is either contained in a {@link com.readboy.mathproblem.MainActivity}
 * in two-pane mode (on tablets) or
 * on handsets.
 */
public class ExplainPageFragment extends SubjectFragment{

    Bundle mArguments;
    private int mExampleCount;
    
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private CirclePageIndicator mCirclePageIndicator;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExplainPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArguments = getArguments();
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the subject content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mString = getArguments().getString(ARG_ITEM_ID);
        }

        if(mSubject!=null){
            mSubject.readExample(mLoader);
            mPlayer.playSound(SoundPlayer.EXAMPLE, mSubject.getGrade(),mSubject.getSubject());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView;

        if(mSubject==null){
            rootView = super.onCreateView(inflater,container,savedInstanceState);
        }else{
            rootView = inflater.inflate(R.layout.explain_pager_fragment, container, false);

            mPager = (ViewPager) rootView.findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
            mPager.setAdapter(mPagerAdapter);

            mCirclePageIndicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
            mCirclePageIndicator.setViewPager(mPager);

        }

        return rootView;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Fragment fragment = new ExplainFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_ITEM_ID,position);
            fragment.setArguments(arguments);

            return fragment;
        }

        @Override
        public int getCount() {
            if(mSubject==null){
                return 0;
            }
            return mSubject.getExampleCount();
        }
    }

    @Override
    public void onAttach(Activity activity){
    	super.onAttach(activity);
    }
}
