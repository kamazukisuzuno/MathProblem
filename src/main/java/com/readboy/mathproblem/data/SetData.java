package com.readboy.mathproblem.data;

import com.readboy.mathproblem.subject.SubjectItem;

/**
 * Created by suzuno on 13-8-2.
 */
public interface SetData {
    public void loadData(DataLoader loader,SubjectItem subject);
    public void setSoundPlayer(SoundPlayer player);
}