package com.readboy.mathproblem;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.readboy.mathproblem.uipresentation.SubjectFragment;
import com.readboy.mathproblem.uipresentation.GradelistFragment;


/**
 * An activity representing a list of Subjects. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link com.readboy.mathproblem.uipresentation.GradelistFragment} and the item details
 * (if present) is a {@link com.readboy.mathproblem.uipresentation.SubjectFragment}.
 * <p>
 * This activity also implements the required
 * {@link com.readboy.mathproblem.uipresentation.GradelistFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MainActivity extends Activity
        implements GradelistFragment.Callbacks,ActionBar.TabListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_twopane);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.subject_list);
        GradelistFragment gradelistFragment = (GradelistFragment)fragment;
        gradelistFragment.setActivateOnItemClick(true);

        init();

        Log.v("MainActivity", "onCreate");
        // TODO: If exposing deep links into your app, handle intents here.
    }

    private void init(){
        toggleTabs();
        addTabs("guide");
        addTabs("example");
        addTabs("test");
    }

    /**
     * Callback method from {@link com.readboy.mathproblem.uipresentation.GradelistFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onSubclassSelected(int gradeIndex, int subclassIndex) {
        // In two-pane mode, show the detail view in this activity by
        // adding or replacing the detail fragment using a
        // fragment transaction.
        Bundle arguments = new Bundle();
        arguments.putString(SubjectFragment.ARG_ITEM_ID,"gradeIndex:"+gradeIndex+",subclassIndex:"+subclassIndex);
        SubjectFragment fragment = new SubjectFragment();
        fragment.setArguments(arguments);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);

        transaction.replace(R.id.subject_detail_container, fragment).commit();

        Log.v("MainActivity","on subclass selected");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add("Toogle Tabs");
        menu.add("Add Tabs");

        MenuItem actionItem = menu.add("Share");

        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        actionItem.setIcon(android.R.drawable.ic_menu_share);

        ShareActionProvider provider = new ShareActionProvider(this);
        provider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        provider.setShareIntent(createShareIntent());
        actionItem.setActionProvider(provider);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getTitle()=="Toogle Tabs"){
            toggleTabs();
        }
        if(item.getTitle()=="Add Tabs"){
            final ActionBar bar = getActionBar();
            final int tabCount = bar.getTabCount();
            final String text = "Tab " + tabCount;
            bar.addTab(bar.newTab()
                    .setText(text).setTabListener(this));
        }
        return true;
    }

    private void toggleTabs(){
        final ActionBar bar = getActionBar();
        if(bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS){
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            //bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE,ActionBar.DISPLAY_SHOW_TITLE);
        }else{
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            //bar.setDisplayOptions(0,ActionBar.DISPLAY_SHOW_TITLE);
        }
    }

    private void addTabs(String tabTitle) {
        final ActionBar bar = getActionBar();
        final int tabCount = bar.getTabCount();
        final String text = tabTitle;
        //ActionBar.Tab tab = bar.newTab();
        bar.addTab(bar.newTab()
                .setText(text).setTabListener(this));
    }

    private Intent createShareIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;
    }

    private void readConfig(){
        String appName = getString(R.string.app_name);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }
}
