package com.example.agriventure.ui.learn;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LearnViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LearnViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}