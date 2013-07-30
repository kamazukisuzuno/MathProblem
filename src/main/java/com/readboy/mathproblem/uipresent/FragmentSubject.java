package com.readboy.mathproblem.uipresent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.subject.SubjectContent;
import com.readboy.mathproblem.subject.SubjectItem;

import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a single Subject detail screen.
 * This fragment is either contained in a {@link com.readboy.mathproblem.MainActivity}
 * in two-pane mode (on tablets) or
 * on handsets.
 */
public class FragmentSubject extends Fragment {

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentSubject() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the subject content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = SubjectContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listview_grade_and_subject, container, false);

        ExpandableListView expandableListView = (ExpandableListView) rootView;

        //create top level title
        List<Map<String,String>> gradeList = new ArrayList<Map<String, String>>();
        Map<String,String> grade1= new HashMap<String, String>();
        grade1.put("grade","grade1");
        Map<String,String> grade2= new HashMap<String, String>();
        grade2.put("grade", "grade2");
        Map<String,String> grade3= new HashMap<String, String>();
        grade3.put("grade", "grade3");
        gradeList.add(grade1);
        gradeList.add(grade2);
        gradeList.add(grade3);

        //create sub level container
        List<Map<String,String>> contentList1 = new ArrayList<Map<String, String>>();
        Map<String,String> gradeContent1_1 = new HashMap<String, String>();
        Map<String,String> gradeContent1_2 = new HashMap<String, String>();
        gradeContent1_1.put("child","subject1");
        gradeContent1_2.put("child","subject2");
        contentList1.add(gradeContent1_1);
        contentList1.add(gradeContent1_2);

        //create sub content
        List<Map<String,String>> contentList2 = new ArrayList<Map<String, String>>();
        Map<String,String> gradeContent2_1 = new HashMap<String, String>();
        Map<String,String> gradeContent2_2 = new HashMap<String, String>();
        Map<String,String> gradeContent2_3 = new HashMap<String, String>();
        gradeContent2_1.put("child","subject1");
        gradeContent2_2.put("child","subject2");
        gradeContent2_3.put("child","subject3");
        contentList2.add(gradeContent2_1);
        contentList2.add(gradeContent2_2);
        contentList2.add(gradeContent2_3);

        List<Map<String,String>> contentList3 = new ArrayList<Map<String, String>>();
        Map<String,String> gradeContent3_1 = new HashMap<String, String>();
        Map<String,String> gradeContent3_2 = new HashMap<String, String>();
        Map<String,String> gradeContent3_3 = new HashMap<String, String>();
        Map<String,String> gradeContent3_4 = new HashMap<String, String>();
        gradeContent3_1.put("child","subject1");
        gradeContent3_2.put("child","subject2");
        gradeContent3_3.put("child","subject3");
        gradeContent3_4.put("child","subject4");
        contentList3.add(gradeContent3_1);
        contentList3.add(gradeContent3_2);
        contentList3.add(gradeContent3_3);
        contentList3.add(gradeContent3_4);

        //add sub level container to list
        List<List<Map<String, String>>> contentLists = new ArrayList<List<Map<String,String>>>();
        contentLists.add(contentList1);
        contentLists.add(contentList2);
        contentLists.add(contentList3);

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(getActivity(),gradeList,R.layout.expandablelist_grade,new String[]{"grade"},new int[]{R.id.grade},
                contentLists,R.layout.expandablelist_subject,new String[]{"child"},new int[]{R.id.subject});

        expandableListView.setAdapter(adapter);

        /*expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;
            }
        });*/

        return rootView;
    }

    private void initialize(){

    }
}
