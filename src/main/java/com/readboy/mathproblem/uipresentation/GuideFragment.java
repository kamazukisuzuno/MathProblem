package com.readboy.mathproblem.uipresentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.DataLoader;
import com.readboy.mathproblem.data.DataStructure;
import com.readboy.mathproblem.data.LoadData;
import com.readboy.mathproblem.subject.SubjectItem;
import com.readboy.mathproblem.widget.TextViewWithPicture;

import java.util.List;

/**
 * Created by suzuno on 13-8-1.
 */
public class GuideFragment extends Fragment implements LoadData{

    private String mContent;
    private List<byte[]> mImage;

    public GuideFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.guide_fragment,container,false);
        TextViewWithPicture tv = (TextViewWithPicture)rootView.findViewById(R.id.grade);
        tv.updateTextView(mContent,mImage);
        tv.setMovementMethod(new ScrollingMovementMethod());
        return rootView;
    }

    public void loadData(DataLoader loader,SubjectItem subject){
        subject.readGuide(loader);
        mContent = subject.getGuideContent();
        mImage = subject.getGuideResource();
    }

}
