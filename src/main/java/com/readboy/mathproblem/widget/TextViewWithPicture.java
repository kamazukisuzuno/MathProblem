package com.readboy.mathproblem.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.List;

/**
 * Created by suzuno on 13-8-4.
 */
public class TextViewWithPicture extends TextView{


    public TextViewWithPicture(Context context) {
        super(context);
    }

    public TextViewWithPicture(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWithPicture(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void updateTextView(String text,List<byte[]> imageResourceList){

        if(imageResourceList==null||imageResourceList.size()==0){
            this.setText(text);
            return;
        }
        StringBuilder sb = new StringBuilder(text);
        sb.indexOf("<P m=\"0\" >");
        SpannableString spannable = new SpannableString(text);
        Bitmap image;
        BitmapDrawable imageDrawable;
        ImageSpan imageSpan;
        int start;
        int sbstart;
        int size = imageResourceList.size();
        int count = 0;

        for(int i=0;i<size;i++){
            image = BitmapFactory.decodeByteArray(imageResourceList.get(i), 0, imageResourceList.get(i).length);
            imageDrawable = new BitmapDrawable(image);
            imageDrawable.setBounds(0, 0, imageDrawable.getIntrinsicWidth(), imageDrawable.getIntrinsicHeight());
            imageSpan = new ImageSpan(imageDrawable, ImageSpan.ALIGN_BASELINE);

            sbstart = sb.indexOf("<P m=\"0\" >");
            if(sbstart==-1){
                break;
            }
            start = sbstart + count;
            spannable.setSpan(imageSpan,start, start+10, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            sb.delete(sbstart, sbstart + 10);
            count = count + 10;
        }
        this.setText(spannable);
    }


}
