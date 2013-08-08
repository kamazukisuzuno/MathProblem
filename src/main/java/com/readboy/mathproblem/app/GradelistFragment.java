package com.readboy.mathproblem.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.DataStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A list fragment representing a list of Subjects. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ExplainPageFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class GradelistFragment extends ExpandableListFragment {

    private final static String LOG_TAG = "GradelistFragment";

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Grade list
     */
    List<Map<String,String>> mGradeList = new ArrayList<Map<String, String>>(7);

    /**
     * sub class lists container
     */
    List<List<Map<String, String>>> mSubContainerLists = new ArrayList<List<Map<String, String>>>();


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onSubclassSelected(int gradeIndex, int subclassindex);
    }

    /**
     * A subject implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onSubclassSelected(int gradeIndex, int subclassIndex) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GradelistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(getActivity(),mGradeList, R.layout.expandablelist_grade,new String[]{"grade"},new int[]{R.id.grade},
                mSubContainerLists,R.layout.expandablelist_subject,new String[]{"child"},new int[]{R.id.subject});

        // TODO: replace with a real list adapter.
        setExpandableListAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public void loadList(DataStructure data,String caesura){

        int gradeCount = data.getGradeCount();
        for(int i=0;i<gradeCount;i++){
            addGroup(data.getGradeTitle(i));
            int childCount = data.getSubjectCount(i);
            for(int j=0;j<childCount;j++){
                String title = data.getSubjectTitle(i,j);
                if(i==6){
                    addChild(i,title.substring(title.indexOf(caesura)+1,title.length()));
                }else{
                    addChild(i,title);
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the subject implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Log.v(LOG_TAG,"on child click "+groupPosition);
        mCallbacks.onSubclassSelected(groupPosition, childPosition);
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        Log.v(LOG_TAG,"on group click "+groupPosition);
        return false;
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        Log.v(LOG_TAG,"on group collapse "+groupPosition);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        Log.v(LOG_TAG,"on group expand "+groupPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void addGroup(String groupName,int index){
        addGroup(groupName);
    }

    @Override
    public void addGroup(String groupName){

        Map<String,String> grade= new HashMap<String, String>();
        grade.put("grade",groupName);
        mGradeList.add(grade);

        List<Map<String,String>> contentList = new ArrayList<Map<String, String>>();
        mSubContainerLists.add(contentList);
    }

    @Override
    public void addChild(int groupIndex,String childName,int index){
        List<Map<String,String>> contentList = mSubContainerLists.get(groupIndex);
        Map<String,String> subclass = new HashMap<String, String>();
        subclass.put("child",childName);
        contentList.add(index,subclass);
    }

    @Override
    public void addChild(int groupIndex,String childName){
        List<Map<String,String>> contentList = mSubContainerLists.get(groupIndex);
        Map<String,String> subclass = new HashMap<String, String>();
        subclass.put("child", childName);
        contentList.add(subclass);
    }
}
