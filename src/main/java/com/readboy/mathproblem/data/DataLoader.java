package com.readboy.mathproblem.data;

import android.util.Log;

import com.readboy.mathproblem.LogWriter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by suzuno on 13-7-26.
 */
public class DataLoader {

    private static final String LOG_TAG = "DataLoader";

    private RandomAccessFile mData;
    private LogWriter mLogWriter;

    public boolean seek(long offset){
        mLogWriter.logVerbose(LOG_TAG, "seek at offset " + offset);

        try {
            mData.seek(offset);
            mLogWriter.logError(LOG_TAG, "seek at offset " + offset + " success");
            return true;
        } catch (IOException e) {

            mLogWriter.logError(LOG_TAG, "seek at offset " + offset + " error");
            e.printStackTrace();
            return false;
        }
    }

    private int readInt() throws IOException {
        mLogWriter.logVerbose(LOG_TAG,"read int at offset "+mData.getFilePointer());

        int result = mData.read() | mData.read() << 8 | mData.read() << 16
                        | mData.read() << 24;

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+result);

        return result;
    }

    private short readShort() throws IOException {
        mLogWriter.logVerbose(LOG_TAG,"read short at offset "+mData.getFilePointer());

        short result = (short) (mData.read() | mData.read() << 8);

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+result);

        return result;
    }

    private byte[] readBytes(int size) throws IOException{
        mLogWriter.logVerbose(LOG_TAG,"read "+size+" bytes at offset "+mData.getFilePointer());

        byte[] bytes = new byte[size];
        mData.read(bytes);

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+expandTable(bytes));

        return bytes;
    }

    private char readChar() throws IOException {
        mLogWriter.logVerbose(LOG_TAG,"read char at offset "+mData.getFilePointer());

        int a = mData.read(), b = mData.read() << 8;
        char result = (char) (a | b);

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return "+result);

        return result;
    }

    private String readString(int size) throws IOException {
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

    private int[] readTable(int tableAbsAdr) throws Exception {
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
        int[] table = new int[size];
        mData.seek(tableAbsAdr);
        for (int i = 0; i < size; i++) {
            table[i] = readInt();
        }

        mLogWriter.logVerbose(LOG_TAG,"file pointer now at offset "+mData.getFilePointer()+",return table "+expandTable(table));

        return table;
    }

    private int[] addressTranslate(int[] adrTable,int rootAdr){
        mLogWriter.logVerbose(LOG_TAG, "translate address,root address " + rootAdr + ",table sequence "+expandTable(adrTable));

        for(int i=0;i<adrTable.length;i++){
            adrTable[i]+=rootAdr;
        }

        mLogWriter.logVerbose(LOG_TAG, "translate address,root address " + rootAdr + ",table sequence "+expandTable(adrTable));

        return adrTable;
    }

    private String readContent(int... absAdr) throws IOException {
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



    private ArrayList<byte[]> readImage(int imageAbsAdr) throws IOException{
        mLogWriter.logVerbose(LOG_TAG,"read Image at offset "+imageAbsAdr);

        ArrayList<byte[]> list= new ArrayList<byte[]>();

        int startRelaAdr;
        int endRelaAdr;
        int lastEndAdr = imageAbsAdr;
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

    private String expandTable(int[] addressTable){
        StringBuilder stringBuilder = new StringBuilder(addressTable[0]);
        for(int i=1;i<addressTable.length;i++){
            stringBuilder.append(" ");
            stringBuilder.append(addressTable[i]);
        }
        return stringBuilder.toString();
    }


    private String expandTable(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder(bytes[0]);
        for(int i=1;i<bytes.length;i++){
            stringBuilder.append(" ");
            stringBuilder.append(bytes[i]);
        }
        return stringBuilder.toString();
    }
}
