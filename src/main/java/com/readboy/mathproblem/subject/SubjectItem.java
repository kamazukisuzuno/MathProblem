package com.readboy.mathproblem.subject;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.readboy.mathproblem.R;
import com.readboy.mathproblem.data.DataLoader;
import com.readboy.mathproblem.data.SoundPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A subject item representing a piece of content.
 */
public class SubjectItem{

    private static final String READ_ERROR = "read content error";

    public static final int UNSOLVED = 1;
    public static final int SOLVED_WRONG = 2;
    public static final int SOLVED_WRONG_TWICE = 3;
    public static final int SOLVED_RIGHT = 4;

    private int mGrade;
    private int mSubject;

    private int mExampleCount;
    private int mTestCount;

    private long mGuideContentAbsAdr;
    private long mGuideResListAbsAdr;
    private long mExampleListAbsAdr;
    private long mTestListAbsAdr;

    private boolean     hasReadGuideContent;
    private boolean     hasReadGuideRes;
    private boolean     hasReadExample;
    private boolean     hasReadTest;

    private String          mGuideContent;
    private List<byte[]>    mGuideRes;
    private List<String>    mExampleContentList;
    private List<List<byte[]>>    mExampleResList;
    private List<String>    mTestContentList;
    private List<String>    mTestAnswerList;
    private List<String>    mTestExplainList;
    private List<List<byte[]>> mTestResList;

    private Map<Integer,Integer> mSolvedProblem = new HashMap<Integer, Integer>();
    private int mSolvedProblemCount;

    public SubjectItem() {

    }

    public static SubjectItem loadSubject(DataLoader loader,long itemAbsAdr) throws Exception {
        long guideContentAbsAdr = 0;
        long guideResListAbsAdr = 0;
        long exampleListAbsAdr = 0;
        long testListAbsAdr = 0;

        long[] moduleAdrList = loader.readTable(itemAbsAdr);
        for(int i=0;i<moduleAdrList.length;i++){
            final long moduleAbsAdr =moduleAdrList[i] + itemAbsAdr;

            loader.seek(moduleAbsAdr);

            int size = loader.readInt();
            int type = loader.readInt();

            switch(type){
                case 0x500c9:
                    loader.skip(16);

                    int testOffsetAdr = loader.readInt();

                    //int titleSize = loader.readInt();
                    //String title = loader.readString(titleSize);

                    testListAbsAdr = testOffsetAdr + moduleAbsAdr;
                    break;
                default:
                    int contentListAdr = loader.readInt();
                    switch(contentListAdr){
                        case 0:
                            guideContentAbsAdr = loader.readInt() + moduleAbsAdr;
                            guideResListAbsAdr = loader.readInt();
                            guideResListAbsAdr = guideResListAbsAdr==0?0:guideResListAbsAdr + moduleAbsAdr;

                            //int titleSize = loader.readInt();
                            //String title = loader.readString(titleSize);
                            break;
                        default:
                            //int titleSize = loader.readInt();
                            //String title = loader.readString(titleSize);

                            exampleListAbsAdr = contentListAdr + moduleAbsAdr;
                            break;
                    }

            }
        }

        SubjectItem item = new SubjectItem();
        item.mGuideContentAbsAdr = guideContentAbsAdr;
        item.mGuideResListAbsAdr = guideResListAbsAdr;
        item.mExampleListAbsAdr = exampleListAbsAdr;
        item.mTestListAbsAdr = testListAbsAdr;

        return item;
    }

    public String getGuideContent(){
        return mGuideContent;
    }

    public List<byte[]> getGuideResource(){
        return mGuideRes;
    }

