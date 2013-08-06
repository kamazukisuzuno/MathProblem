package com.readboy.mathproblem.widget;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.readboy.mathproblem.MainActivity;
import com.readboy.mathproblem.data.DataLoader;
import com.readboy.mathproblem.data.DataStructure;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.mathproblem.subject.SubjectItem;

/**
 * Created by suzuno on 13-8-6.
 */
public class SubjectFragment extends Fragment {

    protected SubjectItem   mSubject;
    protected DataStructure mData;
    protected DataLoader    mLoader;
    protected SoundPlayer   mPlayer;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        MainActivity main = (MainActivity) activity;
        mSubject = main.getSubject();
        mData    = main.getData();
        mLoader  = main.getLoader();
        mPlayer  = main.getSoundPlayer();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mSubject = null;
        mData    = null;
        mLoader  = null;
        mPlayer  = null;
    }
}
