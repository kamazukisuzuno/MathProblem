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
import com.readboy.mathproblem.data.SetData;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.mathproblem.subject.SubjectItem;
import com.readboy.mathproblem.widget.TextViewWithPicture;

import java.util.List;

/**
 * Created by suzuno on 13-8-2.
 */
public class ExplainFragment extends Fragment implements SetData {

    private int mIndex;
    private String mContent;
    private List<byte[]> mImage;

    private SubjectItem mSubject;
    private SoundPlayer mPlayer;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExplainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mIndex = getArguments().getInt(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        mContent = mSubject.getExampleContent(mIndex);
        mImage = mSubject.getExampleResource(mIndex);

        View rootView = inflater.inflate(R.layout.explain_fragment,container,false);
        TextViewWithPicture tv = (TextViewWithPicture) rootView.findViewById(R.id.example);
        tv.updateTextView(mContent,mImage);
        tv.setMovementMethod(new ScrollingMovementMethod());
        return rootView;
    }

    @Override
    public void loadData(DataLoader loader, SubjectItem subject) {

        mSubject = subject;
    }

	@Override
	public void setSoundPlayer(SoundPlayer player) {
		mPlayer = player;
	}
}
