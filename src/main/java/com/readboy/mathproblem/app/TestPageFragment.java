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
import android.widget.Button;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.libs.CirclePageIndicator;


/**
 * Created by suzuno on 13-8-2.
 */
public class TestPageFragment extends SubjectFragment implements View.OnClickListener {

    Bundle mArguments;

    private CirclePageIndicator mCirclePageIndicator;
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

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TestPageFragment() {
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
            mSubject.readTest(mLoader);
            mPlayer.playSound(SoundPlayer.TEST, mSubject.getGrade(),mSubject.getSubject());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        if(mSubject==null){

            rootView = super.onCreateView(inflater, container, savedInstanceState);

        }else{
            rootView = inflater.inflate(R.layout.test_pager_fragment, container, false);

            mPager = (ViewPager) rootView.findViewById(R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
            mPager.setAdapter(mPagerAdapter);

            mCirclePageIndicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
            mCirclePageIndicator.setViewPager(mPager);

            Button a,b,c,d,submit;
            a = (Button) rootView.findViewById(R.id.a);
            b = (Button) rootView.findViewById(R.id.b);
            c = (Button) rootView.findViewById(R.id.c);
            d = (Button) rootView.findViewById(R.id.d);
            submit = (Button) rootView.findViewById(R.id.submit);

            a.setOnClickListener(this);
            b.setOnClickListener(this);
            c.setOnClickListener(this);
            d.setOnClickListener(this);
            submit.setOnClickListener(this);

        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        String answer = null;
        switch (v.getId()){
            case R.id.a:
                answer = "A";
                break;
            case R.id.b:
                answer = "B";
                break;
            case R.id.c:
                answer = "C";
                break;
            case R.id.d:
                answer = "D";
                break;
            case R.id.submit:
                mSubject.submit(getActivity());
                return;
        }

        if(answer!=null){
            mSubject.solveProblem(answer,mCirclePageIndicator.getCurrentPage(),mPlayer);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            Fragment fragment = new TestFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_ITEM_ID,position);
            fragment.setArguments(arguments);

            return fragment;
        }

        @Override
        public int getCount() {
        	if(mSubject!=null){
                return mSubject.getTestCount();
        	}
        	else return 0;
        }
    }
	
    @Override
    public void onAttach(Activity activity){
    	super.onAttach(activity);
    }
}
