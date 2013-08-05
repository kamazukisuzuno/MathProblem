package com.readboy.mathproblem;

import android.app.ActionBar;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.readboy.mathproblem.data.DataLoader;
import com.readboy.mathproblem.data.DataStructure;
import com.readboy.mathproblem.data.SetData;
import com.readboy.mathproblem.data.SoundPlayer;
import com.readboy.mathproblem.subject.SubjectItem;
import com.readboy.mathproblem.uipresentation.ExplainPageFragment;
import com.readboy.mathproblem.uipresentation.GradelistFragment;
import com.readboy.mathproblem.uipresentation.GuideFragment;
import com.readboy.mathproblem.uipresentation.TestPageFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * An activity representing a list of Subjects. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link com.readboy.mathproblem.uipresentation.GradelistFragment} and the item details
 * (if present) is a {@link com.readboy.mathproblem.uipresentation.ExplainPageFragment}.
 * <p>
 * This activity also implements the required
 * {@link com.readboy.mathproblem.uipresentation.GradelistFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MainActivity extends FragmentActivity
        implements GradelistFragment.Callbacks,ActionBar.TabListener {

    private String guide;
    private String example;
    private String test;

    private int mCurrentGrade = -1;
    private int mCurrentSubject = -1;
    private int mCurrentModule;

    private Map<String,ActionBar.Tab> mTabs = new HashMap<String, ActionBar.Tab>();
    private DataLoader          mLoader;
    private DataStructure       mData;
    private GradelistFragment   mListFragment;
    private SubjectItem         mSubject;
    private SoundPlayer 		mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_twopane);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.subject_list);
        GradelistFragment gradelistFragment = (GradelistFragment)fragment;
        mListFragment = gradelistFragment;
        gradelistFragment.setActivateOnItemClick(true);

        init();

        Log.v("MainActivity", "onCreate");
        // TODO: If exposing deep links into your app, handle intents here.
    }

    private void init(){
        guide = getString(R.string.guide);
        example = getString(R.string.example);
        test = getString(R.string.test);

        toggleTabs();
        addTabs(guide);
        addTabs(example);
        addTabs(test);

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String dataFilePath = filePath+"/yingyongti.mxd";
        String soundFilePath = filePath+"/yytsph.bin";

        try {
            mLoader = new DataLoader(dataFilePath);
            mPlayer = SoundPlayer.getSoundPlayer(new File(soundFilePath));
            mData = new DataStructure();
        } catch (FileNotFoundException e) {
            Toast.makeText(this,R.string.data_not_found,Toast.LENGTH_LONG).show();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        mData.loadSubjectAddress(mLoader);
        mData.setGradeTitle(new String[]{
                getString(R.string.grade1),
                getString(R.string.grade2),
                getString(R.string.grade3),
                getString(R.string.grade4),
                getString(R.string.grade5),
                getString(R.string.grade6),
                getString(R.string.grade7),
        });
        mListFragment.loadList(mData,getString(R.string.caesura));
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
        SubjectItem item;
        GuideFragment fragment = new GuideFragment();
        try {
            item = SubjectItem.loadSubject(mLoader, mData.getSubjectAdr(gradeIndex, subclassIndex));
            item.setGrade(gradeIndex);
            item.setSubject(subclassIndex);
            mSubject = item;
            fragment.loadData(mLoader,item);
            fragment.setSoundPlayer(mPlayer);
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"load subject item error",Toast.LENGTH_LONG).show();
        }


        mCurrentModule = 0;
        mTabs.get(guide).select();

        fragment.setArguments(arguments);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.subject_detail_container, fragment).commit();

        mCurrentGrade = gradeIndex;
        mCurrentSubject = subclassIndex;

        Log.v("MainActivity","on subclass selected");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add("Config");

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
        if(item.getTitle()=="Config"){

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
        ActionBar.Tab tab = bar.newTab()
                .setText(text).setTabListener(this);
        mTabs.put(tabTitle,tab);
        bar.addTab(tab);
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

        mCurrentModule = tab.getPosition();

        Bundle arguments = new Bundle();
        arguments.putInt(DataStructure.GRADE, mCurrentGrade);
        arguments.putInt(DataStructure.SUBJECT,mCurrentSubject);

        Fragment fragment;
        switch (mCurrentModule){
            case 0:
                fragment = new GuideFragment();
                break;
            case 1:
                fragment = new ExplainPageFragment();
                break;
            case 2:
                fragment = new TestPageFragment();
                break;
            default:
                fragment = new GuideFragment();

        }

        SetData loader = (SetData) fragment;
        if(mSubject!=null){
            loader.loadData(mLoader,mSubject);
            loader.setSoundPlayer(mPlayer);
        }

        fragment.setArguments(arguments);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.subject_detail_container, fragment).commit();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        //Toast.makeText(this,"on tab unselected",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        //Toast.makeText(this,"on tab reselected",Toast.LENGTH_SHORT).show();
    }
    
    public void playSound(int soundId,int grade,int subject){
    	try {
			mPlayer.playSound(soundId, grade, subject);
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

    @Override
    public void onPause(){
        super.onPause();
        mPlayer.reset();
    }
}
