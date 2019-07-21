package com.example.ecommerce.helper;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerce.home.Home;

public final class ViewHelper extends ViewModel {
    MutableLiveData<Fragment> observer=new MutableLiveData<>();
    Fragment currentFragment;
    public boolean firstRun=true;
    public boolean recreate=false;
    public ViewHelper()
    {
        this.currentFragment=new Home();
        observer.setValue(currentFragment);
    }
    public void loadView(Fragment fragment)
    {
        if(!currentFragment.getClass().getSimpleName().equals(fragment.getClass().getSimpleName()))
        {
            currentFragment=fragment;
            observer.setValue(currentFragment);
        }
    }
    public void preForRecreate()
    {
        observer.setValue(currentFragment);
        recreate=true;
    }
    public void registerView(Fragment fragment)
    {
        if(!currentFragment.getClass().getSimpleName().equals(fragment.getClass().getSimpleName()))
            currentFragment=fragment;
    }
    public LiveData<Fragment> getObserver()
    {
        return observer;
    }
}
