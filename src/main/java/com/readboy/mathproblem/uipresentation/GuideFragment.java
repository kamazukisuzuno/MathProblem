package com.readboy.mathproblem.uipresentation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.DataLoader;
import com.readboy.mathproblem.data.DataStructure;
import com.readboy.mathproblem.data.SetData;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.mathproblem.subject.SubjectItem;
import com.readboy.mathproblem.widget.TextViewWithPicture;

import java.io.IOException;
import java.util.List;

/**
 * Created by suzuno on 13-8-1.
 */
public class GuideFragment extends Fragment implements SetData{

    private String mContent;
    private List<byte[]> mImage;
    private SoundPlayer mPlayer;
    private SubjectItem mSubject;
    
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
        mSubject = subject;
        mContent = subject.getGuideContent();
        mImage = subject.getGuideResource();
    }
    
    @Override
    public void onAttach(Activity activity){
    	super.onAttach(activity);
    	if(mPlayer!=null&&mSubject!=null){
    		try {
				mPlayer.playSound(SoundPlayer.GUIDE, mSubject.getGrade(),mSubject.getSubject());
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

	@Override
	public void setSoundPlayer(SoundPlayer player) {
		mPlayer = player;
	}

}
