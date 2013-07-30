package com.readboy.mathproblem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.readboy.mathproblem.uipresent.FragmentSubject;
import com.readboy.mathproblem.uipresent.FragmentSubjectList;
import com.readboy.mathproblem.widget.ExpandableListFragment;


/**
 * An activity representing a list of Subjects. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link com.readboy.mathproblem.uipresent.FragmentSubjectList} and the item details
 * (if present) is a {@link com.readboy.mathproblem.uipresent.FragmentSubject}.
 * <p>
 * This activity also implements the required
 * {@link com.readboy.mathproblem.uipresent.FragmentSubjectList.Callbacks} interface
 * to listen for item selections.
 */
public class MainActivity extends FragmentActivity
        implements FragmentSubjectList.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_twopane);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.subject_list);
        FragmentSubjectList fragmentSubjectList = (FragmentSubjectList)fragment;
        fragmentSubjectList.setActivateOnItemClick(true);

        Log.v("MainActivity", "onCreate");
        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link FragmentSubjectList.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
        Bundle arguments = new Bundle();
            arguments.putString(FragmentSubject.ARG_ITEM_ID, id);
            FragmentSubject fragment = new FragmentSubject();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.subject_detail_container, fragment)
                    .commit();
    }

    private void readConfig(){
        String appName = getString(R.string.app_name);
    }
}
