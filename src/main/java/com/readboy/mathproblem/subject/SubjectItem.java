package com.readboy.mathproblem.subject;

/**
 * A subject item representing a piece of content.
 */
public class SubjectItem {

    public static final int MASK_TYPE_GRADE   = 0xf0;
    public static final int MASK_TYPE_SUBJECT = 0xf0;

    public static final int TYPE_GRADE          = 1;
    public static final int TYPE_SUBJECT        = 2;
    public static final int TYPE_TECHNIQUE      = 3;




    private String  id;
    private String  content;
    private int     mType;

    public SubjectItem(String id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }

    public String getId(){
        return id;
    }


}
