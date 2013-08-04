package com.readboy.mathproblem.data;

import com.readboy.mathproblem.LogWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suzuno on 13-7-29.
 */
public class DataStructure {

    private static final String LOG_TAG="DataStructure";
    public static final int HEAD = 0x100;

    public static final String GRADE = "grade";
    public static final String SUBJECT = "subject";

    private LogWriter mLogWriter = new LogWriter();

    private String[] mGradeList;

    private List<List<String>> mSubjectTitleList;
    private List<List<Long>>   mSubjectModuleAdrList;

    public boolean loadSubjectAddress(DataLoader dataLoader){
        dataLoader.seek(HEAD);

        //dataStruAbsAdr is the address where data structure begins
        long dataStruAbsAdr = 0;

        //load data structure absolute address,its offset address relative to header address is record at header address
        try {
            dataStruAbsAdr = dataLoader.readInt()+HEAD;
        } catch (IOException e) {
            mLogWriter.logError(LOG_TAG, "error happened when load data structure absolute address from header");
            e.printStackTrace();
            return false;
        }

        dataLoader.seek(dataStruAbsAdr);

        //rootSectAbsAdr is the root section address
        long rootSectAbsAdr;

        //load
        try {
            dataLoader.readInt();
            dataLoader.readInt();
            rootSectAbsAdr = dataLoader.readInt()+dataStruAbsAdr;
        } catch (IOException e) {
            mLogWriter.logError(LOG_TAG, "error happened when load root section structure absolute address");
            e.printStackTrace();
            return false;
        }

        //
        long[] subjectAdrList;
        String[] subjectTitleList;
        int size;

        //
        try {
            subjectAdrList = dataLoader.readTable(rootSectAbsAdr);
            size = subjectAdrList.length;
            //
            for(int i=0;i<size;i++){
                subjectAdrList[i] = adrTranslate(subjectAdrList[i],rootSectAbsAdr);
            }

            subjectTitleList = new String[size];
        } catch (Exception e) {
            mLogWriter.logError(LOG_TAG,"error happend when load subject address list");
            e.printStackTrace();
            return false;
        }

        mSubjectTitleList = new ArrayList<List<String>>();
        mSubjectModuleAdrList = new ArrayList<List<Long>>();
        for(int i=0;i<7;i++){
            mSubjectTitleList.add(new ArrayList<String>());
            mSubjectModuleAdrList.add(new ArrayList<Long>());
        }

        for(int i=0;i<size;i++){
            try {
                loadSubject(dataLoader,subjectAdrList[i],mSubjectTitleList,mSubjectModuleAdrList);
            } catch (IOException e) {
                subjectTitleList[i] = null;
            }
        }

        return true;
    }

    private long adrTranslate(long offsetAdr,long rootAdr){
        return offsetAdr + rootAdr;
    }

    private void loadSubject(DataLoader loader,long subjectAbsAdr,List<List<String>> titleList,List<List<Long>> adrList) throws IOException{
        loader.seek(subjectAbsAdr);

        loader.readInt(); //size,of no usage
        loader.readInt(); //type,of no usage
        long moduleListAdr = loader.readInt();
        int titleSize = loader.readInt();
        String title = loader.readString(titleSize);
        long moduleListAbsAdr = moduleListAdr + subjectAbsAdr;

        int grade = Integer.valueOf(title.substring(0,1))-1;
        String realTitle = title.substring(2,title.length());

        titleList.get(grade).add(realTitle);
        adrList.get(grade).add(moduleListAbsAdr);
    }

    public int getGradeCount(){
        return 7;
    }

    public void setGradeTitle(String[] titleList){
        mGradeList = titleList;
    }

    public String getGradeTitle(int index){

        new LogWriter().logWarning("GradeListFragment",mGradeList[index]);
        return mGradeList[index];
    }

    public int getSubjectCount(int grade){
        return mSubjectTitleList.get(grade).size();
    }

    public String getSubjectTitle(int grade,int position){
        return mSubjectTitleList.get(grade).get(position);
    }

    public long getSubjectAdr(int grade,int position){
        return mSubjectModuleAdrList.get(grade).get(position);
    }
}