    public void readGuide(DataLoader loader){

        if(!hasReadGuideContent){
            hasReadGuideContent = true;
            try {
                mGuideContent = loader.readContent(mGuideContentAbsAdr);
            } catch (IOException e) {
                e.printStackTrace();
                mGuideContent = READ_ERROR;
            }
            hasReadGuideContent = true;
        }
        if(!hasReadGuideRes){
            hasReadGuideRes = true;
            try {
                if(mGuideResListAbsAdr==0){
                    mGuideRes = new ArrayList<byte[]>();
                }else{
                    mGuideRes = loader.readImage(mGuideResListAbsAdr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getExampleContent(int index){

        if(index<0||index>=mExampleCount) return READ_ERROR;
        return mExampleContentList.get(index);
    }

    public List<byte[]> getExampleResource(int index){

        if(index<0||index>=mExampleCount) return null;
        return mExampleResList.get(index);
    }

    public void readExample(DataLoader loader){
        if(!hasReadExample && mExampleListAbsAdr!=0){
            hasReadExample = true;
            mExampleContentList = new ArrayList<String>();
            mExampleResList = new ArrayList<List<byte[]>>();
            try {
                long[] exampleAdrList = loader.readTable(mExampleListAbsAdr);
                mExampleCount = exampleAdrList.length;
                for(int i=0;i<mExampleCount;i++){
                    long exampleAbsAdr = exampleAdrList[i] + mExampleListAbsAdr;
                    loader.seek(exampleAbsAdr);

                    int size = loader.readInt();
                    int type = loader.readInt();
                    int listAdr = loader.readInt();
                    int contentAdr = loader.readInt();
                    int resAdr = loader.readInt();
                    //int titleSize = loader.readInt();
                    //String title = loader.readString(titleSize);

                    String content = loader.readContent(contentAdr+exampleAbsAdr);

                    List<byte[]> res;
                    if(resAdr==0){
                        res = new ArrayList<byte[]>();
                    }else{
                        res= loader.readImage(resAdr+exampleAbsAdr);
                    }


                    mExampleContentList.add(content);
                    mExampleResList.add(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getTestContent(int index){

        if(index<0||index>=mTestCount) return null;
        return mTestContentList.get(index);
    }

    public String getTestAnswer(int index){

        if(index<0||index>=mTestCount) return null;
        return mTestAnswerList.get(index);
    }

    public String getTestExplain(int index){

        if(index<0||index>=mTestCount) return null;
        return mTestExplainList.get(index);
    }

    public List<byte[]> getTestResource(int index){

        if(index<0||index>=mTestCount) return null;
        return mTestResList.get(index);
    }

    public void readTest(DataLoader loader){

        if(!hasReadTest && mTestListAbsAdr!=0){
            hasReadTest = true;
            mTestContentList = new ArrayList<String>();
            mTestAnswerList = new ArrayList<String>();
            mTestExplainList = new ArrayList<String>();
            mTestResList = new ArrayList<List<byte[]>>();

            try {
                long[] testAdrList = loader.readTable(mTestListAbsAdr);
                mTestCount = testAdrList.length;
                for(int i=0;i<mTestCount;i++){
                    final long testAbsAdr = testAdrList[i] + mTestListAbsAdr;
                    loader.seek(testAbsAdr);

                    int contentAdr = loader.readInt();
                    int answerAdr = loader.readInt();
                    int explainAdr = loader.readInt();
                    int resAdr = loader.readInt();
                    int voiceAdr = loader.readInt();
                    int ansIndex = loader.readShort();
                    int expIndex = loader.readShort();


                    String content = loader.readContent(contentAdr + testAbsAdr);
                    String answer  = loader.readContent(answerAdr  + testAbsAdr);
                    String explain = loader.readContent(explainAdr + testAbsAdr);

                    List<byte[]> res;
                    if(resAdr==0){
                        res = new ArrayList<byte[]>();
                    }else{
                        res = loader.readImage(resAdr + testAbsAdr);
                    }

                    mTestContentList.add(content);
                    mTestAnswerList.add(answer);
                    mTestExplainList.add(explain);
                    mTestResList.add(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getExampleCount(){
        return mExampleCount;
    }

    public int getTestCount(){
        return mTestCount;
    }

    public void solveProblem(String answer,int index,SoundPlayer player){
        Integer i = mSolvedProblem.get(index);

        if(i==null){
            mSolvedProblemCount++;

            if(getTestAnswer(index).contains(answer)){
                mSolvedProblem.put(index,SOLVED_RIGHT);
                playResult(player,SOLVED_RIGHT,index);
            }else{
                mSolvedProblem.put(index,SOLVED_WRONG);
                playResult(player,SOLVED_WRONG,index);
            };

            return;
        }

        switch(i){
            case SOLVED_RIGHT:
            case SOLVED_WRONG_TWICE:
                return;
            case SOLVED_WRONG:
                if(getTestAnswer(index).contains(answer)){
                    mSolvedProblem.put(index,SOLVED_RIGHT);
                    playResult(player,SOLVED_RIGHT,index);
                }else{
                    mSolvedProblem.put(index,SOLVED_WRONG_TWICE);
                    playResult(player,SOLVED_WRONG_TWICE,index);
                };
        }


    }

    private void playResult(SoundPlayer player,int result,int index){

        if(result == SubjectItem.SOLVED_RIGHT){
            player.playSound(SoundPlayer.RIGHT, 0, 0);
        }else if(result == SubjectItem.SOLVED_WRONG){

            player.playSound(SoundPlayer.WRONG, 0, 0);
        }else if(result == SubjectItem.SOLVED_WRONG_TWICE){
            String answer = mTestAnswerList.get(index);
            if(answer.contains("A")){
                player.playSound(SoundPlayer.ANSWER_IS_A, 0, 0);
            }else if(answer.contains("B")){
                player.playSound(SoundPlayer.ANSWER_IS_B, 0, 0);
            }else if(answer.contains("C")){
                player.playSound(SoundPlayer.ANSWER_IS_C, 0, 0);
            }else if(answer.contains("D")){
                player.playSound(SoundPlayer.ANSWER_IS_D, 0, 0);
            }
        }
    }

    public void submit(Context context){
        Toast.makeText(context,mSolvedProblemCount+",score:"+computeScore(),Toast.LENGTH_SHORT).show();
    }

    private int computeScore(){
        int scored = 0;
        int totalCount = getTestCount();
        for(int i=0;i<totalCount;i++){
            Integer solved = mSolvedProblem.get(i);
            if(solved!=null){
                if(solved==SOLVED_RIGHT){
                    scored++;
                }
            }
        }
        return (int) ((float)scored/totalCount*100);
    }

    public int getGrade() {
        return mGrade;
    }

    public void setGrade(int mGrade) {
        this.mGrade = mGrade;
    }

    public int getSubject() {
        return mSubject;
    }

    public void setSubject(int mSubject) {
        this.mSubject = mSubject;
    }
}
