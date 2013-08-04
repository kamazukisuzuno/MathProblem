package com.readboy.mathproblem.data;

import android.util.Log;

import com.readboy.mathproblem.LogWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by suzuno on 13-7-26.
 */
public class DataLoader {

    private static final String LOG_TAG = "DataLoader";

    private RandomAccessFile mData;
    private LogWriter mLogWriter = new LogWriter();

    public DataLoader(){

    }

    public DataLoader(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        mData = new RandomAccessFile(file,"r");
    }

    public void setData(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        mData = new RandomAccessFile(file,"r");
    }

    public void release(){
        try {
            mData.close();
        } catch (IOException e) {
            mLogWriter.logWarning(LOG_TAG, "error release dataloader");
            e.printStackTrace();
        }
        mData = null;
    }

    public boolean seek(long offset){
        mLogWriter.logVerbose(LOG_TAG, "seek at offset " + offset);

        try {
            mData.seek(offset);
            mLogWriter.logVerbose(LOG_TAG, "seek at offset " + offset + " success");
            return true;
        } catch (IOException e) {
            mLogWriter.logError(LOG_TAG, "seek at offset " + offset + " error");
            e.printStackTrace();
            return false;
        }
    }

    public boolean skip(int count){
        mLogWriter.logVerbose(LOG_TAG, "skip bytes count: " + count);

        try {
            mData.skipBytes(count);
            mLogWriter.logVerbose(LOG_TAG, "file pointer now at offset " + mData.getFilePointer());
            return true;
        } catch (IOException e) {
            mLogWriter.logVerbose(LOG_TAG, "skip bytes error ");
            e.printStackTrace();
            return false;
        }
    }

    public int readInt() throws IOException {
        mLogWriter.logVerbose(LOG_TAG,"read int at offset "+mData.getFilePointer());

        int result = mData.read() | mData.read() << 8 | mData.read() << 16
                        | mData.read() << 24;

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+result);

        return result;
    }

    public short readShort() throws IOException {
        mLogWriter.logVerbose(LOG_TAG,"read short at offset "+mData.getFilePointer());

        short result = (short) (mData.read() | mData.read() << 8);

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+result);

        return result;
    }

    public byte[] readBytes(int size) throws IOException{
        mLogWriter.logVerbose(LOG_TAG,"read "+size+" bytes at offset "+mData.getFilePointer());

        byte[] bytes = new byte[size];
        mData.read(bytes);

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+expandTable(bytes));

        return bytes;
    }

    public char readChar() throws IOException {
        mLogWriter.logVerbose(LOG_TAG,"read char at offset "+mData.getFilePointer());

        int a = mData.read(), b = mData.read() << 8;
        char result = (char) (a | b);

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+result);

        return result;
    }

    public String readString(int size) throws IOException {
        mLogWriter.logVerbose(LOG_TAG,"read string(length "+size+") at offset "+mData.getFilePointer());

        size = size -2;
        char[] charsequence = new char[size >> 1];
        for (int i = 0; i < charsequence.length; i++) {
            charsequence[i] = readChar();
        }
        mData.skipBytes(2);

        String result = String.valueOf(charsequence);

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+result);

        return result;
    }

    public long[] readTable(long tableAbsAdr) throws Exception {
        mLogWriter.logVerbose(LOG_TAG,"read address table at offset "+mData.getFilePointer());

        mData.seek(tableAbsAdr);
        int size = 0;
        while (readInt() != 0) {
            size++;
        }
        if (size == 0) {
            throw new Exception("empty table or wrong table address"
                    + String.format("%#010x", tableAbsAdr));
        }
        long[] table = new long[size];
        mData.seek(tableAbsAdr);
        for (int i = 0; i < size; i++) {
            table[i] = readInt();
        }

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return table "+expandTable(table));

        return table;
    }

    public long[] addressTranslate(long[] adrTable,long rootAdr){
        mLogWriter.logVerbose(LOG_TAG, "translate address,root address " + rootAdr + ",table sequence "+expandTable(adrTable));

        for(int i=0;i<adrTable.length;i++){
            adrTable[i]+=rootAdr;
        }

        mLogWriter.logVerbose(LOG_TAG, "translate address,root address " + rootAdr + ",table sequence "+expandTable(adrTable));

        return adrTable;
    }

    public String readContent(long... absAdr) throws IOException {
        mLogWriter.logVerbose(LOG_TAG,"read content at offset "+expandTable(absAdr));

        StringBuilder builder = new StringBuilder();
        StringBuilder temp;
        for (int i=0;i<absAdr.length;i++) {
            mData.seek(absAdr[i]);
            int size = readInt();
            temp = new StringBuilder(readString(size).replace("<T>","").replace("</T>",""));
            int start = 0;
            while((start = temp.indexOf("<I v="))!=-1){
                temp.replace(start,start+11,"        ");
            }
            builder.append(temp);
            if(i!=absAdr.length-1){
                builder.append("\n");
            }
        }
        String result = builder.toString();

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+result);

        return result;
    }



    public ArrayList<byte[]> readImage(long imageAbsAdr) throws IOException{
        mLogWriter.logVerbose(LOG_TAG,"read Image at offset "+imageAbsAdr);

        ArrayList<byte[]> list= new ArrayList<byte[]>();

        int startRelaAdr;
        int endRelaAdr;
        long lastEndAdr = imageAbsAdr;
        mData.seek(imageAbsAdr);
        while(((startRelaAdr = readInt())!=0)&&((endRelaAdr = readInt())!=0)){

            lastEndAdr += 4;
            mData.seek(startRelaAdr + imageAbsAdr);
            byte[] image = readBytes(endRelaAdr - startRelaAdr);
            mData.seek(lastEndAdr);
            list.add(image);
        }

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return image count "+list.size());
        return list;
    }

    public String expandTable(long[] addressTable){
        StringBuilder stringBuilder = new StringBuilder(addressTable[0]+"");
        for(int i=1;i<addressTable.length;i++){
            stringBuilder.append(" ");
            stringBuilder.append(addressTable[i]);
        }
        return stringBuilder.toString();
    }


    public String expandTable(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder(bytes[0]);
        for(int i=1;i<bytes.length;i++){
            stringBuilder.append(" ");
            stringBuilder.append(bytes[i]);
        }
        return stringBuilder.toString();
    }
}
