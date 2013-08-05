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
    public static final int TEST = 3;
    public static final int RIGHT = 10;
    public static final int WRONG = 30;
	public static final int ANSWER_IS_A		= 60;
	public static final int ANSWER_IS_B		= 61;
	public static final int ANSWER_IS_C		= 62;
	public static final int ANSWER_IS_D		= 63;
	public static final int RESULT_00	= 51;
	public static final int RESULT_20	= 52;
	public static final int RESULT_40	= 53;
	public static final int RESULT_60	= 54;
	public static final int RESULT_80	= 55;
	public static final int RESULT_100	= 56;


    private MediaPlayer mPlayer = new MediaPlayer();
    private RandomAccessFile mSound;
    private Map<Integer,Sound> mapping = new HashMap<Integer, Sound>();


    public static SoundPlayer getSoundPlayer(File soundFile) throws IOException{
        SoundPlayer player = new SoundPlayer();
        try {
            player.mSound = new RandomAccessFile(soundFile,"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        player.init();
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
        int soundIndex;
    	
    	mPlayer.reset();
        
        switch(soundId){
        case GUIDE:
        	soundIndex = (grade+1)*100+subject+1;
        	break;
        case RIGHT:
        	soundIndex = (int)(11+Math.random()*8);
        	break;
        case WRONG:
        	soundIndex = (int)(31+Math.random()*5);
        	break;
        case EXAMPLE:
        case TEST:
        case ANSWER_IS_A:
        case ANSWER_IS_B:
        case ANSWER_IS_C:
        case ANSWER_IS_D:
        case RESULT_00:
        case RESULT_20:
        case RESULT_40:
        case RESULT_60:
        case RESULT_80:
        case RESULT_100:
        	soundIndex = soundId;
        default:
        	soundIndex = soundId;
        }

        Sound sound = mapping.get(soundIndex);
        if(sound!=null){
        	mPlayer.setDataSource(mSound.getFD(), sound.mOffset,sound.mLength);
            mPlayer.prepare();
            mPlayer.start();
        }
        
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

    public void reset(){
        if(mPlayer!=null){
            mPlayer.reset();
        }
    }
}
