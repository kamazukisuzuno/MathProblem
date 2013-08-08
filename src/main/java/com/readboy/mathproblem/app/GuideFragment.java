package com.readboy.mathproblem.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.mathproblem.view.TextViewWithPicture;

import java.util.List;

/**
 * Created by suzuno on 13-8-1.
 */
public class GuideFragment extends SubjectFragment{

    private String mContent;
    private List<byte[]> mImage;
    
    public GuideFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mSubject!=null){
            mSubject.readGuide(mLoader);
            mContent = mSubject.getGuideContent();
            mImage = mSubject.getGuideResource();
            mPlayer.playSound(SoundPlayer.GUIDE, mSubject.getGrade(),mSubject.getSubject());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView;

        if(mSubject==null){
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }else{
            rootView = inflater.inflate(R.layout.guide_fragment,container,false);
            TextViewWithPicture tv = (TextViewWithPicture)rootView.findViewById(R.id.grade);
            tv.updateTextView(mContent,mImage);
            tv.setMovementMethod(new ScrollingMovementMethod());
        }

        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity){
    	super.onAttach(activity);
    }

}
