package fr.univpau.kayu.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScanViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ScanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Ici sera le scan");
    }

    public LiveData<String> getText() {
        return mText;
    }
}