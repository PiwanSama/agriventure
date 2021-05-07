package com.example.agriventure.ui.credit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreditViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreditViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is market fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}