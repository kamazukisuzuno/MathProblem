package com.readboy.mathproblem.data;

import android.media.MediaPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suzuno on 13-8-4.
 */
public class SoundPlayer {

    public static final int GUIDE = 1;
    public static final int EXAMPLE = 2;
    public static final int TEST 3;
    public static final int RIGHT 10;
    public static final int WRONG 30;
    public static final int A;
    public static final int B;
    public static final int C;
    public static final int D;
    public static final int RESULT00;
    public static final int RESULT20;
    public static final int RESULT40;
    public static final int RESULT60;
    public static final int RESULT80;
    public static final int RESULT100;


    private MediaPlayer mPlayer = new MediaPlayer();
    private RandomAccessFile mSound;
    private Map<Integer,Sound> mapping = new HashMap<Integer, Sound>();


    public static SoundPlayer getSoundPlayer(File soundFile){
        SoundPlayer player = new SoundPlayer();
        try {
            player.mSound = new RandomAccessFile(soundFile,"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return player;
    }

    private int readInt() throws IOException {
        return mSound.read() | mSound.read() << 8 | mSound.read() << 16
                | mSound.read() << 24;
    }

    public void init() throws IOException{
        mSound.skipBytes(8);
        int count = readInt();

        int type;
        for(int i=0;i<count;i++){
            type = readInt();

            mapping.put(type, new Sound(readInt(),readInt()));
        }
    }

    public void playSound(int soundId,int grade,int subject) throws IllegalArgumentException, IllegalStateException, IOException{
        mPlayer.reset();
        switch(soundId){
            case 0:
                switch(grade){
                    case GRADE1:
                        code = 100 + section;
                        break;
                    case GRADE2:
                        code = 200 + section;
                        break;
                    case GRADE3:
                        code = 300 + section;
                        break;
                    case GRADE4:
                        code = 400 + section;
                        break;
                    case GRADE5:
                        code = 500 + section;
                        break;
                    case GRADE6:
                        code = 600 + section;
                        break;
                    case YYTJQ:
                        code = 700 + section;
                        break;
                }
                break;
            case LTJX:
                code = 2;
                break;
            case TBCS:
                code = 3;
                break;
            case SRIGHT:
                code = (int)(11+Math.random()*8);
                break;
            case SWRONG:
                code = (int)(31+Math.random()*5);
                break;
            case SWRONGA:
            case SWRONGB:
            case SWRONGC:
            case SWRONGD:
                break;
            case SRESULT00:
            case SRESULT20:
            case SRESULT40:
            case SRESULT60:
            case SRESULT80:
            case SRESULT100:
                break;
            default:
                break;
        }

        player.setDataSource(soundFileDescriptor, mapping.get(code).mOffset,mapping.get(code).mLength);
        player.prepare();
        player.start();
    }

    class Sound{
        private int mOffset;
        private int mLength;

        Sound(int offset,int length){
            mOffset = offset;
            mLength = length;
        }
    }

    public void release(){
        if(mPlayer!=null){
            mPlayer.reset();
            mPlayer.release();
        }
    }
}
