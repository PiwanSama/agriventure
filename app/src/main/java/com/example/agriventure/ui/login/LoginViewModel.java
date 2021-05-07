package com.example.agriventure.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public LoginViewModel(){
        mText = new MutableLiveData<String>();
        mText.setValue("This is a login fragment");
    }

    public MutableLiveData<String> getText(){
        return mText;
    }
}
