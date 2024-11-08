package com.example.apo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<String> woeid = new MutableLiveData<>();

    public LiveData<String> getWoeid() {
        return woeid;
    }

    public void setWoeid(String novoWoeid) {
        woeid.setValue(novoWoeid);
    }
}