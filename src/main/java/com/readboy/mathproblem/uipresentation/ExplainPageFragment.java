package com.readboy.mathproblem.uipresentation;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.DataLoader;
import com.readboy.mathproblem.data.SetData;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.mathproblem.subject.SubjectItem;
import com.readboy.mathproblem.widget.CirclePageIndicator;

/**
 * A fragment representing a single Subject detail screen.
 * This fragment is either contained in a {@link com.readboy.mathproblem.MainActivity}
 * in two-pane mode (on tablets) or
 * on handsets.
 */
public class ExplainPageFragment extends Fragment implements SetData {

    Bundle mArguments;
    private int mExampleCount;
    private DataLoader mLoader;
    private SubjectItem mSubject;
    private SoundPlayer mPlayer;
    
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v("ExplainPageFragment","on create view");

        View rootView = inflater.inflate(R.layout.explain_pager_fragment, container, false);

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mCirclePageIndicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);
        mCirclePageIndicator.setViewPager(mPager);

        return rootView;
    }

    private void initialize(){

    }

    @Override
    public void loadData(DataLoader loader, SubjectItem subject) {
        mLoader = loader;
        mSubject = subject;
        subject.readExample(loader);
        mExampleCount = subject.getExampleCount();
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

            SetData loader = (SetData) fragment;
            loader.loadData(mLoader,mSubject);

            return fragment;
        }

        @Override
        public int getCount() {
            return mExampleCount;
        }
    }

	@Override
	public void setSoundPlayer(SoundPlayer player) {
		mPlayer = player;
	}
	
    @Override
    public void onAttach(Activity activity){
    	super.onAttach(activity);
        Toast.makeText(activity,"Explain Page Fragment On Attach",Toast.LENGTH_SHORT).show();
        if(mPlayer!=null&&mSubject!=null){
    		try {
				mPlayer.playSound(SoundPlayer.EXAMPLE, mSubject.getGrade(),mSubject.getSubject());
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
