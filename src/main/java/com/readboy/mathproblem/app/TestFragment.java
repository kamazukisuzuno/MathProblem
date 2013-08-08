package com.readboy.mathproblem.app;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.view.TextViewWithPicture;

import java.util.List;

/**
 * Created by suzuno on 13-8-2.
 */
public class TestFragment extends SubjectFragment{

    private int mIndex;
    private String mContent;
    private String mAnswer;
    private String mExplain;
    private List<byte[]> mImage;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TestFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mSubject!=null){
            mContent = mSubject.getTestContent(mIndex);
            mAnswer = mSubject.getTestAnswer(mIndex);
            mExplain = mSubject.getTestExplain(mIndex);
            mImage = mSubject.getTestResource(mIndex);
        }

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mIndex = getArguments().getInt(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView;
        if(mSubject==null){
            rootView = super.onCreateView(inflater, container, savedInstanceState);
        }else{
            rootView = inflater.inflate(R.layout.test_fragment,container,false);
            TextViewWithPicture tv = (TextViewWithPicture) rootView.findViewById(R.id.test);
            tv.updateTextView(mContent,mImage);
            tv.setMovementMethod(new ScrollingMovementMethod());
        }

        return rootView;
    }
}
