package com.readboy.mathproblem.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.readboy.mathproblem.MainActivity;
import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.DataLoader;
import com.readboy.mathproblem.data.DataStructure;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.mathproblem.subject.SubjectItem;
import com.readboy.mathproblem.view.TextViewWithPicture;

import java.util.zip.Inflater;

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

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.guide_fragment,container,false);
        TextViewWithPicture tv = (TextViewWithPicture)rootView.findViewById(R.id.grade);
        tv.setText(R.string.error_no_subject_selected);
        return rootView;
    }
}
