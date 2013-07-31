package com.readboy.mathproblem.uipresentation;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.subject.SubjectItem;

import java.util.ArrayList;

/**
 * A fragment representing a single Subject detail screen.
 * This fragment is either contained in a {@link com.readboy.mathproblem.MainActivity}
 * in two-pane mode (on tablets) or
 * on handsets.
 */
public class SubjectFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The subject content this fragment is presenting.
     */
    private SubjectItem mItem;

    /**
     * Grade and subject list;
     */
    private ArrayList<String> mGradeList;
    private ArrayList<String> mSubjectList;

    private String mString;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SubjectFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the subject content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mString = getArguments().getString(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.v("SubjectFragment","on create view");

        View rootView = inflater.inflate(R.layout.fragment_subject_detail, container, false);

        ((TextView) rootView.findViewById(R.id.subject_detail)).setText(mString);

        return rootView;
    }

    private void initialize(){

    }
}
