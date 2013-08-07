package com.readboy.mathproblem.uipresentation;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.widget.SubjectFragment;
import com.readboy.mathproblem.widget.TextViewWithPicture;

import java.util.List;

/**
 * Created by suzuno on 13-8-2.
 */
public class ExplainFragment extends SubjectFragment {

    private int mIndex;
    private String mContent;
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
    public ExplainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mIndex = getArguments().getInt(ARG_ITEM_ID);
        }

        mContent = mSubject.getExampleContent(mIndex);
        mImage = mSubject.getExampleResource(mIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.explain_fragment,container,false);
        TextViewWithPicture tv = (TextViewWithPicture) rootView.findViewById(R.id.example);
        tv.updateTextView(mContent,mImage);
        tv.setMovementMethod(new ScrollingMovementMethod());
        return rootView;
    }

}
