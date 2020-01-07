package fr.univpau.kayu.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import fr.univpau.kayu.R;

public class SettingsFragment extends Fragment {
    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = layoutInflater.inflate(R.layout.fragment_settings, container, false);

        return root;
    }
}